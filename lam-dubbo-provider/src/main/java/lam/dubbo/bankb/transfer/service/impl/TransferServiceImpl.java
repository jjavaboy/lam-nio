package lam.dubbo.bankb.transfer.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import lam.dubbo.bankb.transfer.dao.TransferDao;
import lam.dubbo.bankb.transfer.model.Transfer;
import lam.dubbo.bankb.transfer.service.TransferService;
import lam.dubbo.bankb.user.service.AccountService;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月4日
* @version 1.0
*/
public class TransferServiceImpl implements TransferService{
	
	private TransferDao transferDao;

	private AccountService accountService;
	
	public void setTransferDao(TransferDao transferDao) {
		this.transferDao = transferDao;
	}
	
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@Transactional
	@Override
	public boolean doTransfer(Transfer transfer) {
		//1.查询是否存在这条记录
		Transfer oldTransfer = transferDao.getById(transfer.getMessageId());
		
		//2.如果不存在则插入一条记录
		if(oldTransfer == null){
			int result = transferDao.insert(transfer);
			if(result == 0)
				oldTransfer = transferDao.getById(transfer.getMessageId());
		}
		
		//3.同一事务下
		//更新记录状态
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("messageId", transfer.getMessageId());
		param.put("fromStatus", Transfer.Status.INITIAL.getValue());
		param.put("toStatus", Transfer.Status.TRANSFED.getValue());
		param.put("updateTime", new Date());
		int result = transferDao.updateStatus(param);
		//表示该messageId已经被转过账过，直接返回true
		if(result != 1)
			return true;
		//修改用户账号加钱
		boolean rs = accountService.addAccountMoney(transfer.getToUserId(), transfer.getMoney());
		return rs;
	}

}

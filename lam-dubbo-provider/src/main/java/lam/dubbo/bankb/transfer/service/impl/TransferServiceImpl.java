package lam.dubbo.bankb.transfer.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import lam.dubbo.bankb.transfer.dao.TransferDao;
import lam.dubbo.bankb.transfer.model.Transfer;
import lam.dubbo.bankb.transfer.service.TransferService;
import lam.dubbo.bankb.user.model.Account;
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
	
	private static Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);
	
	private static Gson gson = new Gson();
	
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
			boolean result = insert(transfer);
			logger.info(String.format("insert Transfer:%s, result:%b.", gson.toJson(transfer), result));
		}
		
		//3.同一事务下
		//更新记录状态
		boolean result = updateStatus(transfer.getMessageId(), Transfer.Status.INITIAL.getValue(), Transfer.Status.TRANSFED.getValue());
		logger.info(String.format("update transfer status to %d, when messageId:%s and old status:%d, result:%d.", 
				Transfer.Status.INITIAL.getValue(),  Transfer.Status.TRANSFED.getValue(), transfer.getMessageId(), result));
		//transfer的状态不是'INITIAL'，表示该messageId已经被修改过，直接返回true
		if(!result)
			return true;
		//修改用户账号加钱
		Account account = accountService.getById(transfer.getToUserId());
		if(account == null){
			account = new Account();
			account.setUserId(transfer.getToUserId());
			account.setMoney(0D);
			account.setCreateTime(new Date());
			account.setUpdateTime(account.getCreateTime());
			boolean r = accountService.insert(account);
			logger.info(String.format("insert Account:%s, result:%b.", gson.toJson(account), r));
		}
		boolean rs = accountService.addAccountMoney(transfer.getToUserId(), transfer.getMoney());
		logger.info(String.format("addAccountMoney, toUserId:%d, money:%f, result:%b.", transfer.getToUserId(), transfer.getMoney(), rs));
		return rs;
	}

	@Override
	public boolean insert(Transfer transfer) {
		if(transfer.getCreateTime() == null || transfer.getUpdateTime() == null){
			transfer.setCreateTime(new Date());
			transfer.setUpdateTime(transfer.getCreateTime());
		}
		transfer.setStatus(Transfer.Status.INITIAL.getValue());
		return transferDao.insert(transfer) == 1;
	}

	@Override
	public boolean updateStatus(String messageId, byte fromStatus, byte toStatus) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("messageId", messageId);
		param.put("fromStatus", fromStatus);
		param.put("toStatus", toString());
		param.put("updateTime", new Date());
		int result = transferDao.updateStatus(param);
		return result == 1;
	}

}

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
		StringBuilder sb = new StringBuilder("\nbegin transaction:").append(transfer.getMessageId()).append('\n');
		try{
			//1.给账号加钱
			boolean result = accountService.addAccountMoney(transfer.getToUserId(), transfer.getMoney());
			
			sb.append("addAccountMoney").append(", toUserId:").append(transfer.getToUserId()).append(", money:")
			.append(transfer.getToUserId()).append(", result:").append(result).append('\n');
			if(!result){
				return false;
			}
			
			//2.插入转账记录
			result = insert(transfer);
			
			sb.append("insert Transfer:").append(gson.toJson(transfer)).append(", result:").append(result).append('\n');
			if(!result){
				throw new RuntimeException("insert Transfer messageId:" + transfer.getMessageId() + " fail!!");
			}
			sb.append("end transaction:").append(transfer.getMessageId()).append('\n');
			return result;
		}catch(Exception e){
			sb.append("error").append(e.getMessage()).append('\n')
			  .append("rollback transaction");
			throw e;
		}finally{
			logger.info(sb.toString());
		}
	}

	@Override
	public boolean insert(Transfer transfer) {
		if(transfer.getCreateTime() == null || transfer.getUpdateTime() == null){
			transfer.setCreateTime(new Date());
			transfer.setUpdateTime(transfer.getCreateTime());
		}
		transfer.setStatus(Transfer.Status.TRANSFED.getValue()); //默认是插入已经转账状态的记录，要么失败，要么成功，插入这步跟修改账号 钱是同一事务的。
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

	@Override
	public Transfer getById(String messageId) {
		return transferDao.getById(messageId);
	}

}

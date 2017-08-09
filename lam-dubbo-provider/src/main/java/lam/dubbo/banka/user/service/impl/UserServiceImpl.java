package lam.dubbo.banka.user.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import lam.dubbo.banka.user.dao.UserDao;
import lam.dubbo.banka.user.model.User;
import lam.dubbo.banka.user.service.UserService;
import lam.dubbo.bankb.transfer.model.Transfer;
import lam.mq.model.MQessage;
import lam.mq.service.MQService;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月4日
* @version 1.0
*/
public class UserServiceImpl implements UserService{
	
	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	private static Gson gson = new Gson();

	private UserDao userDao;
	
	private MQService mqService;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void setMqService(MQService mqService) {
		this.mqService = mqService;
	}
	
	@Transactional
	@Override
	public boolean decreaseUserMoneyCrossBank(Integer fromUserId, Integer toUserId, double money) {
		StringBuilder sb = new StringBuilder("\nbegin transaction:\n");
		try{
			//1.减去用户的钱
			boolean result = decreaseUserMoneyInnerBank(fromUserId, money);
			
			sb.append("descreseUserMoney, userId:").append(fromUserId).append(", money:").append(money).append(", result:").append(result).append('\n');

			if(!result){
				User oldUser = userDao.getById(fromUserId);
				sb.append(String.format("user:%s, money not enouth, it need %f at least.", gson.toJson(oldUser), money)).append('\n');
				return false;
			}
			
			//2.插入消息到队列
			Transfer transfer = new Transfer();
			transfer.setFromUserId(fromUserId);
			transfer.setToUserId(toUserId);
			transfer.setMoney(money);
			transfer.setFromBrand("BankA");
			transfer.setCreateTime(new Date());
			transfer.setUpdateTime(transfer.getUpdateTime());
			
			MQessage message = new MQessage();
			message.setName(Transfer.class.getSimpleName());
			message.setType(MQessage.Type.QUEUE);
			message.setText(gson.toJson(transfer));
			
			result = mqService.sendQueue(message);
			sb.append(String.format("sendQueue, message:%s, result:%b.", gson.toJson(message), result)).append('\n');
			//发消息不成功，则回滚减钱的第1步
			if(!result){
				throw new RuntimeException("sendQueue fail !!");
			}
			sb.append("end transaction\n");
			return result;
		}catch(Exception e){
			sb.append("rollback transaction");
			throw e;
		}finally{
			logger.info(sb.toString());
		}
	}
	
	private boolean decreaseUserMoneyInnerBank(int userId, double money){
		User param = new User();
		param.setUserId(userId);
		param.setMoney(money);
		param.setUpdateTime(new Date());
		
		return userDao.decreaseUserMoney(param) == 1;
	}

	@Override
	public boolean insert(User user) {
		return userDao.insert(user) == 1;
	}

}

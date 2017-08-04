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
		User oldUser = userDao.getById(fromUserId);
		if(oldUser == null){
			User user = new User();
			user.setUserId(fromUserId);
			user.setMoney(0D);
			user.setCreateTime(new Date());
			user.setUpdateTime(user.getCreateTime());
			userDao.insert(user);
		}
		//1.减去用户的钱
		User param = new User();
		param.setUserId(fromUserId);
		param.setMoney(money);
		int result = userDao.decreaseUserMoney(param);
		//减失败，则不够钱，直接返回
		if(result != 1){
			oldUser = userDao.getById(fromUserId);
			logger.info(String.format("%s, money not enouth, it need %f at least.", gson.toJson(oldUser), money));
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
		message.setClazz(Transfer.class);
		
		boolean rs = mqService.sendQueue(message);
		//发消息不成功，则回滚减钱的第1步
		if(!rs){
			//handle here...
		}
		return rs;
	}

}

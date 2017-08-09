package lam.dubbo.banka.user.service;

import lam.dubbo.banka.user.model.User;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月4日
* @version 1.0
*/
public interface UserService {
	
	public boolean decreaseUserMoneyCrossBank(Integer fromUserId, Integer toUserId, double money);
	
	public boolean insert(User user);

}

package lam.dubbo.banka.user.dao;

import java.util.Map;

import lam.dubbo.banka.user.model.User;
import lam.dubbo.common.MyBatisDao;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月4日
* @version 1.0
*/
@MyBatisDao
public interface UserDao {
	
	public User getById(int userId);
	
	public int insert(User user);
	
	public int decreaseUserMoney(User user);

}

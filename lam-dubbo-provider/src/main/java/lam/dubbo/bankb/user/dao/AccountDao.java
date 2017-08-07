package lam.dubbo.bankb.user.dao;

import lam.dubbo.bankb.user.model.Account;
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
public interface AccountDao {
	
	public Account getById(int userId);
	
	public int insert(Account account);
	
	public int addMoney(Account account);

}

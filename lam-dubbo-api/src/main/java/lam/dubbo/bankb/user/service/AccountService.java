package lam.dubbo.bankb.user.service;

import lam.dubbo.bankb.user.model.Account;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月4日
* @version 1.0
*/
public interface AccountService {
	
	public Account getById(int userId);
	
	public boolean insert(Account account);
	
	public boolean addAccountMoney(Integer userId, double money);

}

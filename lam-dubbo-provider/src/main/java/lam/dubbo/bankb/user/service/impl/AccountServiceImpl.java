package lam.dubbo.bankb.user.service.impl;

import lam.dubbo.bankb.user.dao.AccountDao;
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
public class AccountServiceImpl implements AccountService{

	private AccountDao accountDao;
	
	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}
	
	@Override
	public boolean addAccountMoney(Integer userId, double money) {
		Account account = new Account();
		account.setUserId(userId);
		account.setMoney(money);
		return accountDao.addMoney(account) == 1;
	}

}

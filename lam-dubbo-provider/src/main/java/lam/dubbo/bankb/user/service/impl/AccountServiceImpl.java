package lam.dubbo.bankb.user.service.impl;

import java.util.Date;

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
	public Account getById(int userId) {
		return accountDao.getById(userId);
	}
	
	@Override
	public boolean insert(Account account) {
		return accountDao.insert(account) == 1;
	}
	
	@Override
	public boolean addAccountMoney(Integer userId, double money) {
		Account account = new Account();
		account.setUserId(userId);
		account.setMoney(money);
		account.setUpdateTime(new Date());
		return accountDao.addMoney(account) == 1;
	}

}

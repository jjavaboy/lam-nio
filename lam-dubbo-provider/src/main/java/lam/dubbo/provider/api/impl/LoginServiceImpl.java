package lam.dubbo.provider.api.impl;

import lam.dubbo.api.LoginService;
import lam.dubbo.core.DateUtil;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年2月26日
* @versio 1.0
*/
public class LoginServiceImpl implements LoginService{
	
	private static String className = LoginServiceImpl.class.getName();

	@Override
	public boolean login(String username) {
		System.out.println(String.format("%s [%s] - args:%s", DateUtil.now(), className, username));
		return false;
	}

	@Override
	public boolean logout(String username) {
		System.out.println(String.format("%s [%s] - args:%s", DateUtil.now(), className, username));
		return false;
	}

}

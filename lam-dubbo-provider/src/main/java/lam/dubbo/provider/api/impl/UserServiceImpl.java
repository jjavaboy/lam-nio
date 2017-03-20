package lam.dubbo.provider.api.impl;

import lam.dubbo.api.UserService;
import lam.dubbo.core.DateUtil;

/**
* <p>
* this class implements UserService interface
* </p>
* @author linanmiao
* @date 2016年10月30日
* @versio 1.0
*/
public class UserServiceImpl implements UserService{
	
	private static final String className = UserServiceImpl.class.getName();

	@Override
	public String sayHello(String username) {
		System.out.println(String.format("%s [%s] - args:%s", DateUtil.now(), className, username));
		return "hello, " + username + "!";
	}

	@Override
	public String sayGoodBye(String username) {
		System.out.println(String.format("%s [%s] - args:%s", DateUtil.now(), className, username));
		return "good bye, " + username + "!";
	}

}

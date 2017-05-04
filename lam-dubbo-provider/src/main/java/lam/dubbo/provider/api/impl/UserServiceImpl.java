package lam.dubbo.provider.api.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public String sayHello(String username) {
		logger.info("%s - args:%s", "sayHello", username);
		return "hello, " + username + "!";
	}

	@Override
	public String sayGoodBye(String username) {
		logger.info("%s - args:%s", "sayGoodBye", username);
		return "good bye, " + username + "!";
	}

}

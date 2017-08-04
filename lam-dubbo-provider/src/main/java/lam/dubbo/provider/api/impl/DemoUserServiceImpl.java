package lam.dubbo.provider.api.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lam.dubbo.api.DemoUserService;

/**
* <p>
* this class implements UserService interface
* </p>
* @author linanmiao
* @date 2016年10月30日
* @versio 1.0
*/
public class DemoUserServiceImpl implements DemoUserService{
	
	private static Logger logger = LoggerFactory.getLogger(DemoUserServiceImpl.class);

	@Override
	public String sayHello(String username) {
		logger.info("{} - args:{}", "sayHello", username);
		return "hello, " + username + "!";
	}

	@Override
	public String sayGoodBye(String username) {
		logger.info("{} - args:{}", "sayGoodBye", username);
		return "good bye, " + username + "!";
	}

}

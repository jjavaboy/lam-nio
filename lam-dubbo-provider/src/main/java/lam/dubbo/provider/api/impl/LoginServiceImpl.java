package lam.dubbo.provider.api.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lam.dubbo.api.LoginService;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年2月26日
* @versio 1.0
*/
public class LoginServiceImpl implements LoginService{
	
	private static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

	@Override
	public boolean login(String username) {
		logger.info("{} - args:{}", "login", username);
		sleep(1000);
		return true;
	}
	
	private void sleep(long millisecond){
		try {
			Thread.sleep(millisecond);
		} catch (InterruptedException e) {
			logger.error("thread sleep InterruptedException", e);
		}
	}

	@Override
	public boolean logout(String username) {
		logger.info("{} - args:{}", "logout", username);
		return true;
	}

}

package lam.dao.sample.model;

import lam.dao.support.model.TimeBean;

/**
* <p>
* foo model
* </p>
* @author linanmiao
* @date 2017年6月29日
* @version 1.0
*/
public class Foo extends TimeBean{
	
	private static final long serialVersionUID = -1284042554731268678L;

	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}

package lam.mongo.model;

import java.io.Serializable;

/**
* <p>
* mongodb中model对应collection
* </p>
* @author linanmiao
* @date 2016年12月5日
* @version 1.0
*/
public class Foo implements Serializable{

	private static final long serialVersionUID = 1539206268628916473L;

	private String name;
	
	private String fullname;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	
}

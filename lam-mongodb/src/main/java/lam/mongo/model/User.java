package lam.mongo.model;

import java.io.Serializable;

/**
* <p>
* user model
* </p>
* @author linanmiao
* @date 2016年10月28日
* @versio 1.0
*/
public class User implements Serializable{

	private static final long serialVersionUID = 1262493434263535746L;
	
	private Integer id;
	
	private String username;
	
	private String fullname;
	
	private Integer gender;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}
	
}

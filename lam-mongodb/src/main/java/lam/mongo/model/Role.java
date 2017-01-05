package lam.mongo.model;

import java.io.Serializable;

import lam.mongo.json.exclusion.JsonExclusion;

/**
* <p>
* role class
* </p>
* @author linanmiao
* @date 2016年12月6日
* @version 1.0
*/
public class Role extends UpdateModel implements Serializable{

	private static final long serialVersionUID = 5606096426718358052L;
	
	private Integer id;
	
	private String key;
	
	private String rolename;

	@JsonExclusion
	private String temporaryName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getTemporaryName() {
		return temporaryName;
	}

	public void setTemporaryName(String temporaryName) {
		this.temporaryName = temporaryName;
	}
	
}

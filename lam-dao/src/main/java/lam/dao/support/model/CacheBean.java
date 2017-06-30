package lam.dao.support.model;

import java.io.Serializable;

import lam.dao.support.annotation.Expire;
import lam.dao.support.annotation.Uid;

/**
* <p>
* base bean for cahce
* </p>
* @author linanmiao
* @date 2017年6月29日
* @version 1.0
*/
public class CacheBean implements Serializable{
	
	private static final long serialVersionUID = -5421678645942677812L;
	
	@Uid(name="id")
	private Long id;
	
	@Expire(name="expire")
	private Long expire;
	
	public CacheBean(){
		expire = System.currentTimeMillis() + 1000 * 60 * 60 * 3;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getExpire() {
		return expire;
	}

	public void setExpire(Long expire) {
		this.expire = expire;
	}
	
}

package lam.dao.support.model;

import java.io.Serializable;

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
	
	private long expire;

	public long getExpire() {
		return expire;
	}

	public void setExpire(long expire) {
		this.expire = expire;
	}
	
}

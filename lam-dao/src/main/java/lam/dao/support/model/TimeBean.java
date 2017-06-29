package lam.dao.support.model;

import java.util.Date;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年6月29日
* @version 1.0
*/
public class TimeBean extends CacheBean{
	
	private static final long serialVersionUID = 246657845885584273L;

	private String creator;
	
	private Date createdTime;
	
	private String updator;
	
	private Date updatedTime;

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	
}

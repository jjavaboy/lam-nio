package lam.mongo.model;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* update model
* </p>
* @author linanmiao
* @date 2016年12月6日
* @version 1.0
*/
public class UpdateModel implements Serializable{

	private static final long serialVersionUID = -2043632127403012667L;

	private Date createTime;
	
	private Date updateTime;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}

package lam.dubbo.banka.user.model;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月4日
* @version 1.0
*/
public class User implements Serializable{

	private static final long serialVersionUID = 8966666651296167564L;

	private Integer userId;
	
	private double money;
	
	private Date createTime;
	
	private Date updateTime;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

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

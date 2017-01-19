package org.lam.hongbao.core.model;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.Gson;

/**
* <p>
* hong bao record
* </p>
* @author linanmiao
* @date 2017年1月18日
* @versio 1.0
*/
public class HongBaoRecord implements Serializable{

	private static final long serialVersionUID = -5048378336882889773L;
	
	private Long id;
	
	private Long userId;
	
	private int money;
	
	private Long hongbaoId;
	
	private byte status;
	
	private Date createTime;
	
	private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
	
	public Long getHongbaoId() {
		return hongbaoId;
	}
	
	public void setHongbaoId(Long hongbaoId) {
		this.hongbaoId = hongbaoId;
	}
	
	public byte getStatus() {
		return status;
	}
	
	public void setStatus(byte status) {
		this.status = status;
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
	
	public String toString(){
		return new Gson().toJson(this);
	}
	
}

package org.lam.hongbao.core.model;

import java.io.Serializable;
import java.util.Date;

import org.lam.hongbao.core.constant.Status;

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
	
	private double money;
	
	private Long hongbaoId;
	
	private byte status = Status.HongBaoRecord.UNCONSUME.getValue();
	
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

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
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
	
}

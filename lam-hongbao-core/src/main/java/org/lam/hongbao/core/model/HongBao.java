package org.lam.hongbao.core.model;

import java.io.Serializable;
import java.util.Date;

import org.lam.hongbao.core.constant.HongBaoRange;
import org.lam.hongbao.core.constant.Status;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年1月18日
* @versio 1.0
*/
public class HongBao implements Serializable {

	private static final long serialVersionUID = -5048378336882889773L;

	private Long id;
	
	private Long userId;
	
	private double money;
	
	//红包的最小值
	private double minMoney = HongBaoRange.MIN.getValue();
	
	//红包的最大值
	private double maxMoney = HongBaoRange.MAX.getValue();
	
	//数量
	private int num;
	
	private byte status = Status.HongBao.UNCONSUME.getValue();
	
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
	
	public double getMinMoney() {
		return minMoney;
	}
	
	public void setMinMoney(int minMoney) {
		this.minMoney = minMoney;
	}
	
	public double getMaxMoney() {
		return maxMoney;
	}
	
	public void setMaxMoney(int maxMoney) {
		this.maxMoney = maxMoney;
	}
	
	public int getNum() {
		return num;
	}
	
	public void setNum(int num) {
		this.num = num;
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

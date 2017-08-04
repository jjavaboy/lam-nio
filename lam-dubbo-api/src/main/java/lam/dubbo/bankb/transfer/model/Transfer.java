package lam.dubbo.bankb.transfer.model;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* 转账
* </p>
* @author linanmiao
* @date 2017年8月4日
* @version 1.0
*/
public class Transfer implements Serializable{

	private static final long serialVersionUID = -6757388644248198060L;
	
	/**唯一字段*/
	private String messageId;
	private Integer fromUserId;
	private String fromBrand;
	private Integer toUserId;
	private double money;
	private byte status;
	private Date createTime;
	private Date updateTime;
	
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public Integer getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(Integer fromUserId) {
		this.fromUserId = fromUserId;
	}
	public String getFromBrand() {
		return fromBrand;
	}
	public void setFromBrand(String fromBrand) {
		this.fromBrand = fromBrand;
	}
	public Integer getToUserId() {
		return toUserId;
	}
	public void setToUserId(Integer toUserId) {
		this.toUserId = toUserId;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
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
	
	public static enum Status{
		/**初始化*/
		INITIAL((byte)1),
		/**转账失败*/
		TRANSFE_FAIL((byte)2),
		/**转账成功*/
		TRANSFED((byte)3);
		
		private byte value;
		
		private Status(byte value){
			this.value = value;
		}
		
		public byte getValue() {
			return value;
		}
	}
	
}

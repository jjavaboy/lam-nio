package lam.mq.model;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月3日
* @version 1.0
*/
public class MQessage implements Serializable{

	private static final long serialVersionUID = -491224685098114863L;
	
	private Integer id;
	private String Name;
	private Type type;
	private String text;
	private Class<?> clazz;
	private Date createTime;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Class<?> getClazz() {
		return clazz;
	}
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public boolean isQueue(){
		return type == Type.QUEUE;
	}
	
	public boolean isTopic(){
		return type == Type.TOPIC;
	}
	
	public static enum Type{
		
		QUEUE((byte)1),
		
		TOPIC((byte)2);
		
		private byte value;
		
		private Type(byte value){
			this.value = value;
		}
		
		public byte getValue(){
			return this.value;
		}
	}
	
}

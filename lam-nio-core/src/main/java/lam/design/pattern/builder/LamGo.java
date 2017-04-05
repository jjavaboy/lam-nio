package lam.design.pattern.builder;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* 建造者模式
* </p>
* @author linanmiao
* @date 2017年4月5日
* @version 1.0
*/
public class LamGo implements Serializable{
	
	private static final long serialVersionUID = 1332420762715614221L;
	
	private int id;
	private String nickname;
	private String username;
	private String address;
	private Date createDate;

	public int getId() {
		return id;
	}

	public String getNickname() {
		return nickname;
	}

	public String getUsername() {
		return username;
	}

	public String getAddress() {
		return address;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public static class Builder{
		private LamGo lamGo = new LamGo();
		
		public Builder setId(int id) {
			lamGo.id = id;
			return this;
		}

		public Builder setNickname(String nickname) {
			lamGo.nickname = nickname;
			return this;
		}

		public Builder setUsername(String username) {
			lamGo.username = username;
			return this;
		}

		public Builder setAddress(String address) {
			lamGo.address = address;
			return this;
		}

		public Builder setCreateDate(Date createDate) {
			lamGo.createDate = createDate;
			return this;
		}

		public LamGo get(){
			return lamGo;
		}
		
	}
	
}

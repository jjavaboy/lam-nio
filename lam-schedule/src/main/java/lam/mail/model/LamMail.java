package lam.mail.model;

import java.io.Serializable;

import org.springframework.mail.SimpleMailMessage;

/**
* <p>
* mail model
* </p>
* @author linanmiao
* @date 2017年5月8日
* @version 1.0
*/
public class LamMail implements Serializable{
	private static final long serialVersionUID = -8894904361951531319L;

	private boolean passwordEncrypted = Boolean.TRUE.booleanValue();//encrypted:true in default.
	
	private SimpleMailMessage simpleMailMessage;
	
	public void setPasswordEncrypted(boolean passwordEncrypted) {
		this.passwordEncrypted = passwordEncrypted;
	}
	
	public boolean isPasswordEncrypted() {
		return passwordEncrypted;
	}
	
	public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
		this.simpleMailMessage = simpleMailMessage;
	}
	
	public SimpleMailMessage getSimpleMailMessage() {
		return simpleMailMessage;
	}

}

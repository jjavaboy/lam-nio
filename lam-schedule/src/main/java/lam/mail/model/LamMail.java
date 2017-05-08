package lam.mail.model;

import org.springframework.mail.SimpleMailMessage;

/**
* <p>
* mail model
* </p>
* @author linanmiao
* @date 2017年5月8日
* @version 1.0
*/
public class LamMail {
	
	private SimpleMailMessage simpleMailMessage;
	
	public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
		this.simpleMailMessage = simpleMailMessage;
	}
	
	public SimpleMailMessage getSimpleMailMessage() {
		return simpleMailMessage;
	}

}

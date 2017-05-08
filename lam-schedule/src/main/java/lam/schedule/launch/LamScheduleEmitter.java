package lam.schedule.launch;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.mail.SimpleMailMessage;

import lam.mail.model.LamMail;
import lam.mail.send.LamMailSender;

/**
* <p>
* 先锋部队<br/>
* 其实就是测试一下，噶噶~~
* </p>
* @author linanmiao
* @date 2017年5月8日
* @version 1.0
*/
public class LamScheduleEmitter implements Emitter{

	private LamMailSender lamMailSender;
	
	private SimpleMailMessage templateMessage;
	
	@Override
	public void emit() {
		LamMail lamMail = new LamMail();
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(templateMessage.getFrom());
		simpleMailMessage.setTo(templateMessage.getTo());
		simpleMailMessage.setSubject(templateMessage.getSubject());
		simpleMailMessage.setSentDate(new Date());
		simpleMailMessage.setText(templateMessage.getText());
		lamMail.setSimpleMailMessage(simpleMailMessage);;
		lamMailSender.send(lamMail);
	}
	
	public void setLamMailSender(LamMailSender lamMailSender) {
		this.lamMailSender = lamMailSender;
	}
	
	public void setTemplateMessage(SimpleMailMessage templateMessage) {
		this.templateMessage = templateMessage;
	}

}

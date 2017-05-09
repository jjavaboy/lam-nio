package lam.schedule.impl;

import java.util.Date;

import org.springframework.mail.SimpleMailMessage;

import lam.mail.model.LamMail;
import lam.mail.send.LamMailSender;
import lam.schedule.SupportSchedule;

/**
* <p>
* send mail schedule
* </p>
* @author linanmiao
* @date 2017年5月9日
* @version 1.0
*/
public class LamMailSchedule implements SupportSchedule{
	
	private LamMailSender lamMailSender;

	private SimpleMailMessage templateMessage;
	
	public void setLamMailSender(LamMailSender lamMailSender) {
		this.lamMailSender = lamMailSender;
	}
	
	public void setTemplateMessage(SimpleMailMessage templateMessage) {
		this.templateMessage = templateMessage;
	}
	
	@Override
	public void run() {
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

	@Override
	public void cancelNext(Date fromDate, Date toDate) {
		// TODO Auto-generated method stub
		
	}

}

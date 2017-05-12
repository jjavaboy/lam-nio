package lam.schedule.launch;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

import lam.mail.model.LamMail;
import lam.mail.send.LamMailSender;
import lam.schedule.constant.Constant;

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
	
	private static Logger logger = LoggerFactory.getLogger(LamScheduleEmitter.class);

	private LamMailSender lamMailSender;
	
	private SimpleMailMessage templateMessage;
	
	@Override
	public void emit() {
		String scheduleTest = System.getProperty(Constant.JVM_CM_SCHEDULE_TEST);
		String logBuilder = String.format("-D%s=%s", Constant.JVM_CM_SCHEDULE_TEST, scheduleTest);
		if(!Boolean.TRUE.toString().equals(scheduleTest)){
			logger.info(logBuilder + "==>>do not emit test");
			return ;
		}
		logger.info(logBuilder + "==>>emit test");
		LamMail lamMail = new LamMail();
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(templateMessage.getFrom());
		simpleMailMessage.setTo(templateMessage.getTo());
		simpleMailMessage.setSubject(templateMessage.getSubject() + "test");
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

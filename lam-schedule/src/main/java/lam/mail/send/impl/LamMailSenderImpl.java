package lam.mail.send.impl;

import java.util.Properties;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import lam.mail.model.LamMail;
import lam.mail.send.LamMailSender;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年5月8日
* @version 1.0
*/
@Service(value="lamMailSender")
public class LamMailSenderImpl extends JavaMailSenderImpl implements LamMailSender{
	
	private static Logger logger = LoggerFactory.getLogger(LamMailSenderImpl.class);

	@Resource(name="javaMailSenderImpl")
	private JavaMailSenderImpl javaMailSenderImpl;
	
	@Resource(name="templateMessage")
	private SimpleMailMessage templateMessage;
	
	@Override
	public boolean send(LamMail lamMail) {
		boolean result;
		SimpleMailMessage mailMessage = new SimpleMailMessage(templateMessage);
		mailMessage.setFrom("");
		mailMessage.setTo("");
		mailMessage.setSubject("First Subject");
		mailMessage.setText("You're the lucky one.");
		
		try{
			Properties javaMailProperties = new Properties();
			javaMailProperties.put("mail.smtp.auth", true);  
			javaMailProperties.put("mail.smtp.ssl.enable", true);  
			javaMailProperties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
			javaMailProperties.put("mail.smtp.timeout", 25000);
			
			javaMailSenderImpl.setJavaMailProperties(javaMailProperties);
			javaMailSenderImpl.send(mailMessage);
			result = Boolean.TRUE.booleanValue();
		}catch(Exception e){
			logger.error("send mail fail", e);
			result = Boolean.FALSE.booleanValue();
		}
		logger.info("send mail, param:{} ==>> result:{}", mailMessage, result);
		return result;
	}

}

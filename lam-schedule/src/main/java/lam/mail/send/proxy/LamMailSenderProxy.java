package lam.mail.send.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lam.mail.model.LamMail;
import lam.mail.send.LamMailSender;

/**
* <p>
* LamMailSender proxy class
* </p>
* @author linanmiao
* @date 2017年5月8日
* @versio 1.0
*/
public class LamMailSenderProxy implements LamMailSender{
	
	private static Logger logger = LoggerFactory.getLogger(LamMailSenderProxy.class);
	
	private long time;
	
	private LamMailSender lamMailSender;
	
	public LamMailSenderProxy(LamMailSender lamMailSender){
		this.lamMailSender = lamMailSender;
	}

	@Override
	public boolean send(LamMail lamMail) {
		this.start();
		boolean result = lamMailSender.send(lamMail);
		long timeCost = this.endAndCost();
		logger.info("send mail, param:{} ==>> result:{}, timeCost(ms):{}", 
				lamMail.getSimpleMailMessage(), result, timeCost);
		return result;
	}
	
	public long start(){
		this.time = now();
		return this.time;
	}
	
	public long endAndCost(){
		long end = now();
		long tempTime = this.time;
		this.time = end;
		return end - tempTime;
	}
	
	private long now(){
		return System.currentTimeMillis();
	}

}

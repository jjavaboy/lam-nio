package lam.mail.send.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lam.log.LogSupport;
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
public class LamMailSenderProxy extends LogSupport implements LamMailSender{
	
	private static Logger logger = LoggerFactory.getLogger(LamMailSenderProxy.class);
	
	private LamMailSender lamMailSender;
	
	public LamMailSenderProxy(LamMailSender lamMailSender){
		this.lamMailSender = lamMailSender;
	}

	@Override
	public boolean send(LamMail lamMail) {
		super.start();
		boolean result = lamMailSender.send(lamMail);
		long timeCost = super.endAndCost();
		logger.info("send mail, param:{} ==>> result:{}, timeCost(ms):{}", 
				lamMail.getSimpleMailMessage(), result, timeCost);
		return result;
	}

}

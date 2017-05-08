package lam.schedule.launch;

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
	
	@Override
	public void emit() {
		LamMail lamMail = new LamMail();
		lamMailSender.send(lamMail);
	}
	
	public void setLamMailSender(LamMailSender lamMailSender) {
		this.lamMailSender = lamMailSender;
	}

}

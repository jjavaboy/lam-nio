package lam.mail.send;

import lam.mail.model.LamMail;

/**
* <p>
* send email interface
* </p>
* @author linanmiao
* @date 2017年5月8日
* @version 1.0
*/
public interface LamMailSender {
	
	/**
	 * send mail.
	 * @param lamMail mail model
	 * @return true means success, otherwise, fail.
	 */
	public boolean send(LamMail lamMail);

}

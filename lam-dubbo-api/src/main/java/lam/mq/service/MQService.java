package lam.mq.service;

import lam.mq.model.MQessage;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月3日
* @version 1.0
*/
public interface MQService {
	
	public boolean sendQueue(MQessage message);
	
	public boolean sendTopic(MQessage message);

}

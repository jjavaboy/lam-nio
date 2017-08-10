package lam.dao.id.service;

import lam.dao.id.model.Sequence;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月10日
* @version 1.0
*/
public interface SequenceService {
	
	public Sequence getSequence(String name);
	
	//public boolean insert(Sequence sequence);
	
	//public boolean updateStepIfValueEquals(String name, int step, long oldValue, long newValue);

}

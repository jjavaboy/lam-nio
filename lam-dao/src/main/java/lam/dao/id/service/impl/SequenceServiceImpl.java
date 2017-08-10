package lam.dao.id.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import lam.dao.id.dao.SequenceDao;
import lam.dao.id.model.Sequence;
import lam.dao.id.service.SequenceService;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月10日
* @version 1.0
*/
public class SequenceServiceImpl implements SequenceService {
	
	@Autowired
	private SequenceDao sequenceDao;
	
	public void setSequenceDao(SequenceDao sequenceDao) {
		this.sequenceDao = sequenceDao;
	}

	@Override
	public Sequence getSequence(String name) {
		Sequence sequence = sequenceDao.getSequence(name);
		if(sequence == null){
			sequence = new Sequence();
			sequence.setName(name);
			sequence.setStep(Sequence.DEFAULT_STEP);
			sequence.setOldValue(0);
			sequence.setNewValue(0);
			this.insert(sequence);
			sequence = sequenceDao.getSequence(name);
		}
		//spin lock
		while(!updateStepIfValueEquals(sequence.getName(), sequence.getStep(), sequence.getOldValue(), sequence.getNewValue())){
			sequence = sequenceDao.getSequence(name);
		}
		Sequence newSequence = new Sequence();
		newSequence.setName(sequence.getName());
		newSequence.setStep(sequence.getStep());
		newSequence.setOldValue(sequence.getNewValue());
		newSequence.setNewValue(sequence.getNewValue() + sequence.getStep());
		newSequence.setCreatedTime(sequence.getCreatedTime());
		newSequence.setUpdatedTime(sequence.getUpdatedTime());
		return newSequence;
	}

	private boolean insert(Sequence sequence) {
		if(sequence.getCreatedTime() == null || sequence.getUpdatedTime() == null){
			sequence.setCreatedTime(new Date());
			sequence.setUpdatedTime(sequence.getCreatedTime());
		}
		return sequenceDao.insert(sequence) == 1;
	}

	private boolean updateStepIfValueEquals(String name, int step, long oldValue, long newValue) {
		Sequence sequence = new Sequence();
		sequence.setName(name);
		sequence.setStep(step);
		sequence.setOldValue(oldValue);
		sequence.setNewValue(newValue);
		sequence.setUpdatedTime(new Date());
		return sequenceDao.updateStep(sequence) == 1;
	}

}

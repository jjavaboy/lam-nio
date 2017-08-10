package lam.dao.id.dao;

import lam.dao.id.model.Sequence;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月10日
* @version 1.0
*/
@MyBatisDao
public interface SequenceDao {
	
	public Sequence getSequence(String name);
	
	public int insert(Sequence sequence);
	
	public int updateStep(Sequence sequence);

}

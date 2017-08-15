package lam.dao.id.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class IdSequence {
	
	private static volatile IdSequence INSTANCE;
	
	private Logger logger = LoggerFactory.getLogger(IdSequence.class);
	
	/**
	 * best to be less than 200,
	 */
	private int cacheSize;
	
	private SequenceService sequenceService;
	
	private ConcurrentHashMap<String, ConcurrentLinkedQueue<Long>> sequenceQueue;
	
	private IdSequence(){
		cacheSize = 50;
		sequenceService = SpringContextHolder.getBean(SequenceService.class);
		sequenceQueue = new ConcurrentHashMap<String, ConcurrentLinkedQueue<Long>>();
	}
	
	public static IdSequence getInstance(){
		if(INSTANCE == null){
			synchronized (IdSequence.class) {
				if(INSTANCE == null){
					INSTANCE = new IdSequence();
				}
			}
		}
		return INSTANCE;
	}
	
	/**
	 * <b>
	 * It's better to use method nextId(Class<?> clazz).
	 * <b/>
	 * @param sequenceName
	 * @return id value
	 */
	public Long nextId(String sequenceName){
		ConcurrentLinkedQueue<Long> idQueue = sequenceQueue.get(sequenceName);
		if(idQueue == null){
			//putIfAbsent method ensure synchronized
			sequenceQueue.putIfAbsent(sequenceName, new ConcurrentLinkedQueue<Long>());
			idQueue = sequenceQueue.get(sequenceName);
		}
		//double check id queue size
		if(idQueue.size() < cacheSize){
			synchronized(idQueue){
				if(idQueue.size() < cacheSize){
					Sequence sequence = sequenceService.getSequence(sequenceName);
					if(sequence == null)
						throw new NullPointerException("Can't get Sequence from name " + sequenceName);
					long id = sequence.getOldValue();
					while(id < sequence.getNewValue()){
						idQueue.offer(Long.valueOf(id++));
					}
				}
			}
		}
		Long idValue = idQueue.poll();
		if(idValue == null){
			//throw new NullPointerException("Can't get id value from Sequeue named " + sequenceName);
			logger.warn("Can't get id value from Sequeue named " + sequenceName);
			//spin to get next id
			idValue = nextId(sequenceName);
		}
		
		return idValue;
	}
	
	public Long nextId(Class<?> clazz){
		return nextId(clazz.getName());
	}

}

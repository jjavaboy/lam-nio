package org.lam.hongbao.core.constant;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年1月19日
* @version 1.0
*/
public class CacheKeys {
	
	public static final String KEY_SPLIT = ":";
	
	private static final String HONGBAO_QUEUE_CONSUME = "hongbao:queue:consume";
	
	private static final String HONGBAO_QUEUE_UNCONSUME = "hongbao:queue:unconsume";
	
	private static final String HONGBAO_MAP_TAKE = "hongbao:map:take";
	
	public static String hongbaoQueueConsumeKey(long hongbaoId){
		return HONGBAO_QUEUE_CONSUME + KEY_SPLIT + hongbaoId;
	}
	
	public static String hongbaoQueueUnConsumeKey(long hongbaoId){
		return HONGBAO_QUEUE_UNCONSUME + KEY_SPLIT + hongbaoId;
	}
	
	public static String hongbaoMapTakeKey(long hongbaoId){
		return HONGBAO_MAP_TAKE + KEY_SPLIT + hongbaoId;
	}

}

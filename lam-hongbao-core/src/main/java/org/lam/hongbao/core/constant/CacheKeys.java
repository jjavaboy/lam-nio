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
	
	/** 已经抢过红包的map*/
	private static final String HONGBAO_MAP_TAKEN = "hongbao:map:taken";
	
	private static final String HONGBAO_TAKEING = "hongbao:taking:%d:%d";
	
	public static String hongbaoQueueConsumeKey(long hongbaoId){
		return HONGBAO_QUEUE_CONSUME + KEY_SPLIT + hongbaoId;
	}
	
	public static String hongbaoQueueUnConsumeKey(long hongbaoId){
		return HONGBAO_QUEUE_UNCONSUME + KEY_SPLIT + hongbaoId;
	}
	
	public static String hongbaoMapTakenKey(long hongbaoId){
		return HONGBAO_MAP_TAKEN + KEY_SPLIT + hongbaoId;
	}
	
	public static String hongbaoTakingKey(long hongbaoId, long userId){
		return String.format(HONGBAO_TAKEING, hongbaoId, userId);
	}
	
	public static void main(String[] args){
		System.out.println(Long.MAX_VALUE);
		System.out.println(hongbaoTakingKey(Long.MAX_VALUE, 0));
	}

}

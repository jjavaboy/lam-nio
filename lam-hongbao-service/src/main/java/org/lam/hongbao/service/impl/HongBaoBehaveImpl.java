package org.lam.hongbao.service.impl;

import java.util.List;

import org.lam.hongbao.core.constant.CacheKeys;
import org.lam.hongbao.core.model.HongBao;
import org.lam.hongbao.core.model.HongBaoRecord;
import org.lam.hongbao.core.service.HongBaoBehave;
import org.lam.hongbao.service.factory.HongBaoFactory;
import org.lam.redis.client.RedisClient;

import redis.clients.jedis.Jedis;

/**
* <p>
* hong bao behave
* </p>
* @author linanmiao
* @date 2017年1月19日
* @version 1.0
*/
public class HongBaoBehaveImpl implements HongBaoBehave{

	@Override
	public boolean issueHongBao(HongBao hongBao) {
		List<HongBaoRecord> recordList = HongBaoFactory.generateRandomMoneyHongBao(hongBao);
		RedisClient client = new RedisClient("192.168.204.127", 6378, null);
		Jedis jedis = client.getResource();
		try{
		String key = CacheKeys.hongbaoQueueUnConsumeKey(hongBao.getId());
		boolean exists = jedis.exists(key);
		if(exists){
			jedis.del(key);
		}
		String[] recordArray = toStringArray(recordList);
		jedis.lpush(key, recordArray);
		}finally{
			client.close(jedis);
			client.close();
		}
		return false;
	}
	
	private String[] toStringArray(List<HongBaoRecord> recordList){
		String[] array = new String[recordList.size()];
		for(int idx = 0; idx < recordList.size(); idx++){
			array[idx] = recordList.get(idx).toString();
		}
		return array;
	}

}

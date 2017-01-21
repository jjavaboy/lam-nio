package org.lam.hongbao.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.lam.hongbao.core.constant.CacheKeys;
import org.lam.hongbao.core.constant.Status;
import org.lam.hongbao.core.model.HongBao;
import org.lam.hongbao.core.model.HongBaoRecord;
import org.lam.hongbao.core.service.HongBaoBehave;
import org.lam.hongbao.core.service.HongBaoRecordService;
import org.lam.hongbao.core.service.HongBaoService;
import org.lam.hongbao.service.concurrent.DistributedExecutor;
import org.lam.hongbao.service.factory.HongBaoFactory;
import org.lam.redis.client.RedisClient;

import com.google.gson.Gson;

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

	private HongBaoService hongBaoService;
	
	private HongBaoRecordService hongBaoRecordService;
	
	RedisClient client = new RedisClient("192.168.204.127", 6378, null);

	@Override
	public boolean issueHongBao(HongBao hongBao) {
		List<HongBaoRecord> recordList = HongBaoFactory.generateRandomMoneyHongBao(hongBao);
		for(HongBaoRecord record : recordList){
			hongBaoRecordService.saveHongBaoRecord(record);
		}
		
		Jedis jedis = client.getResource();
		try{
			String key = CacheKeys.hongbaoQueueUnConsumeKey(hongBao.getId());
			boolean exists = jedis.exists(key);
			if(exists){
				jedis.del(key);
			}
			String[] recordArray = toJsonArray(recordList);
			jedis.lpush(key, recordArray);
		}finally{
			client.close(jedis);
		}
		return false;
	}
	
	private String[] toJsonArray(List<HongBaoRecord> recordList){
		String[] array = new String[recordList.size()];
		Gson gson = new Gson();
		for(int idx = 0; idx < recordList.size(); idx++){
			array[idx] = gson.toJson(recordList.get(idx));
		}
		return array;
	}

	@Override
	public boolean takeHongBao(long userId, long hongbaoId) {
		final Jedis jedis = client.getResource();
		try{
			HongBao hongBao = hongBaoService.findHongBao(hongbaoId);
			if(hongBao == null || hongBao.getStatus() == Status.HongBao.CONSUME.getValue()){
				return false;
			}
			String hongbaoRecordId = jedis.hget(CacheKeys.hongbaoMapTakenKey(hongbaoId), String.valueOf(userId));
			if(StringUtils.isNotBlank(hongbaoRecordId)){
				return false;
			}
			final long fUserId = userId, fHongbaoId = hongbaoId;
			boolean take = new DistributedExecutor(CacheKeys.hongbaoTakingKey(hongbaoId, userId)){
				@Override
				public boolean execute() {
					String recordStr = jedis.lpop(CacheKeys.hongbaoQueueUnConsumeKey(fHongbaoId));
					if(StringUtils.isBlank(recordStr)){
						return false;
					}
					Gson gson = new Gson();
					HongBaoRecord record = gson.fromJson(recordStr, HongBaoRecord.class);
					record.setUserId(fUserId);
					
					jedis.lpush(CacheKeys.hongbaoQueueConsumeKey(fHongbaoId), gson.toJson(record));
					return true;
			}}.run();
			
			return take;
		}finally{
			client.close(jedis);
		}
	}

	@Override
	public boolean finalizeHongBao(long hongbaoId) {
		Jedis jedis = client.getResource();
		try{
			String consumeHongbaoKey = CacheKeys.hongbaoQueueConsumeKey(hongbaoId);
			long consumeHongbaoLen = jedis.llen(consumeHongbaoKey);
			if(consumeHongbaoLen == 0){
				return true;
			}
			Gson gson = new Gson();
			Date now = new Date();
			while(consumeHongbaoLen-- > 0){
				String recordStr = jedis.lpop(consumeHongbaoKey);
				HongBaoRecord record = gson.fromJson(recordStr, HongBaoRecord.class);
				record.setStatus(Status.HongBaoRecord.CONSUME.getValue());
				record.setUpdateTime(now);
				hongBaoRecordService.updateHongBaoRecord(record);
			}
			String unConsumeHongbaoKey = CacheKeys.hongbaoQueueUnConsumeKey(hongbaoId);
			long unConsumeHongbaoLen = jedis.llen(unConsumeHongbaoKey);
			if(unConsumeHongbaoLen == 0){
				HongBao hongBao = hongBaoService.findHongBao(hongbaoId);
				hongBao.setStatus(Status.HongBao.CONSUME.getValue());
				hongBao.setUpdateTime(new Date());
				hongBaoService.updateHongBao(hongBao);
			}
			return true;
		}finally{
			client.close(jedis);
		}
	}

}

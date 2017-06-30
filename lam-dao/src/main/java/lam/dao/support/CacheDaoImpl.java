package lam.dao.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

import org.lam.redis.client.RedisClient;

import com.google.gson.Gson;

import lam.dao.support.annotation.Expire;
import lam.dao.support.annotation.Uid;
import redis.clients.jedis.Jedis;

/**
* <p>
* cache dao
* </p>
* @author linanmiao
* @date 2017年6月29日
* @version 1.0
*/
public class CacheDaoImpl<T> implements CacheDao<T>{
	
	private Gson gson = new Gson();
	
	private Random random = new Random();
	
	private Class<T> clazz;
	
	private String keyPrefix;
	
	private RedisClient redisClient;
	
	private Dao<T> dao;
	
	public void setRedisClient(RedisClient redisClient) {
		this.redisClient = redisClient;
	}
	
	public CacheDaoImpl(Class<T> clazz, String keyPrefix){
		checkAnnotation(Uid.class);
		checkAnnotation(Expire.class);
		this.clazz = clazz;
		this.keyPrefix = keyPrefix;
	}

	@Override
	public boolean set(T t) {
		Object uidValue = getFieldValue(t, getAnnotationField(Uid.class));
		Jedis jedis = redisClient.getResource();
		try{
			//random time:0 - 10 minute
			setFieldValue(t, Expire.class, generateRandomExpire(10));
			String rt = jedis.set(toKey(uidValue), gson.toJson(t));
			return "OK".equals(rt) ;
		}finally{
			redisClient.close(jedis);
		}
	}

	@Override
	public T get(Long id) {
		Jedis jedis = redisClient.getResource();
		try{
			String json = jedis.get(toKey(id));
			T t = gson.fromJson(json, clazz);
			resetCacheIfExpire(t, id);
			return t;
		}finally{
			redisClient.close(jedis);
		}
	}
	
	@Override
	public boolean delete(Long id) {
		Jedis jedis = redisClient.getResource();
		try{
			Long rt = jedis.del(toKey(id));
			return rt != null && rt.longValue() > 0;
		}finally{
			redisClient.close(jedis);
		}
	}
	
	private void checkAnnotation(Class<? extends Annotation> annotation){
		for(Field field : clazz.getDeclaredFields()){
			if(Modifier.isAbstract(field.getModifiers()) || Modifier.isTransient(field.getModifiers())){
				continue ;
			}
			if(field.getAnnotation(annotation) != null){
				return ;
			}
		}
		throw new IllegalArgumentException("field of " + clazz.getName() + " do not has annotation:" + annotation.getName());
	}
	
	private String toKey(Object id){
		Objects.requireNonNull(id, "id can not be null");
		return toKey(Long.parseLong(id.toString()));
	}
	
	private String toKey(Long id){
		Objects.requireNonNull(id, "id can not be null");
		return this.keyPrefix + ":" + id;
	}
	
	private void makeAccessible(Field field){
		if(!field.isAccessible()){
			field.setAccessible(true);
		}
	}
	
	/** 生成3天有效期 并随机加多randomMinutes分钟可以避免 多个缓存同时失效 
	 * @param randomMinutes 随机的分钟数，最后会换算成秒
	 * **/
	private long generateRandomExpire(int randomMinutes){
		return System.currentTimeMillis() + 1000 * 60 * 60 * 3 + random.nextInt(1000 * 60 * randomMinutes);
	}
	
	private T resetCacheIfExpire(T t, Long id){
		Object v = getFieldValue(t, getAnnotationField(Expire.class));
		Date date = new Date(Long.parseLong(v.toString()));
		if(!date.before(new Date())){
			//reset cache
			synchronized (toKey(id)) {
				T data = dao.get(id);
				setFieldValue(data, Expire.class, generateRandomExpire(10));
				this.set(data);
				return data;
			}
		}
		return t;
	}
	
	private Field getAnnotationField(Class<? extends Annotation> annotation){
		Field f = null;
		for(Field field : clazz.getDeclaredFields()){
			if(field.getAnnotation(annotation) != null){
				f = field;
			}
		}
		return f;
	}
	
	private Object getFieldValue(Object object, Field f){
		if(f == null){
			return null;
		}
		Object v = null;
		try {
			makeAccessible(f);
			v = f.get(object);
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		return v;
	}
	
	private void setFieldValue(Object object, Class<? extends Annotation> annotation, Object value){
		Field field = getAnnotationField(annotation);
		try {
			makeAccessible(field);
			field.set(object, value);
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}

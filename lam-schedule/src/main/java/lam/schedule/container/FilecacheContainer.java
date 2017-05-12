package lam.schedule.container;

import java.io.File;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import lam.rpcframework.container.LContainer;
import lam.schedule.cache.LCache;
import lam.schedule.cache.impl.FileCache;
import lam.schedule.constant.Constant;
import lam.schedule.util.SystemProperties;

/**
* <p>
* file cache container
* </p>
* @author linanmiao
* @date 2017年5月11日
* @version 1.0
*/
public class FilecacheContainer implements LContainer{
	
	private LCache cache;

	@Override
	public void start() {
		String cacheFile = SystemProperties.getProperty(Constant.JVM_CM_CACHE_FILE);
		Objects.requireNonNull(cacheFile, "system parameter " + Constant.JVM_CM_CACHE_FILE + " value cann't be null");
		cache = new FileCache(cacheFile);
		cache.init();
	}

	@Override
	public void close() {
		cache.clear();
	}
	
	public String get(String key){
		return cache.get(key);
	}
	
	public void set(String key, String value){
		cache.set(key, value);
	}
	
	public void remove(String key){
		cache.remove(key);
	}
	
	public boolean contains(String key){
		return cache.contains(key);
	}

}

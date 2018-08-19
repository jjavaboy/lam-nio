package lam.cache;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.google.common.collect.Maps;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年8月19日
* @versio 1.0
*/
public class DemoEhcacheService {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoEhcacheService.class);
	
	private static final String DEMO_CACHE_NAME = "demoCache";
	
    private static final String DEMO_CAHCE_KEY1 = "key1";
    private static final String DEMO_CAHCE_KEY2 = "key2";
	
    @Autowired
    private CacheManager cacheManager;


    private Cache getDemoCache() {
        Cache demoCache = cacheManager.getCache(DEMO_CACHE_NAME);
        if (demoCache == null) {
            LOGGER.warn("[getDemoCache] demoCache is null");
            return null;
        }
        return demoCache;
    }
    
    public void refresh() {
        Cache demoCache = getDemoCache();
        if (demoCache == null) {
            return;
        }

        Map<String, String> value1 = new HashMap<String, String>();
        Map<String, String> value2 = new HashMap<String, String>();
        
        //fill key-value into value1 value2 in your business
        //...
        
        demoCache.put(DEMO_CAHCE_KEY1, value1);
        demoCache.put(DEMO_CAHCE_KEY2, value2);
    }

}

package org.lam.redis.tool;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import redis.clients.jedis.Jedis;

/**
 * @author: linanmiao
 */
public class DataAgent {

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            2,
            2,
            0,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
    );

    private static Logger LOGGER = LoggerFactory.getLogger(DataAgent.class);

    private static final Gson gson = new Gson();

    private Jedis jedis;

    public <T> T get(String key, Class<T> clazz, DataGetter<T> dataGetter) {
        String value = jedis.get(key);
        if(StringUtils.isNotBlank(value)) {
            return gson.fromJson(value, clazz);
        }
        T t = dataGetter.get();
        value = gson.toJson(t);

        collect(key, value);

        jedis.set(key, value);
        return t;
    }

    private void collect(String key, String value) {
        int length = value.length();
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }
}

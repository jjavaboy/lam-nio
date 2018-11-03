package org.lam.redis.tool;

/**
 * @author: linanmiao
 */
public interface DataGetter<T>{

    <T> T get();

}

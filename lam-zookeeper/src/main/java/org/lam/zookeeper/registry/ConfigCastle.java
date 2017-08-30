package org.lam.zookeeper.registry;

import java.util.Objects;
import java.util.Properties;

import lam.util.SConfigCastle;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月29日
* @version 1.0
*/
public class ConfigCastle {
	
	private static final String ZOOKEEPER_FILE = "zookeeper.properties";
	
	public static String getZookeeperProperties(String key){
		Properties p = SConfigCastle.getProperties(ZOOKEEPER_FILE);
		Objects.requireNonNull(p, ZOOKEEPER_FILE + " is null");
		return SConfigCastle.getValue(p, key);
	}
	
	public static int getZookeeperInt(String key){
		String value = getZookeeperProperties(key);
		
		return Integer.parseInt(value);
	}

}

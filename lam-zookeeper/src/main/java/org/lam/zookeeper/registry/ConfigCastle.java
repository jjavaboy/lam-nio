package org.lam.zookeeper.registry;

import lam.util.SConfigCastle;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月29日
* @version 1.0
*/
public class ConfigCastle extends SConfigCastle{
	
	private final String[] CONFIG_FILES = {"zookeeper.properties"};
	
	private static class InstanceHolder{
		private static ConfigCastle INSTANCE = new ConfigCastle();
	}
	
	public static ConfigCastle getInstance(){
		return InstanceHolder.INSTANCE;
	}
	
	@Override
	public String[] getPropertyFiles() {
		return CONFIG_FILES;
	}

}

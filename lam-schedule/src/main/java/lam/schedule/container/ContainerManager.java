package lam.schedule.container;

import java.io.Closeable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lam.rpcframework.container.LContainer;
import lam.util.FinalizeUtils;

/**
* <p>
* container manager
* </p>
* @author linanmiao
* @date 2017年5月11日
* @version 1.0
*/
public class ContainerManager implements Closeable{
	
	private static Logger logger = LoggerFactory.getLogger(ContainerManager.class);
	
	private ContainerManager(){}
	
	private static class ContainerManagerHolder{
		private static ContainerManager INSTANCE = new ContainerManager();
	}
	
	public static ContainerManager getInstance(){
		return ContainerManagerHolder.INSTANCE;
	}
	
	private ConcurrentMap<Type, LContainer> map = new ConcurrentHashMap<Type, LContainer>();
	
	public void loadContainer(String commandValue){
		if(StringUtils.isBlank(commandValue)){
			return ;
		}
		String[] containers = commandValue.split(",");
		checkContainer(containers);
		for(String c : containers){
			try{
				LContainer lc = toContainer(c);
				add(Type.toType(c), lc);
				logger.info("loadContainer, container:{}, class:{}.", c, lc.getClass().getName());
				lc.start();
			}catch(Exception e){
				throw new RuntimeException("create container fail:" + c);
			}
		}
	}
	
	public void close(){
		Set<Type> types = map.keySet();
		for(Type type : types){
			FinalizeUtils.closeNotQuietly(map.get(type));
		}
	}
	
	private LContainer toContainer(String container) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		String className = "lam.schedule.container." + container.substring(0, 1).toUpperCase() + container.substring(1) + "Container";
		Class<?> clazz = Class.forName(className);
		return (LContainer) clazz.newInstance();
	}
	
	private void checkContainer(String[] containers){
		for(String type : containers){
			if(!Type.contain(type)){
				throw new IllegalArgumentException("Not support container:" + type);
			}
		}		
	}
	
	public void add(Type type, LContainer container){
		map.putIfAbsent(type, container);
	}
	
	public LContainer get(Type type){
		return map.get(type);
	}
	
	public enum Type{
		
		FILECACHE("filecache");
		
		private String value;
		
		private Type(String value){
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		public static boolean contain(String value){
			return toType(value) != null ? true : false;
		}
		
		public static Type toType(String value){
			for(Type t : values()){
				if(t.value.equals(value)){
					return t;
				}
			}
			return null;
		}
	}

}

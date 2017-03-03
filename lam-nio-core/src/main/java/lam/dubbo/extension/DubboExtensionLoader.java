package lam.dubbo.extension;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
* <p>
* 模拟dubbo的扩展类<br/>
* com.alibaba.dubbo.common.extension.ExtensionLoader<T>
* </p>
* @author linanmiao
* @date 2017年3月3日
* @version 1.0
*/
public class DubboExtensionLoader<T>{
	
	private static final ConcurrentMap<Class<?>, DubboExtensionLoader<?>> EXTENSION_LOADERS = 
			new ConcurrentHashMap<Class<?>, DubboExtensionLoader<?>>();
	
	private Class<?> type;
	private DubboExtensionFactory objectFactory;
	private final Holder<T> adaptiveInstanceHolder = new Holder<T>();
	
	private DubboExtensionLoader(Class<?> type){
		this.type = type;
		//this.objectFactory = type == DubboExtensionFactory.class ? null : 
	}
	
	public static <T> DubboExtensionLoader<T> getExtensionLoader(Class<T> type){
		if(type == null){
			throw new IllegalArgumentException("type is null");
		}
		if(!type.isInterface()){
			throw new IllegalArgumentException("type " + type.getName()  + " is not an interface");
		}
		if(!hasExtensionAnnotation(type)){
			throw new IllegalArgumentException("type " + type.getName() + " has not " + DubboSPI.class.getName() + " annotation");
		}
		DubboExtensionLoader<T> instance = (DubboExtensionLoader<T>) EXTENSION_LOADERS.get(type);
		if(instance == null){
			EXTENSION_LOADERS.putIfAbsent(type, new DubboExtensionLoader<T>(type));
			instance = (DubboExtensionLoader<T>) EXTENSION_LOADERS.get(type);
		}
		return instance;
	}
	
	private static boolean hasExtensionAnnotation(Class<?> clazz){
		return clazz.isAnnotationPresent(DubboSPI.class);
	}
	
	public T getAdaptiveExtension(){
		T value = adaptiveInstanceHolder.get();
		if(value == null){
			synchronized (adaptiveInstanceHolder) {
				value = adaptiveInstanceHolder.get();
				if(value == null){
					value = createAdaptiveExtension();
					adaptiveInstanceHolder.set(value);
				}
			}
		}
		return value;
	}

	private T createAdaptiveExtension() {
		
		return null;
	}
	
	private T injectExtension(T instance){
		if(objectFactory != null){
			
		}
		return instance;
	}

}

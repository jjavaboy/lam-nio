package lam.dubbo.extension;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;

import com.sun.xml.internal.txw2.IllegalAnnotationException;

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
	
	private static final String DUBBO_INTERNAL_DIRECTORY = "META-INF/dubbo/internal/";
	
	private static final ConcurrentMap<Class<?>, DubboExtensionLoader<?>> EXTENSION_LOADERS = 
			new ConcurrentHashMap<Class<?>, DubboExtensionLoader<?>>();
	
	private Class<?> type;
	private DubboExtensionFactory objectFactory;
	private final Holder<T> adaptiveInstanceHolder = new Holder<T>();
	private final Holder<Map<String, Class<?>>> classCacheHolder = new Holder<Map<String, Class<?>>>();
	private String cachedDefaultName;

	private volatile Class<?> adaptiveClass;
	private Set<Class<?>> wrapperClassSet;
	private final ConcurrentMap<Class<?>, String> cachedNames = new ConcurrentHashMap<Class<?>, String>();
	private final ConcurrentMap<String, Holder<Object>> instanceHolderCache = new ConcurrentHashMap<String, Holder<Object>>();
	private static final ConcurrentMap<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<Class<?>, Object>();
	
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
	
	public T getExtension(String name) {
		if(StringUtils.isBlank(name)){
			throw new IllegalArgumentException("name is null");
		}
		Holder<Object> holder = instanceHolderCache.get(name);
		if(holder == null){
			instanceHolderCache.putIfAbsent(name, new Holder<Object>());
			holder = instanceHolderCache.get(name);
		}
		Object instance = holder.get();
		if(instance == null){
			synchronized (holder) {
				instance = holder.get();
				if(instance == null){
					instance = createExtension(name);
					holder.set(instance);
				}
			}
		}
		return (T) instance;
	}
	
	public T getDefaultExtension(){
		getExtensionClasses();
		if(StringUtils.isBlank(cachedDefaultName)){
			return null;
		}
		T t = getExtension(cachedDefaultName);
		return t;
	}
	
	private Object createExtension(String name) {
		Class<?> clazz = getExtensionClasses().get(name);
		if(clazz == null){
			throw new IllegalArgumentException("Can not find extension class naming " + name);
		}
		try {
            T instance = (T) EXTENSION_INSTANCES.get(clazz);
            if (instance == null) {
                EXTENSION_INSTANCES.putIfAbsent(clazz, (T) clazz.newInstance());
                instance = (T) EXTENSION_INSTANCES.get(clazz);
            }
            injectExtension(instance);
            Set<Class<?>> wrapperClasses = wrapperClassSet;
            if (wrapperClasses != null && wrapperClasses.size() > 0) {
                for (Class<?> wrapperClass : wrapperClasses) {
                    instance = injectExtension((T) wrapperClass.getConstructor(type).newInstance(instance));
                }
            }
            return instance;
        } catch (Throwable t) {
            throw new IllegalStateException("Extension instance(name: " + name + ", class: " +
                    type + ")  could not be instantiated: " + t.getMessage(), t);
        }
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
		try {
			T t = injectExtension((T)getAdaptiveExtensionClass().newInstance());
			return t;
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot get adaptive extension type:" + type.getName()
				+ e.getMessage(), e);
		}
	}
	
	private T injectExtension(T instance){
		try {
            if (objectFactory != null) {
                for (Method method : instance.getClass().getMethods()) {
                    if (method.getName().startsWith("set")
                            && method.getParameterTypes().length == 1
                            && Modifier.isPublic(method.getModifiers())) {
                        Class<?> pt = method.getParameterTypes()[0];
                        try {
                            String property = method.getName().length() > 3 ? method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4) : "";
                            Object object = objectFactory.getExtension(pt, property);
                            if (object != null) {
                                method.invoke(instance, object);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
	}
	
	private Class<?> getAdaptiveExtensionClass(){
		getExtensionClasses();
		if(adaptiveClass != null){
			return adaptiveClass;
		}
		return adaptiveClass = createAdaptiveExtensionClass();
	}

	private Class<?> createAdaptiveExtensionClass() {
		String code = createAdaptiveExtensionClassCode();
		ClassLoader classLoader = findClassLoader();
		DubboCompiler compiler = DubboExtensionLoader.getExtensionLoader(DubboCompiler.class).getAdaptiveExtension();
		return compiler.compile(code, classLoader);
	}
	
    private String createAdaptiveExtensionClassCode() {
		StringBuilder codeBuidler = new StringBuilder();
        Method[] methods = type.getMethods();
        boolean hasAdaptiveAnnotation = false;
        for(Method m : methods) {
            if(m.isAnnotationPresent(DubboAdaptive.class)) {
                hasAdaptiveAnnotation = true;
                break;
            }
        }
        // 完全没有Adaptive方法，则不需要生成Adaptive类
        if(! hasAdaptiveAnnotation)
            throw new IllegalStateException("No adaptive method on extension " + type.getName() + ", refuse to create the adaptive class!");
        
        codeBuidler.append("package " + type.getPackage().getName() + ";");
        codeBuidler.append("\nimport " + DubboExtensionLoader.class.getName() + ";");
        codeBuidler.append("\npublic class " + type.getSimpleName() + "$Adpative" + " implements " + type.getCanonicalName() + " {");
        
        for (Method method : methods) {
            Class<?> rt = method.getReturnType();
            Class<?>[] pts = method.getParameterTypes();
            Class<?>[] ets = method.getExceptionTypes();

            DubboAdaptive adaptiveAnnotation = method.getAnnotation(DubboAdaptive.class);
            StringBuilder code = new StringBuilder(512);
            if (adaptiveAnnotation == null) {
                code.append("throw new UnsupportedOperationException(\"method ")
                        .append(method.toString()).append(" of interface ")
                        .append(type.getName()).append(" is not adaptive method!\");");
            } else {
                int urlTypeIndex = -1;
                for (int i = 0; i < pts.length; ++i) {
                    if (pts[i].equals(URL.class)) {
                        urlTypeIndex = i;
                        break;
                    }
                }
                // 有类型为URL的参数
                if (urlTypeIndex != -1) {
                    // Null Point check
                    String s = String.format("\nif (arg%d == null) throw new IllegalArgumentException(\"url == null\");",
                                    urlTypeIndex);
                    code.append(s);
                    
                    s = String.format("\n%s url = arg%d;", URL.class.getName(), urlTypeIndex); 
                    code.append(s);
                }
                // 参数没有URL类型
                else {
                    String attribMethod = null;
                    
                    // 找到参数的URL属性
                    LBL_PTS:
                    for (int i = 0; i < pts.length; ++i) {
                        Method[] ms = pts[i].getMethods();
                        for (Method m : ms) {
                            String name = m.getName();
                            if ((name.startsWith("get") || name.length() > 3)
                                    && Modifier.isPublic(m.getModifiers())
                                    && !Modifier.isStatic(m.getModifiers())
                                    && m.getParameterTypes().length == 0
                                    && m.getReturnType() == URL.class) {
                                urlTypeIndex = i;
                                attribMethod = name;
                                break LBL_PTS;
                            }
                        }
                    }
                    if(attribMethod == null) {
                        throw new IllegalStateException("fail to create adative class for interface " + type.getName()
                        		+ ": not found url parameter or url attribute in parameters of method " + method.getName());
                    }
                    
                    // Null point check
                    String s = String.format("\nif (arg%d == null) throw new IllegalArgumentException(\"%s argument == null\");",
                                    urlTypeIndex, pts[urlTypeIndex].getName());
                    code.append(s);
                    s = String.format("\nif (arg%d.%s() == null) throw new IllegalArgumentException(\"%s argument %s() == null\");",
                                    urlTypeIndex, attribMethod, pts[urlTypeIndex].getName(), attribMethod);
                    code.append(s);

                    s = String.format("%s url = arg%d.%s();",URL.class.getName(), urlTypeIndex, attribMethod); 
                    code.append(s);
                }
                
                String[] value = adaptiveAnnotation.value();
                // 没有设置Key，则使用“扩展点接口名的点分隔 作为Key
                if(value.length == 0) {
                    char[] charArray = type.getSimpleName().toCharArray();
                    StringBuilder sb = new StringBuilder(128);
                    for (int i = 0; i < charArray.length; i++) {
                        if(Character.isUpperCase(charArray[i])) {
                            if(i != 0) {
                                sb.append(".");
                            }
                            sb.append(Character.toLowerCase(charArray[i]));
                        }
                        else {
                            sb.append(charArray[i]);
                        }
                    }
                    value = new String[] {sb.toString()};
                }
                
                boolean hasInvocation = false;
                for (int i = 0; i < pts.length; ++i) {
                    if (pts[i].getName().equals("com.alibaba.dubbo.rpc.Invocation")) {
                        // Null Point check
                        String s = String.format("\nif (arg%d == null) throw new IllegalArgumentException(\"invocation == null\");", i);
                        code.append(s);
                        s = String.format("\nString methodName = arg%d.getMethodName();", i); 
                        code.append(s);
                        hasInvocation = true;
                        break;
                    }
                }
                
                String defaultExtName = cachedDefaultName;
                String getNameCode = null;
                for (int i = value.length - 1; i >= 0; --i) {
                    if(i == value.length - 1) {
                        if(null != defaultExtName) {
                            if(!"protocol".equals(value[i]))
                                if (hasInvocation) 
                                    getNameCode = String.format("url.getMethodParameter(methodName, \"%s\", \"%s\")", value[i], defaultExtName);
                                else
                                    getNameCode = String.format("url.getParameter(\"%s\", \"%s\")", value[i], defaultExtName);
                            else
                                getNameCode = String.format("( url.getProtocol() == null ? \"%s\" : url.getProtocol() )", defaultExtName);
                        }
                        else {
                            if(!"protocol".equals(value[i]))
                                if (hasInvocation) 
                                    getNameCode = String.format("url.getMethodParameter(methodName, \"%s\", \"%s\")", value[i], defaultExtName);
                                else
                                    getNameCode = String.format("url.getParameter(\"%s\")", value[i]);
                            else
                                getNameCode = "url.getProtocol()";
                        }
                    }
                    else {
                        if(!"protocol".equals(value[i]))
                            if (hasInvocation) 
                                getNameCode = String.format("url.getMethodParameter(methodName, \"%s\", \"%s\")", value[i], defaultExtName);
                            else
                                getNameCode = String.format("url.getParameter(\"%s\", %s)", value[i], getNameCode);
                        else
                            getNameCode = String.format("url.getProtocol() == null ? (%s) : url.getProtocol()", getNameCode);
                    }
                }
                code.append("\nString extName = ").append(getNameCode).append(";");
                // check extName == null?
                String s = String.format("\nif(extName == null) " +
                		"throw new IllegalStateException(\"Fail to get extension(%s) name from url(\" + url.toString() + \") use keys(%s)\");",
                        type.getName(), Arrays.toString(value));
                code.append(s);
                
                s = String.format("\n%s extension = (%<s)%s.getExtensionLoader(%s.class).getExtension(extName);",
                        type.getName(), DubboExtensionLoader.class.getSimpleName(), type.getName());
                code.append(s);
                
                // return statement
                if (!rt.equals(void.class)) {
                    code.append("\nreturn ");
                }

                s = String.format("extension.%s(", method.getName());
                code.append(s);
                for (int i = 0; i < pts.length; i++) {
                    if (i != 0)
                        code.append(", ");
                    code.append("arg").append(i);
                }
                code.append(");");
            }
            
            codeBuidler.append("\npublic " + rt.getCanonicalName() + " " + method.getName() + "(");
            for (int i = 0; i < pts.length; i ++) {
                if (i > 0) {
                    codeBuidler.append(", ");
                }
                codeBuidler.append(pts[i].getCanonicalName());
                codeBuidler.append(" ");
                codeBuidler.append("arg" + i);
            }
            codeBuidler.append(")");
            if (ets.length > 0) {
                codeBuidler.append(" throws ");
                for (int i = 0; i < ets.length; i ++) {
                    if (i > 0) {
                        codeBuidler.append(", ");
                    }
                    codeBuidler.append(ets[i].getCanonicalName());
                }
            }
            codeBuidler.append(" {");
            codeBuidler.append(code.toString());
            codeBuidler.append("\n}");
        }
        codeBuidler.append("\n}");
        /*if (logger.isDebugEnabled()) {
            logger.debug(codeBuidler.toString());
        }*/
        return codeBuidler.toString();
	}

	private Map<String, Class<?>> getExtensionClasses() {
		Map<String, Class<?>> classMap = classCacheHolder.get();
		if(classMap == null){
			synchronized (classCacheHolder) {
				classMap = classCacheHolder.get();
				if(classMap == null){
					classMap = loadExtensionClasses();
					classCacheHolder.set(classMap);
				}
			}
		}
		return classMap;
	}

	private Map<String, Class<?>> loadExtensionClasses() {
		final DubboSPI dubboSpi = type.getAnnotation(DubboSPI.class);
		if(dubboSpi != null){
			String val = dubboSpi.value();
			cachedDefaultName = val;
		}
		Map<String, Class<?>> extensionClasses = new HashMap<String, Class<?>>();
		//...
		loadFile(extensionClasses, DUBBO_INTERNAL_DIRECTORY);
		return extensionClasses;
	}

	private void loadFile(Map<String, Class<?>> extensionClasses, String dir) {
		String filename = dir + type.getName();
		try{
			java.util.Enumeration<java.net.URL> urls;
			ClassLoader classLoader = findClassLoader();
			if(classLoader != null){
				urls = classLoader.getResources(filename);
			}else{
				urls = ClassLoader.getSystemResources(filename);
			}
			if(urls == null){
				return ;
			}
			while(urls.hasMoreElements()){
				URL url = urls.nextElement();
				loadURL(classLoader, extensionClasses, url);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void loadURL(ClassLoader classLoader, Map<String, Class<?>> extensionClasses, URL url) {
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
			String line = null;
			while((line = reader.readLine()) != null){
				if(StringUtils.isBlank(line) || (line = line.trim()).indexOf('#') == 0){
					continue ;
				}
				int idx = line.indexOf('=');
				String name = null;
				if(idx > 0){
					name = line.substring(0, idx);
					line = line.substring(idx + 1);
				}
				if(!line.isEmpty()){
					Class<?> clazz = Class.forName(line, true, classLoader);
					if(!type.isAssignableFrom(clazz)){
						throw new IllegalStateException("Error when load extension class(interface: " +
                                type + ", class line: " + clazz.getName() + "), class " 
                                + clazz.getName() + "is not subtype of interface.");
					}
					if(clazz.isAnnotationPresent(DubboAdaptive.class)){
						if(adaptiveClass == null){
							adaptiveClass = clazz;
						}else if(!adaptiveClass.equals(clazz)){
							throw new IllegalStateException("More than 1 adaptive class found: "
                                    + adaptiveClass.getClass().getName()
                                    + ", " + clazz.getClass().getName());
						}
					}else{
						try{
							clazz.getConstructor(type);
							if(wrapperClassSet == null){
								wrapperClassSet = new HashSet<Class<?>>();
							}
							wrapperClassSet.add(clazz);
						}catch(NoSuchMethodException e){
							clazz.getConstructors();
							if(name != null && !name.isEmpty()){
								if(!cachedNames.containsKey(clazz)){
									cachedNames.putIfAbsent(clazz, name);
								}
								Class<?> cls = extensionClasses.get(name);
								if(cls == null){
									extensionClasses.put(name, clazz);
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(reader != null) reader.close();
			}catch(java.io.IOException e){}
		}
	}

	private ClassLoader findClassLoader(){
		return DubboExtensionLoader.class.getClassLoader();
	}

}

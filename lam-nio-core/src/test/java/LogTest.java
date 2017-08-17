import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.AsyncAppender;
import org.apache.log4j.helpers.NullEnumeration;
import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.Log4jLoggerAdapter;

import lam.util.Strings;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月17日
* @version 1.0
*/
public class LogTest {
	
	private static Logger logger = LoggerFactory.getLogger(LogTest.class);
	
	static{
		Object o1 = org.apache.log4j.ConsoleAppender.class;
		Object o11 = org.apache.log4j.WriterAppender.class;
		Object o12 = org.apache.log4j.AppenderSkeleton.class;
		
		Object o2 = org.apache.log4j.DailyRollingFileAppender.class;
		Object o21 = org.apache.log4j.FileAppender.class;
		Object o22 = org.apache.log4j.WriterAppender.class;
		Object o23 = org.apache.log4j.AppenderSkeleton.class;
		
		Object o3 = org.apache.log4j.AsyncAppender.class;
		Object o31 = org.apache.log4j.AppenderSkeleton.class;
		
		org.apache.log4j.Logger log4jLogger = null;
		try {
			Field field1 = Log4jLoggerAdapter.class.getDeclaredField("logger");
			if(!field1.isAccessible())
				field1.setAccessible(true);
			log4jLogger = (org.apache.log4j.Logger) field1.get(logger);
		} catch (NoSuchFieldException | SecurityException e2) {
			e2.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		Enumeration<?> e = log4jLogger.getParent().getAllAppenders();
		if(e != null && e != NullEnumeration.getInstance()){
			while(e.hasMoreElements()){
				Object o = e.nextElement();
				if(o instanceof AsyncAppender){
					Field field;
					try {
						field = AsyncAppender.class.getDeclaredField("buffer");
						if(!field.isAccessible())
							field.setAccessible(true);
						final List<?> buffer = (List<?>) field.get(o);
						Runtime.getRuntime().addShutdownHook(new Thread(){
							@Override
							public void run() {
								if(!Strings.isNullOrEmpty(buffer)){
									for(Object obj : buffer)
										System.out.println("not consume msg:" + ((LoggingEvent)obj).getMessage());
								}
							}
						});
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
	
	public static void main(String[] args){
		//Logger logger = LoggerFactory.getLogger(LogTest.class);
		//org.slf4j.impl.Log4jLoggerAdapter loggerAdapter = (Log4jLoggerAdapter) logger;
		
		
		for(int i = 0; i < 10000; i++){
			logger.info("log " + i);
		}
		System.exit(0);
	}
	
	private static class Foo{
		Integer id;
		
		public int getId(){
			return id;
		}
	}

}

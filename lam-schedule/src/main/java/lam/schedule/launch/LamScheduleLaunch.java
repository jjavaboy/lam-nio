package lam.schedule.launch;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import lam.mail.send.LamMailSender;
import lam.rpcframework.ExportFramework;
import lam.schedule.constant.Constant;

/**
* <p>
* launch class
* </p>
* @author linanmiao
* @date 2017年5月8日
* @version 1.0
*/
public class LamScheduleLaunch {
	
	private static Logger logger = LoggerFactory.getLogger(LamScheduleLaunch.class);
	
	static ClassPathXmlApplicationContext context;
	
	static ExportFramework exportFramework;
	
	static{
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				if(context != null){
					context.close();
				}
				logger.info("shutdown hook runs.");
			}
		});
	}
	
	public static void main(String[] args){
		context = new ClassPathXmlApplicationContext(new String[]{"spring-context.xml"});
		context.start();
		logger.info("spring started.");
		
		LamMailSender lamMailSenderProxy = context.getBean("lamMailSenderProxy", LamMailSender.class);
		
		int port = 6666;
		if(StringUtils.isNotBlank(System.getProperty(Constant.JVM_CM_RPC_PORT))){
			try{
				port = Integer.parseInt(System.getProperty(Constant.JVM_CM_RPC_PORT));
			}catch(NumberFormatException n){
			}
		}
	
		ExportFramework exportFramework = new ExportFramework();
		try {
			exportFramework.export(lamMailSenderProxy, port);
		} catch (Exception e1) {
			logger.error("ExportFramework export fail", e1);
			System.exit(-1);
		}
		synchronized (LamScheduleLaunch.class) {
			try {
				LamScheduleLaunch.class.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

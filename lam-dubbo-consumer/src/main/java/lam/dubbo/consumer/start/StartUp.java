package lam.dubbo.consumer.start;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import lam.dubbo.api.UserService;

/**
* <p>
* consumer main class
* </p>
* @author linanmiao
* @date 2016年10月30日
* @versio 1.0
*/
public class StartUp {
	
	public static void main(String[] args){
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"dubbo-lam-consumer.xml"});
        context.start();
 
        UserService userService = (UserService)context.getBean("userService"); // 获取远程服务代理
        String hello = userService.sayHello("linanmiao"); // 执行远程方法
 
        System.out.println( hello ); // 显示调用结果
        try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
        context.close();
	}

}

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.com.caucho.hessian.io.Hessian2Output;
import com.alibaba.dubbo.rpc.service.EchoService;

import lam.dubbo.banka.user.service.UserService;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年4月11日
* @version 1.0
*/
public class Test {
	
	public static void main(String[] args){
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dubbo-lam-consumer-test.xml");
		context.start();
		
		UserService userService = (UserService) context.getBean("userService");
		
		
		for(int i = 0; i < 3; i++){
			boolean r = userService.decreaseUserMoneyCrossBank(1, 2, 100);
			System.out.println("i:" + i + ", result:" + r);
		}
		try {
			TimeUnit.MILLISECONDS.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		context.close();
		
		
	}

}

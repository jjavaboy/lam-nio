package lam.spring.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年6月16日
* @versio 1.0
*/
//@SpringBootApplication, same as @Configuration, @EnableAutoConfiguration and @ComponentScan together.
//自定义的httpMessageConverter和ViewResolver可在lam.spring.boot.config.MyConfiguration在添加。
@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

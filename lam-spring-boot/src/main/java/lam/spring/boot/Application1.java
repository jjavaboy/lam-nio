package lam.spring.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import lam.spring.boot.config.MyConfiguration;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年6月16日
* @versio 1.0
*/
//自定义的httpMessageConverter和ViewResolver可在lam.spring.boot.config.MyConfiguration在添加。
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application1 {
	
	public static void main(String[] args) {
		SpringApplication.run(Application1.class, args);
	}

}

package lam.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * @description: Application
 * @author: linanmiao
 * @date: 2018/12/31 16:37
 * @version: 1.0
 */
@EnableWebFlux
@SpringBootApplication(scanBasePackages = "lam.webflux")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

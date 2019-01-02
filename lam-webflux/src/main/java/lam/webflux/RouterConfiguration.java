package lam.webflux;

import lam.webflux.dispatch.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.reactive.function.server.*;
import reactor.netty.resources.ConnectionProvider;

/**
 * @description: RouterConfiguration
 * @author: linanmiao
 * @date: 2018/12/31 16:44
 * @version: 1.0
 */
@Configuration
public class RouterConfiguration {

    @Bean
    @Autowired
    public RouterFunction<ServerResponse> gatewayRoute(Dispatcher dispatcher) {
        return RouterFunctions.route()
                .GET("/**", dispatcher::dispatch)
                .POST("/**", dispatcher::dispatch)
                .build();
    }

    @Bean
    public ReactorResourceFactory resourceFactory() {
        ReactorResourceFactory factory = new ReactorResourceFactory();
        factory.setUseGlobalResources(false);
        factory.setConnectionProviderSupplier(() -> {
            return ConnectionProvider.fixed("DispatchConnectionProvider");
        });
        return factory;
    }

}

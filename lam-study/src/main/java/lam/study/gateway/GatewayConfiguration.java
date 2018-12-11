package lam.study.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lam.study.gateway.route.DataBaseRouteLocator;

/**
 * @author: linanmiao
 */
@Configuration
public class GatewayConfiguration {

    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    private ZuulProperties zuulProperties;

    @Bean(name = "dataBaseRouteLocator")
    public RouteLocator initBeanRouteLocator() {
        return new DataBaseRouteLocator(this.serverProperties.getServletPath(), this.zuulProperties);
    }

}

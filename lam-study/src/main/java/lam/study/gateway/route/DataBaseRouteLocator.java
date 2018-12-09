package lam.study.gateway.route;

import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import java.util.Map;

/**
 * @description: DataBaseRouteLocator
 * @author: linanmiao
 * @date: 2018/12/9 20:55
 * @version: 1.0
 */
public class DataBaseRouteLocator extends BaseRouteLocator{

    public DataBaseRouteLocator(String servletPath, ZuulProperties zuulProperties) {
        super(servletPath, zuulProperties);
    }

    @Override
    protected Map<String, ZuulProperties.ZuulRoute> customeLocateRoutes() {
        return null;
    }
}

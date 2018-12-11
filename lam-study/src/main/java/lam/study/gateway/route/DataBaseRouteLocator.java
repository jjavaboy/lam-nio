package lam.study.gateway.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: DataBaseRouteLocator
 * @author: linanmiao
 * @date: 2018/12/9 20:55
 * @version: 1.0
 */
public class DataBaseRouteLocator extends BaseRouteLocator{

    private static final Logger LOGGER = LoggerFactory.getLogger(DataBaseRouteLocator.class);

    public DataBaseRouteLocator(String servletPath, ZuulProperties zuulProperties) {
        super(servletPath, zuulProperties);
    }

    @Override
    protected Map<String, ZuulProperties.ZuulRoute> customeLocateRoutes() {
        Map<String, ZuulProperties.ZuulRoute> routeMap = new HashMap<String, ZuulProperties.ZuulRoute>();
        for (int index = 0; index < 3; index++) {
            String key = String.format("key-%d", index);
            String path = String.format("path-%d", index);
            String location = String.format("location-%d", index);
            String serviceId = String.format("serviceId-%d", index);
            String url = String.format("url-%d", index);

            ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
            zuulRoute.setId(String.valueOf(index));
            zuulRoute.setPath(path);
            zuulRoute.setLocation(location);
            zuulRoute.setServiceId(serviceId);
            zuulRoute.setUrl(url);
            zuulRoute.setRetryable(Boolean.TRUE);

            routeMap.put(key, zuulRoute);
        }
        LOGGER.info("[customeLocateRoutes] route:{}", routeMap);
        return routeMap;
    }
}

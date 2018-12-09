package lam.study.gateway.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * use org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator with eureka cluster.<br/>
 * but we custom out Route Locator.
 * @description: BaseRouteLocator
 * @author: linanmiao
 * @date: 2018/12/9 20:14
 * @version: 1.0
 */
public abstract class BaseRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRouteLocator.class);

    protected final String servletPath;

    protected final ZuulProperties zuulProperties;

    public BaseRouteLocator(String servletPath, ZuulProperties zuulProperties) {
        super(servletPath, zuulProperties);
        this.servletPath    = servletPath;
        this.zuulProperties = zuulProperties;
        LOGGER.info("construct {} instance, servletPath:{}, ZuulProperties:{}", getClass().getName(), this.servletPath, this.zuulProperties);
    }

    @Override
    protected void doRefresh() {
        super.doRefresh();
    }

    @Override
    public void refresh() {
       this.doRefresh();
    }

    @Override
    protected Map<String, ZuulProperties.ZuulRoute> locateRoutes() {
        Map<String, ZuulProperties.ZuulRoute> routeMap = new LinkedHashMap<String, ZuulProperties.ZuulRoute>();
        // locate route info from application.yml or application.properties file
        routeMap.putAll(super.locateRoutes());
        // locate route info from db, cache, remote rpc, and so on, depending on yourself.
        routeMap.putAll(this.customeLocateRoutes());
        /**
         zuul:
             routes:
                 sample:
                     path: /sample/**
                     url: http://localhost:8090/
         */
        Map<String, ZuulProperties.ZuulRoute> routes = new LinkedHashMap<String, ZuulProperties.ZuulRoute>();
        for (Map.Entry<String, ZuulProperties.ZuulRoute> entry : routeMap.entrySet()) {
            String path = entry.getKey();
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (StringUtils.hasText(zuulProperties.getPrefix())) {
                path = zuulProperties.getPrefix() + path;
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
            }
            routes.put(path, entry.getValue());
        }
        return routes;
    }

    protected abstract Map<String, ZuulProperties.ZuulRoute> customeLocateRoutes();
}

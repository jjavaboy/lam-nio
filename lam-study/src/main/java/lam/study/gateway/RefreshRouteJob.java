package lam.study.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author: linanmiao
 */
@Service
@Lazy(false)
@EnableScheduling
public class RefreshRouteJob implements Runnable{

    @Autowired
    ApplicationEventPublisher publisher;

    @Qualifier("dataBaseRouteLocator")
    @Autowired
    RouteLocator routeLocator;

    @Override
    @Scheduled(cron = "*/30 * * * * ?")
    public void run() {
        RoutesRefreshedEvent routesRefreshedEvent = new RoutesRefreshedEvent(routeLocator);
        publisher.publishEvent(routesRefreshedEvent);
    }

}

package lam.study.gateway.httpapplication.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author: linanmiao
 */
@Component
public class MyBean implements InitializingBean, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyBean.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info("afterPropertiesSet()");
    }

    @Override
    public void destroy() throws Exception {
        LOGGER.info("destroy()");
    }

}

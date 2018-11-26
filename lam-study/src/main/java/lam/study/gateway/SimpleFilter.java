package lam.study.gateway;

import com.netflix.zuul.ZuulFilter;

/**
 * @author: linanmiao
 */
public class SimpleFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return null;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    @Override
    public Object run() {
        return null;
    }
}

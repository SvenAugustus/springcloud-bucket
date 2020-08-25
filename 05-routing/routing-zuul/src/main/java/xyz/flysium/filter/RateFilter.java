package xyz.flysium.filter;


import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流
 *
 * @author zeno
 */
@Component
public class RateFilter extends ZuulFilter {

    private static final AtomicInteger COUNT = new AtomicInteger(0);

    private static final RateLimiter RATE_LIMITER = RateLimiter.create(5);

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        // 获取上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        if (!RATE_LIMITER.tryAcquire()) {
            // TODO
            System.out.println("rate filter 拿不到令牌，被限流了: " + COUNT.incrementAndGet());
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
        }
        return null;
    }

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        // TODO 值越小，越在前， 限流要最早
        return -9999;
    }

}

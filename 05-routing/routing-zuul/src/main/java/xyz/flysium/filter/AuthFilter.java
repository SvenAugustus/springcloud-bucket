package xyz.flysium.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 鉴权
 *
 * @author zeno
 */
@Component
public class AuthFilter extends ZuulFilter {

    /**
     * 该过滤器是否生效
     */
    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String uri = request.getRequestURI();

        System.out.println("来源uri：" + uri);
        // 只有此接口 /service-provider/ 才被拦截
        if (uri.contains("/sample-provider/")) {
            return true;
        }
        return false;
    }

    /**
     * 拦截后的具体业务逻辑
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        String token = request.getHeader("Authorization");
        if ("1234".equals(token)) {
            // TODO 如果token是1234，过
            System.out.println("auth filter: 校验通过");
        } else {
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            requestContext.setResponseBody("认证失败");
        }
        requestContext.setSendZuulResponse(true);
        return null;
    }

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        // TODO 值越小，越在前
        return 4;
    }

}

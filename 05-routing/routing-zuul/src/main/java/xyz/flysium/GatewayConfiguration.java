package xyz.flysium;

import com.google.common.collect.Sets;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;

/**
 * @author zeno (Sven Augustus)
 * @version 1.0
 */
@Configuration
public class GatewayConfiguration {

    /**
     * CORS
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/path-1/**")
                        .allowedOrigins("https://allowed-origin.com")
                        .allowedMethods("GET", "POST");
            }
        };
    }

    @Bean
    public RouteLocator additionalRouteLocator(ServerProperties server, ZuulProperties zuulProperties) {
        return new SimpleRouteLocator(server.getServlet().getContextPath(),
                                      zuulProperties) {
            @Override
            protected Map<String, ZuulRoute> getRoutesMap() {
                final Map<String, ZuulRoute> routesMap = super.getRoutesMap();
                final ZuulRoute route = new ZuulRoute();
                route.setPath("/rt/**");
                route.setServiceId("service-provider");
                route.setRetryable(false);
                route.setStripPrefix(true);
                route.setCustomSensitiveHeaders(true);
                route.setSensitiveHeaders(Sets.newHashSet("token"));
                routesMap.put(route.getPath(), route);
                return routesMap;
            }
        };
    }

}

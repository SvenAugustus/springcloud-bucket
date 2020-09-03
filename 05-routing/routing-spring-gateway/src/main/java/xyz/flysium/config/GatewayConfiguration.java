package xyz.flysium.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;
import xyz.flysium.filter.RateFilter;

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
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许cookies跨域 Access-Control-Allow-Methods
        config.setAllowCredentials(true);
        // 允许向该服务器提交请求的URI，* 表示全部允许，在SpringMVC中，如果设成 *，会自动转成当前请求头中的 Origin
        // 允许访问的头信息,* 表示全部
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        // 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.setMaxAge(18000L);
        // 允许提交请求的方法类型 OPTIONS,HEAD，GET,PUT，POST，DELETE，PATCH ，*表示全部允许
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source =
                new org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource(
                        new PathPatternParser());
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                      .route("", r -> r.path("/rt/**")
                                       .and()
                                       .method(HttpMethod.GET)
                                       .filters(f -> f.stripPrefix(1)
                                                      .filter(new RateFilter(), -999)
//                                                      .filter(new AuthFilter(), 4)
                                                      .hystrix(config -> config
                                                              .setName("mycmd")
                                                              .setFallbackUri("forward:/fallback")
                                                      )
                                                      .addResponseHeader("X-TestHeader", "foobar")
                                                      .modifyResponseBody(String.class, String.class,
                                                                          (exchange, response) -> {
                                                                              return Mono.just("modifyResponseBody>>" +
                                                                                               response);
                                                                          }
                                                      )
                                       )
                                       .uri("lb://service-provider")
                      )
                      .build();
    }

    @RequestMapping("/fallback")
    public Mono<String> fallback(@RequestParam("message") String message) {
        return Mono.just("fallback->" + message);
    }

}

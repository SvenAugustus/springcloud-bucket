package xyz.flysium;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author zeno (Sven Augustus)
 * @version 1.0
 */
@SpringBootApplication
@EnableZuulProxy
public class RoutingUsingZuulApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(RoutingUsingZuulApplication.class).run(args);
    }
}

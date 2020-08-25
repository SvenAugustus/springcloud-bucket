package xyz.flysium;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author zeno (Sven Augustus)
 * @version 1.0
 */
@SpringBootApplication
public class RoutingUsingSpringGatewayApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(RoutingUsingSpringGatewayApplication.class).run(args);
    }
}

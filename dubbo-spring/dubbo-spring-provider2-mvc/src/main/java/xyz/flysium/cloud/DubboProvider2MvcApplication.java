package xyz.flysium.cloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Boot.
 *
 * @author zeno
 */
@SpringBootApplication
@EnableDiscoveryClient
// @EnableDubbo
public class DubboProvider2MvcApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DubboProvider2MvcApplication.class)
                .run(args);
    }

}

package xyz.flysium.cloud;

import org.springframework.boot.WebApplicationType;
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
public class DubboProvider2NoneApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DubboProvider2NoneApplication.class)
                // FIXME 必须加上，适应 StandardRestService 中的 写法
                .web(WebApplicationType.NONE)
                .run(args);
    }

}

package xyz.flysium;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import xyz.flysium.api.EchoService;

/**
 * Boot.
 *
 * @author zeno
 */
@SpringBootApplication
@EnableDiscoveryClient
public class DubboConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboConsumerApplication.class, args);
    }

    @Reference
    private EchoService echoService;

    @Bean
    public CommandLineRunner commandline() {
        return args -> {
            System.out.println("--------- Dubbo RPC Invoke ->" + echoService.echo("zeno"));
            System.out.println("--------- Dubbo RPC Invoke ->" + echoService.echo("SvenAugustus"));
        };
    }

}

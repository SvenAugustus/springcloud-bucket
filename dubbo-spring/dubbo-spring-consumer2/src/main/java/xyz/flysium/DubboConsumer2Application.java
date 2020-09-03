package xyz.flysium;

import com.alibaba.cloud.dubbo.annotation.DubboTransported;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import xyz.flysium.api.RestService;
import xyz.flysium.api.User;
import xyz.flysium.api.UserService;
import xyz.flysium.service.DubboFeignRestService;
import xyz.flysium.service.FeignRestService;

import java.util.HashMap;
import java.util.Map;

/**
 * Boot.
 *
 * @author zeno
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
public class DubboConsumer2Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DubboConsumer2Application.class)
                .run(args);
    }

    @Reference
    private UserService userService;

    @Reference(version = "1.0.0", protocol = "dubbo")
    private RestService restService;

    @Autowired
    @Lazy
    private FeignRestService feignRestService;

    @Autowired
    @Lazy
    private DubboFeignRestService dubboFeignRestService;

    @Value("${application.subscribe-provider.serviceId.serviceA}")
    private String providerApplicationName;

    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;

    @Bean
    @LoadBalanced
    @DubboTransported
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ApplicationRunner userServiceRunner() {
        return arguments -> {
            User user = new User();
            user.setId(1L);
            user.setName("zeno");
            user.setAge(29);

            // save User
            System.out.printf("UserService.save(%s) : %s\n", user,
                              userService.save(user));

            // find all Users
            System.out.printf("UserService.findAll() : %s\n", user,
                              userService.findAll());

            // remove User
            System.out.printf("UserService.remove(%d) : %s\n", user.getId(),
                              userService.remove(user.getId()));
        };
    }

    @Bean
    public ApplicationRunner callRunner() {
        return arguments -> {
            //      callRestService();
        };
    }


    @Scheduled(fixedDelay = 10 * 1000L)
    public void onScheduled() {
        callRestService();
    }

    private void callRestService() {

        // To call /path-variables
        callPathVariables();

        // To call /headers
        callHeaders();

        // To call /param
        callParam();

        // To call /params
        callParams();

        // To call /request/body/map
        callRequestBodyMap();
    }

    private void callPathVariables() {
        // Dubbo Service call
        System.out.println(restService.pathVariables("a", "b", "c"));
        // Spring Cloud Open Feign REST Call (Dubbo Transported)
        System.out.println(dubboFeignRestService.pathVariables("c", "b", "a"));
        // Spring Cloud Open Feign REST Call
        System.out.println(feignRestService.pathVariables("b", "a", "c"));

        // RestTemplate call
        System.out.println(restTemplate.getForEntity(
                "http://" + providerApplicationName + "//path-variables/{p1}/{p2}?v=c",
                String.class, "a", "b"));
    }

    private void callHeaders() {
        // Dubbo Service call
        System.out.println(restService.headers("a", "b", 10));
        // Spring Cloud Open Feign REST Call (Dubbo Transported)
        System.out.println(dubboFeignRestService.headers("b", 10, "a"));
        // Spring Cloud Open Feign REST Call
        System.out.println(feignRestService.headers("b", "a", 10));
    }

    private void callParam() {
        // Dubbo Service call
        System.out.println(restService.param("zeno"));
        // Spring Cloud Open Feign REST Call (Dubbo Transported)
        System.out.println(dubboFeignRestService.param("zeno"));
        // Spring Cloud Open Feign REST Call
        System.out.println(feignRestService.param("zeno"));
    }

    private void callParams() {
        // Dubbo Service call
        System.out.println(restService.params(1, "1"));
        // Spring Cloud Open Feign REST Call (Dubbo Transported)
        System.out.println(dubboFeignRestService.params("1", 1));
        // Spring Cloud Open Feign REST Call
        System.out.println(feignRestService.params("1", 1));

        // RestTemplate call
        System.out.println(restTemplate.getForEntity(
                "http://" + providerApplicationName + "/param?param=zeno", String.class));
    }

    private void callRequestBodyMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", 53);
        data.put("name", "SvenAugustus");
        data.put("age", 18);

        // Dubbo Service call
        System.out.println(restService.requestBodyMap(data, "Hello,World"));
        // Spring Cloud Open Feign REST Call (Dubbo Transported)
        System.out.println(dubboFeignRestService.requestBody("Hello,World", data));
        // Spring Cloud Open Feign REST Call
        System.out.println(feignRestService.requestBody("Hello,World", data));

        // RestTemplate call
        System.out.println(restTemplate.postForObject(
                "http://" + providerApplicationName + "/request/body/map?param=宗离", data,
                User.class));
    }


}

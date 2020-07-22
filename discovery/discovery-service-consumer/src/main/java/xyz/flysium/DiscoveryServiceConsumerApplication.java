/*
 * MIT License
 *
 * Copyright (c) 2020 SvenAugustus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package xyz.flysium;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zeno
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class DiscoveryServiceConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServiceConsumerApplication.class, args);
    }

    @Autowired
    private FeignRestService feignRestService;

    @Autowired
    private LoadBalancerClient loadBalancer;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Value("${subscribe-provider.application.name}")
    private String providerApplicationName;

    /**
     * 使用普通的 RestTemplate 方法访问服务
     */
    @Bean
    public CommandLineRunner runner() {
        return args -> {
            System.out.println("restTemplate->" + restTemplate
                    .getForObject("http://" + providerApplicationName + "/getServices", String.class));

            System.out.println("restTemplate->" + restTemplate
                    .getForObject("http://" + providerApplicationName + "/getServices", String.class));

            System.out.println("restTemplate->" + restTemplate
                    .getForObject("http://" + providerApplicationName + "/getServices", String.class));
        };
    }

    /**
     * 使用 openFeign 方式访问服务 （推荐）
     */
    @Bean
    public CommandLineRunner feignRunner() {
        return args -> {
            System.out.println("feignRestService->" + feignRestService.getServices());

            System.out.println("feignRestService->" + feignRestService.getServices());

            System.out.println("feignRestService->" + feignRestService.getServices());
        };
    }

    /**
     * 获取所有服务实例
     */
    @Bean
    public CommandLineRunner servicesRunner() {
        return args -> {
            System.out.println("discoveryClient->" + discoveryClient.getInstances(providerApplicationName));

            System.out.println("discoveryClient->" + discoveryClient.getInstances(providerApplicationName));

            System.out.println("discoveryClient->" + discoveryClient.getInstances(providerApplicationName));
        };
    }

    /**
     * 从所有服务中选择一个服务（轮询）
     */
    @Bean
    public CommandLineRunner loadbalancerRunner() {
        return args -> {
            System.out.println("loadBalancer->" + loadBalancer.choose(providerApplicationName).getUri().toString());
            System.out.println("loadBalancer->" + loadBalancer.choose(providerApplicationName).getUri().toString());
            System.out.println("loadBalancer->" + loadBalancer.choose(providerApplicationName).getUri().toString());
        };
    }


    @Bean
    public ApplicationRunner callRunner() {
        return arguments -> {
            callRestService();
        };
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
        // Spring Cloud Open Feign REST Call
        System.out.println(feignRestService.pathVariables("b", "a", "c"));

        // RestTemplate call
        System.out.println(restTemplate.getForEntity(
                "http://" + providerApplicationName + "//path-variables/{p1}/{p2}?v=c",
                String.class, "a", "b"));
    }

    private void callHeaders() {
        // Spring Cloud Open Feign REST Call
        System.out.println(feignRestService.headers("b", "a", 10));
    }

    private void callParam() {
        // Spring Cloud Open Feign REST Call
        System.out.println(feignRestService.param("zeno"));
    }

    private void callParams() {
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

        // Spring Cloud Open Feign REST Call
        System.out.println(feignRestService.requestBody("Hello,World", data));

        // RestTemplate call
        System.out.println(restTemplate.postForObject(
                "http://" + providerApplicationName + "/request/body/map?param=宗离", data,
                User.class));
    }
}

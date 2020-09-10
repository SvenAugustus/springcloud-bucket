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

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import javax.servlet.Servlet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author zeno
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
public class CircuitBreakerHystrixForRestTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(CircuitBreakerHystrixForRestTemplateApplication.class, args);
    }

    // TODO for HystrixDashboard only
    @Bean
    public ServletRegistrationBean<Servlet> hystrixMetricsStreamServlet() {
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean<Servlet> registrationBean = new ServletRegistrationBean<>(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/actuator/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RestController
    static class TestController {

        @Value("${application.subscribe-provider.serviceId.serviceA}")
        private String providerApplicationName;

        @Autowired
        private RestTemplate restTemplate;

        @HystrixCommand(groupKey="restTemplate",fallbackMethod = "restTemplateError", commandProperties = {
//                @HystrixProperty(name = "fallback.enabled", value = "true"),
//                @HystrixProperty(name = "circuitBreaker.forceOpen", value = "true")
//                @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"),
                @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "2")
        })
        @GetMapping("/restTemplate")
        public String restTemplate(String message) {
            return "restTemplate->" + restTemplate
                    .getForObject("http://" + providerApplicationName + "/echo?message=" +
                                  StringUtils.defaultString(message, "s1"), String.class);
        }

        @HystrixCommand(groupKey="restTemplate2", fallbackMethod = "restTemplateError", commandProperties = {
//                @HystrixProperty(name = "fallback.enabled", value = "true"),
//                @HystrixProperty(name = "circuitBreaker.forceOpen", value = "true")
//                @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"),
                @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "3")
        })
        @GetMapping("/restTemplate2")
        public String restTemplate2(String message) {
            return "restTemplate2->" + restTemplate
                    .getForObject("http://" + providerApplicationName + "/echo?message=" +
                                  StringUtils.defaultString(message, "s2"), String.class);
        }

        protected String restTemplateError(String message) {
            System.out.println("Hystrix (restTemplate) -> 不好意思，我熔断了");
            return "restTemplate 熔断 ->" + message;
        }
    }


}

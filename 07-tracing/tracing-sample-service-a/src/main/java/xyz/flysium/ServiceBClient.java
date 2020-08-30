package xyz.flysium;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.flysium.ServiceBClient.ServiceBClientFallbackFactory;

/**
 * @author zeno (Sven Augustus)
 * @version 1.0
 */
@FeignClient(value = "${subscribe-provider.serviceId.serviceB}", fallbackFactory = ServiceBClientFallbackFactory.class)
public interface ServiceBClient {

    @RequestMapping(value = "/invokeB", method = RequestMethod.GET)
    public String invokeB(@RequestParam("message") String message);

    static class ServiceBClientFallbackFactory implements feign.hystrix.FallbackFactory<ServiceBClient> {

        @Override
        public ServiceBClient create(Throwable cause) {
            return new ServiceBClient() {

                @Override
                public String invokeB(String message) {
                    System.err.println(Thread.currentThread().getName()
                                       + " Hystrix Exception -> " + cause);
                    System.out.println(Thread.currentThread().getName()
                                       + " Hystrix (to B fallbackFactory) -> 不好意思，我熔断了");
                    return Thread.currentThread().getName() + " to B fallbackFactory 熔断 ->" + message;
                }
            };
        }
    }
}

package xyz.flysium;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.flysium.ServiceCClient.ServiceCClientFallbackFactory;

/**
 * @author zeno (Sven Augustus)
 * @version 1.0
 */
@FeignClient(value = "${application.subscribe-provider.serviceId.serviceC}", fallbackFactory = ServiceCClientFallbackFactory.class)
public interface ServiceCClient {

    @RequestMapping(value = "/invokeC", method = RequestMethod.GET)
    public String invokeC(@RequestParam("message") String message);

    static class ServiceCClientFallbackFactory implements feign.hystrix.FallbackFactory<ServiceCClient> {

        @Override
        public ServiceCClient create(Throwable cause) {
            return new ServiceCClient() {

                @Override
                public String invokeC(String message) {
                    System.err.println(Thread.currentThread().getName()
                                       + " Hystrix Exception -> " + cause);
                    System.out.println(Thread.currentThread().getName()
                                       + " Hystrix (to C fallbackFactory) -> 不好意思，我熔断了");
                    return Thread.currentThread().getName() + " to C fallbackFactory 熔断 ->" + message;
                }
            };
        }
    }
}

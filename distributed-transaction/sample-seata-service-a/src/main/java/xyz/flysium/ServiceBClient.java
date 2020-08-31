package xyz.flysium;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author zeno (Sven Augustus)
 * @version 1.0
 */
@FeignClient("${subscribe-provider.serviceId.serviceB}")
public interface ServiceBClient {

    @RequestMapping(value = "/rm_b", method = RequestMethod.GET)
    String rmInvokerB();

}

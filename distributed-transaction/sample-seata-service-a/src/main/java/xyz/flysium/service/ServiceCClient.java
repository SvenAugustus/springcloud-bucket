package xyz.flysium.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author zeno (Sven Augustus)
 * @version 1.0
 */
@FeignClient("${application.subscribe-provider.serviceId.serviceC}")
public interface ServiceCClient {

    @RequestMapping(value = "/rm_c", method = RequestMethod.GET)
    String rmInvokerC();

}

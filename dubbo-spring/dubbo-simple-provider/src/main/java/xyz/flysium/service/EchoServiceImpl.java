package xyz.flysium.service;

import org.apache.dubbo.config.annotation.Service;
import xyz.flysium.api.EchoService;

/**
 * Echo Service.
 *
 * @author zeno (Sven Augustus)
 * @version 1.0
 */
@Service
public class EchoServiceImpl implements EchoService {

    @Override
    public String echo(String message) {
        return "hello, " + message;
    }
}

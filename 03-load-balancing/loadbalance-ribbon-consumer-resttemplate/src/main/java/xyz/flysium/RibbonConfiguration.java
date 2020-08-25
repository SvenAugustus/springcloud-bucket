package xyz.flysium;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;

/**
 * Ribbon 自定义配置
 *
 * @author zeno (Sven Augustus)
 * @version 1.0
 */
@Configuration
public class RibbonConfiguration {

    @Bean
    public IRule ribbonRule() {
        // 默认是 ZoneAvoidanceRule
        return new MyLoadBalancerRule();
    }

    public static class MyLoadBalancerRule extends AbstractLoadBalancerRule {
        @Override
        public Server choose(Object key) {
            final ILoadBalancer loadBalancer = getLoadBalancer();
            if (loadBalancer == null) {
                return null;
            }

            final List<Server> allServers = loadBalancer.getAllServers();
            if (allServers.isEmpty()) {
                return null;
            }
            final List<Server> upServers = loadBalancer.getReachableServers();
            if (upServers.isEmpty()) {
                return null;
            }
            for (int i = 0; i < upServers.size(); i++) {
                // TODO 优先返回端口为9082的服务
                if (Objects.equals(9082, upServers.get(i).getPort())) {
                    return upServers.get(i);
                }
            }

            return upServers.get(0);
        }

        @Override
        public void initWithNiwsConfig(IClientConfig clientConfig) {

        }
    }
}

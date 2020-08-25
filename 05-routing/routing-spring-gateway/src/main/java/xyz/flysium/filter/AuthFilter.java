package xyz.flysium.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * 鉴权
 *
 * @author zeno
 */
@Component
public class AuthFilter implements GatewayFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().toSingleValueMap().get("Authorization");
        if ("1234".equals(token)) {
            // TODO 如果token是1234，过
            System.out.println("auth filter: 校验通过");
        } else {
            return Mono.fromCallable(() -> {
                byte[] bytes = "认证失败".getBytes();
                final ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);
                return response.writeWith(Mono.just(dataBuffer));
            }).subscribeOn(Schedulers.elastic()).flatMap(m -> m);
        }

        return chain.filter(exchange);
    }
}

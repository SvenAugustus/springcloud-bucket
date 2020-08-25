package xyz.flysium.filter;


import com.google.common.util.concurrent.RateLimiter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流
 *
 * @author zeno
 */
@Component
public class RateFilter implements GatewayFilter {

    private static final AtomicInteger COUNT = new AtomicInteger(0);

    private static final RateLimiter RATE_LIMITER = RateLimiter.create(5);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取上下文
        if (!RATE_LIMITER.tryAcquire()) {
            // TODO
            final String message = "rate filter 拿不到令牌，被限流了: " + COUNT.incrementAndGet();
            System.out.println(message);
            return Mono.fromCallable(() -> {
                byte[] bytes = message.getBytes();
                final ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);
                return response.writeWith(Mono.just(dataBuffer));
            }).subscribeOn(Schedulers.elastic()).flatMap(m -> m);
        }
        return chain.filter(exchange);
    }

}

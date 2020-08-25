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

import feign.hystrix.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.flysium.FeignRestServiceB.FeignRestServiceBFallbackFactory;

/**
 * @author Sven Augustus
 * @version 1.0
 */
@FeignClient(value = "${subscribe-provider.serviceId.serviceB}", fallbackFactory = FeignRestServiceBFallbackFactory.class)
public interface FeignRestServiceB {

    @RequestMapping(value = "/invokeBService", method = RequestMethod.GET)
    String invoke(@RequestParam("message") String message);

    @Component
    static class FeignRestServiceBFallbackFactory implements FallbackFactory<FeignRestServiceB> {

        @Override
        public FeignRestServiceB create(Throwable throwable) {

            return new FeignRestServiceB() {
                @Override
                public String invoke(String message) {
                    System.err.println(Thread.currentThread().getName()
                                       + " Hystrix Exception -> " + throwable);
                    System.out.println(Thread.currentThread().getName()
                                       + " Hystrix (openFeign fallbackFactory) -> 不好意思，我熔断B了");
                    return Thread.currentThread().getName() + " openFeign fallbackFactory 熔断B ->" + message;
                }
            };
        }
    }
}

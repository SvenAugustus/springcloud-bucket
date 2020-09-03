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

package xyz.flysium.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.flysium.api.AsyncTestService;
import xyz.flysium.api.AsyncTestService2;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Test.
 *
 * @author zeno (Sven Augustus)
 * @version 1.0
 */
@RestController
@RequestMapping("/")
public class AsyncTestController {

    @Reference
    private AsyncTestService asyncService;

    @Reference
    private AsyncTestService2 asyncService2;

    @GetMapping("/asyncCall")
    public String asyncCall() throws ExecutionException, InterruptedException {
        // 调用直接返回CompletableFuture
        CompletableFuture<String> future = asyncService.sayHello("async1");
        // 增加回调
        future.whenComplete((value, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
            } else {
                System.out.println("CompletableFuture Interface, Response （CompletableFuture call way）: " + value);
            }
        });
        // 早于结果输出
        System.out.println("Executed before response return.");
        return future.get();
    }

    @GetMapping("/asyncCall2")
    public String asyncCall2() throws ExecutionException, InterruptedException {
        // 此调用会立即返回null
        asyncService2.sayHello("zeno");
        // 拿到调用的Future引用，当结果返回后，会被通知和设置到此Future
        CompletableFuture<String> future = RpcContext.getContext().getCompletableFuture();
        // 为Future添加回调
        future.whenComplete((value, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
            } else {
                System.out.println("Normal Interface, Response （CompletableFuture call way）: " + value);
            }
        });
        return future.get();
    }

    @GetMapping("/asyncCall3")
    public String asyncCall3() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = RpcContext.getContext()
                                                     .asyncCall(() -> asyncService2.sayHello("oneway call request1"));
        return future.get();
    }

}

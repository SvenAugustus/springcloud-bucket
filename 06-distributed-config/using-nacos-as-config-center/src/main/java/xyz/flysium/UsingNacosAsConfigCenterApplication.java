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

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.flysium.config.MyApplicationConfig;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import java.util.concurrent.Executor;

@SpringBootApplication
@EnableScheduling
public class UsingNacosAsConfigCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsingNacosAsConfigCenterApplication.class, args);
    }

    @Autowired
    private MyApplicationConfig myApplicationConfig;

    @Scheduled(fixedDelay = 1000L)
    public void onScheduled() {
        // 相应配置变更，nacos 自动刷新
        System.out.println("myApplicationConfig->" + myApplicationConfig);
    }

    /**
     * 示例：监听配置变更
     */
    @Component
    class SampleRunner implements ApplicationRunner {

        @Autowired
        private NacosConfigManager nacosConfigManager;

        @Override
        public void run(ApplicationArguments args) throws Exception {
            System.out.println(String.format("Initial myApplicationConfig=%s", myApplicationConfig));

            nacosConfigManager.getConfigService().addListener(
                    "nacos-config-center.properties", "DEFAULT_GROUP", new Listener() {

                        @Override
                        public void receiveConfigInfo(String configInfo) {
                            Properties properties = new Properties();
                            try {
                                properties.load(new StringReader(configInfo));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println("config changed: " + properties);
                        }

                        @Override
                        public Executor getExecutor() {
                            return null;
                        }
                    });
        }

    }

}

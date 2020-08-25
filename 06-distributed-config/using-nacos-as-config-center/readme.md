
### Spring Cloud 应用获取数据

#### dataID

在 Nacos Config Starter 中，dataId 的拼接格式如下

	${prefix} - ${spring.profiles.active} . ${file-extension}

* `prefix` 默认为 `spring.application.name` 的值，也可以通过配置项 `spring.cloud.nacos.config.prefix`来配置。

* `spring.profiles.active` 即为当前环境对应的 profile，详情可以参考 [Spring Boot文档](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-profiles.html#boot-features-profiles)

	**注意，当 activeprofile 为空时，对应的连接符 `-` 也将不存在，dataId 的拼接格式变成 `${prefix}`.`${file-extension}`**

* `file-extension` 为配置内容的数据格式，可以通过配置项 `spring.cloud.nacos.config.file-extension`来配置。
目前只支持 `properties` 类型。

#### group
* `group` 默认为 `DEFAULT_GROUP`，可以通过 `spring.cloud.nacos.config.group` 配置。


### 自动注入
Nacos Config Starter 实现了 `org.springframework.cloud.bootstrap.config.PropertySourceLocator`接口，并将优先级设置成了最高。

在 Spring Cloud 应用启动阶段，会主动从 Nacos Server 端获取对应的数据，并将获取到的数据转换成 PropertySource 且注入到 Environment 的 PropertySources 属性中，所以使用 @Value 注解也能直接获取 Nacos Server 端配置的内容。

### 动态刷新

Nacos Config Starter 默认为所有获取数据成功的 Nacos 的配置项添加了监听功能，在监听到服务端配置发生变化时会实时触发 `org.springframework.cloud.context.refresh.ContextRefresher` 的 refresh 方法 。
		
如果需要对 Bean 进行动态刷新，请参照 Spring 和 Spring Cloud 规范。推荐给类添加 `@RefreshScope` 或 `@ConfigurationProperties ` 注解，

更多详情请参考 [ContextRefresher Java Doc](http://static.javadoc.io/org.springframework.cloud/spring-cloud-context/2.0.0.RELEASE/org/springframework/cloud/context/refresh/ContextRefresher.html)。

	


## Endpoint 信息查看

Spring Boot 应用支持通过 Endpoint 来暴露相关信息，Nacos Config Starter 也支持这一点。

在使用之前需要在 maven 中添加 `spring-boot-starter-actuator`依赖，并在配置中允许 Endpoints 的访问。

* Spring Boot 1.x 中添加配置 management.security.enabled=false
* Spring Boot 2.x 中添加配置 management.endpoints.web.exposure.include=*

Spring Boot 1.x 可以通过访问 http://127.0.0.1:18084/nacos_config 来查看 Nacos Endpoint 的信息。

Spring Boot 2.x 可以通过访问 http://127.0.0.1:18084/actuator/nacos-config 来访问。

![actuator](https://cdn.nlark.com/lark/0/2018/png/54319/1536986344822-279e1edc-ebca-4201-8362-0ddeff240b85.png)

如上图所示，Sources 表示此客户端从哪些 Nacos Config 配置项中获取了信息，RefreshHistory 表示动态刷新的历史记录，最多保存20条，NacosConfigProperties 则为 Nacos Config Starter 本身的配置。
    	
## More

#### 更多配置项
配置项|key|默认值|说明
----|----|-----|-----
服务端地址|spring.cloud.nacos.config.server-addr||
DataId前缀|spring.cloud.nacos.config.prefix||spring.application.name
Group|spring.cloud.nacos.config.group|DEFAULT_GROUP|
dataID后缀及内容文件格式|spring.cloud.nacos.config.file-extension|properties|dataId的后缀，同时也是配置内容的文件格式，目前只支持 properties
配置内容的编码方式|spring.cloud.nacos.config.encode|UTF-8|配置的编码
获取配置的超时时间|spring.cloud.nacos.config.timeout|3000|单位为 ms
配置的命名空间|spring.cloud.nacos.config.namespace||常用场景之一是不同环境的配置的区分隔离，例如开发测试环境和生产环境的资源隔离等。
AccessKey|spring.cloud.nacos.config.access-key||
SecretKey|spring.cloud.nacos.config.secret-key||
相对路径|spring.cloud.nacos.config.context-path||服务端 API 的相对路径
接入点|spring.cloud.nacos.config.endpoint|UTF-8|地域的某个服务的入口域名，通过此域名可以动态地拿到服务端地址
是否开启监听和自动刷新|spring.cloud.nacos.config.refresh-enabled|true|


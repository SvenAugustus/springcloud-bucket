# 服务间调用
微服务中，很多服务系统都在独立的进程中运行，通过各个服务系统之间的协作来实现一个大项目的所有业务功能。服务系统间 使用多种跨进程的方式进行通信协作，而RESTful风格的网络请求是最为常见的交互方式之一。

Spring cloud提供的方式：

* **RestTemplate** 自由度高
```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
```

* **Feign** 更优雅
```xml
    <!-- Spring Cloud Open Feign -->
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
```




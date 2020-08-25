
# Ribbon 

Ribbon (https://github.com/Netflix/ribbon)  使用的是客户端负载均衡。

**而在Spring Cloud中我们如果想要使用客户端负载均衡，方法很简单，使用`@LoadBalanced`注解即可。**

这样客户端在发起请求的时候会根据负载均衡策略从服务端列表中选择一个服务端，向该服务端发起网络请求，从而实现负载均衡。

上面几种负载均衡，硬件，软件（服务端nginx，客户端ribbon）。目的：将请求分发到其他功能相同的服务。

手动实现，其实也是它的原理，做事的方法。

```sh
手写客户端负载均衡
1、知道自己的请求目的地（虚拟主机名，默认是spring.application.name）
2、获取所有服务端地址列表（也就是注册表）。
3、选出一个地址，找到虚拟主机名对应的ip、port（将虚拟主机名 对应到 ip和port上）。
4、发起实际请求(最朴素的请求)。
```


## 概念

Ribbon是Netflix开发的客户端负载均衡器，为Ribbon配置**服务提供者地址列表**后，Ribbon就可以基于某种**负载均衡策略算法**，自动地帮助服务消费者去请求 提供者。Ribbon默认为我们提供了很多负载均衡算法，例如轮询、随机等。我们也可以实现自定义负载均衡算法。


Ribbon作为Spring Cloud的负载均衡机制的实现，

1. Ribbon可以单独使用，作为一个独立的负载均衡组件。只是需要我们手动配置 服务地址列表。
2. Ribbon与Eureka配合使用时，Ribbon可自动从Eureka Server获取服务提供者地址列表（DiscoveryClient），并基于负载均衡算法，请求其中一个服务提供者实例。
3. Ribbon与OpenFeign和RestTemplate进行无缝对接，让二者具有负载均衡的能力。OpenFeign默认集成了ribbon。

## Ribbon组成


* ribbon-core: 核心的通用性代码。api一些配置。

* ribbon-eureka：基于eureka封装的模块，能快速集成eureka。

* ribbon-examples：学习示例。

* ribbon-httpclient：基于apache httpClient封装的rest客户端，集成了负载均衡模块，可以直接在项目中使用。

* ribbon-loadbalancer：负载均衡模块。

* ribbon-transport：基于netty实现多协议的支持。比如http，tcp，udp等。


## 原理

### 服务注册
注册中心客户端，比如eureka, nacos 会对客户端 ribbon, 实现 `ServerList`， `IPing` 等接口，实现服务的自动发现，把服务的变动更新到本地缓存中。

### 调用
在调用时，解析 restTemplate 中的服务名，找到对应的 `ILoadbalancer`, 找到可用的服务节点列表，再依据 `IRule` 选择算法，选择合适的一个服务节点,
替换服务名为实际的 IP port, 再使用配置的 client （`HTTP Client`, `OKHttp`） 去网络调用 

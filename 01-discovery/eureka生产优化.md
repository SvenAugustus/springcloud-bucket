## Eureka Server 优化
* 自我保护模式`enable-self-preservation`，主要是为了防止因为网络抖动导致上报给 eureka 心跳失败，而被 eureka 误判。
    * 如果微服务数量较少，因为阈值问题，因网络抖动造成误判的概率较小，更多是服务真正不可用，因此可以关闭自我保护模式。
    * 如果微服务数量较多，那么因网络抖动造成误判的概率较大，建议设置开启自我保护模式，并设置合适的自我保护阈值。
* 为保证对不可用服务及时剔除，可以设置更小的时间间隔 `eviction-interval-timer-in-ms`， 默认是60秒
* eureka server 中有三级缓存， register, responseCache (readWrite, readonly)
   * 服务注册(ApplicationResource#addInstance) 会进入 register, 同时失效 readWrite cache
   * 拉取注册表 (ApplicationResource#getApplication） 会读取 responseCache，如果启用readonly (`use-read-only-response-cache`), 直接读readonly, 有直接返回，没有再读 readWrite
   * readonly 由定时器每`response-cache-update-interval-ms` 默认是30秒去 从 readWrite更新过来，`response-cache-auto-expiration-in-seconds` 默认是180秒不访问则自动失效

```yaml
eureka:
  server:
    # 是否开启自我保护模式，看微服务的数量而定
    enable-self-preservation: false
    # 自我保护阈值
    renewal-percent-threshold: 0.85
    # 服务注册表清理间隔（单位毫秒，默认是60*1000）
    # 剔除服务时间间隔    InstanceRegistry#openForTraffic > AbstractInstanceRegistry#postInit, evictionTimer
    eviction-interval-timer-in-ms: 1000
    # 关闭从readOnly读注册表  ->ResponseCacheImpl
    use-read-only-response-cache: false
    # readWrite 和 readOnly 同步时间间隔。
    response-cache-update-interval-ms: 1000
    response-cache-auto-expiration-in-seconds: 180
```

## Eureka Client 优化

```yaml
eureka:
  instance:
    # 发送心跳给server的频率，每隔这个时间会主动心跳一次
    lease-renewal-interval-in-seconds: 1
    # Server从收到client后，下一次收到心跳的间隔时间。超过这个时间没有接收到心跳EurekaServer就会将这个实例剔除
    lease-expiration-duration-in-seconds: 1
  client:
    # 刷新和拉取注册表的时间间隔， 默认30秒
    registry-fetch-interval-seconds: 5
```
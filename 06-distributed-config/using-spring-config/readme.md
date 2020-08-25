## 自动刷新
默认客户端`using-spring-config`只会在重启时 从 `spring-config-server` 拉取最新的配置信息, 运行期间配置在 git 上发现变化, 并不会触发自动刷新。

### 启用自动刷新
POST 客户端 (`using-spring-config`):
```
 POST http://localhost:18080/actuator/refresh 
``` 

还可以采用 github 或 gitlab 的 Webhooks 使用在提交后触发 POST 

但是对于客户端是分布式部署的时候，还是不合适，可以考虑采用 mq 进行广播 ` Spring Cloud Bus`， 参考 https://www.cnblogs.com/fengzheng/p/11242128.html


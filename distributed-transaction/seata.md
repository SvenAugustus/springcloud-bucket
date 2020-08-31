
# Seata
- http://seata.io/zh-cn/

## 使用

- http://seata.io/zh-cn/docs/user/quickstart.html

#### 数据库准备

##### 3306 for `seata-server`

```shell script
mysql -uroot -proot123 -h127.0.0.1 -P3306
```
```shell script
create DATABASE IF NOT EXISTS seata_server default character set utf8 collate utf8_general_ci;
```

* DML 执行 `seata-at-sqls/seata-server.sql`

##### 3307 for `sample-seata-service-a`
 
```shell script
mysql -uroot -proot123 -h127.0.0.1 -P3307
```
```shell script
create DATABASE IF NOT EXISTS seata_rm default character set utf8 collate utf8_general_ci;
use seata_rm;
```

* DML 执行 `seata-at-sqls/seata-service-a.sql`

##### 3308 for `sample-seata-service-b`

```shell script
mysql -uroot -proot123 -h127.0.0.1 -P3308
```
```shell script
create DATABASE IF NOT EXISTS seata_rm default character set utf8 collate utf8_general_ci;
use seata_rm;
```

* DML 执行 `seata-at-sqls/seata-service-b.sql`


##### 3309 for `sample-seata-service-c`

```shell script
mysql -uroot -proot123 -h127.0.0.1 -P3309
```
```shell script
create DATABASE IF NOT EXISTS seata_rm default character set utf8 collate utf8_general_ci;
use seata_rm;
```

* DML 执行 `seata-at-sqls/seata-service-c.sql`

#### 微服务

需要分布式事务的，加上注解`@GlobalTransactional` 即可：
```
@GlobalTransactional
```

#### 启动`seata-server`
在 https://github.com/seata/seata/releases 下载相应版本的 Seata-Server，修改 registry.conf为相应的配置(如果使用 file 则不需要修改)，解压并通过以下命令启动:
```
sh ./bin/seata-server.sh
```

采用 docker 也是可以的。


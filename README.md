# siliver_admin

> 1. 通用后台架构

1. springboot
2. undertow
3. security
4. open-ui
5. mybatis-plus
6. redis,redisson
7. springcloud
8. openFeign
9. nacos
10. mysql
11. fastjson
12. jjwt
13. lombok
14. mongodb
15. flyway
16. sentinel
17. mica-ip2region
18. UserAgentUtils

> 2. 权限

通过application.yml 的自定义 custom.security节点配置。
white是白名单，skip是跳过验证的接口列表，dynamic是动态角色权限验证（name是角色名称，url是权限配置路径）。

HttpSecurity一些方法在security6中已经修改，百度查询时请注意security5之前,security5,security6配置信息会有一些修改

在token验证的同时会生成用户编号，用户ID 和userAgent分析。如果想要所有接口都要分析userAgent，请自行迁移方法。

> 3. 数据库初始化

数据库初始化执行采用flyway，配置信息类为 org.springframework.boot.autoconfigure.flyway.FlywayProperties。如需自定义，请参考该类的注释

mysql-connector-java包已经迁移到mysql-connector-j

> 4. 日志

输入输出日志采用切面进行记录，只切了controller接口，定位目录为Controller。如果不修改切面，请将新建的controller放置到controller目录中。<br>
全局异常使用@RestControllerAdvice进行接口异常的捕获。

> 5. hashredis

添加了HashRedisTemplate的bean,可以直接注入使用

> 6. xss攻击防御

通过继承HttpServletRequestWrapper，实现简单的xss攻击防御

> 7. redission锁进行防幂等攻击

通过实现HandlerInterceptor和自定义的Annotation Idempotent进行接口判断，通过redission进行枷锁，通过ConcurrentHashMap进行所记录并在postHandle
中进行解锁。（实验方案，后续在全局异常切面中再次进行解锁的验证）

> 8. 工具类

目前工具类包括：

1. CustomRandomUtils：随机数字，字母数字，颜色的方法；
2. JwtTokenUtils：创建token,验证token,验证refeshtoken,获取用户名称，获取用户角色，验证过期的方法；
3. SecurityStringUtils：html标签过滤，前端关键字过滤，sql关键字过滤（未完善）的方法

> 9. 数据库链接池

连接池没选用druid，使用自带的hikari，如有需要druid的，请自行添加依赖

> 10. 限流

限流使用了sentinel，需要自行下载服务端，并在项目中添加配置信息。项目中已经引用了spring-cloud-starter-alibaba-sentinel。

> 11.文档

原springdoc-openapi-ui 迁移到 springdoc-openapi-starter-webmvc-ui

> 12. user-Agent

使用UserAgentUtils 进行 user-Agent分析

> 13. mica-ip2region

使用mica-ip2region做了离线IP地址解析的接口小工具
# Spring Boot中的缓存支持（二）使用Redis做集中式缓存

- 参考：[http://blog.didispace.com/springbootcache2/](http://blog.didispace.com/springbootcache2/)
- 参考：[http://blog.csdn.net/renfufei/article/details/38474435](http://blog.csdn.net/renfufei/article/details/38474435)

# 配置redis缓存
- 删除EhCache的配置文件src/main/resources/ehcache.xml

- pom.xml中删除EhCache的依赖，增加redis的依赖：

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

- application.properties中增加redis配置，以本地运行为例，比如：
```yml
spring:
    datasource:
        url: jdbc:mysql://192.168.116.10:3306/test
        username: root
        password: root
        driver-class-name: com.mysql.jdbc.Driver
    jpa:
        properties:
             hibernate:
                 hbm2ddl:
                     auto: create
                 show_sql: true
        database-platform: org.hibernate.dialect.MySQLDialect
    redis:
        host: localhost
        port: 6379
        pool:
             max-idle: 8
             min-idle: 0
             max-active: 8
             max-wait: -1              
```



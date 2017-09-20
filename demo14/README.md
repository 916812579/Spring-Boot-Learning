# Spring Boot中使用Spring-data-jpa让数据访问更简单、更优雅

- 参考：[http://blog.didispace.com/springbootdata2/](http://blog.didispace.com/springbootdata2/)
- 参考：[https://github.com/916812579/Spring-Boot-Learning/blob/master/demo5/README.md](https://github.com/916812579/Spring-Boot-Learning/blob/master/demo5/README.md)


# 工程配置

- pom.xml添加jpa配置

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-freemarker</artifactId>
</dependency>

<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
</dependency>
```

- application.yml数据库基本配置如下
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
                     auto: create-drop
```

`spring.jpa.properties.hibernate.hbm2ddl.auto`是`hibernate`的配置属性，其主要作用是：`自动创建`、`更新`、`验证数据库表结构`。该参数的几种配置如下：

> - `create`：每次加载hibernate时都会`删除上一次的生成的表，然后根据你的model类再重新来生成新表`，哪怕两次没有任何改变也要这样执行，这就是导致数据库表数据丢失的一个重要原因。
> - `create-drop`：每次加载hibernate时`根据model类生成表，但是sessionFactory一关闭,表就自动删除`。
> - `update`：最常用的属性，第一次加载hibernate时根据model类会自动建立起表的结构（前提是先建立好数据库），以后加载hibernate时根据model类自动更新表结构，即使表结构改变了但表中的行仍然存在不会删除以前的行。要注意的是当部署到服务器后，表结构是不会被马上建立起来的，是要等应用第一次运行起来后才会。
> - `validate`：每次加载hibernate时，验证创建数据库表结构，只会和数据库中的表进行比较，不会创建新表，但是会插入新值。

- 创建实体User
```java
package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private Integer age;

	public User(String name, Integer age) {
		super();
		this.name = name;
		this.age = age;
	}
}

```
> 由于配置了`hibernate.hbm2ddl.auto`，因此会自动创建表结构的



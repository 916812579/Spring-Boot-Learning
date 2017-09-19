# Spring Boot属性配置文件详解
- 参考：[http://blog.didispace.com/springbootproperties/](http://blog.didispace.com/springbootproperties/)

默认配置文件
- `application.properties`
- `application.yml`

## 自定义属性与加载

如application.properties或者application.yml配置如下

application.properties
```java
spring.boot.demo.name=demo
spring.boot.demo.version=1.0
```
application.yml
```java
spring:
    boot:
       demo:
           name: demo
           version: 1.0
```

**name:**后面不是需要空格，不然会存在问题

 
然后通过@Value("${属性名}")注解来加载对应的配置属性，具体如下：
```java
package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

// @Component
@Configuration
public class PropertiesDemo {
	 @Value("${spring.boot.demo.name}")
	 private String name;
	 
	 @Value("${spring.boot.demo.version}")
	 private String version;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
```

也可以使用如下方式注入
```java
package com.example.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// @Component
@Component
@ConfigurationProperties(prefix="spring.boot.demo")
public class PropertiesDemo {

	 private String name;
	 
	 private String version;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	
	
}
 
```
`@ConfigurationProperties`指定了前缀，此时属性上面就不能再使用```@Value```注解了否则会报错。如果不使用`@ConfigurationProperties`就需要使用```@Value```注解

## 参数间引用

application.properties
```java
spring.boot.demo.name=demo
spring.boot.demo.version=1.0
spring.boot.demo.port=8080
spring.boot.demo.url=http://localhost:${spring.boot.demo.port}
```

application.yml
```java
spring:
    boot:
       demo:
           name: demo
           version: 1.0
           port: 8080
           url: http://localhost:${spring.boot.demo.port}
```

## 使用随机数

application.yml
```java
spring:
    boot:
       demo:
           name: demo
           version: 1.0
           port: 8080
           url: http://localhost:${spring.boot.demo.port}
           # 随机字符串
           value: ${random.value}
           # 随机int
           number: ${random.int}
           # 随机long
           bignumber: ${random.long}
           # 10以内的随机数
           test1: ${random.int(10)}
           # 10-20的随机数
           test2: ${random.int[10,20]}
```
properties属性文件与上面类似

## 通过命令行设置属性值

相信使用过一段时间```Spring Boot```的用户，一定知道这条命令：```java -jar xxx.jar --server.port=8888```，通过使用```--server.port```属性来设置```xxx.jar```应用的端口为```8888```。

在命令行运行时，连续的两个减号```--```就是对```application.properties```中的属性值进行赋值的标识。所以，```java -jar xxx.jar --server.port=8888```命令，等价于我们在```application.properties```中添加属性```server.port=8888```，该设置在样例工程中可见，读者可通过删除该值或使用命令行来设置该值来验证。

通过命令行来修改属性值固然提供了不错的便利性，但是通过命令行就能更改应用运行的参数，那岂不是很不安全？是的，所以```Spring Boot```也贴心的提供了屏蔽命令行访问属性的设置，只需要这句设置就能屏蔽：```SpringApplication.setAddCommandLineProperties(false)```。          

## 多环境配置

在```Spring Boot```中多环境配置文件名需要满足```application-{profile}.properties```的格式，其中```{profile}```对应你的环境标识，比如：

- ```application-dev.properties```：开发环境
- ```application-test.properties```：测试环境
- ```application-prod.properties```：生产环境

至于哪个具体的配置文件会被加载，需要在```application.properties```文件中通过```spring.profiles.active```属性来设置，其值对应{profile}值。

总结多环境的配置思路：
- ```application.properties```中配置通用内容，并设置```spring.profiles.active=dev```，以开发环境(```application-dev.properties```)为默认配置
- ```application-{profile}.properties```中配置各个环境不同的内容
- 通过命令行方式去激活不同环境的配置 ```java -jar xxx.jar --spring.profiles.active=test```
 


# Spring Boot中的缓存支持（一）注解配置与EhCache使用

- 参考：[http://blog.didispace.com/springbootcache1/](http://blog.didispace.com/springbootcache1/)

## 准备工作

- `application.properties`文件中新增`spring.jpa.properties.hibernate.show_sql=true`，开启hibernate对sql语句的打印
- 引入缓存，pom.xml添加如下配置
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```
- 在Spring Boot主类中增加`@EnableCaching`注解开启缓存功能

```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
```
- 在数据访问接口中，增加缓存配置注解

```java
package com.example.demo.repository;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

@Repository
@CacheConfig(cacheNames = "users")
public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByName(String name);

	User findByNameAndAge(String name, Integer age);

	@Query("from User u where u.name=:name")
	User findUser(@Param("name") String name);
}
```
- 测试类
```java
package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ApplicationTests {
	@Autowired
	private UserRepository userRepository;
	@Test
	public void test() throws Exception {
		// 创建10条记录
		userRepository.save(new User("AAA", 10));
		userRepository.findByName("AAA");
		userRepository.findByName("AAA");
		userRepository.findByName("AAA");
		userRepository.findByName("AAA");
	}
}
```
可以发现后面的多次查询都没有产生新的sql语句。

## Cache注解详解


- `@CacheConfig`：主要用于配置该类中会用到的一些`共用的缓存配置`。在这里`@CacheConfig(cacheNames = "users")`：配置了该数据访问对象中返回的内容将存储于名为`users`的缓存对象中，我们也可以不使用该注解，直接通过`@Cacheable`自己配置缓存集的名字来定义。

- `@Cacheable`：配置了`findByName`函数的返回值将被加入缓存。同时在查询时，会先从缓存中获取，若不存在才再发起对数据库的访问。该注解主要有下面几个参数：

> - `value`、`cacheNames`：两个等同的参数（`cacheNames`为Spring 4新增，作为`value`的别名），用于指定缓存存储的集合名。由于Spring 4中新增了`@CacheConfig`，因此在Spring 3中原本必须有的`value`属性，也成为非必需项了
> 
> - `key`：缓存对象存储在Map集合中的`key`值，非必需，缺省按照函数的所有参数组合作为key值，若自己配置需使用SpEL表达式，比如：`@Cacheable(key = "#p0")`：使用函数第一个参数作为缓存的key值，更多关于SpEL表达式的详细内容可参考官方文档
> 
> - `condition`：缓存对象的条件，非必需，也需使用SpEL表达式，只有满足表达式条件的内容才会被缓存，比如：`@Cacheable(key = "#p0", condition = "#p0.length() < 3")`，表示只有当第一个参数的长度小于3的时候才会被缓存，若做此配置上面的AAA用户就不会被缓存，读者可自行实验尝试。
> 
> - `unless`：另外一个缓存条件参数，非必需，需使用SpEL表达式。它不同于`condition`参数的地方在于它的判断时机，该条件是在函数被调用之后才做判断的，所以它可以通过对result进行判断。
> 
> - `keyGenerator`：用于指定key生成器，非必需。若需要指定一个自定义的key生成器，我们需要去实现`org.springframework.cache.interceptor.KeyGenerator`接口，并使用该参数来指定。需要注意的是：该参数与key是互斥的
> 
> - `cacheManager`：用于指定使用哪个缓存管理器，非必需。只有当有多个时才需要使用
> - `cacheResolver`：用于指定使用那个缓存解析器，非必需。需通过`org.springframework.cache.interceptor.CacheResolver`接口来实现自己的缓存解析器，并用该参数指定。

**除了这里用到的两个注解之外，还有下面几个核心注解：**

 - `@CachePut`：配置于函数上，能够根据参数定义条件来进行缓存，它与`@Cacheable`不同的是，它每次都会真是调用函数，所以主要用于数据新增和修改操作上。它的参数与`@Cacheable`类似，具体功能可参考上面对`@Cacheable`参数的解析
 
 `@CacheEvict`：配置于函数上，通常用在`删除方法`上，用来从缓存中移除相应数据。除了同`@Cacheable`一样的参数之外，它还有下面两个参数：
> - `allEntries`：非必需，默认为false。当为true时，会移除所有数据
> 
> - `beforeInvocation`：非必需，默认为false，会在调用方法之后移除数据。当为true时，会在调用方法之前移除数据。

## 缓存配置
在Spring Boot中通过@EnableCaching注解自动化配置合适的缓存管理器（CacheManager），Spring Boot根据下面的顺序去侦测缓存提供者：

- Generic
- JCache (JSR-107)
- EhCache 2.x
- Hazelcast
- Infinispan
- Redis
- Guava
- Simple

> 可以通过`spring.cache.type`指定具体的缓存管理器

## 配置EHCache

- 在`src/main/resources`目录下创建：`ehcache.xml`
```xml
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:noNamespaceSchemaLocation="ehcache.xsd">
    <cache name="users"
           maxEntriesLocalHeap="200"
           timeToLiveSeconds="600">
    </cache>
</ehcache>
```

- 在pom.xml中加入
```xml
<dependency>
    <groupId>net.sf.ehcache</groupId>
    <artifactId>ehcache</artifactId>
</dependency>
```

> 对于EhCache的配置文件也可以通过`application.properties`文件中使用`spring.cache.ehcache.config`属性来指定，比如：
```property
spring.cache.ehcache.config=classpath:config/another-config.xml
```

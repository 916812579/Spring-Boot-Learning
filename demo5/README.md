# Spring Boot中使用JdbcTemplate访问数据库
- 参考：[http://blog.didispace.com/springbootdata1/](http://blog.didispace.com/springbootdata1/)

## 数据源配置
为了连接数据库需要引入jdbc支持，在pom.xml中引入如下配置：
```xml
   <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jdbc</artifactId>
    </dependency>
```
嵌入式数据库支持

Spring Boot提供自动配置的嵌入式数据库有H2、HSQL、Derby，不需要提供任何连接配置就能使用。
```xml
    <dependency>
        <groupId>org.hsqldb</groupId>
        <artifactId>hsqldb</artifactId>
        <scope>runtime</scope>
    </dependency>
```
连接生产数据源
```xml
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.21</version>
    </dependency>
```
在src/main/resources/application.properties中配置数据源信息
```
    spring.datasource.url=jdbc:mysql://localhost:3306/test
    spring.datasource.username=dbuser
    spring.datasource.password=dbpass
    spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```
使用JdbcTemplate操作数据库

Spring的JdbcTemplate是自动配置的，你可以直接使用@Autowired来注入到你自己的bean中来使用
User表
```sql
    CREATE TABLE T_USER (
    	id INT PRIMARY KEY auto_increment,
    	NAME VARCHAR (50),
    	age INT
    );
```
业务接口类UserService
```java
   package com.example.demo.service;
    
    public interface UserService {
    	/**
    	 * 新增一个用户
    	 * 
    	 * @param name
    	 * @param age
    	 */
    	void create(String name, Integer age);
    
    	/**
    	 * 根据name删除一个用户高
    	 * 
    	 * @param name
    	 */
    	void deleteByName(String name);
    
    	/**
    	 * 获取用户总量
    	 */
    	Integer getAllUsers();
    
    	/**
    	 * 删除所有用户
    	 */
    	void deleteAllUsers();
    }
```
业务接口实现类
```java
    package com.example.demo.service.impl;
    
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.jdbc.core.JdbcTemplate;
    import org.springframework.stereotype.Service;
    
    import com.example.demo.service.UserService;
    
    @Service
    public class UserServiceImpl implements UserService {
    	// 注入 JdbcTemplate
    	@Autowired
    	private JdbcTemplate jdbcTemplate;
    
    	@Override
    	public void create(String name, Integer age) {
    		jdbcTemplate.update("insert into T_USER(NAME, AGE) values(?, ?)", name, age);
    	}
    
    	@Override
    	public void deleteByName(String name) {
    		jdbcTemplate.update("delete from T_USER where NAME = ?", name);
    	}
    
    	@Override
    	public Integer getAllUsers() {
    		return jdbcTemplate.queryForObject("select count(1) from T_USER", Integer.class);
    	}
    
    	@Override
    	public void deleteAllUsers() {
    		jdbcTemplate.update("delete from T_USER");
    	}
    }
``` 
application.properties配置
```
    spring.datasource.url=jdbc:mysql://192.168.116.10:3306/test
    spring.datasource.username=root
    spring.datasource.password=root
    spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```
ApplicationTests测试类编写
```java
    package com.example.demo;
    
     
    
    import org.junit.Assert;
    import org.junit.Before;
    import org.junit.Test;
    import org.junit.runner.RunWith;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
    
    import com.example.demo.service.UserService; 
    
    
    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest
    public class ApplicationTests {
    	
    	@Autowired
    	private UserService userSerivce;
    	@Before
    	public void setUp() {
    		// 准备，清空user表
    		userSerivce.deleteAllUsers();
    	}
    	@Test
    	public void test() throws Exception {
    		// 插入5个用户
    		userSerivce.create("a", 1);
    		userSerivce.create("b", 2);
    		userSerivce.create("c", 3);
    		userSerivce.create("d", 4);
    		userSerivce.create("e", 5);
    		// 查数据库，应该有5个用户
    		Assert.assertEquals(5, userSerivce.getAllUsers().intValue());
    		// 删除两个用户
    		userSerivce.deleteByName("a");
    		userSerivce.deleteByName("e");
    		// 查数据库，应该有5个用户
    		Assert.assertEquals(3, userSerivce.getAllUsers().intValue());
    	}	
    
    }
```
  

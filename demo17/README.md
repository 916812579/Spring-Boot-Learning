# Spring Boot整合MyBatis

- 参考：[http://blog.didispace.com/springbootmybatis/](http://blog.didispace.com/springbootmybatis/)
- 参考：[http://blog.didispace.com/mybatisinfo/](http://blog.didispace.com/mybatisinfo/)

## 配置

- pom.xml中引入依赖

```xml
<dependency>
	<groupId>org.mybatis.spring.boot</groupId>
	<artifactId>mybatis-spring-boot-starter</artifactId>
	<version>1.1.1</version>
</dependency>

<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
</dependency>
```

- application.yml数据库相关配置

```sql
<dependency>
	<groupId>org.mybatis.spring.boot</groupId>
	<artifactId>mybatis-spring-boot-starter</artifactId>
	<version>1.1.1</version>
</dependency>

<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
</dependency>
```

- mysql中创建User表
```bash
mysql> desc user;
+-------+--------------+------+-----+---------+----------------+
| Field | Type         | Null | Key | Default | Extra          |
+-------+--------------+------+-----+---------+----------------+
| id    | bigint(20)   | NO   | PRI | NULL    | auto_increment |
| age   | int(11)      | NO   |     | NULL    |                |
| name  | varchar(255) | NO   |     | NULL    |                |
+-------+--------------+------+-----+---------+----------------+
3 rows in set (0.02 sec)
```

- 创建User映射的操作`UserMapper`
```java
package com.example.demo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.demo.model.User;

@Mapper
public interface UserMapper {
	
	@Select("SELECT * FROM user WHERE NAME = #{name}")
	User findByName(@Param("name") String name);

	@Insert("INSERT INTO user(NAME, AGE) VALUES(#{name}, #{age})")
	int insert(@Param("name") String name, @Param("age") Integer age);
}
```
- 测试类
```java
package com.example.demo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class ApplicationTests {
	
	@Autowired
	private UserMapper userMapper;
	@Test
	@Rollback
	public void findByName() throws Exception {
		userMapper.insert("AAA", 20);
		User u = userMapper.findByName("AAA");
		
		Assert.assertEquals(20, u.getAge().intValue());
	}
}
```

## MyBatis注解配置详解

### 传参方式

- 使用`@Param`


```java
@Insert("INSERT INTO user(NAME, AGE) VALUES(#{name}, #{age})")
int insert(@Param("name") String name, @Param("age") Integer age);
``` 

`@Param`中定义的`name`对应了SQL中的`#{name}`，`age`对应了SQL中的`#{age}`。

- 使用`Map`

```java
@Insert("INSERT INTO USER(NAME, AGE) VALUES(#{name,jdbcType=VARCHAR}, #{age,jdbcType=INTEGER})")
int insertByMap(Map<String, Object> map);
```
传递参数时，`map`中需要包含`key`为`name`和`age`的键值对

- 使用对象(pojo)

```java
@Insert("INSERT INTO USER(NAME, AGE) VALUES(#{name}, #{age})")
int insertByUser(User user);
```
这样`#{name}`、`#{age}`就分别对应了User对象中的`name`和`age`属性

### 增删改查
- `@Insert`
- `@Delete`
- `@Update`
- `@Select`

### 返回结果的绑定

- 通过`@Results`和`@Result`注解来进行绑定

```java
@Results({
    @Result(property = "name", column = "name"),
    @Result(property = "age", column = "age")
})
@Select("SELECT name, age FROM user")
List<User> findAll();
```



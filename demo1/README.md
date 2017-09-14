# spring boot快速入门
- 参考：[http://blog.didispace.com/spring-boot-learning-1/](http://blog.didispace.com/spring-boot-learning-1/)
## 通过 "SPRING INITIALIZR" 工具产生基础项目
1、访问[http://start.spring.io/](http://start.spring.io/)

2、生成maven项目

3、导入maven项目到IDE



- src/main/java下的程序入口：Application

- src/main/resources下的配置文件：application.properties

- src/test/下的测试入口：ApplicationTests

pom.xml文件内容如下

    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    	<modelVersion>4.0.0</modelVersion>
    
    	<groupId>com.example</groupId>
    	<artifactId>demo1</artifactId>
    	<version>0.0.1-SNAPSHOT</version>
    	<packaging>jar</packaging>
    
    	<name>demo1</name>
    	<description>Demo project for Spring Boot</description>
    
    	<parent>
    		<groupId>org.springframework.boot</groupId>
    		<artifactId>spring-boot-starter-parent</artifactId>
    		<version>1.5.7.RELEASE</version>
    		<relativePath /> <!-- lookup parent from repository -->
    	</parent>
    
    	<properties>
    		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    		<java.version>1.8</java.version>
    	</properties>
    
    	<dependencies>
    		<dependency>
    			<groupId>org.springframework.boot</groupId>
    			<artifactId>spring-boot-starter</artifactId>
    		</dependency>
    
    		<dependency>
    			<groupId>org.springframework.boot</groupId>
    			<artifactId>spring-boot-starter-test</artifactId>
    			<scope>test</scope>
    		</dependency>
    	</dependencies>
    
    	<build>
    		<plugins>
    			<plugin>
    				<groupId>org.springframework.boot</groupId>
    				<artifactId>spring-boot-maven-plugin</artifactId>
    			</plugin>
    		</plugins>
    	</build>
    </project>

- spring-boot-starter：核心模块，包括自动配置支持、日志和YAML
- spring-boot-starter-test：测试模块，包括JUnit、Hamcrest、Mockito


## 引入Web模块
- spring-boot-starter-web

pom.xml中添加如下依赖

    <dependency>
    	<groupId>org.springframework.boot</groupId>
    	<artifactId>spring-boot-starter-web</artifactId>
    </dependency> 

## 编写HelloWorld服务

    @RestController
    public class HelloController {
        @RequestMapping("/hello")
        public String index() {
            return "Hello World";
        }
    }

- 启动主程序，打开浏览器访问 http://localhost:8080/hello，可以看到页面输出Hello World

## 单元测试
    package com.example.demo;
    
     
    
    import static org.hamcrest.Matchers.equalTo;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
    
    import org.junit.Before;
    import org.junit.Test;
    import org.junit.runner.RunWith;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.http.MediaType;
    import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
    import org.springframework.test.web.servlet.MockMvc;
    import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
    import org.springframework.test.web.servlet.setup.MockMvcBuilders;
    
    
    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootApplication
    public class ApplicationTests {
    	
    	private MockMvc mvc;
    	@Before
    	public void setUp() throws Exception {
    		mvc = MockMvcBuilders.standaloneSetup(new HelloController()).build();
    	}
    	@Test
    	public void getHello() throws Exception {
    		mvc.perform(MockMvcRequestBuilders.get("/hello").accept(MediaType.APPLICATION_JSON))
    				.andExpect(status().isOk())
    				.andExpect(content().string(equalTo("Hello World")));
    	}
    
    }

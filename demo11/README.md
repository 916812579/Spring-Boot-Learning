# Spring Boot中使用AOP统一处理Web请求日志

- 参考：[http://blog.didispace.com/springbootaoplog/](http://blog.didispace.com/springbootaoplog/)

# 准备工作
- pom.xml中引入如下配置

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

- HelloController 
```java
package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	@ResponseBody
	public String hello(@RequestParam String name) {
		return "Hello " + name;
	}
}

```

# 引入AOP依赖
- pom.xml引入如下配置

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```
如果需要使用CGLIB来实现AOP的时候，需要配置`spring.aop.proxy-target-class=true`

# 实现Web层的日志切面

- 实现AOP的切面主要有以下几个要素：

- 使用`@Aspect`注解将一个java类定义为切面类
- 使用`@Pointcut`定义一个切入点
- 根据需要在切入点不同位置的切入内容
   - 使用`@Before`在切入点开始处切入内容
   - 使用`@After`在切入点结尾处切入内容
   - 使用`@AfterReturning`在切入点return内容之后切入内容（可以用来对处理返回值做一些加工处理）
   - 使用`@Around`在切入点前后切入内容，并自己控制何时执行切入点自身的内容
   - 使用`@AfterThrowing`用来处理当切入内容部分抛出异常之后的处理逻辑

```java
package com.example.demo;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class WebLogAspect {
	private Logger logger = Logger.getLogger(getClass());

	@Pointcut("execution(public * com.example.demo.web..*.*(..))")
	public void webLog() {
		
	}

	@Before("webLog()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		// 接收到请求，记录请求内容
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		// 记录下请求内容
		logger.info("URL : " + request.getRequestURL().toString());
		logger.info("HTTP_METHOD : " + request.getMethod());
		logger.info("IP : " + request.getRemoteAddr());
		logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "."
				+ joinPoint.getSignature().getName());
		logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
	}

	@AfterReturning(returning = "ret", pointcut = "webLog()")
	public void doAfterReturning(Object ret) throws Throwable {
		// 处理完请求，返回内容
		logger.info("RESPONSE : " + ret);
	}
}
```
运行程序，访问http://localhost:8080/hello?name=test会打印如下信息
```
2017-09-19 14:53:36.736  INFO 156 --- [nio-8080-exec-4] com.example.demo.WebLogAspect            : HTTP_METHOD : GET
2017-09-19 14:53:36.736  INFO 156 --- [nio-8080-exec-4] com.example.demo.WebLogAspect            : IP : 0:0:0:0:0:0:0:1
2017-09-19 14:53:36.738  INFO 156 --- [nio-8080-exec-4] com.example.demo.WebLogAspect            : CLASS_METHOD : com.example.demo.web.HelloController.hello
2017-09-19 14:53:36.738  INFO 156 --- [nio-8080-exec-4] com.example.demo.WebLogAspect            : ARGS : [test]
2017-09-19 14:53:56.765  INFO 156 --- [nio-8080-exec-4] com.example.demo.WebLogAspect            : RESPONSE : Hello test
2017-09-19 14:54:14.666  INFO 156 --- [nio-8080-exec-5] com.example.demo.WebLogAspect            : URL : http://localhost:8080/hello
2017-09-19 14:54:14.666  INFO 156 --- [nio-8080-exec-5] com.example.demo.WebLogAspect            : HTTP_METHOD : GET
2017-09-19 14:54:14.666  INFO 156 --- [nio-8080-exec-5] com.example.demo.WebLogAspect            : IP : 0:0:0:0:0:0:0:1
2017-09-19 14:54:14.666  INFO 156 --- [nio-8080-exec-5] com.example.demo.WebLogAspect            : CLASS_METHOD : com.example.demo.web.HelloController.hello
2017-09-19 14:54:14.666  INFO 156 --- [nio-8080-exec-5] com.example.demo.WebLogAspect            : ARGS : [test]
2017-09-19 14:54:14.666  INFO 156 --- [nio-8080-exec-5] com.example.demo.WebLogAspect            : RESPONSE : Hello test
```
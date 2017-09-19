# `Spring Boot`中使用`@Async`实现异步调用

- 参考：[http://blog.didispace.com/springbootasync/](http://blog.didispace.com/springbootasync/)

```同步调用```:指程序按照定义顺序依次执行
```异步调用```:指程序在顺序执行时，不等待异步调用的语句返回结果就执行后面的程序

# 异步调用
在Spring Boot中，我们只需要通过使用`@Async`注解就能简单的将原来的同步函数变为异步函数
- 应用程序需要配置`@EnableAsync`才能让`@Async`起作用

```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
```
- `@Async`可以让原来同步函数变为异步函数

```java
package com.example.demo;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class Task {
	@Async
	public void doTaskOne() throws Exception {
		System.out.println("doTaskOne");
	}

	@Async
	public void doTaskTwo() throws Exception {
		System.out.println("doTaskTwo");
	}

	@Async
	public void doTaskThree() throws Exception {
		System.out.println("doTaskThree");
	}
}
```
- 测试

```java
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private Task task;

	@Test
	public void doTask() throws Exception {

		task.doTaskOne();
		task.doTaskTwo();
		task.doTaskThree();

	}
}
```
输出结果可以多执行几次观察结果

- 注： ```@Async```所修饰的函数不要定义为```static```类型，这样异步调用不会生效

# 异步回调

可以使用`Future<T>`接收异步方法返回的结果
```java
package com.example.demo;

import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

@Component
public class CallBackTask {
	@Async
	public Future<String> doTaskOne() throws Exception {
		System.out.println("doTaskOne");
		return new AsyncResult<>("doTaskOne");
	}

	@Async
	public  Future<String> doTaskTwo() throws Exception {
		System.out.println("doTaskTwo");
		return new AsyncResult<>("doTaskTwo");
	}

	@Async
	public  Future<String> doTaskThree() throws Exception {
		System.out.println("doTaskThree");
		return new AsyncResult<>("doTaskThree");
	}
}
```
测试
```java
@Test
public void doCallBackTask() throws Exception {
	Future<String> taskOneFuture = callBackTask.doTaskOne();
	Future<String> taskTwoFuture = callBackTask.doTaskTwo();
	Future<String> taskThreeFuture = callBackTask.doTaskThree();
	
	while (true) {
		if (taskOneFuture.isDone() && taskTwoFuture.isDone() && taskThreeFuture.isDone()) {
			System.out.println("所有任务已经完成");
			break;
		}
	}
	System.out.println(taskOneFuture.get());
	System.out.println(taskTwoFuture.get());
	System.out.println(taskThreeFuture.get());
}
```

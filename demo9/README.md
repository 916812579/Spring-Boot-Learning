# Spring Boot中使用@Scheduled创建定时任务

- 参考：[http://blog.didispace.com/springbootscheduled/](http://blog.didispace.com/springbootscheduled/)

# 创建定时任务

- 在```Spring Boot```的主类中加入```@EnableScheduling```注解，启用定时任务的配置

```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
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
- 创建定时任务

```java
package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        System.out.println("现在时间：" + dateFormat.format(new Date()));
    }
}
```
- 运行程序，控制台显示如下,5秒一次
```
现在时间：11:25:00
现在时间：11:25:05
现在时间：11:25:10
现在时间：11:25:15
...
```

# @Scheduled详解

- ```@Scheduled(fixedRate = 5000)``` ：上一次开始执行时间点之后5秒再执行
- ```@Scheduled(fixedDelay = 5000)``` ：上一次执行完毕时间点之后5秒再执行
- ```@Scheduled(initialDelay=1000, fixedRate=5000)``` ：第一次延迟1秒后执行，之后按fixedRate的规则每5秒执行一次
- ```@Scheduled(cron="*/5 * * * * *")``` ：通过cron表达式定义规则 


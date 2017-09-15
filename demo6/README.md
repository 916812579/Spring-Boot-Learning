# Spring Boot 使用 Druid 和监控配置
- 参考：[http://blog.csdn.net/catoop/article/details/50925337](http://blog.csdn.net/catoop/article/details/50925337)

pom.xml中添加Druid依赖的jar包

    <dependency>
    	<groupId>com.alibaba</groupId>
    	<artifactId>druid</artifactId>
    	<version>1.0.18</version>
    </dependency>

配置数据源相关信息

    # 数据库访问配置
    # 主数据源，默认的
    spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
    spring.datasource.url=jdbc:mysql://192.168.116.10:3306/test
    spring.datasource.username=root
    spring.datasource.password=root
    spring.datasource.driver-class-name=com.mysql.jdbc.Driver
    
    
    # 下面为连接池的补充设置，应用到上面所有数据源中
    # 初始化大小，最小，最大
    spring.datasource.initialSize=5
    spring.datasource.minIdle=5
    spring.datasource.maxActive=20
    # 配置获取连接等待超时的时间
    spring.datasource.maxWait=60000
    # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 
    spring.datasource.timeBetweenEvictionRunsMillis=60000
    # 配置一个连接在池中最小生存的时间，单位是毫秒 
    spring.datasource.minEvictableIdleTimeMillis=300000
    spring.datasource.validationQuery=SELECT 1
    spring.datasource.testWhileIdle=true
    spring.datasource.testOnBorrow=false
    spring.datasource.testOnReturn=false
    # 打开PSCache，并且指定每个连接上PSCache的大小 
    spring.datasource.poolPreparedStatements=true
    spring.datasource.maxPoolPreparedStatementPerConnectionSize=20
    # 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙 
    spring.datasource.filters=stat,wall,log4j
    # 通过connectProperties属性来打开mergeSql功能；慢SQL记录
    spring.datasource.connectionProperties=druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
    # 合并多个DruidDataSource的监控数据
    #spring.datasource.useGlobalDataSourceStat=true

配置监控统计功能

DruidStatViewServlet

    package com.example.demo.druid;
    
    import javax.servlet.annotation.WebInitParam;
    import javax.servlet.annotation.WebServlet;
    
    import com.alibaba.druid.support.http.StatViewServlet;
    
    @SuppressWarnings("serial")
    @WebServlet(urlPatterns = "/druid/*", 
        initParams={
                @WebInitParam(name="allow",value="127.0.0.1"),// IP白名单 (没有配置或者为空，则允许所有访问)
                @WebInitParam(name="deny",value="192.168.16.111"),// IP黑名单 (存在共同时，deny优先于allow)
                @WebInitParam(name="loginUsername",value="admin"),// 用户名
                @WebInitParam(name="loginPassword",value="admin"),// 密码
                @WebInitParam(name="resetEnable",value="false")// 禁用HTML页面上的“Reset All”功能
        })
    public class DruidStatViewServlet extends StatViewServlet{
    
    }

DruidStatFilter

     package com.example.demo.druid;
     
     import javax.servlet.annotation.WebFilter;
     import javax.servlet.annotation.WebInitParam;
     
     import com.alibaba.druid.support.http.WebStatFilter;
     
     @WebFilter(filterName="druidWebStatFilter",urlPatterns="/*",
     initParams={
         @WebInitParam(name="exclusions",value="*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*")// 忽略资源
     })
     public class DruidStatFilter  extends WebStatFilter {
     
     }
     
Application类
    package com.example.demo;
    
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.boot.web.servlet.ServletComponentScan;
    
    @SpringBootApplication
    @ServletComponentScan
    public class Application {
    
    	public static void main(String[] args) {
    		SpringApplication.run(Application.class, args);
    	}
    }

注意添加 @ServletComponentScan，不然的话DruidStatViewServlet和DruidStatFilter不会起作用

启动应用然后访问：http://localhost:8080/druid/index.html即可查看相关统计信息
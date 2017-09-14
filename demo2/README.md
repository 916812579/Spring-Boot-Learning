# Spring Boot开发Web应用
- 参考：[http://blog.didispace.com/springbootweb/](http://blog.didispace.com/springbootweb/)

## 静态资源的路径
**可以在ResourceProperties发现**

静态资源目录位置默认在classpath下，目录名需符合如下规则：

- /static
-  /public
- /resources
- /META-INF/resources

这些目录可以在src/main/resources/目录下创建

## 模板引擎

Spring Boot提供了默认配置的模板引擎主要有以下几种：

- Thymeleaf
- FreeMarker
- Velocity
- Groovy
- Mustache

模板默认的路径在 **src/main/resources/templates**

##  Thymeleaf
Thymeleaf是一个XML/XHTML/HTML5模板引擎，可用于Web与非Web环境中的应用开发。它是一个开源的Java库，基于Apache License 2.0许可，由Daniel Fernández创建，该作者还是Java加密库Jasypt的作者。

Thymeleaf提供了一个用于整合Spring MVC的可选模块，在应用开发中，你可以使用Thymeleaf来完全代替JSP或其他模板引擎，如Velocity、FreeMarker等。Thymeleaf的主要目标在于提供一种可被浏览器正确显示的、格式良好的模板创建方式，因此也可以用作静态建模。你可以使用它创建经过验证的XML与HTML模板。相对于编写逻辑或代码，开发者只需将标签属性添加到模板中即可。接下来，这些标签属性就会在DOM（文档对象模型）上执行预先制定好的逻辑。

创建模板index.html如下

     <!DOCTYPE html>
     <html>
     <head lang="en">
         <meta charset="UTF-8" />
         <title></title>
     </head>
     <body>
     <h1 th:text="${host}">Hello World</h1>
     </body>
     </html>

pom.xml添加Thymeleaf依赖

    <dependency>
    	<groupId>org.springframework.boot</groupId>
    	<artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>

编写controller如下

    @Controller
    public class HelloController {
        @RequestMapping("/")
        public String index(ModelMap map) {
            // 加入一个属性，用来在模板中读取
            map.addAttribute("host", "http://blog.didispace.com");
            // return模板文件的名称，对应src/main/resources/templates/index.html
            return "index";  
        }
    }

启动应用访问http://localhost:8080/，会显示http://blog.didispace.com
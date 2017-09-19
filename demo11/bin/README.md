# Spring Boot中使用Swagger2构建强大的RESTful API文档
- 参考：[http://blog.didispace.com/springbootswagger2/](http://blog.didispace.com/springbootswagger2/)

pom.xml添加如下依赖

    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger2</artifactId>
        <version>2.2.2</version>
    </dependency>
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger-ui</artifactId>
        <version>2.2.2</version>
    </dependency>
	
	
创建Swagger2配置类

    package com.example.demo.config;
    
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    
    import springfox.documentation.builders.ApiInfoBuilder;
    import springfox.documentation.builders.PathSelectors;
    import springfox.documentation.builders.RequestHandlerSelectors;
    import springfox.documentation.service.ApiInfo;
    import springfox.documentation.spi.DocumentationType;
    import springfox.documentation.spring.web.plugins.Docket;
    import springfox.documentation.swagger2.annotations.EnableSwagger2;
    
    @Configuration
    @EnableSwagger2
    public class Swagger2 {
        @Bean
        public Docket createRestApi() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo())
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("com.example.demo.web"))
                    .paths(PathSelectors.any())
                    .build();
        }
        private ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                    .title("Spring Boot中使用Swagger2构建RESTful APIs")
                    .description("")
                    .termsOfServiceUrl("http://localhost:8080/")
                    .contact("")
                    .version("1.0")
                    .build();
        }
    }


添加文档内容

- @ApiOperation注解来给API增加说明 
- @ApiImplicitParam注解来给参数增加说明
- 

     package com.example.demo.web;
     
     import java.util.ArrayList;
     import java.util.Collections;
     import java.util.HashMap;
     import java.util.List;
     import java.util.Map;
     
     import org.springframework.web.bind.annotation.ModelAttribute;
     import org.springframework.web.bind.annotation.PathVariable;
     import org.springframework.web.bind.annotation.RequestMapping;
     import org.springframework.web.bind.annotation.RequestMethod;
     import org.springframework.web.bind.annotation.RestController;
     
     import com.example.demo.model.User;
     
     import io.swagger.annotations.ApiImplicitParam;
     import io.swagger.annotations.ApiImplicitParams;
     import io.swagger.annotations.ApiOperation;
     
     @RestController 
     @RequestMapping(value="/users")     // 通过这里配置使下面的映射都在/users下 
     public class UserController { 
      
         // 创建线程安全的Map 
         static Map<Long, User> users = Collections.synchronizedMap(new HashMap<Long, User>()); 
      
         @ApiOperation(value="获取用户列表", notes="")
         @RequestMapping(value="/", method=RequestMethod.GET) 
         public List<User> getUserList() { 
             // 处理"/users/"的GET请求，用来获取用户列表 
             // 还可以通过@RequestParam从页面中传递参数来进行查询条件或者翻页信息的传递 
             List<User> r = new ArrayList<User>(users.values()); 
             return r; 
         } 
      
         @ApiOperation(value="创建用户", notes="根据User对象创建用户")
         @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
         @RequestMapping(value="/", method=RequestMethod.POST) 
         public String postUser(@ModelAttribute User user) { 
             // 处理"/users/"的POST请求，用来创建User 
             // 除了@ModelAttribute绑定参数之外，还可以通过@RequestParam从页面中传递参数 
             users.put(user.getId(), user); 
             return "success"; 
         } 
      
         @RequestMapping(value="/{id}", method=RequestMethod.GET) 
         public User getUser(@PathVariable Long id) { 
             // 处理"/users/{id}"的GET请求，用来获取url中id值的User信息 
             // url中的id可通过@PathVariable绑定到函数的参数中 
             return users.get(id); 
         } 
      
         @ApiOperation(value="更新用户详细信息", notes="根据url的id来指定更新对象，并根据传过来的user信息来更新用户详细信息")
         @ApiImplicitParams({
                 @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long"),
                 @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
         })
         @RequestMapping(value="/{id}", method=RequestMethod.PUT) 
         public String putUser(@PathVariable Long id, @ModelAttribute User user) { 
             // 处理"/users/{id}"的PUT请求，用来更新User信息 
             User u = users.get(id); 
             u.setName(user.getName()); 
             u.setAge(user.getAge()); 
             users.put(id, u); 
             return "success"; 
         } 
      
         @RequestMapping(value="/{id}", method=RequestMethod.DELETE) 
         public String deleteUser(@PathVariable Long id) { 
             // 处理"/users/{id}"的DELETE请求，用来删除User 
             users.remove(id); 
             return "success"; 
         } 
      
     }

启动Spring Boot程序，访问：http://localhost:8080/swagger-ui.html
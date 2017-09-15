# Spring Boot构建RESTful API与单元测试
- 参考：[http://blog.didispace.com/springbootrestfulapi/](http://blog.didispace.com/springbootrestfulapi/)

## 重要的几个注解

- @Controller：修饰class，用来创建处理http请求的对象
- @RestController：Spring4之后加入的注解，原来在@Controller中返回json需要@ResponseBody来配合，如果直接用@RestController替代@Controller就不需要再配置@ResponseBody，默认返回json格式。
- @RequestMapping：配置url映射


## lombok

### eclipse lombok 插件安装  

下载：[https://projectlombok.org/downloads/lombok.jar](https://projectlombok.org/downloads/lombok.jar)

    java -jar  .\lombok.jar

或者双击，执行后选择IDE就好了

pom.xml加入如下依赖

    <dependency>
    	<groupId>org.projectlombok</groupId>
    	<artifactId>lombok-maven-plugin</artifactId>
    	<version>1.16.18.1</version>
    </dependency>

如上配置后，就可以在model上添加@Getter、@Setter、@NoArgsConstructor、@AllArgsConstructor自动帮助生成相应的方法

### RESTful API具体设计如下


| 请求类型        | URL           | 功能说明  |
| ------------- |:-------------: | -----:|
| GET      | /users |查询用户列表 |
| POST      | /users      |   创建一个用户 |
| GET | /users/id      |    根据id查询一个用户 |
| PUT | /users/id      |    根据id更新一个用户 |
| DELETE | /users/id      |    根据id删除一个用户 |

### User对象的操作接口
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
    
    @RestController 
    @RequestMapping(value="/users")     // 通过这里配置使下面的映射都在/users下 
    public class UserController { 
     
        // 创建线程安全的Map 
        static Map<Long, User> users = Collections.synchronizedMap(new HashMap<Long, User>()); 
     
        @RequestMapping(value="/", method=RequestMethod.GET) 
        public List<User> getUserList() { 
            // 处理"/users/"的GET请求，用来获取用户列表 
            // 还可以通过@RequestParam从页面中传递参数来进行查询条件或者翻页信息的传递 
            List<User> r = new ArrayList<User>(users.values()); 
            return r; 
        } 
     
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
    	
测试类

    package com.example.demo;
    
    import static org.hamcrest.Matchers.equalTo;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
    
    import org.junit.Before;
    import org.junit.Test;
    import org.junit.runner.RunWith;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
    import org.springframework.test.web.servlet.MockMvc;
    import org.springframework.test.web.servlet.RequestBuilder;
    import org.springframework.test.web.servlet.setup.MockMvcBuilders;
    
    import com.example.demo.web.UserController;
    
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
    
    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootApplication
    public class ApplicationTests {
    
    	private MockMvc mvc;
    
    	@Before
    	public void setUp() throws Exception {
    		mvc = MockMvcBuilders.standaloneSetup(new UserController()).build();
    	}
    
    	@Test
    	public void testUserController() throws Exception {
    		// 测试UserController
    		RequestBuilder request = null;
    
    		// 1、get查一下user列表，应该为空
    		request = get("/users/");
    		mvc.perform(request).andExpect(status().isOk()).andExpect(content().string(equalTo("[]")));
    
    		// 2、post提交一个user
    		request = post("/users/").param("id", "1").param("name", "测试大师").param("age", "20");
        		mvc.perform(request).andExpect(content().string(equalTo("success")));
    
    		// 3、get获取user列表，应该有刚才插入的数据
    		request = get("/users/");
    		mvc.perform(request).andExpect(status().isOk())
    				.andExpect(content().string(equalTo("[{\"id\":1,\"name\":\"测试大师\",\"age\":20}]")));
    
    		// 4、put修改id为1的user
    		request = put("/users/1").param("name", "测试终极大师").param("age", "30");
    		mvc.perform(request).andExpect(content().string(equalTo("success")));
    
    		// 5、get一个id为1的user
    		request = get("/users/1");
    		mvc.perform(request).andExpect(content().string(equalTo("{\"id\":1,\"name\":\"测试终极大师\",\"age\":30}")));
    
    		// 6、del删除id为1的user
    		request = delete("/users/1");
    		mvc.perform(request).andExpect(content().string(equalTo("success")));
    
    		// 7、get查一下user列表，应该为空
    		request = get("/users/");
    		mvc.perform(request).andExpect(status().isOk()).andExpect(content().string(equalTo("[]")));
    
    	}
    
    }
    	

参数绑定的注解：@PathVariable  @ModelAttribute  @RequestParam	
	
	

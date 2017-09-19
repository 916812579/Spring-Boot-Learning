# Spring Boot中Web应用的统一异常处理
- 参考：[http://blog.didispace.com/springbootexception/](http://blog.didispace.com/springbootexception/)
- 参考：[http://www.jianshu.com/p/6e7651705d29](http://www.jianshu.com/p/6e7651705d29)
- 参考：[https://www.2cto.com/kf/201704/625169.html](https://www.2cto.com/kf/201704/625169.html)

Web应用Spring Boot提供了一个默认的映射：/error，当处理中抛出异常之后，会转到该请求中处理，并且该请求有一个全局的错误页面用来展示异常内容。

Spring Boot中实现了默认的error映射，但是在实际应用中，默认的错误页面对用户来说并不够友好，所以需要去实现自己的异常提示



- `@ControllerAdvice` 注解内部使用`@ExceptionHandler`、`@InitBinder`、`@ModelAttribute`注解的方法应用到所有的 `@RequestMapping`注解的方法


使用@ControllerAdvice处理全局异常需要在配置文件中添加如下配置
```java    
    #出现错误时, 直接抛出异常
    spring.mvc.throw-exception-if-no-handler-found=true
    #不要为我们工程中的资源文件建立映射
    spring.resources.add-mappings=false
```

如下处理全局异常
```java
    package com.example.demo;
    
    import javax.servlet.http.HttpServletRequest;
    
    import org.springframework.web.bind.annotation.ControllerAdvice;
    import org.springframework.web.bind.annotation.ExceptionHandler;
    import org.springframework.web.servlet.ModelAndView;
    import org.springframework.web.servlet.NoHandlerFoundException;
    
    /**
     * 全局异常处理类
     */
    @ControllerAdvice
    class GlobalExceptionHandler {
    	public static final String DEFAULT_ERROR_VIEW = "error";
    
    	@ExceptionHandler(value = NoHandlerFoundException.class)
    	public ModelAndView forbidden(HttpServletRequest req, Exception e) throws Exception {
    		ModelAndView mav = new ModelAndView();
    		mav.addObject("exception", e);
    		mav.addObject("url", req.getRequestURL());
    		mav.setViewName("404");
    		return mav;
    	}
    
    	/**
    	 * 处理所有异常
    	 * 
    	 * @param req
    	 * @param e
    	 * @return
    	 * @throws Exception
    	 */
    	@ExceptionHandler(value = Exception.class)
    	public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
    		ModelAndView mav = new ModelAndView();
    		mav.addObject("exception", e);
    		mav.addObject("url", req.getRequestURL());
    		mav.setViewName(DEFAULT_ERROR_VIEW);
    		return mav;
    	}
    
    }
 ```
如果期望返回JSON格式，则需要添加`@ResponseBody`注解
```
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseBody
    public Map<String, Object> forbidden(HttpServletRequest req, Exception e) throws Exception {
    	Map<String, Object> result = new HashMap<>();
    	result.put("exception", e);
    	result.put("url", req.getRequestURL());
    	result.put("name", "404");
    	return result;
    }  
```

除了上面的这种方法，也可以实现ErrorController。这样，springboot就不会自动创建BasicErrorController了，就会调用我们自己实现的Controller。

```java
    package com.example.demo;
    
    import java.util.HashMap;
    import java.util.Map;
    
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    
    import org.springframework.boot.autoconfigure.web.AbstractErrorController;
    import org.springframework.boot.autoconfigure.web.ErrorAttributes;
    import org.springframework.core.Ordered;
    import org.springframework.core.annotation.Order;
    import org.springframework.http.HttpStatus;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.ControllerAdvice;
    import org.springframework.web.bind.annotation.ExceptionHandler;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.ResponseBody;
    import org.springframework.web.bind.annotation.ResponseStatus;
    import org.springframework.web.servlet.ModelAndView;
    import org.springframework.web.servlet.NoHandlerFoundException;
    
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ControllerAdvice
    @Controller
    @RequestMapping("${server.error.path:${error.path:/error}}")
    public class ControllerExceptionHandler extends AbstractErrorController {
    
    	public ControllerExceptionHandler(ErrorAttributes errorAttributes) {
    		super(errorAttributes);
    	}
    
    	@Override
    	public String getErrorPath() {
    		return null;
    	}
    
    	/**
    	 * 500错误.
    	 * 
    	 * @param req
    	 * @param rsp
    	 * @param ex
    	 * @return
    	 * @throws Exception
    	 */
    	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    	@ExceptionHandler(Exception.class)
    	public ModelAndView serverError(HttpServletRequest req, HttpServletResponse rsp, Exception ex) throws Exception {
    		ModelAndView mav = new ModelAndView();
    		mav.addObject("exception", ex);
    		mav.addObject("url", req.getRequestURL());
    		mav.setViewName("/error");
    		return mav;
    	}
    
    	/**
    	 * 404的拦截.
    	 * 
    	 * @param request
    	 * @param response
    	 * @param ex
    	 * @return
    	 * @throws Exception
    	 */
    	@ResponseStatus(code = HttpStatus.NOT_FOUND)
    	@ExceptionHandler(NoHandlerFoundException.class)
    	@ResponseBody
    	public Map<String, Object> notFound(HttpServletRequest request, HttpServletResponse response, Exception ex)
    			throws Exception {
    		Map<String, Object> result = new HashMap<>();
    		result.put("exception", ex);
    		result.put("url", request.getRequestURL());
    		result.put("name", "404");
    		return result;
    	}
    
     
    
    }
```

package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常处理类
 */
// @ControllerAdvice
class GlobalExceptionHandler {
	public static final String DEFAULT_ERROR_VIEW = "error";

 
	@ExceptionHandler(value = NoHandlerFoundException.class)
	@ResponseBody
	public Map<String, Object> forbidden(HttpServletRequest req, Exception e) throws Exception {
		Map<String, Object> result = new HashMap<>();
		result.put("exception", e);
		result.put("url", req.getRequestURL());
		result.put("name", "404");
		return result;
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

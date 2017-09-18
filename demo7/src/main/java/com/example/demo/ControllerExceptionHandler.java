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

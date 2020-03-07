package com.cy.pj.common.web;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cy.pj.common.vo.JsonResult;
/**@ControllerAdvice 描述的类为控制层全局异常处理类*/
//@ResponseBody
//@ControllerAdvice
@RestControllerAdvice //==@ControllerAdvice+@ResponseBody
public class GlobalExceptionHandler {
      /**
       * @ExceptionHandler 描述的方法为异常处理方法,注解内部定义的
       * 异常类型为此方法可以处理的异常类型
       * @return
       */
	  @ExceptionHandler(RuntimeException.class)
	  //@ResponseBody
	  public JsonResult doHandleRuntimeException(
			  RuntimeException e) {
		  e.printStackTrace();
		  return new JsonResult(e);
	  }
	  @ExceptionHandler(ShiroException.class)
	  //@ResponseBody
	  public JsonResult doHandleShiroException(
			  ShiroException e) {
		  JsonResult r=new JsonResult();
		  r.setState(0);
		  if(e instanceof UnknownAccountException) {
			  r.setMessage("账户不存在");
		  }else if(e instanceof LockedAccountException) {
			  r.setMessage("账户已被禁用");
		  }else if(e instanceof IncorrectCredentialsException) {
			  r.setMessage("密码不正确");
		  }else if(e instanceof AuthorizationException) {
			  r.setMessage("没有此操作权限");
		  }else {
			  r.setMessage("系统维护中");
		  }
		  e.printStackTrace();
		  return r;
	  }

}

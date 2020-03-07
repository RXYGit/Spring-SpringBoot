package com.cy.pj.sys.controller;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysUserService;

@RestController
@RequestMapping("/user/")
public class SysUserController {

	@Autowired
   private SysUserService sysUserService;
	
	@RequestMapping("doUpdatePassword")
	public JsonResult doUpdatePassword(
				 String pwd,
				 String newPwd,
				 String cfgPwd) {
			 sysUserService.updatePassword(pwd, newPwd, cfgPwd);
			 return new JsonResult("update ok");
	}

	 
	 @RequestMapping("doFindObjectById")
	 public JsonResult doFindObjectById(Integer id) {
		 return new JsonResult(sysUserService.findObjectById(id));
	 }
	 @RequestMapping("doLogin")//此url需要匿名访问
	 public JsonResult doLogin(String username,String password, boolean isRememberMe) {
		 //1.封装用户名和密码(存储token对象)
		 UsernamePasswordToken token=
		 new UsernamePasswordToken(username, password);
		 //设置记住我(告诉SecurityManager)
		 if(isRememberMe)token.setRememberMe(true);
		 //2.提交用户名和密码进行认证
		 Subject subject=SecurityUtils.getSubject();
		 subject.login(token);//此时会将token提交securityManager
		 return new JsonResult("login ok");
	 }
	
	 @RequestMapping("doUpdateObject")
	 public JsonResult doUpdateObject(SysUser entity,Integer[] roleIds) {
		 sysUserService.updateObject(entity, roleIds);
		 return new JsonResult("update ok");
	 }//DispatcherServlet-->jackson-->toJsonString()
	 @RequestMapping("doSaveObject")
	 public JsonResult doSaveObject(SysUser entity,Integer[] roleIds) {
		 sysUserService.saveObject(entity, roleIds);
		 return new JsonResult("save ok");
	 }
	 
	 @RequestMapping("doValidById")
	 public JsonResult doValidById(Integer id,Integer valid) {
		 sysUserService.validById(id, valid);
		 return new JsonResult("update ok");
	 }
	 
	 @RequestMapping("doFindPageObjects")
	 public JsonResult doFindPageObjects(String username,Integer pageCurrent) {
		 System.out.println("targetClassName="+sysUserService.getClass().getName());
		 return new JsonResult(sysUserService.findPageObjects(username, pageCurrent));
	 }
	
}











package com.cy.pj.sys.controller;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysMenuService;
import com.cy.pj.sys.vo.SysUserMenuVo;
/**
 * 在此controller定义页面请求的响应
 */
@Controller
@RequestMapping("/")
public class PageController {
	   @Autowired
	   private SysMenuService sysMenuService;
	   @RequestMapping("doIndexUI")
	   public String doIndexUI(Model model) {
		   SysUser user=(SysUser)
		   SecurityUtils.getSubject().getPrincipal();
		   List<SysUserMenuVo> userMenus=
		   sysMenuService.findUserMenus(user.getId());
		   System.out.println("userMenus="+userMenus);
		   
		   model.addAttribute("username",user.getUsername());
		   model.addAttribute("userMenus", userMenus);
		   return "starter";//返回给DispatcherServlet
	   }//DispatcherServlet会调用视图解析器对象view进行解析,并将解析结果响应客户端.
	   /**返回分页页面*/
	   @RequestMapping("doPageUI")
	   public String doPageUI() {
		   //try{Thread.sleep(3000);}catch(Exception e) {}
		   return "common/page";
	   }
	   @RequestMapping("doLoginUI")
	   public String doLoginUI() {
		   return "login";
	   }
	   /** 基于此方法返回日志列表页面*/
//	   @RequestMapping("log/log_list")
//	   public String doLogUI() {
//		   return "sys/log_list";
//	   }
	   /** 基于此方法返回菜单列表页面*/
//	   @RequestMapping("menu/menu_list")
//	   public String doMenuUI() {
//		   return "sys/menu_list";
//	   }
	   //rest风格(软件架构编码风格)的url
	   //{}为rest表达式(内容为自己定义的变量)
	   //@PathVariable 注解用于告诉spring mvc模块,方法参数的值来自url
	   @RequestMapping("{module}/{moduleUI}")
	   public String doModuleUI(@PathVariable String moduleUI) {
		   return "sys/"+moduleUI;
	   }
	   
}


















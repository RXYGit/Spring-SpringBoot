package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.util.CookieUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	
	/**
	 * RPC调用,不要求必须先启动服务提供者
	 */
	@Reference(check = false)     
	private DubboUserService userService;
	
	@Autowired
	private JedisCluster jedisCluster;
	
	/**
	 * http://www.jt.com/user/login.html
	 * http://www.jt.com/user/register.html
	 * @return
	 */
	@RequestMapping("/{moduleName}")
	public String module(@PathVariable String moduleName) {
		
		return moduleName;
	}
	
	/**
	 * 用户的新增
	 * 1:url:http://www.jt.com/user/doRegister
	 * 2:参数password: admin123,username: admin321,phone: 18366104717
	 * 3:返回值 SysResult对象
	 */
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult saveUser(User user) {
		
		userService.saveUser(user);
		return SysResult.success();
	}
	
	/**
	 * 完成用户单点登陆操作
	 * 1:url:http://www.jt.com/user/doLogin?=0.23423423423424
	 * 2:参数:username: xxxxx, password:  xxxxxxx
	 * 3:返回值:SysResult对象
	 * 
	 * 业务:检验用户信息,返回数据
	 * 		ticket=uuid
	 * 		ticket=null
	 */
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult findUserByUP(User user,HttpServletResponse response) {
		
		String ticket = userService.findUserUP(user);
		if(StringUtils.isEmpty(ticket)) {
			return SysResult.fail();
		}
		/**
		 * 如果ticket有值,则将数据保存到cookie中
		 * www.jt.com   manger.jt.com  sso.jt.com
		 * www.jt.com/a/index  只有请求路径/a的地址可以访问该cookie
		 * www.jt.com/index    不能访问cookie
		 */
		Cookie cookie = new Cookie("JT_TICKET", ticket);
		cookie.setDomain("jt.com");//Domain设定Cookie的共享(一个网址绑定一个Cookie)在jt.com域中实现共享
		cookie.setPath("/");//Path是Cookie的权限 一般都写/
		cookie.setMaxAge(7*24*3600); //设置超时秒的时间7天
		response.addCookie(cookie);  //把数据传入到Cookie中
		
		return SysResult.success();
	}
	
	/**
	 * 实现用户退出的操作
	 * 需求:分析:
	 * 		1:当用户点击退出按钮时,应该重定向到系统首页
	 * 		2:根据cookie查询ticket
	 * 		3:删除redis,根据ticket删除redis
	 * 		4:删除cookie信息
	 * url:http://www.jt.com/user/logout.html
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response) {
		
		String ticket = CookieUtil.getCookieValue(request,"JT_TICKET");
		//2.判断是否成功获取ticket信息
		if(!StringUtils.isEmpty(ticket)) {
			jedisCluster.del(ticket);	//删除redis中的数据.
			CookieUtil.deleteCookie(response,"JT_TICKET","jt.com","/");
		}
		return "redirect:/";	//跳转系统首页
		
		
//		//1:动态获取指定的Cookie信息,并且数组校验数据是否有效
//		Cookie[] cookies = request.getCookies();
//		String ticket = null;
//		if (cookies != null && cookies.length>0) {
//			for (Cookie cookie : cookies) {
//				//System.out.println(cookie);
//				if ("JT_TICKET".equals(cookie.getName())) {
//					ticket = cookie.getValue();
//					break;
//				}
//			}
//		}
//		//2:判断是否成功获取ticket信息
//		if (!StringUtils.isEmpty(ticket)) {
//			jedisCluster.del(ticket);  //删除redis数据
//			//利用request对象只能得到key-value,无法获取其他的属性值
//			//一般删除cookie,需要覆盖原有的cookie
//			CookieUtil.deleteCookie(response,"JT_TICKET","jt.com","/");
//		}
//		return "redirect:/";
	}
}





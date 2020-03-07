package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.service.UserService;
import com.jt.util.CookieUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

//为用户提供JSON数据
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private JedisCluster jedisCluster;
	
	/**
	 * 实现用户数据的校验,跨域访问.
	 * url:http://sso.jt.com/user/check/chenchen/1
	 *   参数: callback
	 *   返回值:SysResult的VO对象
	 */
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable String param,@PathVariable Integer type,
			String callback) {
		
		boolean flag = userService.checkUser(param,type);
		SysResult sysResult = SysResult.success(flag);
		//int a = 1/0;
		return new JSONPObject(callback, sysResult);
		
		/*JSONPObject jsonpObject = null;
		try {
			//业务调用返回true/false即可
			boolean flag = userService.checkUser(param,type);
			SysResult sysResult = SysResult.success(flag);
			jsonpObject = new JSONPObject(callback, sysResult);
			//int a = 1/0;
		} catch (Exception e) {
			e.printStackTrace();
			SysResult sysResult = SysResult.fail(); //失败
			jsonpObject = new JSONPObject(callback, sysResult);
		}
		return jsonpObject;*/
	}
	
	/**
	 * 完成用户登录的注册信息
	 * url:http://sso.jt.com/user/query/151027b1-7a29-4123-8889-0fad4e2a5291?callback=jsonp1582854291675&_=1582854291741
	 * 参数:/{ticket}, callback信息
	 * 返回值结果:SysResult对象
	 * 业务说明:
	 * 		1:根据ticket可以动态获取redis中用户的信息
	 * 		2:如果ticke有数据,则获取的是userJSON数据
	 */
	@RequestMapping("/query/{ticket}")
	public JSONPObject findUserByTicket(@PathVariable String ticket,String callback,HttpServletResponse response) {
		
		String userJSON = jedisCluster.get(ticket);
		if (StringUtils.isEmpty(userJSON)) {
			//缓存中没有数据,证明ticket有问题,cookie有误,应该删除cookie
			
//			//如何删除cookie,设置删除时间为0,并且要做覆盖操作
//			Cookie cookie = new Cookie("JT_TICKET", "");
//			cookie.setDomain("jt.com");
//			cookie.setPath("/");
//			cookie.setMaxAge(0);
//			response.addCookie(cookie);
			CookieUtil.deleteCookie(response,"JT_TICKET","jt.com","/");
			return new JSONPObject(callback, SysResult.fail());
		}
		SysResult sysResult = SysResult.success(userJSON);
		return new JSONPObject(callback, sysResult);
	}
}







package com.jt.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.jt.pojo.User;
import com.jt.util.CookieUtil;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.ThreadLocalUtil;

import redis.clients.jedis.JedisCluster;

@Component
public class UserInterceptor implements HandlerInterceptor{
	/**
	 * springMVC单独封装的拦截器API,简化调用过程,否则得配置web.xml
	 * 拦截器说明:
	 * 	一般使用拦截器时主要的目的是为了控制页面请求的跳转
	 * 	1:preHandler  处理器执行之前
	 * 	2;posthandler 处理器执行完成之后执行
	 * 	3:afterCompletion 全部业务逻辑执行完成之后并且视图渲染之后拦截
	 */
	
	/**
	 * 控制用户登录
	 * boolean :
	 * 		true: 放行请求  一般与重定向关联(拦截以后的操作)
	 * 		false:拦截请求
	 * 如果用户登录:放行,未登录:拦截   
	 * 如何判断用户是否登录:
	 * 		1:检查用户是否有cookie信息
	 * 		2:检查redis服务器中是否有ticket信息   上述操作判断正常,则表示用户以登录,放行
	 */
	@Autowired
	private JedisCluster jedisCluster;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		//1:判断用户的cookie信息
		String ticket = CookieUtil.getCookieValue(request, "JT_TICKET");
		if (!StringUtils.isEmpty(ticket)) {
			//2:ticket不为null,有值
			String userJSON = jedisCluster.get(ticket);
			if (!StringUtils.isEmpty(userJSON)) {
				//redis数据一切正常  利用request对象传递用户信息
				User user = ObjectMapperUtil.toObj(userJSON, User.class);
				request.setAttribute("JT_USER", user);
				ThreadLocalUtil.setUser(user);//通过线程(利用ThreadLocal)存入数据
				return true; //请求放行
			}else {
				//用户有ticket,但是redis没有该数据,说明用户的cookie有问题
				CookieUtil.deleteCookie(response, "JT_TICKET", "jt.com", "/");
			}
		}
		//重定向到用户的登录界面
		response.sendRedirect("/user/login.html");
		return false;
	}
	
	
	/**
	 * 删除threadLocal中的数据,防止内存泄漏
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		ThreadLocalUtil.remove();
	}
}

package com.cy.pj.common.web;
import java.time.Instant;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.cy.pj.common.exception.ServiceException;
/**
 * Spring MVC中的拦截器定义：基于此对象实现对登录操作的时间访问限制
 * 地层调用过程：
 * DispatcherServlet--->HandlerInterceptor-->XxxController
 */
public class TimeAccessInterceptor implements HandlerInterceptor{
	/**
	 * 此方法会在后端控制方法执行之前执行。
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("===preHandler===");
		//1.定义允许访问的时间范围
		Calendar c=Calendar.getInstance();//日历对象
		c.set(Calendar.HOUR_OF_DAY, 9);//设置小时
		c.set(Calendar.MINUTE, 0);//分钟
		c.set(Calendar.SECOND, 0);//秒
		long start=c.getTimeInMillis();//起始访问时间
		c.set(Calendar.HOUR_OF_DAY, 20);
		long end=c.getTimeInMillis();
		//2.获取当前时间进行业务判定
		long currentTime=System.currentTimeMillis();
		if(currentTime<start||currentTime>end)
			throw new ServiceException("请在9~20时间段访问");
		return true;//false表示拒绝对handler(Controller)的执行，true表示放行
	}
}












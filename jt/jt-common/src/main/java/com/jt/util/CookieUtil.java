package com.jt.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
	
	/**
	 * 1:删除cookie
	 * 2:参数:
	 * 		1.传递response对象  2.cookiName  3.domain 4.path
	 * @return 
	 */
	public static void deleteCookie(HttpServletResponse response,String cookieName,
			String domain,String path) {
		Cookie cookie = new Cookie(cookieName, "");
		cookie.setDomain(domain);
		cookie.setPath(path);
		cookie.setMaxAge(0);//立即删除
		//cookie.setMaxAge(-1);  //关闭会话是删除
		response.addCookie(cookie);
	}
	
	/**
	 * 根据cookie的名字   获取cookie对象
	 */
	public static Cookie getCookie(HttpServletRequest request,String cookieName) {
		//1.动态获取指定的cookie信息.
		Cookie[] cookies = request.getCookies();
		if(cookies !=null && cookies.length>0) {
			for (Cookie cookie : cookies) {
				if(cookieName.equals(cookie.getName())) {
					
					return cookie;
				}
			}
		}
		return null;
	}

	/**
	 * 根据cookie名称 获取value值
	 */
	public static String getCookieValue(HttpServletRequest request,String cookieName) {
		Cookie[] cookies = request.getCookies();
		//校验数据是否有效
		String ticket = null;
		if(cookies !=null && cookies.length>0) {
			for (Cookie cookie : cookies) {
				if(cookieName.equals(cookie.getName())) {
					ticket = cookie.getValue();
					break;
				}
			}
		}
		return ticket;
	}
	
}






package com.jt.util;

import com.jt.pojo.User;

public class ThreadLocalUtil {

	private static ThreadLocal<User> threadLocal = new ThreadLocal<>();
	/**
	 * 存
	 * @param user
	 */
	public static void setUser(User user) {
		threadLocal.set(user);
	}
	
	/**
	 * 取出
	 * @return
	 */
	public static User getUser() {
		
		return	threadLocal.get();
	}
	
	/**
	 * 删除,关闭
	 */
	public static void remove() {
		threadLocal.remove();//清空数据
	}
}

package com.jt.service;

import com.jt.pojo.User;

/**
 *Dubbo的业务处理接口,名称不能重复
 */
public interface DubboUserService {

	void saveUser(User user);

	String findUserUP(User user);
	
	
}

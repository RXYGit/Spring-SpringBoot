package com.jt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;

	/**
	 * 参数说明:
	 * 	1.param: 校验数据
	 * 	2.type:  校验类型 1,2,3
	 *sql说明:select * from tb_user where phone=#{param}
	 * 结果: 返回true/false
	 */
	@Override
	public boolean checkUser(String param, Integer type) {
		//String column = type==1?"username":(type==2?"phone":"email");
		Map<Integer,String> map = new HashMap<Integer, String>();
		map.put(1, "username");
		map.put(2, "phone");
		map.put(3, "email");
		String column = map.get(type);
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(column, param);
		User user = userMapper.selectOne(queryWrapper);
		return user==null?false:true;
	}
	
}

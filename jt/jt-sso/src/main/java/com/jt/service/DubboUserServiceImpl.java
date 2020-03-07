package com.jt.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

/**
 *RPC的方式进行动态调用
 */
@Service	//dubbo家的jar包	
public class DubboUserServiceImpl implements DubboUserService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JedisCluster jedisCluster;

	/**
	 * 用户入库必须传递email,否则会报错,暂时使用电话号码代替
	 * 密码加密:MD5加密,MD5Hash方式+盐值
	 */
	@Override
	public void saveUser(User user) {
		
		String md5PassWord = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		
		
		user.setPassword(md5PassWord)
			.setEmail(user.getPhone())
			.setCreated(new Date())
			.setUpdated(user.getCreated());
			
		userMapper.insert(user);
	}

	/**
	 * 完成用户数据的校验
	 * 1:校验用户名和密码是否正确    密码  明文----->密文
	 * 2:
	 */
	@Override
	public String findUserUP(User user) {
		
		//注册和登录的密码加密必须一致
		String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Password);
		//根据对象中不为null的属性充当where条件
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>(user);
		User userDB = userMapper.selectOne(queryWrapper);
		
		if (userDB == null) {
			//根据用户名或密码错误
			return null;
		}
		//表示用户信息是有效的,需要SSO操作
		String ticket = UUID.randomUUID().toString();
		//user: 用户名,密码,身份证号,住址,户籍,电话,金额等
		userDB.setPassword("123456伪造的密码");//伪造数据
		String userJSON = ObjectMapperUtil.toJSON(userDB);
		jedisCluster.setex(ticket, 7*24*3600,userJSON);
		//通过页面F12查找Application中的Cookie是否存在       来检验
		return ticket;
	}
	
}

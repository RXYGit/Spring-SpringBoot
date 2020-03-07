package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.pojo.Item;

@Controller
public class IndexController {
	
	/**
	 * 请求:
	 * 	/page/item-list
		/page/item-param-list
		/page/item-add
	   思路:
	  	如何能够动态获取url中的参数地址
	  restFul风格1:
	  	1.参数使用"/"进行分割
	  	2.参数使用{}包裹
	  	3.需要动态获取参数,需要配合注解使用@PathVariable.
	  	节省了:requestMapping方法的编辑.
	  	
	  参数说明：http协议传递  
	  http://localhost:8091/page/item-add/18/女
	  get请求:
	  	http://localhost:8091/page?moduleName=item-add&age=18&sex=女
	 */
	@RequestMapping("/page/{moduleName}")
	public String module(@PathVariable String moduleName){
		
		return moduleName;
	}
	
	/**
	 *  restFul风格2:
	 *     问题:能否简化用户的url请求个数?
	 *     案例:
	 *     1.url:/user/addUser			post类型
	 *     2.url:/user/updateUser		put类型
	 *     3.url:/user/findUser  		get类型
	 *     4.url:/user/deleteUser		delete类型
	 *  解决方案:
	 *  	利用不同的请求类型,实现用户的CRUD操作.
	 *  	url:/user  type="get" 查询
	 *  	url:/user  type="post" 新增
	 */
	//@RequestMapping(value="/user",method=RequestMethod.GET)
	/*@GetMapping("/user")
	@ResponseBody
	public XXX getItemById() {
		
		System.out.println("数据查询");
		return null;
	}
	
	@RequestMapping(value="/user",method=RequestMethod.POST)  //新增
	//@PostMapping("/user")
	@ResponseBody
	public XXX insertUser(XXX XXX) {
		
		System.out.println("数据新增");
		return null;
	}*/
	
}

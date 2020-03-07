package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller	//跳转页面时添加
public class IndexController {
	
	
	@RequestMapping("/index")
	public String index() {
		
		return "index";	//跳转系统首页
	}
}

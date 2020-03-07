package com.jt.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MsgController {
	
	//如果动态获取当前端口号????  从配置文件中
	@Value("${server.port}")
	private Integer port;
	
	@RequestMapping("/getMsg")
	public String getMsg() {
		
		return "你好:当前"+port+"号技师为您服务!!!";
	}
}

package com.jt.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResultController {
	
	//需求:实现item入库操作 
	//url:localhost:8091/save/刘昱江
	@RequestMapping("/save/{username}")
	public String insertItem(@PathVariable(name="username") String name) {
		
		return name;
	}
	
	
	
	
}

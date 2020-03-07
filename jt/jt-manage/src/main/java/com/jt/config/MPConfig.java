package com.jt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
@Configuration  //配置类就是xml配置文件
public class MPConfig {
	
	//MP分页操作需要通过MP分页拦截器才能生效
	
	@Bean	//实例化对象交给spring容器管理
    public PaginationInterceptor paginationInterceptor() {
		
		return new PaginationInterceptor();
    }  
}

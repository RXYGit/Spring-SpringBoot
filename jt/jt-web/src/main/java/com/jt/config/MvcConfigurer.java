package com.jt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jt.interceptor.UserInterceptor;
/**
 * 伪静态核心 拦截.html请求 利用配置类代替 web.xml配置
 * @author LYJ
 *
 */
@Configuration
public class MvcConfigurer implements WebMvcConfigurer{
	
	@Autowired
	private UserInterceptor userInterceptor;
	//开启匹配后缀型配置   /index,/index.html /index.abc ,拦截之后,执行视图解析器
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseSuffixPatternMatch(true);
	}
	
	/**
	 * 添加拦截器的配置
	 *   /cart/**  : /cart/* 代表一级目录
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(userInterceptor)
				.addPathPatterns("/cart/**","/order/**");//拦截以"/cart"为开头的所有路径
	}
	
}

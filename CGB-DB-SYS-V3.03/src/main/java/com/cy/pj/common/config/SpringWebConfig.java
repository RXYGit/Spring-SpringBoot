package com.cy.pj.common.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.cy.pj.common.web.TimeAccessInterceptor;
/**
 * 此类中只做和web相关的配置
 * @author Administrator
 *
 */
@Configuration
public class SpringWebConfig implements WebMvcConfigurer{
	/**
	 * 注册拦截器并设置其拦截器路径
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new TimeAccessInterceptor())
		.addPathPatterns("/user/doLogin");//设置要拦截的路径
	}
}

package com.cy.pj.common.config;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringShiroConfig {
	/**
	 * shiro中的会话管理对象，基于此对象实现shiro中Session对象的管理
	 */
	@Bean   
	public SessionManager sessionManager() {
		 DefaultWebSessionManager sManager= new DefaultWebSessionManager();
		 sManager.setGlobalSessionTimeout(60*60*1000);
	     return sManager;
	}
	/**
	 * 配置记住我管理器对象，shiro框架中的记住我，底层要基于Cookie对象实现。
	 * @return
	 */
	@Bean
	public RememberMeManager rememberMeManager() {
		CookieRememberMeManager rememberMeManager=new CookieRememberMeManager();
		SimpleCookie cookie=new SimpleCookie("rememberMe");
		cookie.setMaxAge(7*24*60*60);
		rememberMeManager.setCookie(cookie);
		return rememberMeManager;
	}
	/**
	 * 配置shiro中的缓存管理器，通过此缓存管理器对象中的缓存对象来对授权信息进行缓存
	 * 说明:
	 * 1)方法名不能写cacheManager(可能会与spring中的CacheManager对象有重复)
	 * 2)方法的返回值对象要注入给SecurityManager对象。
	 * 
	 * 当我们访问一个授权方法时，shiro框架会调用SecurityManager对象对用户进行
	 * 权限检测，此时需要获取用户权限信息，如何获得这个权限信息呢？假如没有配置缓存，每次
	 * 都会查询数据库。配置缓存以后SecurityManager对象会优先从shiro缓存查询数据，
	 * 缓存中没有才会查询数据库。
	 * 
	 * 注意：当修改了用户权限，有了缓存以后，此权限应该在下一次登录以后有效。
	 */
	@Bean
	public CacheManager shiroCacheManager(){//CacheManager不是Cache，而是Cache管理器对象
		 return new MemoryConstrainedCacheManager();
	}
    /**
     * @Bean 注解一般用于描述由@Configuration注解修饰的类中的方法。目的是将方法的调用
     * 交给Spring框架，并且由spring框架管理这个方法返回值对象(例如将此对象存储到spring
     * 容器，并给定其作用域。存储时默认的key为方法名)
     * @return SecurityManager (此对象时shiro框架的核心，由此对象完成认证，授权等功能)
     * 设计说明：在设计方法时，方法的返回值类型以及方法参数能用抽象则用抽象（多态的体现）。
     */
	@Bean
	public SecurityManager securityManager(Realm realm,
			CacheManager cacheManager,
			RememberMeManager rememberMeManager,
			SessionManager sessionManager) {
		DefaultWebSecurityManager sManager=new DefaultWebSecurityManager();
		sManager.setRealm(realm);
		sManager.setCacheManager(cacheManager);
		sManager.setRememberMeManager(rememberMeManager);
		sManager.setSessionManager(sessionManager);
		return sManager;
	}
	/**
	 * 初始化过滤器工厂bean对象(地层要通过此对象创建过滤器工厂，然后通过工厂创建过滤器)
	 * 思考：
	 * 1)为什么要创建过滤器呢？(通过过滤器对象请求数据进行校验，过滤等操作，例如检查用户是否已认证)
	 * 2)过滤器对请求或响应数据进行过滤时要指定规则吗？(哪些请求要过滤，哪些不要过滤)
	 * 3)对于要过滤的请求，我们通过谁对用户数据进行校验呢？(例如可以在SecurityManager中进行认证检测)
	 * @return
	 */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactory(
    		SecurityManager securityManager) {//如何查找实参(方法上默认有@Autowired注解)
    	//1.构建工厂bean对象(FactoryBean规范由spring定义，规范的实现在当前模块由shiro框架实现，例如ShiroFilterFactoryBean)
    	ShiroFilterFactoryBean fBean=new ShiroFilterFactoryBean();
    	//2.为fBean对象注入SecurityManager对象
    	fBean.setSecurityManager(securityManager);
    	//3.设置登录url(没有经过认证的请求，需要跳转到这个路径对应的页面)
    	fBean.setLoginUrl("/doLoginUI");
    	//4.设置请求过滤规则?(例如哪些请求要进行认证检测，哪些请求不需要)
    	Map<String,String> filterChainDefinitionMap=new LinkedHashMap<>();
    	 //静态资源允许匿名访问:"anon"(Shiro框架指定这些常亮值)//官网http://shiro.apache.org/web.html
    	filterChainDefinitionMap.put("/bower_components/**","anon");
    	filterChainDefinitionMap.put("/build/**","anon");
    	filterChainDefinitionMap.put("/dist/**","anon");
    	filterChainDefinitionMap.put("/plugins/**","anon");
    	filterChainDefinitionMap.put("/user/doLogin","anon");
    	filterChainDefinitionMap.put("/doLogout","logout");//触发此url时系统地层会清session，然后跳转到LoginUrl
		 //除了匿名访问的资源,其它都要认证("authc")后访问，这里要记住，需认证访问的资源，要写在下面
    	//filterChainDefinitionMap.put("/**","authc");
    	filterChainDefinitionMap.put("/**","user");//记住我功能实现时，认证规则需要修改。
    	fBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return fBean;
	}
    //==============shiro授权配置=====(参考官网:shiro.apache.org)
    @Bean
    public AuthorizationAttributeSourceAdvisor 
           authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
    	AuthorizationAttributeSourceAdvisor advisor=new AuthorizationAttributeSourceAdvisor();
    	advisor.setSecurityManager(securityManager);
    	return advisor;
    }
	 
}

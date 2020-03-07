package com.cy.pj.common.aspect;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
/**
 * 缓存切面
 * @author Administrator
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@Aspect
@Component
public class SysCacheAspect {
	  //hashMap 线程不安全
	  //HashTable 线程安全,但性能很差(商场购物,只允许一个人进去)
	  //ConcurrentHashMap 线程安全,JDK7进行了分段加锁(类似于商场有5层,每层一个用户).
	  //ConcurrentHashMap 线程安全,JDK8进行单元格加锁+CAS算法(类似锁了商场的试衣间)
	  private Map<String,Object> cacheMap=new ConcurrentHashMap<>();
	  //@Pointcut("@annotation(com.cy.pj.common.annotation.RequiredCache)")
	  //public void doCachePointCut() {}
	
	  //@Around("doCachePointCut()")
	  @Around("@annotation(com.cy.pj.common.annotation.RequiredCache)")
	  public Object arount(ProceedingJoinPoint jp)throws Throwable{
		  System.out.println("Get data from cache");
		  Object obj=cacheMap.get("dept.findObjects");//key需要按指定规则进行设计
		  if(obj!=null) return obj;
		  Object result=jp.proceed();
		  System.out.println("Put Data to cache");
		  cacheMap.put("dept.findObjects",result);
		  return result;
	  }
	  /**当切入点对应的方法执行OK以后会执行如下方法,进行cache清除操作*/
	  @AfterReturning("@annotation(com.cy.pj.common.annotation.ClearCache)")
	  public void clear() {
		  System.out.println("===clear cache===");
		  cacheMap.clear();
	  }
}













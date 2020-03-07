package com.cy.pj.common.aspect;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.service.SysLogService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Aspect 注解描述的类型为一个AOP应用中的切面类型,此类的构成:
 * 1)切入点 (在哪些方法执行时,我们要织入扩展功能):例如所有地铁站的入口
 * 2)通知 (要织入的扩展功能):例如安检.
 */
//@Order(Integer.MIN_VALUE)
@Slf4j
@Aspect
@Component
public class SysLogAspect {
      /**
       * @Pointcut 注解用于描述方法,描述的方法不需要写具体内容.底层运行时
       * 会获取方法上的注解,并拿到注解中定义的表达式.
       * bean()为一种切入点表达式的定义方式,()中的内容为一个bean对象或者
       * 为一堆bean对象(例如*ServiceImpl)
       * 系统底层会认为bean表达式描述的对象中所有方法的集合为切入点.
       */
	  //@Pointcut("bean(sysUserServiceImpl)")
	  @Pointcut("@annotation(com.cy.pj.common.annotation.RequiredLog)")
	  public void logPointCut() {}
	  /**
	   * 扩展功能的实现,在切面类型中要使用特殊标记进行描述,这个标记为注解
	   * @param jp 连接点(JoinPoint):对应切入点描述的方法中的一个目标方法
	   * @return 为目标方法的执行结果
	   * @throws Throwable
	   */
	  @Around("logPointCut()")
	  public Object around(ProceedingJoinPoint jp)//ProceedingJoinPoint继承JoinPoint
	  throws Throwable{
		  long start=System.currentTimeMillis();
		  log.info("start {}",start);
		  try {
		  Object targetMethodResult=jp.proceed();//调用下一个切面或目标方法
		  long end=System.currentTimeMillis();
		  log.info("end {}",end);
		  //记录用户正常行为日志
		  saveLog(jp,end-start);
		  return targetMethodResult;//
		  }catch(Throwable e) {
		  log.error("error {}", e.getMessage()); 
		  //写异常日志
		  throw e;
		  }
	  }
	  @Autowired
	  private SysLogService sysLogService;
	  /**将用户行为信息写入到数据库*/
	  private void saveLog(ProceedingJoinPoint jp,long time)
	  throws Exception{
		  //1.获取行为日志(借助连接点对象)
		  //1.1 获取目标类
		  Class<?> targetClass=jp.getTarget().getClass();
		  //1.2获取目标方法名
		  MethodSignature ms=(MethodSignature)jp.getSignature();
		  String targetClsMethod=
		  targetClass.getName()+"."+ms.getName();
		  //1.3获取目标方法实际参数
		  String params=Arrays.toString(jp.getArgs());
		  
		  //1.4获取操作名称(由此注解RequiredLog指定)
		  //1.4.1获取目标方法对象(Method)-->反射技术(应用的起点为Class对象)
		  //Method targetMethod=
		  //targetClass.getMethod(ms.getName(),ms.getParameterTypes());
		  Method targetMethod=ms.getMethod();//局限性
		  System.out.println("targetMethod="+targetMethod);
		  //1.4.2获取方法对象上的注解
		  RequiredLog requiredLog=
		  targetMethod.getAnnotation(RequiredLog.class);
		  //1.4.3获取注解中operation属性
		  String operation="operation";
		  if(requiredLog!=null) {
			  operation=requiredLog.operation();
		  }
		  //2.封装行为日志
		  SysLog log=new SysLog();
		  log.setIp("192.168.175.1");
		  log.setUsername("cgb1910");
		  log.setOperation(operation);
		  log.setMethod(targetClsMethod);//目标方法:类全名+方法名
		  log.setParams(params);//目标方法实际参数:
		  log.setTime(time);
		  log.setCreatedTime(new Date());
		  //3.写日志
		  sysLogService.saveObject(log);
	  }
}
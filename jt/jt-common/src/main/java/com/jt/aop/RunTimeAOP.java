package com.jt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect	//切面注解
public class RunTimeAOP {
	
	//所有的业务方式  service
	@Around(value="execution(* com.jt.service..*.*(..))")
	public Object around(ProceedingJoinPoint joinPoint) {
		Long startTime = System.currentTimeMillis();
		Object obj = null;
		try {
			obj = joinPoint.proceed();
			Long endTime = System.currentTimeMillis();
			Long runTime = endTime - startTime;
			String targetName = joinPoint.getSignature().getDeclaringTypeName();
			String methodName = joinPoint.getSignature().getName();
			Object[] args = joinPoint.getArgs();
			String kind = joinPoint.getKind();
			Object target = joinPoint.getTarget();	
			System.out.println("类名:"+targetName);
			System.out.println("方法名:"+methodName);
			System.out.println("参数信息:"+args);
			System.out.println("kind:"+kind);
			System.out.println("目标对象:"+target);
			System.out.println("执行时间:"+runTime);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException();
		}	//执行目标方法
		return obj;
	}
	
}

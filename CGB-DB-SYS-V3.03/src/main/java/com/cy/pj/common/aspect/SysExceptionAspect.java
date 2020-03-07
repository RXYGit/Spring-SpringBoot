package com.cy.pj.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
/**
 * 基于AOP实现业务层异常监控
 * @author Administrator
 */
@Slf4j
@Aspect
@Component
public class SysExceptionAspect {
	    /**
	     * 异常通知:
	     * @param jp 连接点对象(指向了正在执行的目标方法)
	     * @param e 此变量用于接收执目标方法出现的异常.
	     */
	    @AfterThrowing(value="bean(sysLogServiceImpl)",throwing = "e")
	    public void doMonitorException(JoinPoint jp,Exception e) {
	    	//获取方法签名(封装了你要调用的方法信息:方法名,参数,返回值类型)
	    	MethodSignature ms=(MethodSignature)jp.getSignature();
			log.error("{}'exception msg is {}",ms.getName(),e.getMessage());
	    }//作业:将异常日志信息,写入到日志文件?(扩展:播放音频,发短信,发email-JavaMail)
	    //监控:
	    //1.CPU,磁盘,内存,网络
	    //2.Tomcat,JVM
	    //3.应用(service,...)
}






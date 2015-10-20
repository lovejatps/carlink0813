package com.uniits.carlink.logs;

import org.aspectj.lang.ProceedingJoinPoint;

public class XxLogIntercepter {
	
	public Object aroundMethod(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("method around start");
		System.out.println(pjp.getSignature().getName());
		System.out.println(pjp.getThis().getClass());
		Object obj = pjp.proceed();
		System.out.println("method around end");
		return obj ;
	}
}

package com.aspect.handler;

import org.aspectj.lang.ProceedingJoinPoint;

public interface AspectHandler {

  Object execute(ProceedingJoinPoint jp, String key) throws Throwable;
  
}

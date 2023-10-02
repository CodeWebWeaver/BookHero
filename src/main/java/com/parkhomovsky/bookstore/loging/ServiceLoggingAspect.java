package com.parkhomovsky.bookstore.loging;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLoggingAspect {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceLoggingAspect.class);

    @Pointcut("execution(public * com.parkhomovsky.bookstore.service..*.*(..))")
    public void loggableMethods() {
        // specifying loggable methods
    }

    @Around("loggableMethods()")
    public Object logMethodProcessing(ProceedingJoinPoint pjp) throws Throwable {
        String args = Arrays.stream(pjp.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(","));
        String methodSignature = pjp.getSignature().toShortString();
        LOG.debug("Executing {}, args=[{}]", methodSignature, args);

        Object executionResult = pjp.proceed();

        LOG.debug("Successfully executed {}, result: {}", methodSignature, executionResult);
        return executionResult;
    }

    @AfterThrowing(pointcut = "loggableMethods()", throwing = "e")
    public void logAfterThrowingException(RuntimeException e) {
        LOG.error("Execution failed: {}", e.getMessage(), e);
    }
}

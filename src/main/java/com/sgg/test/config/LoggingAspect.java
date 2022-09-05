package com.sgg.test.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class LoggingAspect {

    private final ConcurrentHashMap<String, Logger> loggerMap = new ConcurrentHashMap<>();

    private Logger getLogger(String fqName) {
        return loggerMap.computeIfAbsent(fqName, LoggerFactory::getLogger);
    }

    @Pointcut("within(com.sgg.test..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void apiPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)")
    public void serviceAndDaoPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    @Around("applicationPackagePointcut() && apiPointcut()")
    public Object logAroundApi(ProceedingJoinPoint joinPoint) throws Throwable {
        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
        Logger logger = getLogger(declaringTypeName);
        String methodName = joinPoint.getSignature().getName();
        String methodArgs = Arrays.toString(joinPoint.getArgs());
        if (logger.isInfoEnabled()) {
            logger.info("Enter: {}() with argument[s] = {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            long start = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            logger.info("Exit: {}() with result = {}. Execution time : {} ms", methodName, result, elapsedTime);
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("Illegal arguments: in '{}({})'", methodName, methodArgs);
            throw e;
        }
    }

    @Around("applicationPackagePointcut() && serviceAndDaoPointcut()")
    public Object logAroundOther(ProceedingJoinPoint joinPoint) throws Throwable {
        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
        Logger logger = getLogger(declaringTypeName);
        String methodName = joinPoint.getSignature().getName();
        String methodArgs = Arrays.toString(joinPoint.getArgs());
        if (logger.isDebugEnabled()) {
            logger.debug("Enter: '{}({})'", methodName, methodArgs);
        }
        try {
            long start = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            logger.debug("Exit: '{}({})' with result = {}. Execution time : {} ms", methodName, methodArgs, result, elapsedTime);
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument: in '{}({})'", methodName, methodArgs);
            throw e;
        }
    }

    @AfterThrowing(pointcut = "applicationPackagePointcut() && (apiPointcut() || serviceAndDaoPointcut())", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
        Logger logger = getLogger(declaringTypeName);
        String method = joinPoint.getSignature().getName();
        String methodArgs = Arrays.toString(joinPoint.getArgs());
        String errorClassName = e.getClass().getSimpleName();
        if (logger.isTraceEnabled()) {
            logger.error("{} in '{}({})': Cause: {}", errorClassName, method, methodArgs, e.getLocalizedMessage(), e);
        } else if (logger.isDebugEnabled()) {
            logger.error("{} in '{}({})': Cause: {}", errorClassName, method, methodArgs, e.getLocalizedMessage());
        }
    }

}

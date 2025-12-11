package com.farkas.familymealmate.aop;

import com.farkas.familymealmate.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(public * * (..))")
    public void publicOperations() {
    }

    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void serviceClasses() {
    }

    @Before("publicOperations() && serviceClasses()")
    public void logServiceMethodCall(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String request = args.length == 0 ? "null" : Arrays.stream(args)
                .map(Objects::toString)
                .collect(Collectors.joining(", "));

        log.info("call service, name: {}, request: {} ", joinPoint.getSignature().getName(), request);
    }

    @AfterReturning(pointcut = "publicOperations() && serviceClasses()", returning = "response")
    public void logServiceMethodResponse(JoinPoint joinPoint, Object response) {
        log.info("service, name: {}, response: {} ", joinPoint.getSignature().getName(), response == null ? "null" : response.toString());
    }

    @AfterThrowing(pointcut = "publicOperations() && serviceClasses()", throwing = "error")
    public void logServiceError(JoinPoint joinPoint, Throwable error) {
        log.debug("Exception in service {}",
                joinPoint.getSignature().getName());
        if (error instanceof ServiceException se) {
            log.error("ServiceException occurred: code={}, message={}",
                    se.getErrorCode(), se.getMessage(), se);
        } else {
            log.error("Unexpected exception in service", error);
        }
    }

}

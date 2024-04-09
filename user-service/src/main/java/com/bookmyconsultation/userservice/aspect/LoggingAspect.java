package com.bookmyconsultation.userservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Component
@Aspect
public class LoggingAspect {

    @Around("execution(* com.bookmyconsultation..*(..))")
    public Object applyLogging (ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        Object[] inputArgs = joinPoint.getArgs();
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("Entering CLASS_NAME:{} METHOD_NAME:{} with input parameters {}",className, methodName, inputArgs);
        Object result = joinPoint.proceed();
        stopWatch.stop();
        log.info("Exiting CLASS_NAME:{} METHOD_NAME:{} with return value [{}] and execution time of {}",className,methodName,
                result,getTimeInMinutesAndSeconds(stopWatch.getTotalTimeMillis()));
        return  result;
    }

    private String getTimeInMinutesAndSeconds(long timeInMilliseconds) {
        long minutes = (timeInMilliseconds / 1000) / 60;
        long seconds = (timeInMilliseconds / 1000) % 60;
        return minutes + " minutes and " + seconds + " seconds";
    }
}

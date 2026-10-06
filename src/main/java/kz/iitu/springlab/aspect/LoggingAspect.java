package kz.iitu.springlab.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Order(2)
public class LoggingAspect {

    @Before("kz.iitu.springlab.aspect.Pointcuts.serviceOperation()")
    public void logBefore(JoinPoint jp) {
        System.out.println(
                "[LOG] -> " +
                        jp.getSignature().getName() +
                        " args=" +
                        Arrays.toString(jp.getArgs())
        );
    }

    @AfterReturning(
            pointcut = "kz.iitu.springlab.aspect.Pointcuts.serviceOperation()",
            returning = "result"
    )
    public void logAfterReturning(JoinPoint jp, Object result) {
        System.out.println(
                "[LOG] <- " +
                        jp.getSignature().getName() +
                        " result=" +
                        result
        );
    }

    @AfterThrowing(
            pointcut = "kz.iitu.springlab.aspect.Pointcuts.serviceOperation()",
            throwing = "ex"
    )
    public void logAfterThrowing(JoinPoint jp, Throwable ex) {
        System.out.println(
                "[LOG] !! " +
                        jp.getSignature().getName() +
                        " exception=" +
                        ex.getClass().getSimpleName() +
                        ": " +
                        ex.getMessage()
        );
    }
}


package com.xhtt.common.aspect;

import com.xhtt.common.utils.DateUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
public class NaAspect {
    @Pointcut("@annotation(com.xhtt.common.annotation.NaLocalDateTime)")
    public void naAspect() {
    }

    @Around("naAspect()")
    public void before(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        point.proceed();
        Object[] as = point.getArgs();
        //业务上知道使时间 所以强转了
        LocalDateTime t = (LocalDateTime) as[0];
        if (null != t && DateUtils.NA_DATE.equals(t)) {
            as[0] = null;
        }

    }
}

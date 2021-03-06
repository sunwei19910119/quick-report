package com.xhtt.common.aspect;

import com.google.gson.Gson;
import com.xhtt.common.annotation.SysLog;
import com.xhtt.common.utils.HttpContextUtils;
import com.xhtt.common.utils.IPUtils;
import com.xhtt.modules.sys.entity.SysLogEntity;
import com.xhtt.modules.sys.entity.SysUserEntity;
import com.xhtt.modules.sys.service.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;


/**
 * 系统日志，切面处理类
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017年3月8日 上午11:07:35
 */
@Aspect
@Component
public class SysLogAspect {
    @Autowired
    private SysLogService sysLogService;
    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    @Pointcut("@annotation(com.xhtt.common.annotation.SysLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        //获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        taskExecutor.execute(() -> saveSysLog(point, time, request));

        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time, HttpServletRequest request) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLogEntity sysLog = new SysLogEntity();
        SysLog syslog = method.getAnnotation(SysLog.class);
        if (syslog != null) {
            //注解上的描述
            sysLog.setOperation(syslog.value());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");

        //请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            String params = new Gson().toJson(args[0]);
            sysLog.setParams(params);
        } catch (Exception e) {

        }

        //获取request
//        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //设置IP地址
        sysLog.setIp(IPUtils.getIpAddr(request));

        //用户名
//		String username =((SysUserEntity) SecurityUtils.getSubject().getPrincipal()).getUsername();
        SysUserEntity user = null;
        for (Object o : args) {
            try {
                user = (SysUserEntity) o;
                break;
            } catch (Exception e) {
                continue;
            }
        }
        if (null != user) {
            sysLog.setUsername(user.getName());
        }

        sysLog.setTime(time);
        sysLog.setCreateDate(new Date());
        //保存系统日志
		sysLogService.save(sysLog);
    }
}

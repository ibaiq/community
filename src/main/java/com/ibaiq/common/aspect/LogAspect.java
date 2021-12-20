package com.ibaiq.common.aspect;

import com.alibaba.fastjson.JSON;
import com.ibaiq.common.annotation.Log;
import com.ibaiq.common.enums.HttpMethod;
import com.ibaiq.entity.Message;
import com.ibaiq.entity.OperLog;
import com.ibaiq.exception.BaseException;
import com.ibaiq.mapper.OperLogMapper;
import com.ibaiq.service.async.AsyncService;
import com.ibaiq.utils.ServletUtils;
import com.ibaiq.utils.StringUtils;
import com.ibaiq.utils.ip.AddressUtils;
import com.ibaiq.utils.ip.IpUtils;
import com.ibaiq.utils.spring.SecurityUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author 十三
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private AsyncService async;
    @Autowired
    private OperLogMapper mapper;


    private static final InheritableThreadLocal<OperLog> operLog = new InheritableThreadLocal<>();
    private static final InheritableThreadLocal<String> online = new InheritableThreadLocal<>();
    private static final InheritableThreadLocal<Long> time = new InheritableThreadLocal<>();


    @Pointcut("@annotation(com.ibaiq.common.annotation.Log)")
    public void initialize() {
    }

    @SneakyThrows
    @Around("initialize()")
    public Object surround(ProceedingJoinPoint point) {
        CountDownLatch latch = new CountDownLatch(2);
        // 执行前
        before(point, latch);
        // 执行
        latch.await();
        long start = System.currentTimeMillis();
        Object result = point.proceed();
        long end = System.currentTimeMillis();
        time.set(end - start);
        // 执行后异步操作
        async.log_asyncRecordLog(point, null, result);
        return result;
    }


    private void before(JoinPoint point, CountDownLatch latch) {
        Log log = getAnnotationLog(point);
        if (log == null) {
            return;
        }
        HttpServletRequest request = ServletUtils.getRequest();
        OperLog operLog = new OperLog();
        async.log_setIpAndAddress(operLog, latch, request);
        async.log_setUrlAndMethodAndParam(point, operLog, log, latch, request);
        online.set(SecurityUtils.getUser().getUsername());
        LogAspect.operLog.set(operLog);
    }

    @AfterThrowing(pointcut = "initialize()", throwing = "e")
    public void doAfterThrowing(JoinPoint point, Exception e) {
        async.log_asyncRecordLog(point, e, null);
    }

    public void setUrlAndMethodAndParam(JoinPoint point, OperLog operLog, Log log, CountDownLatch latch, HttpServletRequest request) {
        // 获取请求路径
        operLog.setOperUrl(request.getRequestURI());
        // 设置请求方式
        operLog.setRequestMethod(request.getMethod());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(point, operLog, request);
        }
        latch.countDown();
    }

    public void setIpAndAddress(OperLog operLog, CountDownLatch latch, HttpServletRequest request) {
        // 获取请求地址
        String ip = IpUtils.getIpAddress(request);
        operLog.setOperIp(ip);
        // 获取请求地点
        operLog.setOperLocation(AddressUtils.getCityInfo(ip));
        latch.countDown();
    }

    public void handleLog(final JoinPoint point, final Exception e, Object result) {
        try {
            Log log = getAnnotationLog(point);
            if (log == null) {
                return;
            }
            OperLog operLog = LogAspect.operLog.get();
            operLog.setStatus(true);
            // 设置时间
            operLog.setOperTime(new Date());
            // 设置执行耗时时间
            operLog.setConsume(time.get());
            // 获取操作用户
            operLog.setOperName(online.get());
            // 返回参数
            operLog.setOperResult(JSON.toJSONString(result));
            if (e != null) {
                operLog.setStatus(false);
                operLog.setErrorMsg(e.getMessage());
                Message message = new Message(((BaseException) e).getCode(), e.getMessage());
                operLog.setOperResult(JSON.toJSONString(message));
            }
            // 获取接口方法全名称
            String className = point.getTarget().getClass().getName();
            String methodName = point.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");
            // 设置action动作
            operLog.setBusinessType(log.businessType().ordinal());
            // 设置标题
            operLog.setModule(log.module());
            // 保存数据库
            mapper.insert(operLog);
        } finally {
            operLog.remove();
            online.remove();
            time.remove();
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operLog 操作日志
     */
    private void setRequestValue(JoinPoint point, OperLog operLog, HttpServletRequest request) {
        String requestMethod = operLog.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(point.getArgs());
            operLog.setOperParam(params);
        } else {
            Map<?, ?> paramsMap = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            operLog.setOperParam(JSON.toJSONString(paramsMap));
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint point) {
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }

    /**
     * 参数拼装
     */
    @SneakyThrows
    private String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (StringUtils.isNotNull(o) && !isFilterObject(o)) {
                    String json = JSON.toJSONString(o);
                    params.append(json).append(" ");
                }
            }
        }
        return params.toString().trim();
    }


    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }

        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                          || o instanceof BindingResult;
    }

}

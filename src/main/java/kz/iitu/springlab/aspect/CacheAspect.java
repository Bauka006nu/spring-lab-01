package kz.iitu.springlab.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
@Order(4)
public class CacheAspect {

    private static final Logger log = LoggerFactory.getLogger(CacheAspect.class);

    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    @Around("@annotation(kz.iitu.springlab.cache.SimpleCache)")
    public Object cached(ProceedingJoinPoint pjp) throws Throwable {
        String key = pjp.getSignature().toShortString() + Arrays.toString(pjp.getArgs());

        Object value = cache.get(key);
        if (value != null) {
            log.info("[CACHE] HIT {}", key);
            return value;               // proceed() не вызываем, метод не запускается
        }

        log.info("[CACHE] MISS {}", key);
        Object result = pjp.proceed();
        if (result != null) {
            cache.put(key, result);
        }
        return result;
    }
}
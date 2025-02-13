package ru.t1.demo.aspectloggerstarter.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.t1.demo.aspectloggerstarter.config.HttpLoggingProperties;

import java.util.Arrays;

@Aspect
public class HttpLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(HttpLoggingAspect.class);
    private final HttpLoggingProperties properties;

    public HttpLoggingAspect(HttpLoggingProperties properties) {
        this.properties = properties;
    }

    @Pointcut("@annotation(ru.t1.demo.aspectloggerstarter.HttpLogger) || within(@ru.t1.demo.aspectloggerstarter.HttpLogger *)")
    public void pointcut() {}

    //Для примера разные адвайсы используют разный логгер level
    @Before("pointcut()")
    public void logBefore(JoinPoint joinPoint) {
        if (("INFO").equals(properties.getLevel())){
            logger.info("=== HTTP REQUEST START ===");
            logger.info("Method: {}", joinPoint.getSignature().toShortString());
            logger.info("Arguments: {}", Arrays.toString(joinPoint.getArgs()));
        }
        if (("DEBUG").equals(properties.getLevel())){
            logger.debug("=== HTTP REQUEST START ===");
            logger.debug("Method: {}", joinPoint.getSignature().toShortString());
            logger.debug("Arguments: {}", Arrays.toString(joinPoint.getArgs()));
        }
        if (("ERROR").equals(properties.getLevel())){
            logger.error("=== HTTP REQUEST START ===");
            logger.error("Method: {}", joinPoint.getSignature().toShortString());
            logger.error("Arguments: {}", Arrays.toString(joinPoint.getArgs()));
        }
    }

    @AfterThrowing(pointcut = "pointcut()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        if (("ERROR").equals(properties.getLevel())){
            logger.error("=== HTTP REQUEST FAILED ===");
            logger.error("Method: {}", joinPoint.getSignature().toShortString());
            logger.error("Exception: {}", exception.getMessage(), exception);
        }

    }
    @AfterReturning(pointcut = "pointcut()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        if (("DEBUG").equals(properties.getLevel())){
            logger.debug("=== HTTP REQUEST RETURNED  ===");
            logger.debug("Method: {}", joinPoint.getSignature().toShortString());
            logger.debug("Response: {}", result);
        }
    }
}
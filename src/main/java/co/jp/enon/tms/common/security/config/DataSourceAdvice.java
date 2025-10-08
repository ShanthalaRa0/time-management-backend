package co.jp.enon.tms.common.security.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.stereotype.Component;

@Component
public class DataSourceAdvice {

  @Around("@annotation(dataSource)")
  public void adviceMethod(ProceedingJoinPoint proceedingJoinPoint, DataSource dataSource)
      throws Throwable {
    DataSourceContextHolder.setDataSourceType(dataSource.value());
    proceedingJoinPoint.proceed();
  }
}
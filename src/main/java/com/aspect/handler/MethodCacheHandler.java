package com.aspect.handler;

import java.io.Serializable;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.mvel2.MVEL;

import com.aspect.cache.CacheRepositories;
import com.aspect.config.CacheConfig;

public class MethodCacheHandler implements AspectHandler {

  private CacheRepositories cacheRespositor;
  private Map<String, CacheConfig> caches;

  public MethodCacheHandler(CacheRepositories cacheRespositor, Map<String, CacheConfig> caches) {
    this.cacheRespositor = cacheRespositor;
    this.caches = caches;
  }

  @Override
  public Object execute(ProceedingJoinPoint jp, String key) throws Throwable {
    CacheConfig config = caches.get(key);

    Object[] args = jp.getArgs();

    String space = config.getSpace();
    
    // 缓存域
    String domainExpression = config.getDomainExpression();
    String domain = (domainExpression == null) ? null : String.valueOf(MVEL.eval(domainExpression, args));

    // 缓存的key
    String expression = config.getKeyExpression();
    Object value = String.valueOf(MVEL.eval(expression, args));

    // 从缓存取, 取不到时 执行目标并保存到缓存
    Object target = cacheRespositor.getCacheValue(space, domain, value.toString());
    if (target == null) {
      target = jp.proceed();
      if (target != null) {
        cacheRespositor.setCacheValue(space, domain, value.toString(), (Serializable) target, config.getTimeout());
      }
    }
    return target;
  }

}

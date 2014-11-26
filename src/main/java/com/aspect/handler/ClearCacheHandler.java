package com.aspect.handler;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.mvel2.MVEL;
import org.springframework.beans.factory.annotation.Autowired;

import com.aspect.cache.CacheRepositories;
import com.aspect.config.CacheConfig;
import com.aspect.config.ClearConfig;

public class ClearCacheHandler implements AspectHandler {

  private CacheRepositories cacheRespositor;
  private Map<String, ClearConfig> clears;
  private Map<String, CacheConfig> caches;

  public ClearCacheHandler(CacheRepositories cacheRespositor, Map<String, ClearConfig> clears, Map<String, CacheConfig> caches) {
    this.cacheRespositor = cacheRespositor;
    this.clears = clears;
    this.caches = caches;
  }

  @Autowired
  public Object execute(ProceedingJoinPoint jp, String key) throws Exception {
    ClearConfig config = clears.get(key);
    Object[] args = jp.getArgs();

    // 缓存域
    String domainExpression = config.getDomainExpression();
    String domain = (domainExpression==null) ? null : String.valueOf(MVEL.eval(domainExpression, args));
    
    List<String> clearList = config.getCachedConfigs();
    for(String cacheKey : clearList) {
      CacheConfig cacheConfig = caches.get(cacheKey);

      // 缓存的key
      String expression = cacheConfig.getKeyExpression();
      Object value = String.valueOf(MVEL.eval(expression, args));
      if(value instanceof Collection) {
        for(Object v : (Collection<?>)value) {
          cacheRespositor.clearCacheValue(cacheConfig.getSpace(), domain, String.valueOf(v));
        }
      } else {
        cacheRespositor.clearCacheValue(cacheConfig.getSpace(), domain, cacheKey);
      }
    }
    return null;
  }

}

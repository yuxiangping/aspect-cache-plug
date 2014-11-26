package com.aspect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

import com.aspect.cache.Cache;
import com.aspect.cache.CacheRepositories;
import com.aspect.config.CacheConfig;
import com.aspect.config.ClearConfig;
import com.aspect.handler.ClearCacheHandler;
import com.aspect.handler.MethodCacheHandler;

/**
 * Advice for aspect config.
 */
public class AdviceAspect implements Ordered, InitializingBean {

  private static Logger logger = LoggerFactory.getLogger(AdviceAspect.class);

  /**
   * config
   */
  private String rootKey;
  private Cache cache;
  private List<CacheConfig> caches;
  private List<ClearConfig> clears;

  private MethodCacheHandler cacheHandler;
  private ClearCacheHandler clearHandler;

  private Map<String, CacheConfig> cacheMap;
  private Map<String, ClearConfig> clearMap;

  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.notNull(rootKey, "The 'rootKey' not null.");
    Assert.notNull(cache, "The 'cache' not null.");
    Assert.notNull(caches, "The 'caches' not null.");
    Assert.notNull(clears, "The 'clears' not null." );

    CacheRepositories respositor = new CacheRepositories(rootKey, cache);

    initAspectConfig();

    cacheHandler = new MethodCacheHandler(respositor, cacheMap);
    clearHandler = new ClearCacheHandler(respositor, clearMap, cacheMap);
  }

  @Override
  public int getOrder() {
    return 0;
  }

  public Object execute(ProceedingJoinPoint jp, Aspect aspect) throws Throwable {
    long start = System.currentTimeMillis();

    String key = aspect.key();
    if (logger.isDebugEnabled()) {
      logger.debug("Jion point aspect handler, config key '" + key + "'");
    }

    Object target = null;
    try {
      target = cache(jp, key);

      clear(jp, key);
    } catch (Exception ex) {
      logger.error("Execute aspect handler has error, config key '" + key + "'. Cause:" + ex.getMessage(), ex);
      throw ex;
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Execute finished point aspect handler, config key '" + key + "' in '" + (System.currentTimeMillis() - start) + "' ms.");
    }

    return target;
  }

  private void initAspectConfig() {
    cacheMap = new HashMap<String, CacheConfig>(caches.size());
    for (CacheConfig config : caches) {
      cacheMap.put(config.getKey(), config);
    }

    clearMap = new HashMap<String, ClearConfig>(clears.size());
    for (ClearConfig config : clears) {
      clearMap.put(config.getKey(), config);
    }
  }

  private void clear(ProceedingJoinPoint jp, String key) throws Throwable {
    if (!clearMap.containsKey(key)) {
      return;
    }
    clearHandler.execute(jp, key);
  }

  private Object cache(ProceedingJoinPoint jp, String key) throws Throwable {
    if (cacheMap.containsKey(key)) {
      return jp.proceed();
    }
    return cacheHandler.execute(jp, key);
  }

  public void setRootKey(String rootKey) {
    this.rootKey = rootKey;
  }

  public void setCache(Cache cache) {
    this.cache = cache;
  }

  public void setCaches(List<CacheConfig> caches) {
    this.caches = caches;
  }

  public void setClears(List<ClearConfig> clears) {
    this.clears = clears;
  }
  
}

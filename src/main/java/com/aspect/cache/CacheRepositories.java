package com.aspect.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;


public class CacheRepositories {

  private static final String separator = ".";

  private String rootKey = "aspect.cache.";

  private Cache cache;

  /**
   * for manage
   */
  public static final Map<String, String> spaces = new HashMap<String, String>();

  public CacheRepositories(String rootKey, Cache cache) {
    this.rootKey = rootKey;
    this.cache = cache;
  }

  public Object getCacheValue(String space, String domain, String key) {
    StringBuilder buff = new StringBuilder();
    buff.append(rootKey).append(space).append(separator);
    if (domain != null) {
      buff.append(domain).append(separator);
    }
    buff.append(key);
    return cache.get(buff.toString());
  }

  /**
   * @param timeout min
   */
  public void setCacheValue(String space, String domain, String key, Serializable value, int expire) {
    if (!spaces.containsKey(space)) {
      spaces.put(space, rootKey + space);
    }

    StringBuilder buff = new StringBuilder();
    buff.append(rootKey).append(space).append(separator);

    if (domain == null) {
      buff.append(key);
      cache.set(buff.toString(), value, expire);
      return;
    }

    String domainKey = buff.toString() + domain;
    buff.append(domain).append(separator);

    String cacheKey = buff.toString();

    Set<String> keys = (Set<String>) cache.get(domainKey);
    if (CollectionUtils.isEmpty(keys)) {
      keys = new HashSet<String>();
    }
    keys.add(cacheKey);

    cache.set(cacheKey, value, expire);
    cache.set(domainKey, (Serializable) keys, expire);
  }

  public void clearCacheValue(String space, String domain, String key) {
    StringBuilder buff = new StringBuilder();
    buff.append(rootKey).append(space).append(separator);
    if (domain == null) {
      buff.append(key);
      cache.remove(buff.toString());
      return;
    }

    String domainKey = buff.toString() + domain;
    buff.append(domain).append(separator);

    Set<String> keys = (Set<String>) cache.get(domainKey);
    if (!CollectionUtils.isEmpty(keys)) {
      if (key != null) {
        buff.append(key);
        cache.remove(buff.toString());
      } else {
        for (String dk : keys) {
          cache.remove(dk);
        }
      }
    }
  }

}

package com.aspect.config;

public class CacheConfig {

  private String key;
  private String space;
  /**
   * 缓存域表达式 / 一般按用户域划分
   */
  private String domainExpression;
  /**
   * 缓存key的表达式
   */
  private String keyExpression;
  /**
   * 超时。 单位：分钟
   */
  private int timeout;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getSpace() {
    return space;
  }

  public void setSpace(String space) {
    this.space = space;
  }

  public String getDomainExpression() {
    return domainExpression;
  }

  public void setDomainExpression(String domainExpression) {
    this.domainExpression = domainExpression;
  }

  public String getKeyExpression() {
    return keyExpression;
  }

  public void setKeyExpression(String keyExpression) {
    this.keyExpression = keyExpression;
  }

  public int getTimeout() {
    return timeout;
  }

  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }

}

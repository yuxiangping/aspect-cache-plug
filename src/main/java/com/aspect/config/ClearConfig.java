package com.aspect.config;

import java.util.List;

public class ClearConfig {

  private String key;  
  private String domainExpression;
  private List<String> cachedConfigs;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getDomainExpression() {
    return domainExpression;
  }

  public void setDomainExpression(String domainExpression) {
    this.domainExpression = domainExpression;
  }

  public List<String> getCachedConfigs() {
    return cachedConfigs;
  }

  public void setCachedConfigs(List<String> cachedConfigs) {
    this.cachedConfigs = cachedConfigs;
  }
  
}

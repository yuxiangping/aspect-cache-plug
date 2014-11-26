package com.aspect.cache;

import java.io.Serializable;

public interface Cache {

  <T extends Serializable> T get(String key);
  
  boolean set(String key, Serializable value);
  
  boolean set(String key, Serializable value, int expire);
  
  boolean remove(String key);
  
}

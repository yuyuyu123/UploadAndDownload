package com.yulin.download_upload.api;

import java.util.HashMap;
import retrofit2.Converter;

public class ManagerFactory {
  private static ManagerFactory factory;
  private static HashMap<String, Object> serviceMap = new HashMap<>();

  public static ManagerFactory getFactory() {
    if (factory == null) {
      synchronized (ManagerFactory.class) {
        if (factory == null) factory = new ManagerFactory();
      }
    }
    return factory;
  }

  public <T> T getManager(Class<T> clz) {
    Object service = serviceMap.get(clz.getName());
    if (service == null) {
      service = RetrofitFactory.getRetrofit().create(clz);
      serviceMap.put(clz.getName(), service);
    }
    return (T) service;
  }

  public <T> T getManager(Class<T> clz, Converter.Factory factory) {
    Object service = serviceMap.get(clz.getName());
    if (service == null) {
      service = RetrofitFactory.getRetrofit(factory).create(clz);
      serviceMap.put(clz.getName(), service);
    }
    return (T) service;
  }
}

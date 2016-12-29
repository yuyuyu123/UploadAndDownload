package com.yulin.download_upload.api;

import android.support.annotation.NonNull;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {
  private static Retrofit mRetrofit;
  private static OkHttpClient mClient;
  private static String baseUrl = "http://www.baidu.com";

  public static void setBaseUrl(@NonNull String url) {
    baseUrl = url;
  }

  public static void setOkhttpClient(@NonNull OkHttpClient client) {
    mClient = client;
  }

  /**
   * 获取配置好的retrofit对象来生产Manager对象
   */
  public static Retrofit getRetrofit() {
    if (mRetrofit == null) {
//      if (baseUrl == null || baseUrl.length() <= 0) {
//        throw new IllegalStateException("请在调用getFactory之前先调用setBaseUrl");
//      }

      Retrofit.Builder builder = new Retrofit.Builder();

      builder.baseUrl(baseUrl)
          .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
          .addConverterFactory(GsonConverterFactory.create());

      if (mClient != null) builder.client(mClient);

      mRetrofit = builder.build();
    }
    return mRetrofit;
  }

  /**
   * 获取配置好的retrofit对象来生产Manager对象
   */
  public static Retrofit getRetrofit(Converter.Factory factory) {
    if (baseUrl == null || baseUrl.length() <= 0) {
      throw new IllegalStateException("请在调用getFactory之前先调用setBaseUrl");
    }

    Retrofit.Builder builder = new Retrofit.Builder();

    builder.baseUrl(baseUrl)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(factory);

    if (mClient != null) builder.client(mClient);

    return builder.build();
  }
}

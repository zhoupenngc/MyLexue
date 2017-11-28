package com.lexue365.utils;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DELL on 2017/11/27.
 */

public class RetrofitUtil {
    private static RetrofitUtil retrofitUtil;
    private static Retrofit retrofit;

    private RetrofitUtil() {
    }
    //双重检验锁实现单例模式
    public static RetrofitUtil getInstance(){
        if (retrofitUtil==null){
            synchronized (RetrofitUtil.class){
                if (retrofitUtil==null){
                    retrofitUtil= new RetrofitUtil();
                }
            }
        }
        return retrofitUtil;
    }
    //封装Retrofit 封装OkHttp RxJava
    private synchronized Retrofit getRetrofit(String url){
        //设置拦截器
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("Error", message);
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //创建OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).connectTimeout(5000, TimeUnit.SECONDS)
                .build();
        if (retrofit ==null){
            retrofit = new Retrofit.Builder().client(okHttpClient).baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public <T> T getApiService(String url,Class<T> cl){
        Retrofit retrofit = getRetrofit(url);
        return retrofit.create(cl);
    }
}

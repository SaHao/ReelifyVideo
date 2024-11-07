package com.reelify.kkkkwillo.net.request;



import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.reelify.kkkkwillo.net.help.NullOnEmptyConverterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitManager {
    private static RetrofitManager retrofitManager = null;
    OkHttpClient okHttpClient = null;

    private RetrofitManager() {
        initOkhttp();
    }

    public static RetrofitManager getRetrofitManager() {
        if (null == retrofitManager) retrofitManager = new RetrofitManager();
        return retrofitManager;
    }

    private void initOkhttp() {
        OkHttpClient.Builder defaultBuilder = initDefaultBuilder();
        okHttpClient = defaultBuilder.build();
    }

    private OkHttpClient.Builder initDefaultBuilder() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //打印retrofit日志
                Log.d("RetrofitLog", "retrofitBack = " + message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .retryOnConnectionFailure(true)
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        return builder;
    }

    public AppService getAppService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.HOST).client(okHttpClient)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        return retrofit.create(AppService.class);
    }
}

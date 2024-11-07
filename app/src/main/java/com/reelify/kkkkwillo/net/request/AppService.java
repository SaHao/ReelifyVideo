package com.reelify.kkkkwillo.net.request;



import com.reelify.kkkkwillo.bean.ConfigInfo;
import com.reelify.kkkkwillo.bean.EventInfo;
import com.reelify.kkkkwillo.bean.ListInfo;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by Administrator on 2017/6/5.
 */

public interface AppService {
    /**
     * @return
     */
    @POST("api/app/config")
    @Headers("Content-Type:application/json")
    Observable<ConfigInfo> getConfig(@Body RequestBody requestBody);
//    @POST("api/app/config")
//    Observable<ConfigInfo> getConfig(@Query("gaid") String event, @Query("attributes") String attributes ,@Query("app") String app);
    @POST("api/app/video")
    @Headers("Content-Type:application/json")
    Observable<ListInfo> getList(@Body RequestBody requestBody);
    @POST("api/app/event")
    @Headers("Content-Type:application/json")
    Observable<EventInfo> postEvent(@Body RequestBody requestBody);
}

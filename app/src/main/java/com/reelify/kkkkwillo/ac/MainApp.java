package com.reelify.kkkkwillo.ac;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustAttribution;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.AdjustEvent;
import com.adjust.sdk.OnAttributionChangedListener;
import com.google.android.exoplayer2.database.DefaultDatabaseProvider;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.reelify.kkkkwillo.utils.AdIdManager;
import com.reelify.kkkkwillo.utils.MySettings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainApp extends Application {
    String appToken = "b08zrwdnkm4g";
    private static Cache cache;
    public static int PlayingPosition = -1;

    public static RecyclerView RV = null;
    public static boolean isNew;

    @Override
    public void onCreate() {
        super.onCreate();
        MySettings.init(this);
        initAD();
        LeastRecentlyUsedCacheEvictor evictor = new LeastRecentlyUsedCacheEvictor(700 * 1024 * 1024);
        cache = new SimpleCache(new File(getCacheDir(), "media"), evictor);
        MySettings.getInstance().saveSetting("session", MySettings.getInstance().getIntSetting("session") + 1);
        if ( MySettings.getInstance().getStringSetting("idList").isEmpty()){
            List<String> idList=new ArrayList<>();
            idList.add("-1");
            MySettings.getInstance().saveSetting("idList",idList);
        }
        if (MySettings.getInstance().getStringSetting("name").isEmpty()){
            MySettings.getInstance().saveSetting("name", AdIdManager.RandomLetter());
        }
        if (MySettings.getInstance().getStringSetting("userId").isEmpty()){
            MySettings.getInstance().saveSetting("userId", AdIdManager.RandomNum());
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    private void initAD() {
        String environment = AdjustConfig.ENVIRONMENT_PRODUCTION;
        AdjustConfig config = new AdjustConfig(this, appToken, environment);
        config.enableSendingInBackground();
        config.setOnAttributionChangedListener(new OnAttributionChangedListener() {
            @Override
            public void onAttributionChanged(AdjustAttribution attribution) {
                String string = attribution.toString();
            }
        });
        Adjust.initSdk(config);
        registerActivityLifecycleCallbacks(new AdjustLifecycleCallbacks());
    }

    public static Cache getCache() {
        return cache;
    }

    private static final class AdjustLifecycleCallbacks implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            Adjust.onResume();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Adjust.onPause();
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {

        }

    }
}


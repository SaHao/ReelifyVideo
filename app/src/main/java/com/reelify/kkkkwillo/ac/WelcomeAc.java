package com.reelify.kkkkwillo.ac;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustAttribution;
import com.adjust.sdk.AdjustEvent;
import com.adjust.sdk.OnAttributionReadListener;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.gson.Gson;
import com.reelify.kkkkwillo.R;
import com.reelify.kkkkwillo.bean.ConfigInfo;
import com.reelify.kkkkwillo.bean.EventInfo;
import com.reelify.kkkkwillo.bean.ListInfo;
import com.reelify.kkkkwillo.media.MainActivity;
import com.reelify.kkkkwillo.net.request.Config;
import com.reelify.kkkkwillo.net.request.RetrofitManager;
import com.reelify.kkkkwillo.utils.AdIdManager;
import com.reelify.kkkkwillo.utils.MySettings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class WelcomeAc extends AppCompatActivity {
    private int mNum = 0;
    public static long START_TIME;
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mHandler.post(mRunnable);
                mNum++;
            } else if (msg.what == 2) {
                getConfig1();
            }
        }
    };
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (MySettings.getInstance().getStringSetting("gaid").equals("")) {
                new MyAsyncTask().execute();
                mHandler.sendEmptyMessageDelayed(1, 400);
            } else if (MySettings.getInstance().getStringSetting("attribution").equals("")) {
                Log.e("getAttribution", "begin");
                getAttribution();
                mHandler.sendEmptyMessageDelayed(1, 400);
            } else {
                mHandler.removeCallbacks(mRunnable);
                getConfig();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN//状态栏不会被隐藏但activity布局会扩展到状态栏所在位置
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        mHandler.sendEmptyMessageDelayed(1, 200);
    }

    void getConfig() {
        Map<String, Object> map = new HashMap<>();
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("app", "reelify");
        String json = new Gson().toJson(map);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, json);
        Disposable d = RetrofitManager.getRetrofitManager().getAppService().getConfig(requestBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ConfigInfo>() {
                    @Override
                    public void accept(ConfigInfo data) throws Exception {
                        if (null != data) {
                            if (data.getCode() == 0) {
                                if (data.getData() == null) {
                                    return;
                                }
                                MySettings.getInstance().saveSetting("configInfo", data);
                                getList();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {//初始化异常
                        Toast.makeText(getApplicationContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
                        mHandler.sendEmptyMessageDelayed(2, 400);
                    }
                });
    }

    void getConfig1() {
        Map<String, Object> map = new HashMap<>();
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("app", "reelify");
        String json = new Gson().toJson(map);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, json);
        Disposable d = RetrofitManager.getRetrofitManager().getAppServiceT().getConfig(requestBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ConfigInfo>() {
                    @Override
                    public void accept(ConfigInfo data) throws Exception {
                        if (null != data) {
                            if (data.getCode() == 0) {
                                if (data.getData() == null) {
                                    return;
                                }
                                MySettings.getInstance().saveSetting("configInfo", data);
                                getList1();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {//初始化异常
                        Toast.makeText(getApplicationContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
                        mHandler.sendEmptyMessageDelayed(1, 400);
                    }
                });
    }

    void getList() {
        Map<String, Object> map = new HashMap<>();
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("app", "reelify");
        String json = new Gson().toJson(map);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, json);
        Disposable d = RetrofitManager.getRetrofitManager().getAppService().getList(requestBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//        Disposable d = RetrofitManager.getRetrofitManager().getAppService().getList(Config.GAID, Config.ATTR).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ListInfo>() {
                    @Override
                    public void accept(ListInfo data) throws Exception {
                        if (null != data) {
                            if (data.getCode() == 0) {
                                if (data.getData() == null) {
                                    return;
                                }
                                postEvent();
                                MySettings.getInstance().saveSetting("listInfo", data);
                                Intent intent = new Intent(WelcomeAc.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {//初始化异常
                        Toast.makeText(getApplicationContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
                        mHandler.sendEmptyMessageDelayed(1, 400);
                    }
                });
    }

    void getList1() {
        Map<String, Object> map = new HashMap<>();
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("app", "reelify");
        String json = new Gson().toJson(map);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, json);
        Disposable d = RetrofitManager.getRetrofitManager().getAppServiceT().getList(requestBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//        Disposable d = RetrofitManager.getRetrofitManager().getAppService().getList(Config.GAID, Config.ATTR).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ListInfo>() {
                    @Override
                    public void accept(ListInfo data) throws Exception {
                        if (null != data) {
                            if (data.getCode() == 0) {
                                if (data.getData() == null) {
                                    return;
                                }
                                postEvent1();
                                MySettings.getInstance().saveSetting("listInfo", data);
                                Intent intent = new Intent(WelcomeAc.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {//初始化异常
                        Toast.makeText(getApplicationContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
                        mHandler.sendEmptyMessage(2);
                    }
                });
    }

    void postEvent() {
        AdjustEvent adjustEvent = new AdjustEvent("5xryf0");
        Adjust.trackEvent(adjustEvent);
        START_TIME = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("app", "reelify");
        map.put("timestamp", START_TIME);
        map.put("userid", MySettings.getInstance().getStringSetting("userId"));
        map.put("language", Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());
        map.put("version", "1.2.0");
        map.put("event", "app_show_app");
        map.put("session", MySettings.getInstance().getIntSetting("session"));
        map.put("ext1", "");
        map.put("ext2", "");
        map.put("ext3", "");
        String json = new Gson().toJson(map);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, json);
        Disposable d = RetrofitManager.getRetrofitManager().getAppService().postEvent(requestBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EventInfo>() {
                    @Override
                    public void accept(EventInfo data) throws Exception {
                        if (null != data) {
                            if (data.getCode() == 0) {
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {//初始化异常
                        Toast.makeText(getApplicationContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
                        mHandler.sendEmptyMessage(1);
                    }
                });
    }

    void postEvent1() {
        AdjustEvent adjustEvent = new AdjustEvent("5xryf0");
        Adjust.trackEvent(adjustEvent);
        START_TIME = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("app", "reelify");
        map.put("timestamp", START_TIME);
        map.put("userid", MySettings.getInstance().getStringSetting("userId"));
        map.put("language", Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());
        map.put("version", "1.2.0");
        map.put("event", "app_show_app");
        map.put("session", MySettings.getInstance().getIntSetting("session"));
        map.put("ext1", "");
        map.put("ext2", "");
        map.put("ext3", "");
        String json = new Gson().toJson(map);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, json);
        Disposable d = RetrofitManager.getRetrofitManager().getAppServiceT().postEvent(requestBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EventInfo>() {
                    @Override
                    public void accept(EventInfo data) throws Exception {
                        if (null != data) {
                            if (data.getCode() == 0) {
                                Config.HOST="https://api.filmflowvideo.com/";
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {//初始化异常
                        Toast.makeText(getApplicationContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
                        mHandler.sendEmptyMessage(2);
                    }
                });
    }


    private void getAttribution() {
        Adjust.getAttribution(new OnAttributionReadListener() {
            @Override
            public void onAttributionRead(AdjustAttribution attribution) {
                Log.e("getAttribution", "end");
                if (attribution != null) {
                    Log.e("getAttribution", "has");
                    if (!attribution.network.equalsIgnoreCase("Organic") || mNum > 41) {
                        String string = attribution.toString();
                        MySettings.getInstance().saveSetting("attribution", string);
                    }
                } else {
                    Log.e("getAttribution", "none");
                    MySettings.getInstance().saveSetting("attribution", "");
                }
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            String gaid = AdIdManager.getAdId(getApplicationContext());
            MySettings.getInstance().saveSetting("gaid", gaid);
            return null;
        }
    }
//
//    AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
//        @Override
//        protected String doInBackground(Void... params) {
//            StringBuilder result = new StringBuilder();
//            AdvertisingIdClient.Info info = null;
//            try {
//                info = AdvertisingIdClient.getAdvertisingIdInfo(WelcomeAc.this);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (GooglePlayServicesNotAvailableException e) {
//                e.printStackTrace();
//            } catch (GooglePlayServicesRepairableException e) {
//                e.printStackTrace();
//            }
//            result.append(info.getId());
//            MySettings.getInstance().saveSetting("gaid", result.toString());
//
//            return result.toString();
//        }
//
//        @Override
//        protected void onPostExecute(String token) {
//        }
//
//    };


}

//package com.reelify.kkkkwillo.ac;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.pm.ResolveInfo;
//import android.net.Uri;
//import android.os.Bundle;
//import android.telecom.Call;
//import android.text.TextUtils;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.adjust.sdk.Adjust;
//import com.adjust.sdk.AdjustEvent;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.reelify.kkkkwillo.R;
//import com.reelify.kkkkwillo.adapter.AdapterA;
//import com.reelify.kkkkwillo.bean.ConfigInfo;
//import com.reelify.kkkkwillo.bean.EventInfo;
//import com.reelify.kkkkwillo.bean.ListInfo;
//import com.reelify.kkkkwillo.net.request.Config;
//import com.reelify.kkkkwillo.net.request.RetrofitManager;
//import com.reelify.kkkkwillo.utils.MySettings;
//import com.reelify.kkkkwillo.utils.RecycleViewDivider;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.functions.Consumer;
//import io.reactivex.schedulers.Schedulers;
//import okhttp3.Callback;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
//
//public class CustomA extends Activity {
//    RecyclerView Rv;
//    AdapterA adapterA;
//    ListInfo listInfo;
//
//    public static ConfigInfo CONFIG_INFO = new ConfigInfo();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.ac_a);
////        initView();
//        initData();
////        getEvent("jobs_show_card");
////        AdjustEvent adjustEvent = new AdjustEvent(CONFIG_INFO.getData().getReport().getAdjust().getJobs_show_card().getCode());
////        Adjust.trackEvent(adjustEvent);
//    }
//
//    void initView() {
//        Rv = findViewById(R.id.a_rv);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(CustomA.this);
//        layoutManager.setOrientation(RecyclerView.VERTICAL);
//        Rv.setLayoutManager(layoutManager);
//        Rv.addItemDecoration(new RecycleViewDivider(
//                CustomA.this, LinearLayoutManager.VERTICAL, 40, getResources().getColor(R.color.greys)));
//        adapterA = new AdapterA(CustomA.this);
////        adapterA.setOnItemClickListener(mListener);
//        Rv.setAdapter(adapterA);
//    }
//
//    void initData() {
//        Gson gson = new Gson();
//        String config = MySettings.getInstance().getStringSetting("configInfo");
//        CONFIG_INFO = gson.fromJson(config, ConfigInfo.class);
//
//        String info = MySettings.getInstance().getStringSetting("listInfo");
//        listInfo = gson.fromJson(info, ListInfo.class);
//
//
//        if (MySettings.getInstance().getIntSetting("click") >= CONFIG_INFO.data.contacts.limits.click || System.currentTimeMillis() - MySettings.getInstance().getLongSetting("hour") > CONFIG_INFO.data.contacts.limits.hour * 60 * 60 * 1000 || System.currentTimeMillis() - MySettings.getInstance().getLongSetting("hour2") > CONFIG_INFO.data.contacts.limits.hour2 * 60 * 60 * 1000 && MySettings.getInstance().getLongSetting("hour2") != -1) {
//            adapterA.upDateDatas(MySettings.removeInfo(listInfo.getData()));
//        } else {
//            adapterA.upDateDatas(MySettings.sortInfo(listInfo.getData()));
//            if (!MySettings.getInstance().getStringSetting("ptjob").equals("888")) {
//                for (ListInfo.Data data : listInfo.getData()) {
//                    if (data.getType().equalsIgnoreCase("c")) {
//                        MySettings.getInstance().saveSetting("ptjob", "888");
//                        getEvent("jobs_show_ptjob");
//                        AdjustEvent adjustEvent = new AdjustEvent(CONFIG_INFO.getData().getReport().getAdjust().getJobs_show_ptjob().getCode());
//                        Adjust.trackEvent(adjustEvent);
//                        break;
//                    }
//                }
//            }
//        }
//        if (MySettings.getInstance().getLongSetting("hour2")!=-1){
//            if (MySettings.getInstance().getIntSetting("click")>=CONFIG_INFO.data.contacts.limits.click||System.currentTimeMillis()-MySettings.getInstance().getLongSetting("hour")>CONFIG_INFO.data.contacts.limits.hour*60*60*1000||System.currentTimeMillis()-MySettings.getInstance().getLongSetting("hour2")>CONFIG_INFO.data.contacts.limits.hour2*60*1000){
//                adapterA.upDateDatas(MySettings.removeInfo(listInfo.getData()));
//            } else {
//                adapterA.upDateDatas(MySettings.sortInfo(listInfo.getData()));
//                if (!MySettings.getInstance().getStringSetting("ptjob").equals("888")) {
//                    for (ListInfo.Data data : listInfo.getData()) {
//                        if (data.getType().equalsIgnoreCase("c")) {
//                            MySettings.getInstance().saveSetting("ptjob", "888");
//                            getEvent("jobs_show_ptjob");
//                            AdjustEvent adjustEvent = new AdjustEvent(CONFIG_INFO.getData().getReport().getAdjust().getJobs_show_ptjob().getCode());
//                            Adjust.trackEvent(adjustEvent);
//                            break;
//                        }
//                    }
//                }
//            }
//        }else {
//            if (MySettings.getInstance().getIntSetting("click")>=CONFIG_INFO.data.contacts.limits.click||System.currentTimeMillis()-MySettings.getInstance().getLongSetting("hour")>CONFIG_INFO.data.contacts.limits.hour*60*60*1000){
//                adapterA.upDateDatas(MySettings.removeInfo(listInfo.getData()));
//            } else {
//                adapterA.upDateDatas(MySettings.sortInfo(listInfo.getData()));
//                if (!MySettings.getInstance().getStringSetting("ptjob").equals("888")) {
//                    for (ListInfo.Data data : listInfo.getData()) {
//                        if (data.getType().equalsIgnoreCase("c")) {
//                            MySettings.getInstance().saveSetting("ptjob", "888");
//                            getEvent("jobs_show_ptjob");
//                            AdjustEvent adjustEvent = new AdjustEvent(CONFIG_INFO.getData().getReport().getAdjust().getJobs_show_ptjob().getCode());
//                            Adjust.trackEvent(adjustEvent);
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//
//
//    }
//
//    public void invokeApp(ListInfo.Data data) {
//        try {
//            if (data.getContacts().get(0).getUrl() == null || TextUtils.isEmpty(data.getContacts().get(0).getUrl())) {
//                return;
//            }
//            String name = "";
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.getContacts().get(0).getUrl() + "?text=" + data.getContacts().get(0).getText()));
//            ResolveInfo packageManager = CustomA.this.getPackageManager().resolveActivity(intent, 0);
//            if (packageManager != null) {
//                name = packageManager.activityInfo.packageName;
//            }
//            CustomA.this.startActivity(intent);
//            if (name.equals("com.whatsapp")) {
//                getEvent("addtocart_ws");
//                AdjustEvent adjustEvent = new AdjustEvent(CONFIG_INFO.getData().getReport().getAdjust().getAddtocart_ws().getCode());
//                Adjust.trackEvent(adjustEvent);
//            }
//            if (data.contacts.size() > 0) {
//                Gson gson = new Gson();
//                List<String> idList;
//                String idJson=MySettings.getInstance().getStringSetting("idList");
//                idList=gson.fromJson(idJson,new TypeToken<List<String>>() {}.getType());
//                if (idList!=null&&!idList.contains(data.contacts.get(0).getId()+"")){
//                    idList.add(data.contacts.get(0).getId()+"");
//                    MySettings.getInstance().saveSetting("idList",idList);
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
//
//                    map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
//
//                    map.put("action", "contact_lva");
//                    map.put("id", data.contacts.get(0).getId());
//
//                    String json = gson.toJson(map);
//                    MediaType mediaType = MediaType.parse("application/json");
//                    RequestBody requestBody = RequestBody.create(mediaType, json);
//                    getEvent(requestBody);
//                }
//            }
//
//            if (MySettings.getInstance().getIntSetting("click") == -1) {
//                MySettings.getInstance().saveSetting("click", 1);
//            } else {
//                int i = MySettings.getInstance().getIntSetting("click") + 1;
//                MySettings.getInstance().saveSetting("click", i);
//            }
//            if (MySettings.getInstance().getLongSetting("hour2") == -1) {
//                MySettings.getInstance().saveSetting("hour2", System.currentTimeMillis());
//            }
//        } catch (Exception ed) {
//            ed.printStackTrace();
//        }
//    }
//
//    private AdapterA.OnItemClickListener mListener = new AdapterA.OnItemClickListener() {
//        @Override
//        public void onClick(ListInfo.Data data) {
//            if (CONFIG_INFO.data.contacts.showContact) {
//                DialogA dialogA = new DialogA(CustomA.this);
//                dialogA.setTitle("Jobs News");
//                dialogA.setWen1(CONFIG_INFO.data.chatScript.actions.contact);
//                dialogA.setWen2(CONFIG_INFO.data.contact.contents.get(0));
//                dialogA.setWen3(CONFIG_INFO.data.contact.contents.get(1));
//                dialogA.setWen4(CONFIG_INFO.data.contact.contents.get(2));
//                dialogA.setOnclickListener(new DialogA.AnOnclickListener() {
//                    @Override
//                    public void onClick() {
//                        if (!MySettings.getInstance().getStringSetting("adTag").equals("999")) {
//                            MySettings.getInstance().saveSetting("adTag", "999");
//                            getEvent("addtocartlt");
//                            AdjustEvent adjustEvent = new AdjustEvent(CONFIG_INFO.getData().getReport().getAdjust().getAddtocartlt().getCode());
//                            Adjust.trackEvent(adjustEvent);
//                        }
//                        getEvent("addtocartpv");
//                        AdjustEvent adjustEvent = new AdjustEvent(CONFIG_INFO.getData().getReport().getAdjust().getAddtocartpv().getCode());
//                        Adjust.trackEvent(adjustEvent);
//                        dialogA.dismiss();
//                        invokeApp(data);
//                    }
//                });
//                dialogA.show();
//                getEvent("chat_show_dialog");
//                AdjustEvent adjustEvent = new AdjustEvent(CONFIG_INFO.getData().getReport().getAdjust().getChat_show_dialog().getCode());
//                Adjust.trackEvent(adjustEvent);
//                getEvent("click_contact");
////                if (data.contacts.size() > 0) {
////                    Gson gson = new Gson();
////                    List<String> idList=new ArrayList<>();
////                    String idJson=MySettings.getInstance().getStringSetting("idList");
////                    idList=gson.fromJson(idJson,new TypeToken<List<String>>() {}.getType());
////                    if (idList!=null&&!idList.contains(data.contacts.get(0).getId()+"")){
////                        idList.add(data.contacts.get(0).getId()+"");
////                        MySettings.getInstance().saveSetting("idList",idList);
////                        Map<String, Object> map = new HashMap<>();
////                        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
////
////                        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
////
////                        map.put("action", "click_contact");
////                        map.put("id", data.contacts.get(0).getId());
////                        String json = gson.toJson(map);
////                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
////
////                    }
////
////                }
//            } else {
//                if (!MySettings.getInstance().getStringSetting("adTag").equals("999")) {
//                    MySettings.getInstance().saveSetting("adTag", "999");
//                    getEvent("addtocartlt");
//                    AdjustEvent adjustEvent = new AdjustEvent(CONFIG_INFO.getData().getReport().getAdjust().getAddtocartlt().getCode());
//                    Adjust.trackEvent(adjustEvent);
//                }
//                getEvent("addtocartpv");
//                AdjustEvent adjustEvent = new AdjustEvent(CONFIG_INFO.getData().getReport().getAdjust().getAddtocartpv().getCode());
//                Adjust.trackEvent(adjustEvent);
//                invokeApp(data);
//            }
//        }
//
//        @Override
//        public void onClickB(ListInfo.Data data) {
//            Intent intent = new Intent(CustomA.this, CustomB.class);
//            intent.putExtra("dataB", data);
//            startActivity(intent);
//        }
//    };
//
//    void getEvent(String event) {
//        Disposable d = RetrofitManager.getRetrofitManager().getAppService().postEvent(MySettings.getInstance().getStringSetting("gaid"), MySettings.getInstance().getStringSetting("attribution"), event).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
////        Disposable d = RetrofitManager.getRetrofitManager().getAppService().postEvent(Config.GAID, Config.ATTR,event).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<EventInfo>() {
//                    @Override
//                    public void accept(EventInfo data) throws Exception {
//                        if (null != data) {
//                            if (data.getCode() == 0) {
//                            }
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        Toast.makeText(getApplicationContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    void getEvent(RequestBody requestBody) {
//        OkHttpClient okHttpClient = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url(Config.HOST+"vexx/eve1")
//                .addHeader("Content-Type", "application/json")
//                .post(requestBody)
//                .build();
//
//        okHttpClient.newCall(request).enqueue(new Callback() {
//
//            @Override
//            public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
//                if (response.code() == 0) {
//                    System.out.print("dddd");
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
//                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
//            }
//
//        });
//
////        Disposable d = RetrofitManager.getRetrofitManager().getAppService().postEvent(requestBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//////        Disposable d = RetrofitManager.getRetrofitManager().getAppService().postEvent(Config.GAID, Config.ATTR,event).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
////                .subscribe(new Consumer<EventInfo>() {
////                    @Override
////                    public void accept(EventInfo data) throws Exception {
////                        if (null != data) {
////                            if (data.getCode() == 0) {
////                            }
////                        }
////                    }
////                }, new Consumer<Throwable>() {
////                    @Override
////                    public void accept(Throwable throwable) throws Exception {
////                        Toast.makeText(getApplicationContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
////                    }
////                });
//    }
//}

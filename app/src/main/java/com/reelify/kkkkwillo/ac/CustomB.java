//package com.reelify.kkkkwillo.ac;
//
//import static com.reelify.kkkkwillo.ac.CustomA.CONFIG_INFO;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.pm.ResolveInfo;
//import android.net.Uri;
//import android.os.Bundle;
//import android.telecom.Call;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
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
//import com.reelify.kkkkwillo.adapter.AdapterB;
//import com.reelify.kkkkwillo.bean.EventInfo;
//import com.reelify.kkkkwillo.bean.ListInfo;
//import com.reelify.kkkkwillo.net.request.Config;
//import com.reelify.kkkkwillo.net.request.RetrofitManager;
//import com.reelify.kkkkwillo.utils.MySettings;
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
//public class CustomB extends Activity {
//    RecyclerView Rv;
//    TextView tittleTxt, buttonTxt, tips_one, tips_two;
//    ImageView backImg;
//    ListInfo.Data mData;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.ac_b);
//        Intent intent = getIntent();
//        mData = (ListInfo.Data) intent.getSerializableExtra("dataB");
//        initView();
//        initData();
//    }
//
//    void initView() {
//        Rv = findViewById(R.id.b_rv);
//        tittleTxt = findViewById(R.id.txt_tittle);
//        tips_one = findViewById(R.id.tips_one);
//        tips_two = findViewById(R.id.tips_two);
//        backImg = findViewById(R.id.img_back);
//        backImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//        buttonTxt = findViewById(R.id.txt_button);
//        buttonTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mData.getType().equalsIgnoreCase("c")) {
//                    getEvent("chat_show_dialog");
//                    AdjustEvent adjustEvent = new AdjustEvent(CONFIG_INFO.getData().getReport().getAdjust().getChat_show_dialog().getCode());
//                    Adjust.trackEvent(adjustEvent);
//                    DialogA dialogA = new DialogA(CustomB.this);
//                    dialogA.setTitle("Jobs News");
//                    dialogA.setWen1(CONFIG_INFO.data.chatScript.actions.contact);
//                    dialogA.setWen2(CONFIG_INFO.data.contact.contents.get(0));
//                    dialogA.setWen3(CONFIG_INFO.data.contact.contents.get(1));
//                    dialogA.setWen4(CONFIG_INFO.data.contact.contents.get(2));
//                    dialogA.setOnclickListener(new DialogA.AnOnclickListener() {
//                        @Override
//                        public void onClick() {
//                            if (!MySettings.getInstance().getStringSetting("adTag").equals("999")) {
//                                MySettings.getInstance().saveSetting("adTag", "999");
//                                getEvent("addtocartlt");
//                                AdjustEvent adjustEvent = new AdjustEvent(CONFIG_INFO.getData().getReport().getAdjust().getAddtocartlt().getCode());
//                                Adjust.trackEvent(adjustEvent);
//                            }
//                            getEvent("addtocartpv");
//                            AdjustEvent adjustEvent = new AdjustEvent(CONFIG_INFO.getData().getReport().getAdjust().getAddtocartpv().getCode());
//                            Adjust.trackEvent(adjustEvent);
//                            dialogA.dismiss();
//                            invokeApp(mData.getContacts().get(0).getUrl() + "?text=" + mData.getContacts().get(0).getText());
//                        }
//                    });
//                    dialogA.show();
//                    getEvent("click_contact");
////                if (mData.contacts.size()>0){
////                    Gson gson = new Gson();
////                    List<String> idList;
////                    String idJson=MySettings.getInstance().getStringSetting("idList");
////                    idList=gson.fromJson(idJson,new TypeToken<List<String>>() {}.getType());
////                    if (!idList.contains(mData.contacts.get(0).getId()+"")){
////                        idList.add(mData.contacts.get(0).getId()+"");
////                        MySettings.getInstance().saveSetting("idList",idList);
////                        Map<String, Object> map = new HashMap<>();
////                        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
////
////                        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
////
////                        map.put("action", "click_contact");
////                        map.put("id", mData.contacts.get(0).getId());
////                        String json = gson.toJson(map);
////                        MediaType mediaType = MediaType.parse("application/json");
////                        RequestBody requestBody = RequestBody.create(mediaType, json);
////                        getEvent(requestBody);
////                    }
////
////                }
//
//                } else {
//                    DialogB dialogB = new DialogB(CustomB.this);
//                    dialogB.setTitle("Jobs News");
//                    dialogB.setWen1(CONFIG_INFO.data.tips.confirm.install);
//                    dialogB.setWen2(CONFIG_INFO.data.tips.contents.get(0));
//                    dialogB.setOnclickListener(new DialogB.BnOnclickListener() {
//                        @Override
//                        public void onClick() {
//                            dialogB.dismiss();
//                        }
//                    });
//                    dialogB.show();
//                }
//            }
//        });
//        LinearLayoutManager layoutManager = new LinearLayoutManager(CustomB.this);
//        layoutManager.setOrientation(RecyclerView.VERTICAL);
//        Rv.setLayoutManager(layoutManager);
//        adapterB = new AdapterB(CustomB.this);
//        Rv.setAdapter(adapterB);
//        if (CONFIG_INFO.data.rtl){
//            tips_one.setGravity(Gravity.RIGHT);
//            tips_two.setGravity(Gravity.RIGHT);
//        }
//
//    }
//
//    void initData() {
//        adapterB.upDateDatas(mData.getDescriptions());
//        tittleTxt.setText(mData.getTitle());
//        buttonTxt.setText(CONFIG_INFO.data.actions.apply);
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
//                    public void accept(Throwable throwable) throws Exception {//初始化异常
//                        Toast.makeText(getApplicationContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    public void invokeApp(String url) {
//        try {
//            if (url == null || TextUtils.isEmpty(url)) {
//                return;
//            }
//            String name = "";
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            ResolveInfo packageManager = CustomB.this.getPackageManager().resolveActivity(intent, 0);
//            if (packageManager != null) {
//                name = packageManager.activityInfo.packageName;
//            }
//
//            CustomB.this.startActivity(intent);
//            if (name.equals("com.whatsapp")) {
//                getEvent("addtocart_ws");
//                AdjustEvent adjustEvent = new AdjustEvent(CONFIG_INFO.getData().getReport().getAdjust().getAddtocart_ws().getCode());
//                Adjust.trackEvent(adjustEvent);
//            }
//            if (mData.contacts.size()>0){
//                Gson gson = new Gson();
//                List<String> idList;
//                String idJson=MySettings.getInstance().getStringSetting("idList");
//                idList=gson.fromJson(idJson,new TypeToken<List<String>>() {}.getType());
//                if (!idList.contains(mData.contacts.get(0).getId()+"")){
//                    idList.add(mData.contacts.get(0).getId()+"");
//                    MySettings.getInstance().saveSetting("idList",idList);
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
//
//                    map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
//
//                    map.put("action", "contact_lva");
//                    map.put("id", mData.contacts.get(0).getId());
//                    String json = gson.toJson(map);
//                    MediaType mediaType = MediaType.parse("application/json");
//                    RequestBody requestBody = RequestBody.create(mediaType, json);
//                    getEvent(requestBody);
//                }
//
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
////            return ed.getMessage() == null ? "exception!" : ed.getMessage();
//        }
//    }
//}

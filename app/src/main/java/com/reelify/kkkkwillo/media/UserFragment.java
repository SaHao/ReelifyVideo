package com.reelify.kkkkwillo.media;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.google.gson.Gson;
import com.reelify.kkkkwillo.R;
import com.reelify.kkkkwillo.ac.MainApp;
import com.reelify.kkkkwillo.bean.EventInfo;
import com.reelify.kkkkwillo.net.request.RetrofitManager;
import com.reelify.kkkkwillo.utils.AdIdManager;
import com.reelify.kkkkwillo.utils.MySettings;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class UserFragment extends Fragment {


    public UserFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
//        View statusBarView = rootView.findViewById(R.id.status_bar_view);
//        statusBarView.setBackgroundColor(Color.parseColor("#1aa3b3"));
//        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
//            statusBarView.getLayoutParams().height = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
//            statusBarView.requestLayout();
//            return insets.consumeSystemWindowInsets();
//        });

        TextView cache=rootView.findViewById(R.id.cache);
        TextView name=rootView.findViewById(R.id.Nickname);
        name.setText(MySettings.getInstance().getStringSetting("name"));
        if (MainApp.getCache().getCacheSpace()>2048){
            cache.setText(MainApp.getCache().getCacheSpace()/1024/1024+"MB");
        }else {
            cache.setText("0MB");
        }

        cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainApp.getCache().release();
                cache.setText("0MB");
            }
        });
        postShowUser();
        return rootView;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            getActivity().getWindow().setStatusBarColor(R.color.translate);
        }
    }

    void postShowUser() {
        AdjustEvent adjustEvent = new AdjustEvent("ofsowl");
        Adjust.trackEvent(adjustEvent);
        long time = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("app", "reelify");
        map.put("timestamp", time);
        map.put("userid", time + AdIdManager.RandomNum());
        map.put("language", Locale.getDefault().getLanguage()+"-"+Locale.getDefault().getCountry());
        map.put("version", "1.0");
        map.put("event", "videos_show_profil");
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
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(requireContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
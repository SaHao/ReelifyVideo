package com.reelify.kkkkwillo.media;


import static com.reelify.kkkkwillo.ac.WelcomeAc.START_TIME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reelify.kkkkwillo.Dialog.CommentDialog;
import com.reelify.kkkkwillo.R;
import com.reelify.kkkkwillo.ac.DialogA;
import com.reelify.kkkkwillo.ac.DialogB;
import com.reelify.kkkkwillo.ac.DialogC;
import com.reelify.kkkkwillo.ac.MainApp;
import com.reelify.kkkkwillo.bean.ConfigInfo;
import com.reelify.kkkkwillo.bean.EventInfo;
import com.reelify.kkkkwillo.bean.ListInfo;
import com.reelify.kkkkwillo.net.request.RetrofitManager;
import com.reelify.kkkkwillo.utils.AdIdManager;
import com.reelify.kkkkwillo.utils.MySettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 *
 */
public class VideoFragment extends Fragment implements VideoPagerAdapter.VideoEndListener {
    private ViewPager2 viewPager;
    ListInfo listInfo;
    public static ConfigInfo CONFIG_INFO = new ConfigInfo();
    private VideoPagerAdapter adapter;
    private int currentPlayingPosition = -1;
    private DialogA dialogA;
    private DialogB dialogB;
    private DialogC dialogC;

    public VideoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        View statusBarView = rootView.findViewById(R.id.status_bar_view);
        statusBarView.setBackgroundColor(Color.BLACK);

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            statusBarView.getLayoutParams().height = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
            statusBarView.requestLayout();
            return insets.consumeSystemWindowInsets();
        });
        initView(rootView);
        initData();
        return rootView;
    }

    private void initView(View view) {
        viewPager = view.findViewById(R.id.videoViewPager);
        viewPager.setOffscreenPageLimit(1);
        adapter = new VideoPagerAdapter(requireContext(), VideoFragment.this);
        viewPager.setAdapter(adapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                postShow(listInfo.getData().getVideos().get(position).getId() + "", listInfo.getData().getVideos().get(position).getType());
                playVideoAtPosition(position);
            }
        });
    }

    private void initData() {
        Gson gson = new Gson();
        String config = MySettings.getInstance().getStringSetting("configInfo");
        CONFIG_INFO = gson.fromJson(config, ConfigInfo.class);

        String info = MySettings.getInstance().getStringSetting("listInfo");
        listInfo = gson.fromJson(info, ListInfo.class);
        adapter.updateData(listInfo.getData());

    }

    private void restartCurrentVideo(int position) {
        RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);
        RecyclerView.ViewHolder currentViewHolder = recyclerView.findViewHolderForAdapterPosition(position);

        if (currentViewHolder instanceof VideoPagerAdapter.VideoViewHolder) {
            VideoPagerAdapter.VideoViewHolder videoViewHolder = (VideoPagerAdapter.VideoViewHolder) currentViewHolder;

            if (videoViewHolder.exoPlayer != null) {
                // 重置视频到开始位置并重新播放
                videoViewHolder.exoPlayer.seekTo(0);
                videoViewHolder.exoPlayer.setPlayWhenReady(true);
                videoViewHolder.exoPlayer.prepare();
            }
        }
    }

    private void playVideoAtPosition(int position) {
        // 停止当前播放的视频
        if (currentPlayingPosition != -1) {
            RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);
            RecyclerView.ViewHolder oldViewHolder = recyclerView.findViewHolderForAdapterPosition(currentPlayingPosition);
            if (oldViewHolder instanceof VideoPagerAdapter.VideoViewHolder) {
                VideoPagerAdapter.VideoViewHolder oldVideoViewHolder = (VideoPagerAdapter.VideoViewHolder) oldViewHolder;
                if (oldVideoViewHolder.exoPlayer != null) {
                    oldVideoViewHolder.exoPlayer.pause();
                }
            }
        }

        // 播放新页面的视频
        RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);
        RecyclerView.ViewHolder newViewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        if (newViewHolder instanceof VideoPagerAdapter.VideoViewHolder) {
            VideoPagerAdapter.VideoViewHolder newVideoViewHolder = (VideoPagerAdapter.VideoViewHolder) newViewHolder;
            if (newVideoViewHolder.exoPlayer != null) {
//                newVideoViewHolder.exoPlayer.setPlayWhenReady(true);
                newVideoViewHolder.exoPlayer.seekTo(0);
                newVideoViewHolder.exoPlayer.setPlayWhenReady(true);
                newVideoViewHolder.exoPlayer.prepare();
            }
        }

        currentPlayingPosition = position;
    }

    @Override
    public void onVideoEnd(int position) {
        if (CONFIG_INFO.data.end.ends && listInfo.getData().getVideos().get(position).getType().equalsIgnoreCase("C")) {
            postVideEnd(listInfo.getData().getVideos().get(position).getId() + "");
            if (dialogA != null) {
                dialogA.dismiss();
            }
            if (dialogB != null) {
                dialogB.dismiss();
            }

            dialogC = new DialogC(requireContext());
            dialogC.setWen1(CONFIG_INFO.data.action.applyC);
            dialogC.setWen4(CONFIG_INFO.data.end.title);
            dialogC.setOnclickListener(new DialogC.CnOnclickListener() {
                @Override
                public void onClick() {
                    dialogC.dismiss();
                    postVideEndButton(listInfo.getData().getVideos().get(position).getId() + "");
                    if (CONFIG_INFO.getData().getConfirm().isEmpty()) {
                        if (listInfo.getData().getVideos().get(position).getContacts().getType().contains("tg")) {
                            invokeApp(position, listInfo.getData().getVideos().get(position).getContacts().getUrl(), listInfo.getData().getVideos().get(position).getContacts().getType());
                        } else {
                            invokeApp(position, listInfo.getData().getVideos().get(position).getContacts().getUrl() + "?text=" + listInfo.getData().getVideos().get(position).getContacts().getText(), listInfo.getData().getVideos().get(position).getContacts().getType());
                        }
                    } else {
                        showDialogA(position);
                    }
                }

                @Override
                public void replay() {
                    dialogC.dismiss();
                    restartCurrentVideo(position);
                }
            });
            dialogC.show();
            WindowManager.LayoutParams params = dialogC.getWindow().getAttributes();
            params.gravity = Gravity.CENTER;
            params.y = -100;
            dialogC.getWindow().setAttributes(params);

        } else {
            restartCurrentVideo(position);
        }

    }

    @Override
    public void showComment(int position, TextView view) {
        CommentDialog commentDialog = new CommentDialog(requireContext(), listInfo.getData().getVideos().get(position).getComment(), position);
        commentDialog.setOnclickListener(new CommentDialog.clickListener() {
            @Override
            public void onClick() {
                Gson gson = new Gson();
                String info = MySettings.getInstance().getStringSetting("listInfo");
                ListInfo listInfo1 = gson.fromJson(info, ListInfo.class);
                view.setText(listInfo1.getData().getVideos().get(position).getComment().getComments().size() + "");
            }
        });
        commentDialog.show();
        postShowComment(listInfo.getData().getVideos().get(position).getId() + "");
    }

    @Override
    public void showService(int position) {
        postService(listInfo.getData().getVideos().get(position).getId() + "");
        if (CONFIG_INFO.getData().getConfirm().isEmpty()) {
            if (listInfo.getData().getVideos().get(position).getContacts().getType().contains("tg")) {
                invokeApp(position, listInfo.getData().getVideos().get(position).getContacts().getUrl(), listInfo.getData().getVideos().get(position).getContacts().getType());
            } else {
                invokeApp(position, listInfo.getData().getVideos().get(position).getContacts().getUrl() + "?text=" + listInfo.getData().getVideos().get(position).getContacts().getText(), listInfo.getData().getVideos().get(position).getContacts().getType());
            }
        } else {
            showDialogA(position);
        }

    }

    @Override
    public void firstShow(int position) {
        postFirstC(listInfo.getData().getVideos().get(position).getId() + "");
    }

    @Override
    public void firstShowC(int position) {
        postFirst(listInfo.getData().getVideos().get(position).getId() + "");
    }

    @Override
    public void clickButtonC(int position) {
        postButtonC(listInfo.getData().getVideos().get(position).getId() + "");
        if (CONFIG_INFO.getData().getConfirm().isEmpty()) {
            if (listInfo.getData().getVideos().get(position).getContacts().getType().contains("tg")) {
                invokeApp(position, listInfo.getData().getVideos().get(position).getContacts().getUrl(), listInfo.getData().getVideos().get(position).getContacts().getType());
            } else {
                invokeApp(position, listInfo.getData().getVideos().get(position).getContacts().getUrl() + "?text=" + listInfo.getData().getVideos().get(position).getContacts().getText(), listInfo.getData().getVideos().get(position).getContacts().getType());
            }
        } else {
            showDialogA(position);
        }
    }

    public void showDialogA(int position) {
        if (dialogC != null) {
            dialogC.dismiss();
        }
        if (dialogB != null) {
            dialogB.dismiss();
        }
        dialogA = new DialogA(requireContext());
        dialogA.setWen1(CONFIG_INFO.data.action.apply);
        dialogA.setWen2(CONFIG_INFO.data.contacts.contents.get(0));
        dialogA.setWen3(CONFIG_INFO.data.contacts.contents.get(1));
        dialogA.setWen4(CONFIG_INFO.data.contacts.contents.get(2));
        dialogA.setOnclickListener(new DialogA.AnOnclickListener() {
            @Override
            public void onClick() {
                dialogA.dismiss();
                postPopOneButton(listInfo.getData().getVideos().get(position).getId() + "");
                if (listInfo.getData().getVideos().get(position).getContacts().getType().contains("tg")) {
                    invokeApp(position, listInfo.getData().getVideos().get(position).getContacts().getUrl(), listInfo.getData().getVideos().get(position).getContacts().getType());
                } else {
                    invokeApp(position, listInfo.getData().getVideos().get(position).getContacts().getUrl() + "?text=" + listInfo.getData().getVideos().get(position).getContacts().getText(), listInfo.getData().getVideos().get(position).getContacts().getType());
                }
            }
        });
        dialogA.show();
        postPopOne(listInfo.getData().getVideos().get(position).getId() + "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (currentPlayingPosition != -1) {
            RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);
            MainApp.RV = recyclerView;
            RecyclerView.ViewHolder currentViewHolder = recyclerView.findViewHolderForAdapterPosition(currentPlayingPosition);
            if (currentViewHolder instanceof VideoPagerAdapter.VideoViewHolder) {
                VideoPagerAdapter.VideoViewHolder currentVideoViewHolder = (VideoPagerAdapter.VideoViewHolder) currentViewHolder;
                if (currentVideoViewHolder.exoPlayer != null) {
                    currentVideoViewHolder.exoPlayer.release();
                }
            }
        }
    }

    public boolean isAppInstalled(String packageName) {
        PackageManager packageManager = requireContext().getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    public void invokeApp(int position, String url, String type) {
        Gson gson = new Gson();
        List<String> idList = new ArrayList<>();
        String idJson = MySettings.getInstance().getStringSetting("idList");
        idList = gson.fromJson(idJson, new TypeToken<List<String>>() {
        }.getType());
        if (MySettings.getInstance().getStringSetting("Single").isEmpty()) {
            postInvokeBeginSingle(listInfo.getData().getVideos().get(position).getId() + "");
            MySettings.getInstance().saveSetting("Single", "over");
        }
        postInvokeBegin(listInfo.getData().getVideos().get(position).getId() + "");
        try {
            if (url == null || TextUtils.isEmpty(url)) {
                return;
            }
            if (type.contains("ws")) {
                if (isAppInstalled("com.whatsapp")) {
                    Intent intentws = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    requireContext().startActivity(intentws);
                    postInvokeAll(listInfo.getData().getVideos().get(position).getId() + "");
                    postInvokeWs(listInfo.getData().getVideos().get(position).getId() + "");
                    if (idList != null && !idList.contains(listInfo.getData().getVideos().get(position).getId() + "")) {
                        idList.add(listInfo.getData().getVideos().get(position).getId() + "");
                        MySettings.getInstance().saveSetting("idList", idList);
                        postInvokeSuccess(listInfo.getData().getVideos().get(position).getId() + "", url, listInfo.getData().getVideos().get(position).getContacts().getId());
                    }
                } else {
                    if (dialogC != null) {
                        dialogC.dismiss();
                    }
                    if (dialogA != null) {
                        dialogA.dismiss();
                    }
                    dialogB = new DialogB(requireContext());
                    dialogB.setTitle("Jobs News");
                    dialogB.setWen1(CONFIG_INFO.data.action.download);
                    dialogB.setWen2(CONFIG_INFO.data.contacts.guide.get(0));
                    dialogB.setWen3(CONFIG_INFO.data.contacts.guide.get(1));
                    dialogB.setWen4(CONFIG_INFO.data.contacts.guide.get(2));
                    dialogB.setOnclickListener(new DialogB.BnOnclickListener() {
                        @Override
                        public void onClick() {
                            postPopTwoButton(listInfo.getData().getVideos().get(position).getId() + "");
                            dialogB.dismiss();
                            if (type.contains("ws")) {
                                downloadApp(CONFIG_INFO.data.link.wsDownload);
                            } else {
                                downloadApp(CONFIG_INFO.data.link.tgDownload);
                            }
                        }
                    });
                    dialogB.show();
                    postPopTwo(listInfo.getData().getVideos().get(position).getId() + "");
                }

            } else if (type.contains("tg")) {
                if (isAppInstalled("org.telegram.messenger") || isAppInstalled("org.telegram.messenger.web")) {
                    Intent intentws = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    requireContext().startActivity(intentws);
                    postInvokeTg(listInfo.getData().getVideos().get(position).getId() + "");
                    postInvokeAll(listInfo.getData().getVideos().get(position).getId() + "");
                    if (idList != null && !idList.contains(listInfo.getData().getVideos().get(position).getId() + "")) {
                        idList.add(listInfo.getData().getVideos().get(position).getId() + "");
                        MySettings.getInstance().saveSetting("idList", idList);
                        postInvokeSuccess(listInfo.getData().getVideos().get(position).getId() + "", url, listInfo.getData().getVideos().get(position).getContacts().getId());
                    }
                } else {
                    if (dialogC != null) {
                        dialogC.dismiss();
                    }
                    if (dialogA != null) {
                        dialogA.dismiss();
                    }
                    dialogB = new DialogB(requireContext());
                    dialogB.setTitle("Jobs News");
                    dialogB.setWen1(CONFIG_INFO.data.action.download);
                    dialogB.setWen2(CONFIG_INFO.data.contacts.guide.get(0));
                    dialogB.setWen3(CONFIG_INFO.data.contacts.guide.get(1));
                    dialogB.setWen4(CONFIG_INFO.data.contacts.guide.get(2));
                    dialogB.setOnclickListener(new DialogB.BnOnclickListener() {
                        @Override
                        public void onClick() {
                            postPopTwoButton(listInfo.getData().getVideos().get(position).getId() + "");
                            dialogB.dismiss();
                            if (type.contains("ws")) {
                                downloadApp(CONFIG_INFO.data.link.wsDownload);
                            } else {
                                downloadApp(CONFIG_INFO.data.link.tgDownload);
                            }
                        }
                    });
                    dialogB.show();
                    postPopTwo(listInfo.getData().getVideos().get(position).getId() + "");

                }

            } else {
                Intent intentws = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                requireContext().startActivity(intentws);
                postInvokeAll(listInfo.getData().getVideos().get(position).getId() + "");
            }
        } catch (Exception ed) {
            ed.printStackTrace();
        }
    }

    public void downloadApp(String url) {
        try {
            if (url == null || TextUtils.isEmpty(url)) {
                return;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            requireContext().startActivity(intent);
        } catch (Exception ed) {
            ed.printStackTrace();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (currentPlayingPosition != -1) {
            RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);
            MainApp.RV = recyclerView;
            RecyclerView.ViewHolder currentViewHolder = recyclerView.findViewHolderForAdapterPosition(currentPlayingPosition);
            if (currentViewHolder instanceof VideoPagerAdapter.VideoViewHolder) {
                VideoPagerAdapter.VideoViewHolder currentVideoViewHolder = (VideoPagerAdapter.VideoViewHolder) currentViewHolder;
                if (currentVideoViewHolder.exoPlayer != null) {
                    currentVideoViewHolder.exoPlayer.pause();
                }
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (currentPlayingPosition != -1) {
            RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);
            MainApp.RV = recyclerView;
            RecyclerView.ViewHolder currentViewHolder = recyclerView.findViewHolderForAdapterPosition(currentPlayingPosition);
            if (currentViewHolder instanceof VideoPagerAdapter.VideoViewHolder) {
                VideoPagerAdapter.VideoViewHolder currentVideoViewHolder = (VideoPagerAdapter.VideoViewHolder) currentViewHolder;
                if (currentVideoViewHolder.exoPlayer != null) {
                    if (hidden) {
                        currentVideoViewHolder.exoPlayer.pause();
                    } else {
                        currentVideoViewHolder.exoPlayer.play();
                    }

                }
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            getActivity().getWindow().setStatusBarColor(R.color.translate);
        }
        if (currentPlayingPosition != -1) {
            RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);
            MainApp.RV = recyclerView;
            RecyclerView.ViewHolder currentViewHolder = recyclerView.findViewHolderForAdapterPosition(currentPlayingPosition);
            if (currentViewHolder instanceof VideoPagerAdapter.VideoViewHolder) {
                VideoPagerAdapter.VideoViewHolder currentVideoViewHolder = (VideoPagerAdapter.VideoViewHolder) currentViewHolder;
                if (currentVideoViewHolder.exoPlayer != null) {
                    currentVideoViewHolder.exoPlayer.play();
                }
            }
        }
    }

    void postFirst(String videoId) {
        AdjustEvent adjustEvent = new AdjustEvent("8uek0n");
        Adjust.trackEvent(adjustEvent);
        long time = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("app", "reelify");
        map.put("timestamp", time);
        map.put("userid", MySettings.getInstance().getStringSetting("userId"));
        map.put("language", Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());
        map.put("version", "1.2.0");
        map.put("event", "videos_show_list");
        map.put("session", MySettings.getInstance().getIntSetting("session"));
        map.put("ext1", videoId);
        map.put("ext2", "");
        map.put("ext3", time - START_TIME);
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

    void postFirstC(String videoId) {
        AdjustEvent adjustEvent = new AdjustEvent("lj2vi5");
        Adjust.trackEvent(adjustEvent);
        long time = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("app", "reelify");
        map.put("timestamp", time);
        map.put("userid", MySettings.getInstance().getStringSetting("userId"));
        map.put("language", Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());
        map.put("version", "1.2.0");
        map.put("event", "videos_show_clist");
        map.put("session", MySettings.getInstance().getIntSetting("session"));
        map.put("ext1", videoId);
        map.put("ext2", "");
        map.put("ext3", time - START_TIME);
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

    void postShow(String videoId, String type) {
        AdjustEvent adjustEvent = new AdjustEvent("lifv6u");
        Adjust.trackEvent(adjustEvent);
        long time = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("app", "reelify");
        map.put("timestamp", time);
        map.put("userid", MySettings.getInstance().getStringSetting("userId"));
        map.put("language", Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());
        map.put("version", "1.2.0");
        map.put("event", "video_show_card");
        map.put("session", MySettings.getInstance().getIntSetting("session"));
        map.put("ext1", videoId);
        map.put("ext2", type);
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

    void postButtonC(String videoId) {
        AdjustEvent adjustEvent = new AdjustEvent("wae9hr");
        Adjust.trackEvent(adjustEvent);
        long time = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("app", "reelify");
        map.put("timestamp", time);
        map.put("userid", MySettings.getInstance().getStringSetting("userId"));
        map.put("language", Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());
        map.put("version", "1.2.0");
        map.put("event", "click_apply_start");
        map.put("session", MySettings.getInstance().getIntSetting("session"));
        map.put("ext1", videoId);
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

    void postVideEnd(String videoId) {
        AdjustEvent adjustEvent = new AdjustEvent("a6ja2p");
        Adjust.trackEvent(adjustEvent);
        long time = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("app", "reelify");
        map.put("timestamp", time);
        map.put("userid", MySettings.getInstance().getStringSetting("userId"));
        map.put("language", Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());
        map.put("version", "1.2.0");
        map.put("event", "end_show_apply");
        map.put("session", MySettings.getInstance().getIntSetting("session"));
        map.put("ext1", videoId);
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

    void postVideEndButton(String videoId) {
        AdjustEvent adjustEvent = new AdjustEvent("cg3uo2");
        Adjust.trackEvent(adjustEvent);
        long time = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("app", "reelify");
        map.put("timestamp", time);
        map.put("userid", MySettings.getInstance().getStringSetting("userId"));
        map.put("language", Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());
        map.put("version", "1.2.0");
        map.put("event", "click_apply_end");
        map.put("session", MySettings.getInstance().getIntSetting("session"));
        map.put("ext1", videoId);
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

    void postService(String videoId) {
        AdjustEvent adjustEvent = new AdjustEvent("u07u9q");
        Adjust.trackEvent(adjustEvent);
        long time = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("app", "reelify");
        map.put("timestamp", time);
        map.put("userid", MySettings.getInstance().getStringSetting("userId"));
        map.put("language", Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());
        map.put("version", "1.2.0");
        map.put("event", "click_apply_icon");
        map.put("session", MySettings.getInstance().getIntSetting("session"));
        map.put("ext1", videoId);
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

    void postPopOne(String videoId) {
        AdjustEvent adjustEvent = new AdjustEvent("n2hovh");
        Adjust.trackEvent(adjustEvent);
        long time = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("app", "reelify");
        map.put("timestamp", time);
        map.put("userid", MySettings.getInstance().getStringSetting("userId"));
        map.put("language", Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());
        map.put("version", "1.2.0");
        map.put("event", "contact_show_popup");
        map.put("session", MySettings.getInstance().getIntSetting("session"));
        map.put("ext1", videoId);
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

    void postPopOneButton(String videoId) {
        AdjustEvent adjustEvent = new AdjustEvent("o4c89x");
        Adjust.trackEvent(adjustEvent);
        long time = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("app", "reelify");
        map.put("timestamp", time);
        map.put("userid", MySettings.getInstance().getStringSetting("userId"));
        map.put("language", Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());
        map.put("version", "1.2.0");
        map.put("event", "click_contact_btn");
        map.put("session", MySettings.getInstance().getIntSetting("session"));
        map.put("ext1", videoId);
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

    void postPopTwo(String videoId) {
        AdjustEvent adjustEvent = new AdjustEvent("s6vh9i");
        Adjust.trackEvent(adjustEvent);
        long time = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("app", "reelify");
        map.put("timestamp", time);
        map.put("userid", MySettings.getInstance().getStringSetting("userId"));
        map.put("language", Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());
        map.put("version", "1.2.0");
        map.put("event", "download_tip_show");
        map.put("session", MySettings.getInstance().getIntSetting("session"));
        map.put("ext1", videoId);
        map.put("ext2", CONFIG_INFO.getData().getConfirm());
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

    void postPopTwoButton(String videoId) {
        AdjustEvent adjustEvent = new AdjustEvent("85231m");
        Adjust.trackEvent(adjustEvent);
        long time = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("app", "reelify");
        map.put("timestamp", time);
        map.put("userid", MySettings.getInstance().getStringSetting("userId"));
        map.put("language", Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());
        map.put("version", "1.2.0");
        map.put("event", "click_download-btn");
        map.put("session", MySettings.getInstance().getIntSetting("session"));
        map.put("ext1", videoId);
        map.put("ext2", CONFIG_INFO.getData().getConfirm());
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

    void postInvokeBegin(String videoId) {
        AdjustEvent adjustEvent = new AdjustEvent("2riy4s");
        Adjust.trackEvent(adjustEvent);
        long time = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("app", "reelify");
        map.put("timestamp", time);
        map.put("userid", MySettings.getInstance().getStringSetting("userId"));
        map.put("language", Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());
        map.put("version", "1.2.0");
        map.put("event", "addtocartpv");
        map.put("session", MySettings.getInstance().getIntSetting("session"));
        map.put("ext1", videoId);
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

    void postInvokeBeginSingle(String videoId) {
        AdjustEvent adjustEvent = new AdjustEvent("i4psvr");
        Adjust.trackEvent(adjustEvent);
        long time = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("app", "reelify");
        map.put("timestamp", time);
        map.put("userid", MySettings.getInstance().getStringSetting("userId"));
        map.put("language", Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());
        map.put("version", "1.2.0");
        map.put("event", "addtocartlt");
        map.put("session", MySettings.getInstance().getIntSetting("session"));
        map.put("ext1", videoId);
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

    void postShowComment(String videoId) {
        AdjustEvent adjustEvent = new AdjustEvent("rsdlj6");
        Adjust.trackEvent(adjustEvent);
        long time = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("app", "reelify");
        map.put("timestamp", time);
        map.put("userid", MySettings.getInstance().getStringSetting("userId"));
        map.put("language", Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());
        map.put("version", "1.2.0");
        map.put("event", "videos_show_comments");
        map.put("session", MySettings.getInstance().getIntSetting("session"));
        map.put("ext1", videoId);
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

    void postInvokeWs(String videoId) {
        AdjustEvent adjustEvent = new AdjustEvent("fou9bz");
        Adjust.trackEvent(adjustEvent);
        long time = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("app", "reelify");
        map.put("timestamp", time);
        map.put("userid", MySettings.getInstance().getStringSetting("userId"));
        map.put("language", Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());
        map.put("version", "1.2.0");
        map.put("event", "addtocart_ws");
        map.put("session", MySettings.getInstance().getIntSetting("session"));
        map.put("ext1", videoId);
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

    void postInvokeTg(String videoId) {
        AdjustEvent adjustEvent = new AdjustEvent("diylr4");
        Adjust.trackEvent(adjustEvent);
        long time = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("app", "reelify");
        map.put("timestamp", time);
        map.put("userid", MySettings.getInstance().getStringSetting("userId"));
        map.put("language", Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());
        map.put("version", "1.2.0");
        map.put("event", "addtocart_tg");
        map.put("session", MySettings.getInstance().getIntSetting("session"));
        map.put("ext1", videoId);
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

    void postInvokeAll(String videoId) {
        AdjustEvent adjustEvent = new AdjustEvent("918wkk");
        Adjust.trackEvent(adjustEvent);
        long time = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("app", "reelify");
        map.put("timestamp", time);
        map.put("userid", MySettings.getInstance().getStringSetting("userId"));
        map.put("language", Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());
        map.put("version", "1.2.0");
        map.put("event", "addtocart_ok");
        map.put("session", MySettings.getInstance().getIntSetting("session"));
        map.put("ext1", videoId);
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

    void postInvokeSuccess(String videoId, String url, long id) {
        AdjustEvent adjustEvent = new AdjustEvent("918wkk");
        Adjust.trackEvent(adjustEvent);
        long time = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("gaid", MySettings.getInstance().getStringSetting("gaid"));
        map.put("attributes", MySettings.getInstance().getStringSetting("attribution"));
        map.put("app", "reelify");
        map.put("timestamp", time);
        map.put("id", id);
        map.put("userid", MySettings.getInstance().getStringSetting("userId"));
        map.put("language", Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());
        map.put("version", "1.2.0");
        map.put("event", "contact_lva");
        map.put("session", MySettings.getInstance().getIntSetting("session"));
        map.put("ext1", videoId);
        map.put("ext2", url);
        map.put("ext3", time - START_TIME);
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
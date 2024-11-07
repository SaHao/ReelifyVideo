package com.reelify.kkkkwillo.media;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.gson.Gson;
import com.reelify.kkkkwillo.R;
import com.reelify.kkkkwillo.bean.ConfigInfo;
import com.reelify.kkkkwillo.bean.ListInfo;
import com.reelify.kkkkwillo.utils.MySettings;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class MainActivity extends AppCompatActivity {
    private LinearLayout toVideoFragment;
    private LinearLayout toUserFragment;
    ImageView videoImg;
    ImageView userImg;
    private boolean isVideo;
    VideoFragment videoFragment;
    UserFragment userFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN//状态栏不会被隐藏但activity布局会扩展到状态栏所在位置
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        initView();
    }

    private void initView() {
        toVideoFragment = findViewById(R.id.video_layout);
        toUserFragment = findViewById(R.id.user_layout);
        videoImg = findViewById(R.id.video_img);
        userImg = findViewById(R.id.user_img);
        userFragment=new UserFragment();
        videoFragment=new VideoFragment();
        // 默认加载 FragmentA
        loadFragment(videoFragment);
        isVideo = true;
        // 设置按钮点击监听器来切换 Fragment
        toVideoFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isVideo) {
                    loadFragment(userFragment, videoFragment);
                    videoImg.setImageResource(R.drawable.btn_home_on);
                    userImg.setImageResource(R.drawable.btn_user_off);
                    isVideo = true;
                }
            }
        });

        toUserFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVideo) {
                    loadFragment(videoFragment,userFragment);
                    videoImg.setImageResource(R.drawable.btn_home_off);
                    userImg.setImageResource(R.drawable.btn_user_on);
                    isVideo = false;
                }
            }
        });
    }

    private void loadFragment(Fragment from) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        transaction.add(R.id.fragment_container, from);
        transaction.commit();
    }

    private void loadFragment(Fragment from, Fragment to) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        if (!to.isAdded()) {
            transaction.hide(from).add(R.id.fragment_container, to).commit();
        } else {
            transaction.hide(from).show(to).commit();
        }
    }
}

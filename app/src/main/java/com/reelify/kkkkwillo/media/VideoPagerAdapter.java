package com.reelify.kkkkwillo.media;

import static com.reelify.kkkkwillo.media.VideoFragment.CONFIG_INFO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.reelify.kkkkwillo.R;
import com.reelify.kkkkwillo.ac.MainApp;
import com.reelify.kkkkwillo.bean.ListInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class VideoPagerAdapter extends RecyclerView.Adapter<VideoPagerAdapter.VideoViewHolder> {

    private List<ListInfo.Data> dataList = new ArrayList<>();
    private Context context;
    private VideoEndListener videoEndListener;
    private boolean isLike;
    private long downTime;
    private boolean firstShow;
    private boolean firstShowC;
    private  ImageAdapter viewPagerAdapter;
    public VideoPagerAdapter(Context context, VideoEndListener videoEndListener) {
        this.context = context;
        this.videoEndListener = videoEndListener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.chatNum.setText(dataList.get(position).getComment().getComments().size() + "");
            holder.name.setText(dataList.get(position).getName());
            holder.dis.setText(dataList.get(position).getDesc());
            holder.likeNum.setText(dataList.get(position).getFavorite());
            holder.typeC.setText(CONFIG_INFO.getData().getAction().getApplyC());
            if (dataList.get(position).getType().equalsIgnoreCase("C")) {
                if (dataList.get(position).getContacts().getType().contains("ws")) {
                    holder.kefu.setImageResource(R.drawable.btn_whatsapp_off);
                } else if (dataList.get(position).getContacts().getType().contains("tg")) {
                    holder.kefu.setImageResource(R.drawable.btn_telegarm_off);
                } else {
                    holder.kefu.setImageResource(R.drawable.btn_link_off);
                }
                holder.typeC.setVisibility(View.VISIBLE);
                holder.kefu.setVisibility(View.VISIBLE);
            } else {
                holder.typeC.setVisibility(View.GONE);
                holder.kefu.setVisibility(View.GONE);
            }

            if (dataList.get(position).ads) {
                holder.ads.setVisibility(View.VISIBLE);
            } else {
                holder.ads.setVisibility(View.GONE);
            }
            Glide.with(context)
                    .load(dataList.get(position).getAvatar())
                    .placeholder(R.drawable.btn_user_off)
                    .error(R.drawable.icon_avatar)
                    .into(holder.avatar);
            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLike) {
                        holder.like.setImageResource(R.drawable.btn_heart_off);
                        isLike = false;
                    } else {
                        holder.like.setImageResource(R.drawable.btn_heart_down);
                        isLike = true;
                    }
                }
            });
            holder.chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoEndListener.showComment(position, holder.chatNum);
                }
            });
            holder.kefu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoEndListener.showService(position);
                }
            });
            holder.typeC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoEndListener.clickButtonC(position);
                }
            });
            if (!firstShow){
                videoEndListener.firstShow(position);
                firstShow=true;
            }
            if (!firstShowC&&dataList.get(position).getType().equalsIgnoreCase("C")){
                videoEndListener.firstShowC(position);
                firstShowC=true;
            }
            if (dataList.get(position).getSources().get(0).getType().contains("mp4")) {
                String videoUrl = dataList.get(position).getSources().get(0).getSrc();
                holder.recyclerView.setVisibility(View.GONE);
                holder.playerView.setVisibility(View.VISIBLE);
                holder.timeBar.setVisibility(View.VISIBLE);

                ExoPlayer exoPlayer = new ExoPlayer.Builder(context).build();
                holder.playerView.setPlayer(exoPlayer);
                DefaultDataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(context);
                CacheDataSource.Factory cacheDataSourceFactory = new CacheDataSource.Factory()
                        .setCache(MainApp.getCache())  // 使用全局缓存实例
                        .setUpstreamDataSourceFactory(dataSourceFactory)
                        .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
                MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoUrl));
                exoPlayer.setMediaItem(mediaItem);
                exoPlayer.setMediaSource(new ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(mediaItem));
                exoPlayer.prepare();
                exoPlayer.setRepeatMode(Player.REPEAT_MODE_OFF);
                exoPlayer.addListener(new Player.Listener() {
                    @Override
                    public void onPlaybackStateChanged(int playbackState) {

                        if (playbackState == Player.STATE_ENDED) {
                            videoEndListener.onVideoEnd(position);
                        }
                        if (playbackState == ExoPlayer.STATE_READY) {
                            holder.timeBar.setMax((int) exoPlayer.getDuration());
                            holder.updateSeekBar();
                        }
                    }
                });
                holder.playerView.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            downTime = System.currentTimeMillis();
                            return true;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            long upTime = System.currentTimeMillis();
                            if (upTime - downTime < 200) {
                                if (exoPlayer.isPlaying()) {
                                    exoPlayer.pause();
//                                holder.pause.setVisibility(View.VISIBLE);
                                } else {
                                    exoPlayer.play();
                                    holder.pause.setVisibility(View.GONE);
                                }
                            }

                        }
                        return false;
                    }
                });
                holder.timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            exoPlayer.seekTo(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        exoPlayer.pause();
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        exoPlayer.setPlayWhenReady(true);
                    }
                });
                holder.exoPlayer = exoPlayer;
            } else {
                holder.recyclerView.setVisibility(View.VISIBLE);
                holder.playerView.setVisibility(View.GONE);
                holder.timeBar.setVisibility(View.GONE);
                viewPagerAdapter = new ImageAdapter(context, dataList.get(position).getSources());
                holder.recyclerView.setAdapter(viewPagerAdapter);
                holder.recyclerView.setOffscreenPageLimit(3);
                final int scrollDelay = 3000;
                holder.recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (holder.recyclerView.getCurrentItem() == holder.recyclerView.getAdapter().getItemCount() - 1) {
                            holder.recyclerView.setCurrentItem(0, false);
                        } else {
                            holder.recyclerView.setCurrentItem(holder.recyclerView.getCurrentItem() + 1, true);
                        }
                        holder.recyclerView.postDelayed(this, scrollDelay);
                    }
                });
//                if (dataList.get(position).getPicbgm()!=null&&!dataList.get(position).getPicbgm().isEmpty()){
//                    viewPagerAdapter.playAudio(dataList.get(position).getPicbgm());
//                }
            }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
//        return dataList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onViewRecycled(@NonNull VideoViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder.exoPlayer != null) {
            holder.exoPlayer.release();  // 释放资源
            holder.exoPlayer = null;
        }
//        if (viewPagerAdapter != null) {
//            viewPagerAdapter.release();  // 释放资源
//            viewPagerAdapter= null;
//        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(ListInfo.MainData data) {
        dataList.clear();
        if (data != null) {
            dataList.addAll(data.getVideos());
        }
        notifyDataSetChanged();
    }

    public  static class VideoViewHolder extends RecyclerView.ViewHolder {
        StyledPlayerView playerView;
        SeekBar timeBar;
        ExoPlayer exoPlayer;
        ImageView like;
        ImageView avatar;
        ImageView chat;
        ImageView renzheng;
        ImageView pause;
        ImageView ads;
        ImageView kefu;
        TextView name;
        TextView dis;
        TextView likeNum;
        TextView chatNum;
        TextView typeC;
        ViewPager2 recyclerView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.exoPlayerView);
            like = itemView.findViewById(R.id.like_img);
            avatar = itemView.findViewById(R.id.avatar_img);
            chat = itemView.findViewById(R.id.chat_img);
            renzheng = itemView.findViewById(R.id.correct_img);
            ads = itemView.findViewById(R.id.ads);
            kefu = itemView.findViewById(R.id.kefu_img);
            name = itemView.findViewById(R.id.mName);
            dis = itemView.findViewById(R.id.mTitle);
            likeNum = itemView.findViewById(R.id.like_num);
            chatNum = itemView.findViewById(R.id.chat_num);
            typeC = itemView.findViewById(R.id.buttonC);
            timeBar = itemView.findViewById(R.id.time_bar);
            recyclerView = itemView.findViewById(R.id.myRcv);
            pause = itemView.findViewById(R.id.pause_img);
            if (CONFIG_INFO.data.rtl){
                dis.setGravity(Gravity.RIGHT);
                dis.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        private void updateSeekBar() {
            if (exoPlayer != null) {
                timeBar.setProgress((int) exoPlayer.getCurrentPosition());
                timeBar.postDelayed(this::updateSeekBar, 1000);
            }
        }
    }

    // 定义视频结束的回调接口
    public interface VideoEndListener {
        void onVideoEnd(int position);

        void showComment(int position, TextView view);

        void showService(int position);

        void firstShow(int position);

        void firstShowC(int position);

        void clickButtonC(int position);

    }
}

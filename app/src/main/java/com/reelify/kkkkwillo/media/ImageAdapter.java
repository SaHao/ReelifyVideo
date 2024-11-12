package com.reelify.kkkkwillo.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.reelify.kkkkwillo.R;
import com.reelify.kkkkwillo.bean.ListInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<ListInfo.Sources> data = new ArrayList<>();
    private Context context;

    private MediaPlayer mediaPlayer;

    public ImageAdapter(Context context, List<ListInfo.Sources> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Glide.with(context)
                .load(data.get(position).getSrc())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }

    public void playAudio(String audioUrl) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp->{
                mediaPlayer.setLooping(true); // 启用循环播放
                mediaPlayer.start(); // 准备完成后自动开始播放
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

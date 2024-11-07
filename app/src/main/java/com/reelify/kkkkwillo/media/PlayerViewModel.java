package com.reelify.kkkkwillo.media;

import androidx.lifecycle.ViewModel;

import com.google.android.exoplayer2.ExoPlayer;

public class PlayerViewModel extends ViewModel {
    private ExoPlayer exoPlayer;

    public ExoPlayer getExoPlayer() {
        return exoPlayer;
    }

    public void setExoPlayer(ExoPlayer exoPlayer) {
        this.exoPlayer = exoPlayer;
    }
}

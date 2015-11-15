package com.example.judith.myplugin;

/**
 * Created by Judith on 15/11/2015.
 */

import android.media.MediaPlayer;

import android.media.MediaPlayer;

/**
 * Created by Judith on 15/11/2015.
 */
public class CustomMediaPlayer extends MediaPlayer {

    private PlayPauseListener1 mListener1;

    public void setPlayPauseListener(PlayPauseListener1 listener) {
        mListener1 = listener;
    }

    @Override
    public void start() throws IllegalStateException {
        super.start();
        if (mListener1 != null) {
            mListener1.onPlay();
        }
    }

    @Override
    public void pause() throws IllegalStateException {
        super.pause();
        if (mListener1 != null) {
            mListener1.onPause();
        }
    }

    public static interface PlayPauseListener1 {
        void onPlay();
        void onPause();
    }

}


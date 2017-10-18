package com.rain.ximalaya.api;

import android.content.Context;
import android.util.Log;

import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HwanJ.Choi on 2017-10-11.
 */

public class PlayService implements IXmPlayerStatusListener {

    private final static String TAG = "PlayService";

    private XmPlayerManager mPlayer;
    private List<Track> mTracks = new ArrayList<>();

    private volatile static PlayService sPlayService;

    private PlayService(Context context) {
        mPlayer = XmPlayerManager.getInstance(context);
        mPlayer.init();
        mPlayer.addPlayerStatusListener(this);
    }

    public static PlayService getInstance(Context context) {
        if (sPlayService == null) {
            synchronized (PlayService.class) {
                sPlayService = new PlayService(context);
            }
        }
        return sPlayService;
    }

    public void playTracks(List<Track> tracks, int startIndex) {
        if (tracks != null && tracks.size() > 0) {
            mTracks.clear();
            mTracks.addAll(tracks);
        }
        mPlayer.playList(mTracks, startIndex);
        mPlayer.play();
    }

    public void playTrack(Track track) {
        if (track == null)
            return;
        mTracks.clear();
        mTracks.add(track);
        mPlayer.playList(mTracks, 0);
    }

    @Override
    public void onPlayStart() {
        Log.d(TAG, "onPlayStart");
    }

    @Override
    public void onPlayPause() {
        Log.d(TAG, "onPlayPause");
    }

    @Override
    public void onPlayStop() {
        Log.d(TAG, "onPlayStop");

    }

    @Override
    public void onSoundPlayComplete() {
        Log.d(TAG, "onSoundPlayComplete");

    }

    @Override
    public void onSoundPrepared() {
        Log.d(TAG, "onSoundPrepared");

    }

    @Override
    public void onSoundSwitch(PlayableModel playableModel, PlayableModel playableModel1) {

    }

    @Override
    public void onBufferingStart() {
        Log.d(TAG, "onBufferingStart");

    }

    @Override
    public void onBufferingStop() {

    }

    @Override
    public void onBufferProgress(int i) {

    }

    @Override
    public void onPlayProgress(int i, int i1) {

    }

    @Override
    public boolean onError(XmPlayerException e) {
        Log.d(TAG, "onError " + e.getMessage());
        return false;
    }

    public void stop() {
        mPlayer.stop();
    }

    public void release() {

        if (mPlayer.isPlaying()) {
            stop();
        }
        mPlayer.release();
    }
}

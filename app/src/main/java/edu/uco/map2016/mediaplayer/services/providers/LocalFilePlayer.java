package edu.uco.map2016.mediaplayer.services.providers;

import android.content.Context;
import android.media.MediaPlayer;

import edu.uco.map2016.mediaplayer.api.AbstractMediaPlayer;
import edu.uco.map2016.mediaplayer.api.MediaFile;

public class LocalFilePlayer extends AbstractMediaPlayer {
    private Context mContext;
    private MediaPlayer mPlayer;

    public LocalFilePlayer(Context context) {
        mContext = context;
    }

    public void initialize() {
        ready();
    }

    @Override
    protected void load(MediaFile file) {
        mPlayer = MediaPlayer.create(mContext, file.getFileLocationAddress());
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                trackChange();
            }
        });
    }

    @Override
    protected void play(MediaFile file) {
        mPlayer = MediaPlayer.create(mContext, file.getFileLocationAddress());
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                trackChange();
                mPlayer.start();
            }
        });
    }

    @Override
    protected void playCurrent() {
        if (mPlayer != null)
            mPlayer.start();
    }

    @Override
    public void pause() {
        if (mPlayer != null)
            mPlayer.pause();
    }

    @Override
    public void stop() {
        if (mPlayer != null)
            mPlayer.stop();
    }

    @Override
    public void endPlayback() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public long getDuration() {
        if (mPlayer != null)
            return mPlayer.getDuration();
        return -1;
    }

    @Override
    public long getPosition() {
        if (mPlayer != null)
            return mPlayer.getCurrentPosition();
        return -1;
    }

    @Override
    public void seek(long timeInMilliseconds) {
        if (mPlayer != null)
            mPlayer.seekTo((int)timeInMilliseconds);
    }

    @Override
    public boolean isPlaying() {
        if (mPlayer != null)
            return mPlayer.isPlaying();
        return false;
    }

    @Override
    public boolean isSongActive() {
        return mPlayer != null;
    }

    @Override
    public void dispose() {
        if (mPlayer != null)
            mPlayer.release();
    }
}

package edu.uco.map2016.mediaplayer.services.providers.spotify;

import android.util.Log;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import edu.uco.map2016.mediaplayer.api.AbstractMediaPlayer;
import edu.uco.map2016.mediaplayer.api.MediaFile;

public class SpotifyMediaPlayer extends AbstractMediaPlayer implements SpotifyPlayer.NotificationCallback, ConnectionStateCallback {
    private static final String LOG_TAG = "SpotifyMediaPlayer";
    private SpotifyPlayer mPlayer;
    private boolean mUseQueue = false;
    private boolean mSongReady = false;
    private boolean mReady = false;

    SpotifyMediaPlayer() {}

    void initialize(final Config config) {
        Spotify.getPlayer(config, this, new SpotifyPlayer.InitializationObserver() {
            @Override
            public void onInitialized(SpotifyPlayer spotifyPlayer) {
                mPlayer = spotifyPlayer;
                mPlayer.addConnectionStateCallback(SpotifyMediaPlayer.this);
                mPlayer.addNotificationCallback(SpotifyMediaPlayer.this);

                if (mPlayer.isLoggedIn()) {
                    ready();
                } else {
                    mPlayer.login(config.oauthToken);
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    @Override
    protected void load(MediaFile file) {
        mUseQueue = true;
        mSongReady = false;
        mPlayer.playUri(new Player.OperationCallback() {
            @Override
            public void onSuccess() {
                mPlayer.pause(new Player.OperationCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Error error) {

                    }
                });
            }

            @Override
            public void onError(Error error) {

            }
        }, file.getFileLocationAddress().toString(), 0, 0);
    }

    @Override
    protected void play(MediaFile file) {
        mUseQueue = true;
        mSongReady = false;
        mPlayer.playUri(new Player.OperationCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Error error) {

            }
        }, file.getFileLocationAddress().toString(), 0, 0);
    }

    @Override
    protected void playCurrent() {
        if(mUseQueue && mPlayer.getMetadata() != null && mPlayer.getMetadata().currentTrack != null) {
            mPlayer.resume(new Player.OperationCallback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError(Error error) {

                }
            });
        }
    }

    @Override
    public void pause() {
        mPlayer.pause(new Player.OperationCallback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Error error) {

            }
        });
    }

    @Override
    public void stop() {
        mPlayer.pause(new Player.OperationCallback() {
            @Override
            public void onSuccess() {
                mPlayer.seekToPosition(new Player.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        if (mPlayer.getPlaybackState().isPlaying) {
                            mPlayer.pause(new Player.OperationCallback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Error error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Error error) {

                    }
                }, 0);
            }

            @Override
            public void onError(Error error) {

            }
        });
    }

    @Override
    public void endPlayback() {
        pause();
        mUseQueue = false;
        mSongReady = false;
    }

    @Override
    public long getDuration() {
        if (mSongReady) {
            Log.d(LOG_TAG, "getDuration is " + mPlayer.getMetadata().currentTrack.durationMs);
            return mPlayer.getMetadata().currentTrack.durationMs;
        }
        Log.d(LOG_TAG, "getDuration is null");
        return -1;
    }

    @Override
    public long getPosition() {
        if (mSongReady) {
            return mPlayer.getPlaybackState().positionMs;
        }
        return -1;
    }

    @Override
    public void seek(long timeInMilliseconds) {
        if (mUseQueue && mPlayer.getMetadata() != null && mPlayer.getMetadata().currentTrack != null) {
            mPlayer.seekToPosition(new Player.OperationCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Error error) {

                }
            }, (int)timeInMilliseconds);
        }
    }

    @Override
    public boolean isPlaying() {
        return mSongReady && mPlayer.getPlaybackState() != null && mPlayer.getPlaybackState().isPlaying;
    }

    @Override
    public boolean isSongActive() {
        return mUseQueue;
    }

    @Override
    public void dispose() {
        Spotify.destroyPlayer(this);
    }

    @Override
    public void onLoggedIn() {
        ready();
    }

    @Override
    public void onLoggedOut() {

    }

    @Override
    public void onLoginFailed(int i) {

    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onConnectionMessage(String s) {

    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d(LOG_TAG, playerEvent.name());
        switch(playerEvent) {
            case kSpPlaybackNotifyTrackChanged:
                if (mSongReady) {
                    ++playlistIndex;
                    if (playlistIndex < mPlaylist.getListOfMediaFiles().size()) {
                        mSongReady = false;
                        play(mPlaylist.getListOfMediaFiles().get(playlistIndex));
                    }
                } else {
                    mSongReady = true;
                }
                trackChange();
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {

    }

    @Override
    public void clearQueue() {
        super.clearQueue();
        mSongReady = false;
    }

    @Override
    protected void ready() {
        mReady = true;
        super.ready();
    }

    public boolean isPrepared() {
        return mReady;
    }
}

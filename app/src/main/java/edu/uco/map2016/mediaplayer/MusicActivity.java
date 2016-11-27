package edu.uco.map2016.mediaplayer;


import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import edu.uco.map2016.mediaplayer.api.AbstractMediaPlayer;
import edu.uco.map2016.mediaplayer.api.MediaFile;
import edu.uco.map2016.mediaplayer.services.ProviderManagerService;

public class MusicActivity extends Activity {
    private static final String LOG_TAG = "TermProject_player";
    private static final String EXTRA_MEDIA
            = "edu.uco.map2016.mediaplayer.MusicActivity.extra_media";
    private static final String STATE_MEDIA_FILE
            = "edu.uco.map2016.mediaplayer.MusicActivity.state_media_file";
    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    private static final String STATE_POSITION
            = "edu.uco.map2016.mediaplayer.MusicActivity.state_position";

    public static AbstractMediaPlayer mediaPlayer = null;
    public TextView songName, duration;
    private MediaSessionManager mManager;
    private MediaSession mSession;
    private MediaController mController;
    private MediaFile mFile;

    private double timeElapsed = 0, finalTime = 0;
    private Handler durationHandler = new Handler();
    private SeekBar seekbar;
    private ImageButton btnPlay, btnPause, btnFF, btnRew;

    private ServiceConnection mConnection;

    public static Intent getInstance(Context context, MediaFile media) {
        Intent intent = new Intent(context, MusicActivity.class);


        intent.putExtra(EXTRA_MEDIA, media);
        return intent;
    }

    private void createMedia(MediaFile media) {
        Intent providerIntent = new Intent(this, ProviderManagerService.class);
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ProviderManagerService.ProviderManagerBinder binder = (ProviderManagerService.ProviderManagerBinder) service;
                binder.getService().getMediaPlayer(MusicActivity.this, new AbstractMediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(AbstractMediaPlayer player, boolean isPlaying) {
                        mediaPlayer = player;

                        mediaPlayer.setTrackChangeListener(new AbstractMediaPlayer.TrackChangeListener() {
                            @Override
                            public void onTrackChange(MediaFile track, int index) {
                                Log.d(LOG_TAG, "onTrackChange");
                                finalTime = mediaPlayer.getDuration();
                                Log.d(LOG_TAG, Double.toString(finalTime));
                                seekbar.setMax((int) finalTime);
                                durationHandler.postDelayed(updateSeekBarTime, 100);
                            }
                        });
                        player.loadFile(media);
                    }
                }, media);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(providerIntent, mConnection, 0);
    }

    public void startService(View v) {
        Intent serviceIntent = new Intent(MusicActivity.this, NotificationService.class);
        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(serviceIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        Log.d(LOG_TAG, "created");



        Intent serviceIntent = new Intent(MusicActivity.this, NotificationService.class);
        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(serviceIntent);
        //mediaPlayer.start();

//


        songName = (TextView) findViewById(R.id.songName);
        duration = (TextView) findViewById(R.id.songDuration);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        btnPlay = (ImageButton)findViewById(R.id.media_play);
        btnPause = (ImageButton)findViewById(R.id.media_pause);
        btnFF = (ImageButton)findViewById(R.id.media_ff);
        btnRew = (ImageButton)findViewById(R.id.media_rew);
        seekbar.setClickable(false);
        Log.d(LOG_TAG, "play button clickable: " + (btnPlay.isClickable() ? "yes" : "no"));

        Intent intent = getIntent();
        if (intent != null) {
            mFile = intent.getParcelableExtra(EXTRA_MEDIA);
            if (mFile != null) {
                songName.setText(mFile.getName());
                if (mFile.isLocal() && Build.VERSION.SDK_INT >= 23
                        && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
                } else if (mediaPlayer == null) {
                    createMedia(mFile);
                } else {
                    MediaFile oldFile = null;
                    if (savedInstanceState != null)
                        oldFile = savedInstanceState.getParcelable(STATE_MEDIA_FILE);
                    if (oldFile != null && mFile.getFileLocationAddress().equals(oldFile.getFileLocationAddress())) {
                        finalTime = mediaPlayer.getDuration();
                        if (finalTime < 0) {
                            mediaPlayer.setTrackChangeListener(new AbstractMediaPlayer.TrackChangeListener() {
                                @Override
                                public void onTrackChange(MediaFile track, int index) {
                                    Log.d(LOG_TAG, "onTrackChange");
                                    finalTime = mediaPlayer.getDuration();
                                    seekbar.setMax((int) finalTime);
                                    durationHandler.postDelayed(updateSeekBarTime, 100);
                                }
                            });
                            mediaPlayer.loadFile(mFile);
                        } else if (mediaPlayer.isPlaying()) {
                            seekbar.setMax((int) finalTime);
                            durationHandler.postDelayed(updateSeekBarTime, 100);
                        } else {
                            Log.d(LOG_TAG, "song loaded but stopped");
                            seekbar.setMax((int) finalTime);
                            durationHandler.postDelayed(updateSeekBarTime, 100);
                        }
                    } else {
                        createMedia(mFile);
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mConnection != null)
            unbindService(mConnection);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && mediaPlayer == null) {
            createMedia(getIntent().getParcelableExtra(EXTRA_MEDIA));
        }
    }

    public void play(View view) {
        Log.d(LOG_TAG, "play");
        Intent serviceIntent = new Intent(MusicActivity.this, NotificationService.class);
        serviceIntent.setAction(Constants.ACTION.PLAY_ACTION);
        startService(serviceIntent);
    }


    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            if (mediaPlayer == null) {
                finish();
            } else {
                btnFF.setClickable(true);
                btnRew.setClickable(true);
                if (mediaPlayer.isPlaying()) {
                    btnPlay.setClickable(false);
                    btnPause.setClickable(true);
                } else {
                    btnPlay.setClickable(true);
                    btnPause.setClickable(false);
                }
                timeElapsed = mediaPlayer.getPosition();
                if (timeElapsed >= 0 && finalTime >= 0) {

                    seekbar.setProgress((int) timeElapsed);

                    double timeRemaining = finalTime - timeElapsed;
                    duration.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining),
                            TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds
                                    (TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
                }


                durationHandler.postDelayed(this, 100);
            }
        }
    };


    public void pause(View view) {
        Log.d(LOG_TAG, "pause");
        play(view);
    }


    public void forward(View view) {
        Log.d(LOG_TAG, "forward");
        Intent serviceIntent = new Intent(MusicActivity.this, NotificationService.class);
        serviceIntent.setAction(Constants.ACTION.NEXT_ACTION);
        startService(serviceIntent);
    }


    public void rewind(View view) {
        Log.d(LOG_TAG, "rewind");
        Intent serviceIntent = new Intent(MusicActivity.this, NotificationService.class);
        serviceIntent.setAction(Constants.ACTION.PREV_ACTION);
        startService(serviceIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putParcelable(STATE_MEDIA_FILE, mFile);
    }

}

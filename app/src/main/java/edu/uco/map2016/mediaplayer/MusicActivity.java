package edu.uco.map2016.mediaplayer;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import edu.uco.map2016.mediaplayer.api.MediaFile;

public class MusicActivity extends Activity {
    private static final String LOG_TAG = "TermProject_player";
    private static final String EXTRA_MEDIA
            = "edu.uco.map2016.mediaplayer.MusicActivity.extra_media";
    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    private static final String STATE_POSITION
            = "edu.uco.map2016.mediaplayer.MusicActivity.state_position";

    private MediaPlayer mediaPlayer;
    public TextView songName, duration;
    private MediaSessionManager mManager;
    private MediaSession mSession;
    private MediaController mController;

    private double timeElapsed = 0, finalTime = 0;
    private int forwardTime = 2000, backwardTime = 2000;
    private Handler durationHandler = new Handler();
    private SeekBar seekbar;
    private ImageButton btnPlay, btnPause, btnFF, btnRew;

    public static Intent getInstance(Context context, MediaFile media) {
        Intent intent = new Intent(context, MusicActivity.class);


        intent.putExtra(EXTRA_MEDIA, media);
        return intent;
    }

    private void createMedia(MediaFile media) {
        mediaPlayer = MediaPlayer.create(this, media.getFileLocationAddress());
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                btnPlay.setClickable(true);
                btnPause.setClickable(false);
                btnFF.setClickable(false);
                btnRew.setClickable(false);
                finalTime = mediaPlayer.getDuration();
                seekbar.setMax((int) finalTime);
            }
        });
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

       // mediaPlayer.start();
        Intent serviceIntent = new Intent(MusicActivity.this, NotificationService.class);
        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(serviceIntent);




        songName = (TextView) findViewById(R.id.songName);
        duration = (TextView) findViewById(R.id.songDuration);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        btnPlay = (ImageButton)findViewById(R.id.media_play);
        btnPause = (ImageButton)findViewById(R.id.media_pause);
        btnFF = (ImageButton)findViewById(R.id.media_ff);
        btnRew = (ImageButton)findViewById(R.id.media_rew);
        seekbar.setClickable(false);

        Intent intent = getIntent();
        if (intent != null) {
            MediaFile media = intent.getParcelableExtra(EXTRA_MEDIA);
            if (media != null) {
                songName.setText(media.getName());
                if (media.isLocal() && Build.VERSION.SDK_INT >= 23
                        && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
                } else {
                    createMedia(media);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createMedia(getIntent().getParcelableExtra(EXTRA_MEDIA));
        }
    }

    public void play(View view) {
        btnPlay.setClickable(false);
        btnPause.setClickable(true);
        btnFF.setClickable(true);
        btnRew.setClickable(true);
        mediaPlayer.start();
        timeElapsed = mediaPlayer.getCurrentPosition();
        seekbar.setProgress((int) timeElapsed);
        durationHandler.postDelayed(updateSeekBarTime, 100);
    }


    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {

            timeElapsed = mediaPlayer.getCurrentPosition();

            seekbar.setProgress((int) timeElapsed);

            double timeRemaining = finalTime - timeElapsed;
            duration.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining),
                    TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds
                            (TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));


            durationHandler.postDelayed(this, 100);
        }
    };


    public void pause(View view) {
        btnPlay.setClickable(true);
        btnPause.setClickable(false);
        btnFF.setClickable(true);
        btnRew.setClickable(true);
        mediaPlayer.pause();
    }


    public void forward(View view) {

        if ((timeElapsed + forwardTime) <= finalTime) {
            timeElapsed = timeElapsed + forwardTime;


            mediaPlayer.seekTo((int) timeElapsed);
        }
    }


    public void rewind(View view) {

        if ((timeElapsed - backwardTime) > 0) {
            timeElapsed = timeElapsed - backwardTime;


            mediaPlayer.seekTo((int) timeElapsed);
        }
    }

}

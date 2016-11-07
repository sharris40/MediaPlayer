package edu.uco.map2016.mediaplayer;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import edu.uco.map2016.mediaplayer.api.MediaFile;


public class VideoActivity extends Activity {
    private static final String LOG_TAG = "TermProject_player";
    private static final String EXTRA_MEDIA
            = "edu.uco.map2016.mediaplayer.VideoActivity.extra_media";
    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    private static final String STATE_POSITION
            = "edu.uco.map2016.mediaplayer.VideoActivity.state_position";

    private VideoView myVideoView;
    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;

    public static Intent getInstance(Context context, MediaFile media) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra(EXTRA_MEDIA, media);
        return intent;
    }

    private void createMedia(MediaFile media) {
        try {
            myVideoView.setMediaController(mediaControls);
            myVideoView.setVideoURI(media.getFileLocationAddress());

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error initializing media player", e);
        }
        myVideoView.requestFocus();
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                progressDialog.dismiss();

                myVideoView.seekTo(position);
                if (position == 0) {
                    myVideoView.start();
                } else {

                    myVideoView.pause();
                }
            }
        });
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_video);


        if (mediaControls == null) {
            mediaControls = new MediaController(VideoActivity.this);
        }


        myVideoView = (VideoView) findViewById(R.id.video_view);


        progressDialog = new ProgressDialog(VideoActivity.this);
        // set a title for the progress bar
        progressDialog.setTitle("TERM");
        // set a message for the progress bar
        progressDialog.setMessage("Loading...");
        //set the progress bar not cancelable on users' touch
        progressDialog.setCancelable(false);
        // show the progress bar
        progressDialog.show();

        Intent intent = getIntent();
        if (intent != null) {
            MediaFile media = intent.getParcelableExtra(EXTRA_MEDIA);
            if (media != null) {
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());
        myVideoView.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        position = savedInstanceState.getInt("Position");
        myVideoView.seekTo(position);
    }
}

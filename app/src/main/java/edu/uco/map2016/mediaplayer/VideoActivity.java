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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

import edu.uco.map2016.mediaplayer.api.MediaFile;

import static android.content.ContentValues.TAG;


public class VideoActivity extends Activity {
    private static final String LOG_TAG = "TermProject_player";
    private static final String EXTRA_MEDIA
            = "edu.uco.map2016.mediaplayer.VideoActivity.extra_media";
    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    private static final String STATE_POSITION
            = "edu.uco.map2016.mediaplayer.VideoActivity.state_position";

    private VideoView myVideoView;
    private int position = 0 ;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;

    int counter;
    int counter2;

    int height = 0;
    int width = 0;
    int height2 = 0;
    int width2 = 0;

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



        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                progressDialog.dismiss();

                myVideoView.seekTo(position);
                if (position == 0) {
                    myVideoView.start();
                } else {

                    myVideoView.stopPlayback();
                }
            }
        });
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if( savedInstanceState != null ) {
            position = savedInstanceState.getInt("position");
        }


        DisplayMetrics dm;

        dm=new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        height=dm.heightPixels;
        width=dm.widthPixels;
        height2=dm.heightPixels;
        width2=dm.widthPixels;

        // myVideoView.setMinimumHeight(height);
        //
        // myVideoView.setMinimumWidth(width);

        setContentView(R.layout.activity_video);





        if (savedInstanceState != null) {
            // Restore value of members from saved state
            position = savedInstanceState.getInt("Position");


        }


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


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);//Menu Resource, Menu
        return true;
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
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int pos = savedInstanceState.getInt("Position");

        myVideoView.seekTo(pos);


        // Toast.makeText(getApplicationContext(), "QQrefewfeQ", Toast.LENGTH_SHORT).show();

    }




    // This gets called before onPause so pause video here.



    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                int left = myVideoView.getLeft();
                int top = myVideoView.getTop();
                int right =  myVideoView.getRight();
                int botton = myVideoView.getBottom() ;
                myVideoView.layout(left/2, top/2, right/2, botton/2);

                return true;

            case R.id.help:
                int left2 = myVideoView.getLeft();
                int top2 = myVideoView.getTop();
                int right2 = myVideoView.getRight();
                int botton2 = myVideoView.getBottom();
                myVideoView.layout(left2 * 2, top2 * 2, right2 * 2, botton2 * 2);


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        myVideoView.seekTo(position);
        myVideoView.start(); //Or use resume() if it doesn't work. I'm not sure
    }

    // This gets called before onPause so pause video here.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        position = myVideoView.getCurrentPosition();
        myVideoView.pause();
        outState.putInt("position", position);
    }
}

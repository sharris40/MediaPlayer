package edu.uco.map2016.mediaplayer;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import edu.uco.map2016.mediaplayer.api.MediaFile;
import edu.uco.map2016.mediaplayer.utils.Caption;
import edu.uco.map2016.mediaplayer.utils.TimedTextObject;


public class VideoActivity extends Activity implements
        SurfaceHolder.Callback,OnSeekBarChangeListener {

    private boolean tutorialUsed;
    private int tutorialPage;

    private static final String TAG = "VideoActivity";
    public static final int FADE_OUT = 0;
    public static final int SHOW_PROGRESS = 1;
    //private VideoView myVideoView;
    private MediaPlayer player;
    private TextView subtitleText;
    private SubtitleProcessingTask subsFetchTask;
    private SeekBar mSeeker;
    AudioManager audioManager;
    private MessageHandler mHandler;
    private MediaFile mVideo;

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


        tutorialUsed = false;
        tutorialPage = 1;

        // your onCreate method/tasks (when you start this application)
        init();

        if( savedInstanceState != null ) {
            position = savedInstanceState.getInt("position");
        }

        mVideo = getIntent().getParcelableExtra(EXTRA_MEDIA);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.player_view);

        myVideoView = (VideoView) findViewById(R.id.svMain);
        myVideoView.getHolder().addCallback(this);
        myVideoView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        subtitleText = (TextView) findViewById(R.id.offLine_subtitleText);
        mSeeker = (SeekBar) findViewById(R.id.seeker);
        mHandler = new MessageHandler();
        mSeeker.setOnSeekBarChangeListener((SeekBar.OnSeekBarChangeListener) this);
		/*
		 * Adjust subtitles margin based on Screen dimes
		 */
        FrameLayout.LayoutParams rl2 = (FrameLayout.LayoutParams) subtitleText
                .getLayoutParams();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        rl2.bottomMargin = (int) (dm.heightPixels * 0.08);
        subtitleText.setLayoutParams(rl2);


        dm=new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        height=dm.heightPixels;
        width=dm.widthPixels;
        height2=dm.heightPixels;
        width2=dm.widthPixels;



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
            createMedia((MediaFile)getIntent().getParcelableExtra(EXTRA_MEDIA));

        }
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

            case R.id.captions:






                getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                setContentView(R.layout.activity_video);

                myVideoView = (VideoView) findViewById(R.id.video_view);
                myVideoView.getHolder().addCallback(this);
                myVideoView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                subtitleText = (TextView) findViewById(R.id.offLine_subtitleText);
                // mSeeker = (SeekBar) findViewById(R.id.seeker);
                myVideoView.setMediaController(mediaControls);
                mHandler = new MessageHandler();

		/*
		 * Adjust subtitles margin based on Screen dimes
		 */
                FrameLayout.LayoutParams rl2 = (FrameLayout.LayoutParams) subtitleText
                        .getLayoutParams();
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                rl2.bottomMargin = (int) (dm.heightPixels * 0.08);
                subtitleText.setLayoutParams(rl2);


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    // This gets called before onPause so pause video here.




    /////////////////////////////////////////////////////////////////new code





    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        runOnUiThread(new Runnable() {
            public void run() {
                playVideo();
            }
        });

    }

    private void playVideo() {
        try {
            player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.reset();
            player.setDataSource(
                    getApplicationContext(),
                    mVideo.getFileLocationAddress());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null == player) {
                Toast.makeText(getApplicationContext(), "Error",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }
        player.setDisplay(myVideoView.getHolder());
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                subsFetchTask = new VideoActivity.SubtitleProcessingTask();
                subsFetchTask.execute();
                player.start();
                mHandler.sendEmptyMessage(SHOW_PROGRESS);
            }
        });
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });
        player.prepareAsync();

        player.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {

            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int videoWidth,
                                           int videoHeight) {

                // Get the width of the screen
                int screenWidth = getWindowManager().getDefaultDisplay()
                        .getWidth();
                int screenHeight = getWindowManager().getDefaultDisplay()
                        .getHeight();

                // Get the SurfaceView layout parameters
                android.view.ViewGroup.LayoutParams lp = myVideoView.getLayoutParams();

                int displayHeight = (int) (((float) videoHeight / (float) videoWidth) * (float) screenWidth);
                int displayWidth;
                if (displayHeight > screenHeight) {
                    displayHeight = screenHeight;
                    displayWidth = (int) (((float) videoWidth / (float) videoHeight) * (float) screenHeight);
                } else {
                    displayWidth = screenWidth;
                }

                // Set the width of the SurfaceView to the width of the
                // screen
                lp.width = displayWidth;

				/*
				 * Set the height of the SurfaceView to match the aspect ratio
				 * of the video be sure to cast these as floats otherwise the
				 * calculation will likely be 0
				 */
                lp.height = displayHeight;

                // Commit the layout parameters
                myVideoView.setLayoutParams(lp);

            }
        });
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void finish() {
        cleanUp();
        super.finish();
    }

    private void cleanUp() {
        if (subtitleDisplayHandler != null) {
            subtitleDisplayHandler.removeCallbacks(subtitleProcessesor);
        }
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    public TimedTextObject srt;
    private Runnable subtitleProcessesor = new Runnable() {

        @Override
        public void run() {
            if (player != null && player.isPlaying()) {
                int currentPos = player.getCurrentPosition();
                Collection<Caption> subtitles = srt.captions.values();
                for (Caption caption : subtitles) {
                    if (currentPos >= caption.start.mseconds
                            && currentPos <= caption.end.mseconds) {
                        onTimedText(caption);
                        break;
                    } else if (currentPos > caption.end.mseconds) {
                        onTimedText(null);
                    }
                }
            }
            subtitleDisplayHandler.postDelayed(this, 100);
        }
    };
    private Handler subtitleDisplayHandler = new Handler();
    private boolean mDragging;

    //@Override


    // @Override
    //// public void onStartTrackingTouch(SeekBar seekBar) {

    // }

    // @Override
    // public void onStopTrackingTouch(SeekBar seekBar) {

    // }

    public class SubtitleProcessingTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        // @Override
        protected Integer doInBackground(Void... params) {
            // int count;
            int track = -1;
            try {
                MediaPlayer.TrackInfo[] ti = player.getTrackInfo();
                for (int i = 0; i < ti.length; ++i) {
                    Log.d(LOG_TAG, "Track " + i + " type " + ti[i].getTrackType());
                    if (ti[i].getTrackType() == MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT) {
                        return i;
                    }
                }
                /*InputStream stream = getResources().openRawResource(
                        R.raw.sub);
                FormatSRT formatSRT = new FormatSRT();
                srt = formatSRT.parseFile("sample.srt", stream);*/

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "error in downloading subs");
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            /*if (null != srt) {
                subtitleText.setText("");

                subtitleDisplayHandler.post(subtitleProcessesor);
            }*/
            super.onPostExecute(result);
            if (result > -1) {
                Log.d(LOG_TAG, "Selecting track " + result);
                player.selectTrack(result);
                player.setOnTimedTextListener(new MediaPlayer.OnTimedTextListener() {
                    @Override
                    public void onTimedText(MediaPlayer mp, TimedText text) {
                        if (subtitleText != null && text != null) {
                            subtitleText.setText(text.getText());
                            subtitleText.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }
    }

    public void onTimedText(Caption text) {
        if (text == null) {
            subtitleText.setVisibility(View.INVISIBLE);
            return;
        }
        subtitleText.setText(Html.fromHtml(text.content));
        subtitleText.setVisibility(View.VISIBLE);
    }

    public File getExternalFile() {
        File srt = null;
        try {
            srt = new File(getApplicationContext().getExternalFilesDir(null)
                    .getPath() + "/sample.srt");
            srt.createNewFile();
            return srt;
        } catch (Exception e) {
            Log.e(TAG, "exception in file creation");
        }
        return null;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        if (player == null) {
            return;
        }

        if (!fromUser) {
            // We're not interested in programmatically generated changes to
            // the progress bar's position.
            return;
        }

        long duration = player.getDuration();
        if (duration == -1)
            return;
        long newposition = (duration * progress) / 1000L;
        player.seekTo((int) newposition);
    }

    private int setProgress() {
        if (player == null || mDragging) {
            return 0;
        }

        int position = player.getCurrentPosition();
        if (position == -1)
            return 0;
        int duration = player.getDuration();
        if (mSeeker != null && duration > 0) {
            long pos = 1000L * position / duration;
            mSeeker.setProgress((int) pos);
        }

        return position;
    }

    //@Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mDragging = true;
    }

    // @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mDragging = false;
        setProgress();
    }

    private class MessageHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (player == null) {
                return;
            }

            int pos;
            switch (msg.what) {
                case FADE_OUT:
                    break;
                case SHOW_PROGRESS:
                    try {
                        pos = setProgress();
                    } catch (IllegalStateException ise) {
                        ise.printStackTrace();
                        break;
                    }
                    if (!mDragging && player.isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /** this will be called when you switch to other app (or leaves it without closing) */
    @Override
    protected void onPause() {
        super.onPause();
        // pauze tasks
    }

    /** this will be called when you returns back to this app after going into pause state */

    /** this starts when app closes, but BEFORE onDestroy() */
    // please remember field "savedInstanceState", which will be stored in the memory after this method


    /** this starts after onStart(). After this method, onCreate(Bundle b) gets invoked, followed by onPostCreate(Bundle b) method
     * When this method has ended, the app starts skipping the onCreate and onPostCreate method and initiates then.
     * -- *%* A best practice is to add an init() method which have all startup functions.
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // restore state
        tutorialPage = savedInstanceState.getInt("TutPage");
        tutorialUsed = savedInstanceState.getBoolean("tutUsed");
        init();
    }

    /** do your startup tasks here */
    public void init() {
        if (!tutorialUsed) {
            //showTutorial(tutorialPage);
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

package edu.uco.map2016.mediaplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import edu.uco.map2016.mediaplayer.api.MediaFile;

public class PlayerActivity extends Activity {
    private static final String LOG_TAG = "TermProject_player";
    private static final String EXTRA_MEDIA
            = "edu.uco.map2016.mediaplayer.PlayerActivity.extra_media";
    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    private static final String STATE_POSITION
            = "edu.uco.map2016.mediaplayer.PlayerActivity.state_position";

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;

    private VideoView vidMain;
    private View mControlsView;
    private boolean mVisible;
    private boolean mLoaded = false, mPaused = false, mAppPaused = false;
    private int mPosition = 0;

    public static Intent getInstance(Context context, MediaFile media) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(EXTRA_MEDIA, media);
        return intent;
    }

    private void play(MediaFile media) {
        MediaController controller = new MediaController(this);
        vidMain.setMediaController(controller);
        vidMain.setVideoURI(media.getFileLocationAddress());
        vidMain.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                vidMain.start();
            }
        });
    }

    private void restoreInstanceState(Bundle state) {
        if (state != null) {
            Log.d(LOG_TAG, "restoring state");
            mPosition = state.getInt(STATE_POSITION, 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");

        setContentView(R.layout.activity_player);
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        vidMain = (VideoView)findViewById(R.id.actPlayer_vidMain);


        // Set up the user interaction to manually show or hide the system UI.
        vidMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

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
                    if (mPosition > 0) {
                        vidMain.seekTo(mPosition);
                    }
                    play(media);
                    mLoaded = true;
                }
            }
        }
        restoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLoaded) {
            mPosition = vidMain.getCurrentPosition();
            Log.d(LOG_TAG, Integer.toString(mPosition));
            vidMain.pause();
        }
        mAppPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLoaded && mAppPaused) {
            vidMain.seekTo(mPosition);
            if (!mPaused)
                vidMain.start();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        if (mLoaded) {
            Log.d(LOG_TAG, "saving state");
            Log.d(LOG_TAG, Integer.toString(mPosition));
            state.putInt(STATE_POSITION, mPosition);
        }
        super.onSaveInstanceState(state);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                play((MediaFile)getIntent().getParcelableExtra(EXTRA_MEDIA));
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            navigateUpTo(getParentActivityIntent());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            vidMain.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        vidMain.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };

    private final Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}

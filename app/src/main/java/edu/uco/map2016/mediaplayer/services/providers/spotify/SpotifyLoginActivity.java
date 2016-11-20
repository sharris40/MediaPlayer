package edu.uco.map2016.mediaplayer.services.providers.spotify;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.Date;

import edu.uco.map2016.mediaplayer.R;
import edu.uco.map2016.mediaplayer.services.ProviderService;

public class SpotifyLoginActivity extends Activity {
    private static final String LOG_TAG = "SpotifyLoginActivity";

    private static final int REQUEST_CODE = 19348;
    private static final String STATE_STARTED
            = "edu.uco.map2016.mediaplayer.services.providers.spotify.SpotifyLoginActivity.state_started";

    private static final String EXTRA_CONTAINED_INTENT
            = "edu.uco.map2016.mediaplayer.services.providers.spotify.SpotifyLoginActivity.extra_containedIntent";

    private boolean mRequested = false;

    static Intent createInstance(Context context, Intent containedIntent) {
        Intent intent = new Intent(context, SpotifyLoginActivity.class);
        intent.putExtra(EXTRA_CONTAINED_INTENT, containedIntent);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
        setContentView(R.layout.activity_spotify_login);

        if (savedInstanceState != null) {
            mRequested = savedInstanceState.getBoolean(STATE_STARTED, false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
        if (!mRequested) {
            Log.d(LOG_TAG, "running intent check");
            mRequested = true;
            startActivityForResult(getIntent().getParcelableExtra(EXTRA_CONTAINED_INTENT), REQUEST_CODE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putBoolean(STATE_STARTED, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Date now = new Date();
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE) {
            Intent serviceIntent = new Intent(this, SpotifyService.class);
            bindService(serviceIntent, new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    ProviderService.ProviderBinder binder = (ProviderService.ProviderBinder) service;
                    SpotifyService sService = (SpotifyService)binder.getService();
                    AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
                    sService.setAuthenticationData(now, response);
                    Log.d(LOG_TAG, "Unbinding");
                    unbindService(this);
                    finish();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                }
            }, 0);
        }
    }
}

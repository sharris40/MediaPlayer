package edu.uco.map2016.mediaplayer.services;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import edu.uco.map2016.mediaplayer.api.AbstractMediaPlayer;
import edu.uco.map2016.mediaplayer.api.MediaFile;
import edu.uco.map2016.mediaplayer.services.providers.LocalFilePlayer;

public class ProviderManagerService extends Service {
    private LocalFilePlayer mPlayer = null;

    private static final int UNUSED_REQUEST_CODE = 0x7FFFFFFF;
    private static final String LOG_TAG = "ProviderManagerService";

    public class ProviderManagerBinder extends Binder {
        public ProviderManagerService getService() {
            return ProviderManagerService.this;
        }
    }

    private final IBinder mBinder = new ProviderManagerBinder();

    public ProviderManagerService() {}

    @Override
    public void onCreate() {
        super.onCreate();
        //mPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null)
            mPlayer.dispose();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent startIntent;
        for (Class<? extends ProviderService> provider : ProviderRegistry.getInstance().getServices()) {
            startIntent = new Intent(this, provider);
            startService(startIntent);
            bindService(startIntent, new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    ProviderService.ProviderBinder binder = (ProviderService.ProviderBinder) service;
                    ProviderService sService = binder.getService();
                    if (!sService.isConnected() && !sService.isConnectionInteractive() && sService.hasConnectionParameters()) {
                        sService.connect(UNUSED_REQUEST_CODE);
                    }
                    unbindService(this);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {}
            }, 0);
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void getLocalMediaPlayer(Context context, AbstractMediaPlayer.OnPreparedListener listener) {
        if (mPlayer == null) {
            mPlayer = new LocalFilePlayer(this);
            mPlayer.setOnPreparedListener(listener);
            mPlayer.initialize();
        } else {
            listener.onPrepared(mPlayer, mPlayer.isPlaying());
        }
    }

    public void getMediaPlayer(final Context context, final AbstractMediaPlayer.OnPreparedListener listener, MediaFile file) {
        if (file.isLocal()) {
            getLocalMediaPlayer(context, listener);
        } else {
            try {
                Class<?> providerGeneric = Class.forName(file.getProvider()).asSubclass(ProviderService.class);
                if (ProviderService.class.isAssignableFrom(providerGeneric)) {
                    Intent startIntent = new Intent(this, providerGeneric);
                    bindService(startIntent, new ServiceConnection() {
                        @Override
                        public void onServiceConnected(ComponentName name, IBinder service) {
                            ProviderService.ProviderBinder binder = (ProviderService.ProviderBinder) service;
                            ProviderService sService = binder.getService();
                            if (sService.isConnected()) {
                                sService.getMediaPlayer(context, listener);
                            }
                            unbindService(this);
                        }

                        @Override
                        public void onServiceDisconnected(ComponentName name) {

                        }
                    }, 0);
                }
            } catch (ClassNotFoundException nex) {
                Log.wtf(LOG_TAG, "Class " + file.getProvider() + " is not a ProviderService.");
            }
        }
    }
}

package edu.uco.map2016.mediaplayer.services;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;

public class ProviderManagerService extends Service {
    //private static final String PREFERENCES = "provider_manager_preferences";
    //private SharedPreferences mPreferences;

    private static final int UNUSED_REQUEST_CODE = 0x7FFFFFFF;

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
}

package edu.uco.map2016.mediaplayer.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class ProviderManagerService extends Service {
    public class ProviderManagerBinder extends Binder {
        public ProviderManagerService getService() {
            return ProviderManagerService.this;
        }
    }

    private final IBinder mBinder = new ProviderManagerBinder();

    public ProviderManagerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}

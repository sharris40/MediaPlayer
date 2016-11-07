package edu.uco.map2016.mediaplayer.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class SearchService extends Service {
    public class SearchBinder extends Binder {
        public SearchService getService() {
            return SearchService.this;
        }
    }

    private final IBinder mBinder = new SearchBinder();

    public SearchService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}

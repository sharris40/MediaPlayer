package edu.uco.map2016.mediaplayer.services;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.util.SparseArray;

import java.util.LinkedList;
import java.util.Random;

import edu.uco.map2016.mediaplayer.api.SearchQuery;
import edu.uco.map2016.mediaplayer.api.SearchResults;

public class SearchService extends Service {
    private static final String LOG_TAG = "SearchService";
    public class SearchBinder extends Binder {
        public SearchService getService() {
            return SearchService.this;
        }
    }

    public interface SearchListener {
        void onSearchResults(SearchResults results);
    }

    private class ProviderConnection implements ServiceConnection {
        private ProviderService mService;
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ProviderService.ProviderBinder binder = (ProviderService.ProviderBinder) service;
            mService = binder.getService();
            mConnections.add(this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mConnections.remove(this);
        }

        public ProviderService getService() {
            return mService;
        }
    }

    private class InternalSearchListener implements ProviderService.ProviderListener {
        private ProviderService mService;

        InternalSearchListener(ProviderService service) {
            mService = service;
        }

        @Override
        public void onAsynchronousOperationProgress(int requestType, int requestCode, int responseCode, float progress) {

        }

        @Override
        public void onAsynchronousOperationComplete(int requestType, int requestCode, int responseCode) {
            Log.d(LOG_TAG, "onAsynchronousOperationComplete");
            if (requestType == ProviderService.REQUEST_SEARCH && responseCode == ProviderService.RESPONSE_OK) {
                Log.d(LOG_TAG, "Response OK");
                SearchListener listener = mListeners.get(requestCode, null);
                Log.d(LOG_TAG, "Request code: " + Integer.toString(requestCode));
                if (listener != null) {
                    Log.d(LOG_TAG, "Listener found");
                    listener.onSearchResults(mService.getSearchResults(requestCode));
                    mService.removeListener(this);
                }
            }
        }

        @Override
        public void onMessage(int messageCode, int responseCode) {
        }

        void disconnect() {
            mService.removeListener(this);
        }
    }

    private final IBinder mBinder = new SearchBinder();

    private final LinkedList<ProviderConnection> mConnections = new LinkedList<>();
    private final LinkedList<InternalSearchListener> mInternalListeners = new LinkedList<>();

    private final SparseArray<SearchListener> mListeners = new SparseArray<>();

    private final Random random = new Random();

    public SearchService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate start");
        Intent startIntent;
        for (Class<? extends ProviderService> provider : ProviderRegistry.getInstance().getServices()) {
            startIntent = new Intent(this, provider);
            startService(startIntent);
            bindService(startIntent, new ProviderConnection(), 0);
        }
        Log.d(LOG_TAG, "onCreate finish");
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy start");
        for (InternalSearchListener iListener : mInternalListeners) {
            iListener.disconnect();
        }
        for (ServiceConnection connection : mConnections) {
            unbindService(connection);
        }
        Log.d(LOG_TAG, "onDestroy finish");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return mBinder;
    }

    public int search(SearchQuery query, SearchListener listener) {
        Log.d(LOG_TAG, "search start");
        int requestCode;
        do {
            requestCode = random.nextInt();
        } while (mListeners.get(requestCode, null) != null);
        Log.d(LOG_TAG, "Listener null: " + ((listener == null) ? "yes" : "no"));
        Log.d(LOG_TAG, "Result code: " + Integer.toString(requestCode));
        mListeners.put(requestCode, listener);
        for (ProviderConnection connection : mConnections) {
            InternalSearchListener iListener = new InternalSearchListener(connection.getService());
            mInternalListeners.push(iListener);
            connection.getService().addListener(iListener);
            connection.getService().search(requestCode, query);
        }
        return requestCode;
    }

    public SearchListener getListener(int requestCode) {
        return mListeners.get(requestCode);
    }

    public void setListener(int requestCode, SearchListener listener) {
        mListeners.put(requestCode, listener);
    }
}

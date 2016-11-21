package edu.uco.map2016.mediaplayer.services;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.Iterator;
import java.util.LinkedList;

import edu.uco.map2016.mediaplayer.api.AbstractMediaPlayer;
import edu.uco.map2016.mediaplayer.api.SearchQuery;
import edu.uco.map2016.mediaplayer.api.SearchResults;

public abstract class ProviderService extends Service {
    public static final int REQUEST_CONNECT = 1;
    public static final int REQUEST_CHECK_CONNECTION = 2;
    public static final int REQUEST_SEARCH = 1;

    public static final int MESSAGE_CONNECTION_UPDATE = 1;

    public static final int RESPONSE_OK = 0;
    public static final int RESPONSE_INTERRUPTED = -1;
    public static final int RESPONSE_DISCONNECTED = -2;
    public static final int RESPONSE_RUN_ACTIVITY = -3;
    public static final int RESPONSE_EXCEPTION = -4;

    public interface ProviderListener {
        void onAsynchronousOperationProgress(int requestType, int requestCode, int responseCode, float progress);
        void onAsynchronousOperationComplete(int requestType, int requestCode, int responseCode);
        void onMessage(int messageCode, int responseCode);
    }

    public class ProviderBinder extends Binder {
        public ProviderService getService() {
            return ProviderService.this;
        }
    }

    private LinkedList<ProviderListener> mListeners = new LinkedList<>();

    public void addListener(ProviderListener listener) {
        mListeners.push(listener);
    }

    public void removeListener(ProviderListener listener) {
        Iterator<ProviderListener> it = mListeners.iterator();
        ProviderListener current;
        while (it.hasNext()) {
            current = it.next();
            if (current == listener) {
                it.remove();
                return;
            }
        }
    }

    protected void notifyProgress(int requestType, int requestCode, int responseCode, float progress) {
        for (ProviderListener listener : mListeners) {
            listener.onAsynchronousOperationProgress(requestType, requestCode, responseCode, progress);
        }
    }

    protected void notifyComplete(int requestType, int requestCode, int responseCode) {
        for (ProviderListener listener : mListeners) {
            listener.onAsynchronousOperationComplete(requestType, requestCode, responseCode);
        }
    }

    protected void sendMessage(int messageCode, int responseCode) {
        for (ProviderListener listener : mListeners) {
            listener.onMessage(messageCode, responseCode);
        }
    }

    public abstract @NonNull String getProviderName();

    public abstract boolean isConnected();
    public abstract boolean isConnectionInteractive();
    public abstract Intent createConnectionActivity(Activity context);
    public abstract boolean hasConnectionParameters();
    public abstract boolean setConnectionParameters(Bundle params);

    public abstract void connect(int requestCode);
    public abstract void checkConnection(int requestCode);

    public abstract void search(int requestCode, SearchQuery query);
    public abstract SearchResults getSearchResults(int requestCode);
    public abstract void getMediaPlayer(Context context, AbstractMediaPlayer.OnPreparedListener listener);
}

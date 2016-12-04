package edu.uco.map2016.mediaplayer.services.providers.spotify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.uco.map2016.mediaplayer.R;
import edu.uco.map2016.mediaplayer.api.AbstractMediaPlayer;
import edu.uco.map2016.mediaplayer.api.HttpJsonUtility;
import edu.uco.map2016.mediaplayer.api.MediaFile;
import edu.uco.map2016.mediaplayer.api.SearchQuery;
import edu.uco.map2016.mediaplayer.api.SearchResults;
import edu.uco.map2016.mediaplayer.services.ProviderService;

public class SpotifyService extends ProviderService {
    private static final String LOG_TAG = "SpotifyService";

    private static final String STATE_TOKEN = "SpotifyService.token";
    private static final String STATE_EXPIRES = "SpotifyService.expires";
    private static final String PREFERENCES = "spotify_preferences";

    private static String[] SCOPES = new String[] {
        "playlist-read-private",
        "playlist-read-collaborative",
        "streaming",
        "user-library-read",
        "user-read-private"
    };

    private final Binder mBinder = new ProviderBinder();

    private SharedPreferences mPreferences;

    private String mToken = null;
    private Date mExpires = null;
    private SparseArray<SpotifySearchResults> mSearchResults = new SparseArray<>();
    private SpotifyMediaPlayer mPlayer = null;

    private ScheduledExecutorService mExecutor = null;

    public SpotifyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mExecutor = Executors.newScheduledThreadPool(4);
        mPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);

        long expires = mPreferences.getLong(STATE_EXPIRES, -1);
        if (expires > 0) {
            Date expireDate = new Date(expires);
            if (expireDate.after(new Date())) {
                mToken = mPreferences.getString(STATE_TOKEN, null);
                if (mToken != null) {
                    mExpires = expireDate;
                    mExecutor.schedule(() -> {
                        mToken = null;
                        mExpires = null;
                        sendMessage(MESSAGE_CONNECTION_UPDATE, RESPONSE_DISCONNECTED);
                    }, expireDate.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mPlayer != null) {
            mPlayer.dispose();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public @NonNull String getProviderName() {
        return getResources().getString(R.string.provider_spotify);
    }

    @Override
    public boolean isConnected() {
        return mToken != null;
    }

    @Override
    public boolean isConnectionInteractive() {
        return !isConnected();
    }

    @Override
    public Intent createConnectionActivity(Activity context) {
        Resources res = getResources();
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(
                res.getString(R.string.spotify_client_id),
                AuthenticationResponse.Type.TOKEN,
                res.getString(R.string.spotify_authenticate_redirect_uri));
        builder.setScopes(SCOPES);
        Intent containedIntent = AuthenticationClient.createLoginActivityIntent(context, builder.build());
        return SpotifyLoginActivity.createInstance(context, containedIntent);
    }

    @Override
    public boolean hasConnectionParameters() {
        return true;
    }

    @Override
    public boolean setConnectionParameters(Bundle params) {
        return false;
    }

    private void doCheckConnection(int requestCode) {
        Resources res = getResources();
        try {
            URL url = new URL("https://api.spotify.com/v1/me");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(res.getInteger(R.integer.read_timeout));
            connection.setConnectTimeout(res.getInteger(R.integer.connect_timeout));
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + mToken);
            HttpJsonUtility utility = new HttpJsonUtility(this, connection);
            int code = utility.connect();
            if (code >= 200 && code < 300) {
                notifyComplete(REQUEST_CONNECT, requestCode, RESPONSE_OK);
                sendMessage(MESSAGE_CONNECTION_UPDATE, RESPONSE_OK);
            } else {
                Log.e(LOG_TAG, "Error connecting to Spotify services.");
                notifyComplete(REQUEST_CONNECT, requestCode, RESPONSE_EXCEPTION);
                sendMessage(MESSAGE_CONNECTION_UPDATE, RESPONSE_DISCONNECTED);
            }
        } catch (MalformedURLException mex) {
            Log.wtf(LOG_TAG, "Assertion failed: MalformedURLException from hardcoded URL",
                    mex);
        } catch (IOException iex) {
            Log.e(LOG_TAG, "Error connecting to Spotify services.", iex);
            notifyComplete(REQUEST_CONNECT, requestCode, RESPONSE_EXCEPTION);
            sendMessage(MESSAGE_CONNECTION_UPDATE, RESPONSE_DISCONNECTED);
        }
    }

    @Override
    public void connect(int requestCode) {
        mExecutor.execute(() -> {
            if (mToken == null) {
                notifyComplete(REQUEST_CONNECT, requestCode, RESPONSE_RUN_ACTIVITY);
            } else {
                doCheckConnection(REQUEST_CONNECT);
            }
        });
    }

    @Override
    public void checkConnection(int requestCode) {
        mExecutor.execute(() -> {
            if (mToken == null) {
                notifyComplete(REQUEST_CHECK_CONNECTION, requestCode, RESPONSE_DISCONNECTED);
            } else {
                doCheckConnection(REQUEST_CHECK_CONNECTION);
            }
        });
    }

    private void processSearchResults(int requestCode, JSONObject results, boolean isContinuation) {
        Vector<MediaFile> searchResults;
        SpotifySearchResults sSearchResults;
        if (isContinuation) {
            sSearchResults = mSearchResults.get(requestCode);
        } else {
            sSearchResults = new SpotifySearchResults(this);
            mSearchResults.put(requestCode, sSearchResults);
        }
        try {
            JSONObject tracks = results.getJSONObject("tracks");
            if (tracks != null) {
                int limit = tracks.getInt("limit");
                if (limit > 0) {
                    searchResults = new Vector<>(limit);
                    sSearchResults.requestCode = requestCode;
                    sSearchResults.results = searchResults;
                    sSearchResults.total = tracks.getInt("total");
                    sSearchResults.nextQuery = tracks.getString("next");
                    JSONArray items = tracks.getJSONArray("items");
                    if (items != null) {
                        JSONObject track;
                        MediaFile file;
                        for (int i = 0; i < items.length(); ++i) {
                            track = items.getJSONObject(i);
                            if (track != null) {
                                String uri = track.getString("uri");
                                String name = track.getString("name");
                                if (uri != null && name != null) {
                                    file = new MediaFile(name, Uri.parse(uri), MediaFile.TYPE_AUDIO);
                                    file.setProvider(SpotifyService.class.getName());
                                    int duration = (int) (track.getLong("duration_ms") / 1000);
                                    int seconds = duration % 60;
                                    int minutes = (duration / 60) % 60;
                                    int hours = (duration / 3600);
                                    StringBuilder durationStr = new StringBuilder();
                                    if (hours > 0) {
                                        durationStr.append(hours);
                                        durationStr.append(':');
                                    }
                                    if (minutes < 9) {
                                        durationStr.append('0');
                                    }
                                    durationStr.append(minutes);
                                    durationStr.append(':');
                                    if (seconds < 9) {
                                        durationStr.append('0');
                                    }
                                    durationStr.append(seconds);
                                    file.setLengthOfFile(durationStr.toString());
                                    JSONArray artists = track.getJSONArray("artists");
                                    if (artists != null && artists.length() > 0) {
                                        JSONObject artist = artists.getJSONObject(0);
                                        if (artist != null) {
                                            String artistName = artist.getString("name");
                                            if (artistName != null) {
                                                file.setArtist(artistName);
                                            }
                                        }
                                    }
                                    JSONObject album = track.getJSONObject("album");
                                    if (album != null) {
                                        String albumName = album.getString("name");
                                        if (albumName != null) {
                                            file.setAlbum(albumName);
                                        }
                                    }
                                    searchResults.add(file);
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException je) {
            Log.d(LOG_TAG, "JSON exception", je);
        }
    }

    void search(int requestCode, String query, boolean isContinuation) {
        mExecutor.execute(() -> {
            Resources res = getResources();
            try {
                Log.d(LOG_TAG, "started search");
                URL url = new URL(query);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(res.getInteger(R.integer.read_timeout));
                connection.setConnectTimeout(res.getInteger(R.integer.connect_timeout));
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", "Bearer " + mToken);
                HttpJsonUtility utility = new HttpJsonUtility(this, connection);
                int code = utility.connect();
                if (code >= 200 && code < 300) {
                    Log.d(LOG_TAG, "Complete");
                    processSearchResults(requestCode, utility.getJson(), isContinuation);
                    if (!isContinuation) {
                        notifyComplete(REQUEST_SEARCH, requestCode, RESPONSE_OK);
                    } else {
                        SpotifySearchResults results = mSearchResults.get(requestCode);
                        if (results != null && results.currentListener != null) {
                            results.currentListener.onSearchUpdated(results.nextQuery != null);
                            results.currentListener = null;
                        }
                    }
                } else {
                    Log.e(LOG_TAG, "Error connecting to Spotify services.");
                    if (!isContinuation) {
                        notifyComplete(REQUEST_SEARCH, requestCode, RESPONSE_EXCEPTION);
                    } else {
                        SpotifySearchResults results = mSearchResults.get(requestCode);
                        if (results != null && results.currentListener != null) {
                            results.currentListener.onSearchFailed();
                            results.currentListener = null;
                        }
                    }
                }
            } catch (MalformedURLException mex) {
                Log.wtf(LOG_TAG, "Assertion failed: MalformedURLException from hardcoded URL",
                        mex);
            } catch (IOException iex) {
                Log.e(LOG_TAG, "Error connecting to Spotify services.", iex);
                if (!isContinuation) {
                    notifyComplete(REQUEST_SEARCH, requestCode, RESPONSE_EXCEPTION);
                } else {
                    SpotifySearchResults results = mSearchResults.get(requestCode);
                    if (results != null && results.currentListener != null) {
                        results.currentListener.onSearchFailed();
                        results.currentListener = null;
                    }
                }
            }
        });
    }

    @Override
    public void search(int requestCode, SearchQuery query) {
        search(requestCode, "https://api.spotify.com/v1/search?market=from_token&type=track&q=" + Uri.encode(query.getQuery()), false);
    }

    @Override
    public SearchResults getSearchResults(int requestCode) {
        if (mSearchResults.get(requestCode) == null) {
            Log.d(LOG_TAG, "not working");
            return null;
        } else {
            return mSearchResults.get(requestCode);
        }
    }

    @Override
    public void getMediaPlayer(Context context, AbstractMediaPlayer.OnPreparedListener listener) {
        if (mToken != null) {
            if (mPlayer != null) {
                if (mPlayer.isPrepared()) {
                    listener.onPrepared(mPlayer, mPlayer.isPlaying());
                } else {
                    mPlayer.setOnPreparedListener(listener);
                }
            } else {
                mPlayer = new SpotifyMediaPlayer();
                mPlayer.setOnPreparedListener(listener);
                Config config = new Config(this, mToken, getResources().getString(R.string.spotify_client_id));
                mPlayer.initialize(config);
            }
        }
    }

    private void saveAuthenticationDetails() {
        SharedPreferences.Editor editor = mPreferences.edit();
        if (mToken == null) {
            editor.remove(STATE_TOKEN);
            editor.remove(STATE_EXPIRES);
        } else {
            editor.putString(STATE_TOKEN, mToken);
            editor.putLong(STATE_EXPIRES, mExpires.getTime());
        }
        editor.apply();
    }

    boolean setAuthenticationData(@NonNull Date authTime, @NonNull AuthenticationResponse response) {
        boolean result = true;
        if (response.getType() == AuthenticationResponse.Type.TOKEN) {
            Date expireDate = new Date(authTime.getTime() + (long)(response.getExpiresIn()) * 1000L);
            if (expireDate.after(new Date())) {
                mToken = response.getAccessToken();
                mExpires = expireDate;
            } else {
                result = false;
            }
        } else {
            result = false;
        }
        if (result || mToken == null) {
            saveAuthenticationDetails();
        }
        return result;
    }
}

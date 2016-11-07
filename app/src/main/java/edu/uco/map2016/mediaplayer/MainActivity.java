package edu.uco.map2016.mediaplayer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import edu.uco.map2016.mediaplayer.api.MediaFile;
import edu.uco.map2016.mediaplayer.api.SearchQuery;
import edu.uco.map2016.mediaplayer.services.ProviderService;
import edu.uco.map2016.mediaplayer.services.providers.SpotifyService;

public class MainActivity extends Activity {
    private static final String LOG_TAG = "MainActivity";

    Button searchButton;
    Button video;
    Button audio;
    EditText searchTextField;

    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent serviceIntent = new Intent(this, SpotifyService.class);
        startService(serviceIntent);
        bindService(serviceIntent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                SpotifyService.SpotifyBinder binder = (SpotifyService.SpotifyBinder) service;
                SpotifyService sService = binder.getService();
                if (!sService.isConnected()) {
                    Intent spotifyIntent = sService.createConnectionActivity(MainActivity.this);
                    startActivity(spotifyIntent);
                }
                unbindService(this);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        }, 0);

        searchButton = (Button) findViewById(R.id.layout_main_search_button);
        searchTextField = (EditText) findViewById(R.id.layout_main_search_text_field);
        video = (Button) findViewById(R.id.video) ;
        audio = (Button) findViewById(R.id.music);

        mHandler = new Handler(Looper.getMainLooper());

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent spotifyIntent = new Intent(MainActivity.this, SpotifyService.class);
                bindService(spotifyIntent, new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        SpotifyService.SpotifyBinder binder = (SpotifyService.SpotifyBinder) service;
                        final SpotifyService sService = binder.getService();
                        final ServiceConnection connection = this;
                        SearchQuery query = new SearchQuery();
                        query.setTitle(searchTextField.getText().toString());
                        sService.addListener(new ProviderService.ProviderListener() {
                            @Override
                            public void onAsynchronousOperationProgress(int requestType, int requestCode, int responseCode, float progress) {

                            }

                            @Override
                            public void onAsynchronousOperationComplete(int requestType, int requestCode, int responseCode) {
                                Log.d(LOG_TAG, "onAsynchronousOperationComplete");
                                if (requestType == ProviderService.REQUEST_SEARCH) {
                                    if (responseCode == ProviderService.RESPONSE_OK) {
                                        Log.d(LOG_TAG, "success");
                                        ArrayList<MediaFile> files = new ArrayList<>(sService.getSearchResults(requestCode, 0));
                                        Log.d(LOG_TAG, "here");
                                        Intent activityList = new Intent(MainActivity.this, ListActivity.class); //getApplicationContext()
                                        activityList.putExtra("SEARCH", searchTextField.getText().toString());
                                        activityList.putParcelableArrayListExtra("ONLINE_RESULTS", files);
                                        mHandler.post(() -> {
                                            startActivity(activityList);
                                        });
                                    }
                                }
                                unbindService(connection);
                            }

                            @Override
                            public void onMessage(int messageCode, int responseCode) {

                            }
                        });
                        sService.search(1, query);
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                }, 0);
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityList = new Intent(MainActivity.this, VideoActivity.class); //getApplicationContext()

                startActivity(activityList);
            }
        });
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityList = new Intent(MainActivity.this, MusicActivity.class); //getApplicationContext()

                startActivity(activityList);
            }
        });

        Button browseButton = (Button)findViewById(R.id.layout_main_browse_button);
        browseButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OpenActivity.class);
            startActivity(intent);
        });

    }


}
package edu.uco.map2016.mediaplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.uco.map2016.mediaplayer.api.PlaylistInterface;
import edu.uco.map2016.mediaplayer.api.SearchInterface;
import edu.uco.map2016.mediaplayer.services.ProviderManagerService;

public class MainActivity extends Activity {
    private static final String LOG_TAG = "MainActivity";

    public static SearchInterface searchInterface;
    public static PlaylistInterface playlistInterface;

    Button searchButton;
    EditText searchTextField;
    private Button playlistButton;
    private Button createPlaylistButton;
    Button butt1;

    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchInterface = new SearchInterface();
        playlistInterface = PlaylistInterface.readFromFile(this);
        if (playlistInterface == null) {
            playlistInterface = new PlaylistInterface();
        }
        //playlistInterface = PlaylistInterface.readFromFile(this);
        //playlistInterface = new PlaylistInterface();
        //playlistInterface.saveToFile(this);



        Intent serviceIntent = new Intent(this, ProviderManagerService.class);
        startService(serviceIntent);
        /*bindService(serviceIntent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ProviderService.ProviderBinder binder = (ProviderService.ProviderBinder) service;
                SpotifyService sService = (SpotifyService)binder.getService();
                if (!sService.isConnected()) {
                    Intent spotifyIntent = sService.createConnectionActivity(MainActivity.this);
                    startActivity(spotifyIntent);
                }
                unbindService(this);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        }, 0);*/

        searchButton = (Button) findViewById(R.id.layout_main_search_button);
        searchTextField = (EditText) findViewById(R.id.layout_main_search_text_field);
        playlistButton = (Button) findViewById(R.id.layout_main_playlist_button);
        createPlaylistButton = (Button) findViewById(R.id.layout_main_create_playlist_button);
        //butt1 = (Button) findViewById(R.id.butt1);

        mHandler = new Handler(Looper.getMainLooper());

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityList = new Intent(MainActivity.this, SearchListActivity.class); //getApplicationContext()
                activityList.putExtra("SEARCH", searchTextField.getText().toString());
                startActivity(activityList);
            }
        });
        playlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activitySuperPlaylistList = new Intent(MainActivity.this, SuperPlaylistListActivity.class); //getApplicationContext()
                startActivity(activitySuperPlaylistList);
            }
        });
        createPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityCreatePlaylist = new Intent(MainActivity.this, CreatePlaylistActivity.class); //getApplicationContext()

                startActivity(activityCreatePlaylist);
            }
        });

        Button browseButton = (Button)findViewById(R.id.layout_main_browse_button);
        browseButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OpenActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.mnuMain_providers:
                Intent intent = new Intent(this, ProviderListActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

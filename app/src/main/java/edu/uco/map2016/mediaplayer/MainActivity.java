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

import edu.uco.map2016.mediaplayer.services.ProviderManagerService;

public class MainActivity extends Activity {
    private static final String LOG_TAG = "MainActivity";

    Button searchButton;
    Button video;
    Button audio;
    EditText searchTextField;
    Button butt1;

    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        //butt1 = (Button) findViewById(R.id.butt1);
        video = (Button) findViewById(R.id.video) ;
        audio = (Button) findViewById(R.id.music);

        mHandler = new Handler(Looper.getMainLooper());

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityList = new Intent(MainActivity.this, ListActivity.class); //getApplicationContext()
                activityList.putExtra("SEARCH", searchTextField.getText().toString());
                startActivity(activityList);
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

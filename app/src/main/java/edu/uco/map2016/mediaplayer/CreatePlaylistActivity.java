package edu.uco.map2016.mediaplayer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.uco.map2016.mediaplayer.api.Playlist;

public class CreatePlaylistActivity extends Activity {

    Button createPlaylistButton;
    EditText enterTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);

        createPlaylistButton = (Button) findViewById(R.id.layout_create_playlist_create_playlist_button);
        enterTextField = (EditText) findViewById(R.id.layout_create_playlist_enter_text_field);

        createPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.playlistInterface.addPlaylist(new Playlist(enterTextField.getText().toString()));
                Toast.makeText(getApplicationContext(), "Created Playlist: " + enterTextField.getText().toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}

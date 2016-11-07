package edu.uco.map2016.mediaplayer;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;

import edu.uco.map2016.mediaplayer.api.MediaFile;

public class OpenActivity extends Activity {
    private static final String LOG_TAG = "TermProject";
    private static final int REQUEST_BROWSE_LEGACY_AUDIO = 1;
    private static final int REQUEST_BROWSE_LEGACY_VIDEO = 2;
    private static final int REQUEST_BROWSE = 3;
    private static final int REQUEST_BROWSE_AUDIO = 4;
    private static final int REQUEST_BROWSE_VIDEO = 5;
    private static final int REQUEST_STREAM = 6;
    private static final String MIME_AUDIO = "audio/*";
    private static final String MIME_VIDEO = "video/*";
    private static final String MIME_ANY = "*/*";
    private static final String[] MIME_TYPES = new String[]{MIME_AUDIO, MIME_VIDEO};

    private static final String STATE_TYPE = "edu.uco.map2016.mediaplayer.state_type";
    private static final String STATE_PATH = "edu.uco.map2016.mediaplayer.state_path";

    private RadioButton rdoAudio, rdoVideo, rdoBoth;
    private EditText edtUrl;

    private void restoreInstanceState(Bundle state) {
        if (state != null) {
            switch (state.getInt(STATE_TYPE, 0)) {
                case REQUEST_BROWSE_AUDIO:
                    rdoAudio.setChecked(true);
                    break;
                case REQUEST_BROWSE_VIDEO:
                    rdoVideo.setChecked(true);
                    break;
                case REQUEST_BROWSE:
                    if (rdoBoth.isEnabled())
                        rdoBoth.setChecked(true);
            }
            edtUrl.setText(state.getString(STATE_PATH, ""));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        rdoAudio = (RadioButton)findViewById(R.id.actOpen_rdgType_audio);
        rdoVideo = (RadioButton)findViewById(R.id.actOpen_rdgType_video);
        rdoBoth = (RadioButton)findViewById(R.id.actOpen_rdgType_both);

        // ACTION_OPEN_DOCUMENT is not available before 19.
        // The old API doesn't let us find both types at once, so set audio by default.
        if (Build.VERSION.SDK_INT < 19) {
            rdoAudio.setChecked(true);
        } else {
            rdoBoth.setEnabled(true);
            rdoBoth.setChecked(true);
        }
        Button btnBrowse = (Button)findViewById(R.id.actOpen_btnBrowse);
        btnBrowse.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT < 19) {
                try {
                    Intent intent;
                    if (rdoAudio.isChecked()) {
                        intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, REQUEST_BROWSE_LEGACY_AUDIO);
                    } else {
                        intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, REQUEST_BROWSE_LEGACY_VIDEO);
                    }
                } catch (ActivityNotFoundException ae) {
                    // TODO: implement custom file browser for API <19
                    Log.w(LOG_TAG, "TODO: implement custom file browser.");
                }
            } else {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                if (rdoAudio.isChecked()) {
                    intent.setType(MIME_AUDIO);
                    startActivityForResult(intent, REQUEST_BROWSE_AUDIO);
                } else if (rdoVideo.isChecked()) {
                    intent.setType(MIME_VIDEO);
                    startActivityForResult(intent, REQUEST_BROWSE_VIDEO);
                } else {
                    intent.setType(MIME_ANY);
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, MIME_TYPES);
                    startActivityForResult(intent, REQUEST_BROWSE);
                }
            }
        });
        edtUrl = (EditText)findViewById(R.id.actOpen_edtUrl);
        Button btnOpen = (Button)findViewById(R.id.actOpen_btnOpen);
        btnOpen.setOnClickListener(v -> {
            String uriText = edtUrl.getText().toString();
            if (!uriText.isEmpty()) {
                try {
                    URI test = new URI(uriText);
                } catch (URISyntaxException ue) {
                    Toast.makeText(OpenActivity.this,
                                   R.string.actOpen_err_invalidUri,
                                   Toast.LENGTH_LONG)
                         .show();
                    return;
                }
                MediaFile media = new MediaFile("Media File", Uri.parse(uriText), MediaFile.TYPE_NONE);
                Intent playerIntent = VideoActivity.getInstance(this, media);
                startActivity(playerIntent);
            }
        });
        restoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Intent playerIntent;
        if (intent != null && resultCode == RESULT_OK) {
            MediaFile media = null;
            switch(requestCode) {
                case REQUEST_BROWSE:
                    media = new MediaFile("Media File", intent.getData(), MediaFile.TYPE_NONE);
                    playerIntent = VideoActivity.getInstance(this, media);
                    break;
                case REQUEST_BROWSE_AUDIO:
                case REQUEST_BROWSE_LEGACY_AUDIO:
                    media = new MediaFile("Media File", intent.getData(), MediaFile.TYPE_AUDIO);
                    playerIntent = MusicActivity.getInstance(this, media);
                    break;
                case REQUEST_BROWSE_VIDEO:
                case REQUEST_BROWSE_LEGACY_VIDEO:
                    media = new MediaFile("Media File", intent.getData(), MediaFile.TYPE_VIDEO);
                    playerIntent = VideoActivity.getInstance(this, media);
                    break;
                default:
                    return;
            }
            startActivity(playerIntent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        if (rdoAudio.isChecked()) {
            state.putInt(STATE_TYPE, REQUEST_BROWSE_AUDIO);
        } else if (rdoVideo.isChecked()) {
            state.putInt(STATE_TYPE, REQUEST_BROWSE_VIDEO);
        } else {
            state.putInt(STATE_TYPE, REQUEST_BROWSE);
        }
        state.putString(STATE_PATH, edtUrl.getText().toString());
        super.onSaveInstanceState(state);
    }



}

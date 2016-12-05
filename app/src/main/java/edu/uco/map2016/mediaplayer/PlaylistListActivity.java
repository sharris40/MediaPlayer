package edu.uco.map2016.mediaplayer;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Vector;

import edu.uco.map2016.mediaplayer.PlaylistListViewFragment.ListSelectionListener;
import edu.uco.map2016.mediaplayer.api.MediaFile;

import static edu.uco.map2016.mediaplayer.MainActivity.playlistInterface;

public class PlaylistListActivity extends Activity implements ListSelectionListener, PlaylistItemDialogFragment.ManagePlaylistItemListener {
    private static final String LOG_TAG = "PlaylistListActivity";

    public static Vector<String> mListArray = new Vector<String>();

    public static int contactIndex;

    private FragmentManager mFragmentManager;
    private FrameLayout mTitleFrameLayout;

    private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final String TAG = "QuoteViewerActivity";

    public static final int STRING_CAPACITY = 40;

    private static final int CASE_PLAY_FILE = 0;
    private static final int CASE_REMOVE_FILE_FROM_PLAYLIST = 1;
    private static final int CASE_MOVE_UP_IN_PLAYLIST = 2;
    private static final int CASE_MOVE_DOWN_IN_PLAYLIST = 3;
    private static final int CASE_CANCEL = 4;

    public int resultIndex;
    public String playlistToView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_list);

        mListArray.removeAllElements();

        playlistToView = getIntent().getStringExtra("PLAYLIST");

        for (int cntr = 0; cntr < playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().size(); cntr++) {
            if (MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(cntr).getName().length() > STRING_CAPACITY) {
                if (MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(cntr).getDetails().length() > STRING_CAPACITY) {
                    mListArray.add(MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(cntr).getName().substring(0,STRING_CAPACITY) + "..." + "\n" + MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(cntr).getDetails().substring(0,STRING_CAPACITY) + "...");
                }
                else {
                    mListArray.add(MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(cntr).getName().substring(0,STRING_CAPACITY) + "..." + "\n" + MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(cntr).getDetails());
                }
            }
            else {
                if (MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(cntr).getDetails().length() > STRING_CAPACITY) {
                    mListArray.add(MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(cntr).getName() + "\n" + MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(cntr).getDetails().substring(0,STRING_CAPACITY) + "...");
                }
                else {
                    mListArray.add(MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(cntr).getName() + "\n" + MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(cntr).getDetails());
                }
            }
        }

        setContentView(R.layout.activity_playlist_list);

        mTitleFrameLayout = (FrameLayout) findViewById(R.id.listViewPlaylistListLayout);

        mFragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.listViewPlaylistListLayout, new PlaylistListViewFragment());
        fragmentTransaction.commit();

        mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                setLayout();
            }
        });

    }

    private void setLayout() {
        mTitleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)); // width, height
    }



    @Override
    public void onListSelection(int index) {
        contactIndex = index;
    }

    @Override
    public void onManagePlaylistItemSelection(int optionIndex, DialogFragment dialog) {
        switch(optionIndex) {
            case CASE_PLAY_FILE:
                MediaFile file = MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(resultIndex);

                Intent playIntent;
                if (file.getType() == MediaFile.TYPE_AUDIO) {
                    playIntent = MusicActivity.getInstance(this, file);
                } else {
                    playIntent = VideoActivity.getInstance(this, file);
                }
                startActivity(playIntent);
                break;
            case CASE_REMOVE_FILE_FROM_PLAYLIST:
                Toast.makeText(getApplicationContext(), "Removed File: " + MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(resultIndex).getName() + " From Playlist: " + MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getName(), Toast.LENGTH_SHORT).show();
                playlistInterface.removeMediaFile(Integer.parseInt(playlistToView),resultIndex);
                MainActivity.playlistInterface.saveToFile(this);
                finish();
                break;
            case CASE_MOVE_UP_IN_PLAYLIST:
                Toast.makeText(getApplicationContext(), "Moved File Up: " + MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(resultIndex).getName() + " From Playlist: " + MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getName(), Toast.LENGTH_SHORT).show();
                playlistInterface.moveUpMediaFile(Integer.parseInt(playlistToView),resultIndex);
                MainActivity.playlistInterface.saveToFile(this);
                finish();
                break;
            case CASE_MOVE_DOWN_IN_PLAYLIST:
                Toast.makeText(getApplicationContext(), "Moved File Down: " + MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(resultIndex).getName() + " From Playlist: " + MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getName(), Toast.LENGTH_SHORT).show();
                playlistInterface.moveDownMediaFile(Integer.parseInt(playlistToView),resultIndex);
                MainActivity.playlistInterface.saveToFile(this);
                finish();
                break;
            case CASE_CANCEL:

                break;
        }
    }

}

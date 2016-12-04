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

import edu.uco.map2016.mediaplayer.PlaylistDialogFragment.ManagePlaylistListener;
import edu.uco.map2016.mediaplayer.SuperPlaylistListViewFragment.ListSelectionListener;
import edu.uco.map2016.mediaplayer.api.Playlist;

import static edu.uco.map2016.mediaplayer.MainActivity.playlistInterface;

public class SuperPlaylistListActivity extends Activity implements ListSelectionListener, ManagePlaylistListener {

    public static Vector<String> mListArray = new Vector<String>();

    public static int contactIndex;

    private FragmentManager mFragmentManager;
    private FrameLayout mTitleFrameLayout;

    private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final String TAG = "QuoteViewerActivity";

    public static final int STRING_CAPACITY = 40;

    private static final int CASE_PLAY_PLAYLIST = 0;
    private static final int CASE_VIEW_PLAYLIST = 1;
    private static final int CASE_SHUFFLE_PLAYLIST = 2;
    private static final int CASE_DELETE_PLAYLIST = 3;
    private static final int CASE_CANCEL = 4;

    public int resultIndex;

    public static final String RESULT_SHOWN = "result_is_shown";

    public static String wasAnswerShown(Intent result2) {
        return result2.getStringExtra(RESULT_SHOWN);
    }

    public void setAnswerShowResult(String result) {
        Intent data = new Intent();
        data.putExtra(RESULT_SHOWN, result);
        setResult(RESULT_CANCELED, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_playlist_list);

        playlistInterface.retrieveListOfPlaylists();

        mListArray.removeAllElements();


        for (int cntr = 0; cntr < playlistInterface.getListOfPlaylists().size(); cntr++) {
            if (playlistInterface.getListOfPlaylists().get(cntr).getName().length() > STRING_CAPACITY) {
                mListArray.add(playlistInterface.getListOfPlaylists().get(cntr).getName().substring(0,STRING_CAPACITY) + "\n");
            }
            else {
                mListArray.add(playlistInterface.getListOfPlaylists().get(cntr).getName() + "\n");
            }
        }

        setContentView(R.layout.activity_super_playlist_list);

        mTitleFrameLayout = (FrameLayout) findViewById(R.id.listViewSuperPlaylistListLayout);

        mFragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.listViewSuperPlaylistListLayout, new SuperPlaylistListViewFragment());
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
    public void onManagePlaylistSelection(int optionIndex, DialogFragment dialog) {
        switch(optionIndex) {
            case CASE_PLAY_PLAYLIST:
                Playlist playlist = MainActivity.playlistInterface.getListOfPlaylists().get(resultIndex);
                startActivity(MusicActivity.getInstance(this, playlist));
                break;
            case CASE_VIEW_PLAYLIST:
                Intent activityPlayListList = new Intent(SuperPlaylistListActivity.this, PlaylistListActivity.class); //getApplicationContext()
                activityPlayListList.putExtra("PLAYLIST", Integer.toString(resultIndex));
                startActivity(activityPlayListList);
                break;
            case CASE_SHUFFLE_PLAYLIST:
                Toast.makeText(getApplicationContext(), "Shuffled Playlist: " + MainActivity.playlistInterface.getListOfPlaylists().get(resultIndex).getName(), Toast.LENGTH_SHORT).show();
                playlistInterface.shufflePlaylist(resultIndex);
                MainActivity.playlistInterface.saveToFile(this);
                finish();
                break;
            case CASE_DELETE_PLAYLIST:
                Toast.makeText(getApplicationContext(), "Deleted Playlist: " + MainActivity.playlistInterface.getListOfPlaylists().get(resultIndex).getName(), Toast.LENGTH_SHORT).show();
                playlistInterface.removePlaylist(resultIndex);
                MainActivity.playlistInterface.saveToFile(this);
                finish();
                break;
            case CASE_CANCEL:

                break;
        }
    }

}

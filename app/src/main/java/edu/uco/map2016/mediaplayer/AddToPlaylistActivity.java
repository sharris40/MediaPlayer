package edu.uco.map2016.mediaplayer;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.Vector;

public class AddToPlaylistActivity extends Activity
        implements AddToPlaylistViewFragment.ListSelectionListener {

    //TextView searchResultView;

    //public static String[] mListArray;
    public static Vector<String> mListArray = new Vector<String>();
    public static Vector<Integer> mListArrayIndexForSeatchInterface = new Vector<Integer>();

    public static int contactIndex;

    private FragmentManager mFragmentManager;
    private FrameLayout mTitleFrameLayout;

    private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final String TAG = "AddToPlaylistActivity";

    public static final int STRING_CAPACITY = 40;

    public static final int CASE_NOT_A_MENU_OPTION = -1;
    public static final int CASE_SEE_ALL_MUSIC = -2;
    public static final int CASE_SEE_ALL_VIDEO = -3;

    private static final int CASE_PLAY_FILE = 0;
    private static final int CASE_ADD_TO_PLAYLIST = 1;
    private static final int CASE_CANCEL = 2;

    public static final String RESULT_SHOWN = "result_shown";

    public static String getResultShown(Intent result2) {
        return result2.getStringExtra(RESULT_SHOWN);
    }

    public void setResultShown(String result) {
        Intent data = new Intent();
        data.putExtra(RESULT_SHOWN, result);
        setResult(RESULT_OK, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_playlist);

        MainActivity.playlistInterface.retrieveListOfPlaylists();

        mListArray.removeAllElements();


        for (int cntr = 0; cntr < MainActivity.playlistInterface.getListOfPlaylists().size(); cntr++) {
            if (MainActivity.playlistInterface.getListOfPlaylists().get(cntr).getName().length() > STRING_CAPACITY) {
                mListArray.add(MainActivity.playlistInterface.getListOfPlaylists().get(cntr).getName().substring(0,STRING_CAPACITY) + "\n");
            }
            else {
                mListArray.add(MainActivity.playlistInterface.getListOfPlaylists().get(cntr).getName() + "\n");
            }
        }

        setContentView(R.layout.activity_add_to_playlist);

        mTitleFrameLayout = (FrameLayout) findViewById(R.id.listViewAddToPlaylistLayout);

        mFragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.listViewAddToPlaylistLayout, new AddToPlaylistViewFragment());
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

}

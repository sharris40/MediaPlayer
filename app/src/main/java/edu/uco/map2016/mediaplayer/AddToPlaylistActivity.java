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

    //private final DetailsViewFragment mQuoteFragment = new DetailsViewFragment();
    private FragmentManager mFragmentManager;
    private FrameLayout mTitleFrameLayout, mQuotesFrameLayout;

    private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final String TAG = "QuoteViewerActivity";

    public static final int STRING_CAPACITY = 40;

    public static final int CASE_NOT_A_MENU_OPTION = -1;
    public static final int CASE_SEE_ALL_MUSIC = -2;
    public static final int CASE_SEE_ALL_VIDEO = -3;

    private static final int CASE_PLAY_FILE = 0;
    private static final int CASE_ADD_TO_PLAYLIST = 1;
    private static final int CASE_CANCEL = 2;

    public static final String RESULT_SHOWN = "result_is_shown";
    //public static final String RESULT_IS_TRUE = "result_is_true";

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

        /*
        for (int cntr = 0; cntr < searchInterface.getListResult().size(); cntr++) {
            mListArray.add(searchInterface.getListResult().get(cntr).getName());
        }*/

        setContentView(R.layout.activity_add_to_playlist);

        mTitleFrameLayout = (FrameLayout) findViewById(R.id.listViewAddToPlaylistLayout);
        //mQuotesFrameLayout = (FrameLayout) findViewById(R.id.detailsViewMyContactsLayout);

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
        //if (!mQuoteFragment.isAdded()) {
        mTitleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)); // width, height
        mQuotesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0, MATCH_PARENT));

        /*} else {
            mTitleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                    MATCH_PARENT, 1f)); // width, height, weight
            mQuotesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                    MATCH_PARENT, 2f));
        }*/
    }



    @Override
    public void onListSelection(int index) {
        //Toast.makeText(getApplicationContext(), Integer.toString(index), Toast.LENGTH_SHORT).show();
        //if (!mQuoteFragment.isAdded()) {
        /*    FragmentTransaction fragmentTransaction = mFragmentManager
                    .beginTransaction();
            //fragmentTransaction.add(R.id.detailsViewMyContactsLayout, mQuoteFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            mFragmentManager.executePendingTransactions();
*/
        //Toast.makeText(ListActivity.this, "It Worked!", Toast.LENGTH_SHORT).show();

        //}
        /*if (mQuoteFragment.getShownIndex() != index) {
            mQuoteFragment.showIndex(index);
        }*/
        contactIndex = index;
    }

}

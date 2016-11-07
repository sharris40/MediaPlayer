package edu.uco.map2016.mediaplayer;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Vector;

import edu.uco.map2016.mediaplayer.ListViewFragment.ListSelectionListener;
import edu.uco.map2016.mediaplayer.api.MediaFile;
import edu.uco.map2016.mediaplayer.api.SearchInterface;

public class ListActivity extends Activity implements ListSelectionListener {
    private static final String LOG_TAG = "ListActivity";

    //TextView searchResultView;

    public static SearchInterface searchInterface;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Log.d(LOG_TAG, "onCreate");

        final String textToSearch = getIntent().getStringExtra("SEARCH");
        final ArrayList<MediaFile> onlineFiles = getIntent().getParcelableArrayListExtra("ONLINE_RESULTS");

        searchInterface = new SearchInterface();

        searchInterface.search(textToSearch);

        mListArray.removeAllElements();

        mListArray.add("\n");
        mListArrayIndexForSeatchInterface.add(CASE_NOT_A_MENU_OPTION);

        mListArray.add("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tMusic\n");
        mListArrayIndexForSeatchInterface.add(CASE_NOT_A_MENU_OPTION);

        for (int cntr = 0, cntr2 = 0; (cntr < searchInterface.getListResult().size()) && cntr2 < 20; cntr++) {
            if (searchInterface.getListResult().get(cntr).getType() == MediaFile.TYPE_AUDIO) {
                if (searchInterface.getListResult().get(cntr).getName().length() > STRING_CAPACITY) {
                    if (searchInterface.getListResult().get(cntr).getDetails().length() > STRING_CAPACITY) {
                        mListArray.add(searchInterface.getListResult().get(cntr).getName().substring(0,STRING_CAPACITY) + "..." + "\n" + searchInterface.getListResult().get(cntr).getDetails().substring(0,STRING_CAPACITY) + "...");
                    }
                    else {
                        mListArray.add(searchInterface.getListResult().get(cntr).getName().substring(0,STRING_CAPACITY) + "..." + "\n" + searchInterface.getListResult().get(cntr).getDetails());
                    }
                }
                else {
                    if (searchInterface.getListResult().get(cntr).getDetails().length() > STRING_CAPACITY) {
                        mListArray.add(searchInterface.getListResult().get(cntr).getName() + "\n" + searchInterface.getListResult().get(cntr).getDetails().substring(0,STRING_CAPACITY) + "...");
                    }
                    else {
                        mListArray.add(searchInterface.getListResult().get(cntr).getName() + "\n" + searchInterface.getListResult().get(cntr).getDetails());
                    }
                }
                mListArrayIndexForSeatchInterface.add(cntr);
                cntr2++;
            }
        }

        //mListArray.add("See All Music");
        //mListArrayIndexForSeatchInterface.add(CASE_SEE_ALL_MUSIC);

        mListArray.add("\n");
        mListArrayIndexForSeatchInterface.add(CASE_NOT_A_MENU_OPTION);

        mListArray.add("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tVideo\n");
        mListArrayIndexForSeatchInterface.add(CASE_NOT_A_MENU_OPTION);

        for (int cntr = 0, cntr2 = 0; (cntr < searchInterface.getListResult().size()) && cntr2 < 20; cntr++) {
            if (searchInterface.getListResult().get(cntr).getType() == MediaFile.TYPE_VIDEO) {
                if (searchInterface.getListResult().get(cntr).getName().length() > STRING_CAPACITY) {
                    if (searchInterface.getListResult().get(cntr).getDetails().length() > STRING_CAPACITY) {
                        mListArray.add(searchInterface.getListResult().get(cntr).getName().substring(0,STRING_CAPACITY) + "..." + "\n" + searchInterface.getListResult().get(cntr).getDetails().substring(0,STRING_CAPACITY) + "...");
                    }
                    else {
                        mListArray.add(searchInterface.getListResult().get(cntr).getName().substring(0,STRING_CAPACITY) + "..." + "\n" + searchInterface.getListResult().get(cntr).getDetails());
                    }
                }
                else {
                    if (searchInterface.getListResult().get(cntr).getDetails().length() > STRING_CAPACITY) {
                        mListArray.add(searchInterface.getListResult().get(cntr).getName() + "\n" + searchInterface.getListResult().get(cntr).getDetails().substring(0,STRING_CAPACITY) + "...");
                    }
                    else {
                        mListArray.add(searchInterface.getListResult().get(cntr).getName() + "\n" + searchInterface.getListResult().get(cntr).getDetails());
                    }
                }
                mListArrayIndexForSeatchInterface.add(cntr);
                cntr2++;
            }
        }

        mListArray.add("\n");
        mListArrayIndexForSeatchInterface.add(CASE_NOT_A_MENU_OPTION);

        mListArray.add("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tOnline\n");
        mListArrayIndexForSeatchInterface.add(CASE_NOT_A_MENU_OPTION);

        for (int cntr = 0, cntr2 = 0; (cntr < onlineFiles.size()) && cntr2 < 20; cntr++) {
            if (onlineFiles.get(cntr).getType() == MediaFile.TYPE_AUDIO) {
                if (onlineFiles.get(cntr).getName().length() > STRING_CAPACITY) {
                    if (onlineFiles.get(cntr).getDetails().length() > STRING_CAPACITY) {
                        mListArray.add(onlineFiles.get(cntr).getName().substring(0,STRING_CAPACITY) + "..." + "\n" + onlineFiles.get(cntr).getDetails().substring(0,STRING_CAPACITY) + "...");
                    }
                    else {
                        mListArray.add(onlineFiles.get(cntr).getName().substring(0,STRING_CAPACITY) + "..." + "\n" + onlineFiles.get(cntr).getDetails());
                    }
                }
                else {
                    if (onlineFiles.get(cntr).getDetails().length() > STRING_CAPACITY) {
                        mListArray.add(onlineFiles.get(cntr).getName() + "\n" + onlineFiles.get(cntr).getDetails().substring(0,STRING_CAPACITY) + "...");
                    }
                    else {
                        mListArray.add(onlineFiles.get(cntr).getName() + "\n" + onlineFiles.get(cntr).getDetails());
                    }
                }
                mListArrayIndexForSeatchInterface.add(cntr);
                cntr2++;
            }
        }

        //mListArray.add("See All Video");
        //mListArrayIndexForSeatchInterface.add(CASE_SEE_ALL_VIDEO);

        mListArray.add("\n");
        mListArrayIndexForSeatchInterface.add(CASE_NOT_A_MENU_OPTION);

        /*
        for (int cntr = 0; cntr < searchInterface.getListResult().size(); cntr++) {
            mListArray.add(searchInterface.getListResult().get(cntr).getName());
        }*/

        setContentView(R.layout.activity_list);

        mTitleFrameLayout = (FrameLayout) findViewById(R.id.listViewMyContactsLayout);
        //mQuotesFrameLayout = (FrameLayout) findViewById(R.id.detailsViewMyContactsLayout);

        mFragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.listViewMyContactsLayout, new ListViewFragment());
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

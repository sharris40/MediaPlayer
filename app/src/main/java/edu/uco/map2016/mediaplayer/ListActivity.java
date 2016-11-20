package edu.uco.map2016.mediaplayer;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import edu.uco.map2016.mediaplayer.ListViewFragment.ListSelectionListener;
import edu.uco.map2016.mediaplayer.api.MediaFile;
import edu.uco.map2016.mediaplayer.api.SearchInterface;
import edu.uco.map2016.mediaplayer.api.SearchQuery;
import edu.uco.map2016.mediaplayer.api.SearchResults;
import edu.uco.map2016.mediaplayer.services.SearchService;

//TODO: Rename this class so it doesn't match Android's ListActivity.
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
    private ListViewFragment mFragment;

    private static class SavedSearchListener implements SearchService.SearchListener {
        private final LinkedList<SearchResults> mResults = new LinkedList<>();
        private final int mRequestCode;

        SavedSearchListener(int requestCode) {
            mRequestCode = requestCode;
        }

        @Override
        public void onSearchResults(SearchResults results) {
            Log.d(LOG_TAG, "received results from " + results.getSource());
            mResults.push(results);
        }

        public int getRequestCode() {
            return mRequestCode;
        }

        public List<SearchResults> getResults() {
            return mResults;
        }
    }

    private SearchQuery mQuery;
    private String mSearchString;
    private int mRequestCode;
    private boolean mSearchInProgress;
    private Handler mHandler;

    private class SearchConnection implements ServiceConnection {
        SearchService mService = null;
        private SearchService.SearchListener mListener = (results -> {
            Log.d(LOG_TAG, "Listener called; results " + ((results == null) ? "null" : "from " + results.getSource()));
            mHandler.post(() -> mFragment.addResults(results));
        });

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SearchService.SearchBinder binder = (SearchService.SearchBinder)service;
            mService = binder.getService();
            if (mSearchInProgress) {
                SearchService.SearchListener listener = mService.getListener(mRequestCode);
                if (listener instanceof SavedSearchListener) {
                    mService.setListener(mRequestCode, mListener);
                    SavedSearchListener savedListener = (SavedSearchListener)listener;
                    for (SearchResults results : savedListener.getResults()) {
                        mHandler.post(() ->
                                mFragment.addResults(results));
                    }
                } else {
                    mRequestCode = mService.search(mQuery, mListener);
                }
            } else {
                mRequestCode = mService.search(mQuery, mListener);
                mSearchInProgress = true;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mFragment.finishSearch();
            mSearchConnection = null;
        }

        void disconnect() {
            if (mService != null) {
                SavedSearchListener listener = new SavedSearchListener(mRequestCode);
                mService.setListener(mRequestCode, new SavedSearchListener(mRequestCode));
                for(SearchResults result : mFragment.getResults()) {
                    listener.onSearchResults(result);
                }
                unbindService(this);
            }
        }

    }

    private SearchConnection mSearchConnection;

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

        mHandler = new Handler(getMainLooper());

        final String textToSearch = getIntent().getStringExtra("SEARCH");
        mSearchString = textToSearch;

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

        /*
        for (int cntr = 0; cntr < searchInterface.getListResult().size(); cntr++) {
            mListArray.add(searchInterface.getListResult().get(cntr).getName());
        }*/

        setContentView(R.layout.activity_list);

        mTitleFrameLayout = (FrameLayout) findViewById(R.id.listViewMyContactsLayout);
        //mQuotesFrameLayout = (FrameLayout) findViewById(R.id.detailsViewMyContactsLayout);

        mFragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        mFragment = new ListViewFragment();
        fragmentTransaction.add(R.id.listViewMyContactsLayout, mFragment);
        fragmentTransaction.commit();

        Intent searchIntent = new Intent(this, SearchService.class);
        mSearchConnection = new SearchConnection();
        startService(searchIntent);

        if (savedInstanceState != null) {
            String savedSearch = savedInstanceState.getString("SEARCH");
            mSearchInProgress = (savedSearch != null && savedSearch.equals(mSearchString));
        } else {
            mSearchInProgress = false;
        }

        if (mSearchInProgress) {
            mRequestCode = savedInstanceState.getInt("REQUEST");
        }

        mQuery = new SearchQuery();
        mQuery.setQuery(mSearchString);

        bindService(searchIntent, mSearchConnection, 0);

        mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                setLayout();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        if (mSearchInProgress) {
            state.putString("SEARCH", mSearchString);
            state.putInt("REQUEST", mRequestCode);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSearchConnection != null) {
            mSearchConnection.disconnect();
        }
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

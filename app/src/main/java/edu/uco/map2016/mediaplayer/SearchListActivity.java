package edu.uco.map2016.mediaplayer;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import edu.uco.map2016.mediaplayer.SearchListViewFragment.ListSelectionListener;
import edu.uco.map2016.mediaplayer.api.MediaFile;
import edu.uco.map2016.mediaplayer.api.SearchQuery;
import edu.uco.map2016.mediaplayer.api.SearchResults;
import edu.uco.map2016.mediaplayer.services.SearchService;

public class SearchListActivity extends Activity implements ListSelectionListener, AddToPlaylistDialogFragment.PickAddToPlaylistListener {
    private static final String LOG_TAG = "SearchListActivity";
    private static final String FRAGMENT_TAG
            = "edu.uco.map2016.mediaplayer.SearchListActivity.fragment";

    //TextView searchResultView;

    //public static String[] mListArray;
    public static Vector<SearchListItem> mListArray = new Vector<>();
    //public static Vector<Integer> mListArrayIndexForSeatchInterface = new Vector<Integer>();

    public static int contactIndex;

    //private final DetailsViewFragment mQuoteFragment = new DetailsViewFragment();
    private FragmentManager mFragmentManager;
    private FrameLayout mTitleFrameLayout, mQuotesFrameLayout;
    private SearchListViewFragment mFragment;

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

    protected static class SearchListItem implements Parcelable {
        private static final int IS_FILE = 0;
        private static final int IS_HEADER = 1;

        private final MediaFile mFile;
        private final String mHeader;

        public SearchListItem(@NonNull String header) {
            mHeader = header;
            mFile = null;
        }

        public SearchListItem(@NonNull MediaFile file) {
            mFile = file;
            mHeader = null;
        }

        public MediaFile getFile() {
            return mFile;
        }

        public String getHeader() {
            return mHeader;
        }

        public boolean isHeader() {
            return mHeader != null;
        }

        @Override
        public String toString() {
            if (isHeader())
                return mHeader;
            if (mFile.getName().length() > STRING_CAPACITY) {
                if (mFile.getDetails().length() > STRING_CAPACITY) {
                    return mFile.getName().substring(0,STRING_CAPACITY) + "\u2026\n" + mFile.getDetails().substring(0,STRING_CAPACITY) + "\u2026";
                }
                else {
                    return mFile.getName().substring(0,STRING_CAPACITY) + "\u2026\n" + mFile.getDetails();
                }
            }
            else {
                if (mFile.getDetails().length() > STRING_CAPACITY) {
                    return mFile.getName() + "\n" + mFile.getDetails().substring(0,STRING_CAPACITY) + "\u2026";
                }
                else {
                    return mFile.getName() + "\n" + mFile.getDetails();
                }
            }
        }

        protected SearchListItem(Parcel in) {
            int header = in.readInt();
            if (header == IS_HEADER) {
                mHeader = in.readString();
                mFile = null;
            } else {
                mFile = in.readParcelable(MediaFile.class.getClassLoader());
                mHeader = null;
            }
        }

        public static final Creator<SearchListItem> CREATOR = new Creator<SearchListItem>() {
            @Override
            public SearchListItem createFromParcel(Parcel in) {
                return new SearchListItem(in);
            }

            @Override
            public SearchListItem[] newArray(int size) {
                return new SearchListItem[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (mHeader != null) {
                dest.writeInt(IS_HEADER);
                dest.writeString(mHeader);
            } else {
                dest.writeInt(IS_FILE);
                dest.writeParcelable(mFile, flags);
            }
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

    //public static final int CASE_NOT_A_MENU_OPTION = -1;
    //public static final int CASE_SEE_ALL_MUSIC = -2;
    //public static final int CASE_SEE_ALL_VIDEO = -3;
    //public static final int CASE_FILE_IN_FRAGMENT = -4;

    private static final int CASE_PLAY_FILE = 0;
    private static final int CASE_ADD_TO_PLAYLIST = 1;
    private static final int CASE_CANCEL = 2;

    private final int REQUEST_CODE = 0;

    public int resultIndex;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) { return;
        }
        if (requestCode == REQUEST_CODE) {
            if (data == null) {
                return;
            }
            String resultDisplay = AddToPlaylistActivity.getResultShown(data);
            //Toast.makeText(getApplicationContext(), resultDisplay, Toast.LENGTH_SHORT).show();
            MainActivity.playlistInterface.addMediaFile(Integer.parseInt(resultDisplay), mListArray.get(resultIndex).getFile());
            MainActivity.playlistInterface.saveToFile(this);
            Toast.makeText(getApplicationContext(), "Added File: " + mListArray.get(resultIndex).getFile().getName() + " to Playlist: " + MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(resultDisplay)).getName(), Toast.LENGTH_SHORT).show();
            //labelResult.setText(resultDisplay);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Log.d(LOG_TAG, "onCreate");

        mHandler = new Handler(getMainLooper());

        final String textToSearch = getIntent().getStringExtra("SEARCH");
        mSearchString = textToSearch;

        MainActivity.searchInterface.search(textToSearch);

        mListArray.removeAllElements();

        mListArray.add(new SearchListItem("\n"));
        //mListArrayIndexForSeatchInterface.add(CASE_NOT_A_MENU_OPTION);

        mListArray.add(new SearchListItem("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tMusic\n"));
        //mListArrayIndexForSeatchInterface.add(CASE_NOT_A_MENU_OPTION);

        for (int cntr = 0, cntr2 = 0; (cntr < MainActivity.searchInterface.getListResult().size()) && cntr2 < 20; cntr++) {
            if (MainActivity.searchInterface.getListResult().get(cntr).getType() == MediaFile.TYPE_AUDIO) {
                mListArray.add(new SearchListItem(MainActivity.searchInterface.getListResult().get(cntr)));
                //mListArrayIndexForSeatchInterface.add(cntr);
                cntr2++;
            }
        }

        //mListArray.add("See All Music");
        //mListArrayIndexForSeatchInterface.add(CASE_SEE_ALL_MUSIC);

        mListArray.add(new SearchListItem("\n"));
        //mListArrayIndexForSeatchInterface.add(CASE_NOT_A_MENU_OPTION);

        mListArray.add(new SearchListItem("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tVideo\n"));
        //mListArrayIndexForSeatchInterface.add(CASE_NOT_A_MENU_OPTION);

        for (int cntr = 0, cntr2 = 0; (cntr < MainActivity.searchInterface.getListResult().size()) && cntr2 < 20; cntr++) {
            if (MainActivity.searchInterface.getListResult().get(cntr).getType() == MediaFile.TYPE_VIDEO) {
                mListArray.add(new SearchListItem(MainActivity.searchInterface.getListResult().get(cntr)));
                //mListArrayIndexForSeatchInterface.add(cntr);
                cntr2++;
            }
        }

        mListArray.add(new SearchListItem("\n"));
        //mListArrayIndexForSeatchInterface.add(CASE_NOT_A_MENU_OPTION);

        /*
        for (int cntr = 0; cntr < searchInterface.getListResult().size(); cntr++) {
            mListArray.add(searchInterface.getListResult().get(cntr).getName());
        }*/

        setContentView(R.layout.activity_list);

        mTitleFrameLayout = (FrameLayout) findViewById(R.id.listViewMyContactsLayout);
        //mQuotesFrameLayout = (FrameLayout) findViewById(R.id.detailsViewMyContactsLayout);

        mFragmentManager = getFragmentManager();

        Intent searchIntent = new Intent(this, SearchService.class);
        mSearchConnection = new SearchConnection();
        startService(searchIntent);

        if (savedInstanceState != null) {
            ArrayList<SearchListItem> items = savedInstanceState.getParcelableArrayList("LIST");
            if (items != null) {
                mListArray = new Vector<>(items);
            }
            String savedSearch = savedInstanceState.getString("SEARCH");
            mSearchInProgress = (savedSearch != null && savedSearch.equals(mSearchString));
            mFragment = (SearchListViewFragment)mFragmentManager.findFragmentByTag(FRAGMENT_TAG);

            if (mSearchInProgress) {
                mRequestCode = savedInstanceState.getInt("REQUEST");
            }
        } else {
            mSearchInProgress = false;
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            mFragment = new SearchListViewFragment();
            fragmentTransaction.add(R.id.listViewMyContactsLayout, mFragment, FRAGMENT_TAG);
            fragmentTransaction.commit();
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
        state.putParcelableArrayList("LIST", new ArrayList<>(mListArray));
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
        //Toast.makeText(SearchListActivity.this, "It Worked!", Toast.LENGTH_SHORT).show();

        //}
        /*if (mQuoteFragment.getShownIndex() != index) {
            mQuoteFragment.showIndex(index);
        }*/
        Log.d(LOG_TAG, "ListSelection: " + index);
        contactIndex = index;
    }

    @Override
    public void onPickAddToPlaylistClick(int optionIndex, DialogFragment dialog) {
        switch(optionIndex) {
            case CASE_PLAY_FILE:
                MediaFile file = mListArray.get(resultIndex).getFile();

                Intent playIntent;
                if (file.getType() == MediaFile.TYPE_AUDIO) {
                    playIntent = MusicActivity.getInstance(this, file);
                } else {
                    playIntent = VideoActivity.getInstance(this, file);
                }
                startActivity(playIntent);
                break;
            case CASE_ADD_TO_PLAYLIST:
                Intent activityAddToPlayList = new Intent(SearchListActivity.this, AddToPlaylistActivity.class); //getApplicationContext()
                startActivityForResult(activityAddToPlayList, REQUEST_CODE);
                break;
            case CASE_CANCEL:

                break;
        }
    }
}

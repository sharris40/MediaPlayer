package edu.uco.map2016.mediaplayer;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import edu.uco.map2016.mediaplayer.api.MediaFile;
import edu.uco.map2016.mediaplayer.api.SearchResults;

import static edu.uco.map2016.mediaplayer.SearchListActivity.mListArray;

public class SearchListViewFragment extends ListFragment {

    public TextView mListView;

    private static final String LOG_TAG = "TitlesFragment";

    private ArrayAdapter<SearchListActivity.SearchListItem> mAdapter;

    private final LinkedList<SearchResults> mResults = new LinkedList<>();
    private final SparseIntArray mListArrayIndexForSearchResults = new SparseIntArray();
    private int mResultIndex = -1;

    public interface ListSelectionListener {
        public void onListSelection(int index);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        ((SearchListActivity)getActivity()).resultIndex = pos;
        AddToPlaylistDialogFragment d = new AddToPlaylistDialogFragment();
        d.show(getFragmentManager(), "colors");
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        mAdapter = new ArrayAdapter<>(getActivity(),R.layout.fragment_list_view, mListArray);
        setListAdapter(mAdapter);
        getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
    }

    public void addResults(SearchResults results) {
        Log.d(LOG_TAG, "addResults start");
        mResults.push(results);

        mListArray.add(new SearchListActivity.SearchListItem("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + getResources().getString(R.string.frgListView_source, results.getSource()) + "\n"));

        List<MediaFile> onlineFiles = results.getResults();

        for (int cntr = 0, cntr2 = 0; (cntr < onlineFiles.size()) && cntr2 < 20; cntr++) {
            mListArray.add(new SearchListActivity.SearchListItem(onlineFiles.get(cntr)));
            cntr2++;
        }

        mListArray.add(new SearchListActivity.SearchListItem("\n"));

        mAdapter.notifyDataSetChanged();

        Log.d(LOG_TAG, "addResults finish");
    }

    public List<SearchResults> getResults() {
        return mResults;
    }

    public void finishSearch() {
        // TODO: Add logic
    }
}

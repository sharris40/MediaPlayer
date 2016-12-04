package edu.uco.map2016.mediaplayer;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AddToPlaylistViewFragment extends ListFragment {

    public TextView mListView;

    private static final String TAG = "TitlesFragment";
    private ListSelectionListener mListener = null;

    public interface ListSelectionListener {
        public void onListSelection(int index);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        ((AddToPlaylistActivity) getActivity()).setResultShown(Integer.toString(pos));
        ((AddToPlaylistActivity) getActivity()).finish();
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.fragment_add_to_playlist_view, AddToPlaylistActivity.mListArray));
        getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);

        try {
            mListener = (ListSelectionListener) getActivity();
        }
        catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnArticleSelectedListener");
        }
    }
}

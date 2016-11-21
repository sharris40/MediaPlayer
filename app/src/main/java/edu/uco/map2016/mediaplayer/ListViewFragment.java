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

import static edu.uco.map2016.mediaplayer.ListActivity.CASE_FILE_IN_FRAGMENT;
import static edu.uco.map2016.mediaplayer.ListActivity.CASE_NOT_A_MENU_OPTION;
import static edu.uco.map2016.mediaplayer.ListActivity.STRING_CAPACITY;
import static edu.uco.map2016.mediaplayer.ListActivity.mListArray;
import static edu.uco.map2016.mediaplayer.ListActivity.mListArrayIndexForSeatchInterface;

public class ListViewFragment extends ListFragment {

    public TextView mListView;

    private static final String TAG = "TitlesFragment";
    private ListSelectionListener mListener = null;

    private ArrayAdapter<String> mAdapter;

    private final LinkedList<SearchResults> mResults = new LinkedList<>();
    private final SparseIntArray mListArrayIndexForSearchResults = new SparseIntArray();
    private int mResultIndex = -1;

    public interface ListSelectionListener {
        public void onListSelection(int index);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        //getListView().setItemChecked(pos, true);
        //mListener.onListSelection(pos);
        if ((mListArrayIndexForSeatchInterface.get(pos) >= 0) && (mListArrayIndexForSeatchInterface.get(pos) < mListArray.size())) {
            //Toast.makeText(getActivity().getApplicationContext(), ListActivity.searchInterface.getListResult().get(mListArrayIndexForSeatchInterface.get(pos)).getFileLocationAddress().toString(), Toast.LENGTH_SHORT).show();
            ((ListActivity)getActivity()).resultIndex = ListActivity.mListArrayIndexForSeatchInterface.get(pos);
            AddToPlaylistDialogueFragment d = new AddToPlaylistDialogueFragment();
            d.show(getFragmentManager(), "colors");
        }
        else if (mListArrayIndexForSeatchInterface.get(pos) == CASE_FILE_IN_FRAGMENT) {
            ((ListActivity)getActivity()).resultIndex = CASE_FILE_IN_FRAGMENT;
            mResultIndex = mListArrayIndexForSearchResults.get(pos);
            AddToPlaylistDialogueFragment d = new AddToPlaylistDialogueFragment();
            d.show(getFragmentManager(), "colors");
        }
        else {
            switch (mListArrayIndexForSeatchInterface.get(pos)) {
                case CASE_NOT_A_MENU_OPTION:
                    //do nothing
                    break;
                case ListActivity.CASE_SEE_ALL_MUSIC:
                    //open SeeAllMusic Activity
                    break;
                case ListActivity.CASE_SEE_ALL_VIDEO:
                    //open SeeAllVideo Activity
                    break;
            }
        }

        //Toast.makeText(getActivity().getApplicationContext(), Integer.toString(ListActivity.mListArray.size()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        mAdapter = new ArrayAdapter<String>(getActivity(),R.layout.fragment_list_view, mListArray);
        setListAdapter(mAdapter);
        //getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);

        try {
            mListener = (ListSelectionListener) getActivity();
        }
        catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnArticleSelectedListener");
        }

        /*
        mListView = (TextView) getActivity().findViewById(R.id.listView);
        mListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity().getApplicationContext(), "It Worked!", Toast.LENGTH_SHORT).show();

                //Toast.makeText(getActivity().getApplicationContext(), "It Worked!", Toast.LENGTH_SHORT).show();
                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{MyContactsActivity.mContactArray[MyContactsActivity.contactIndex].getEmail()});
                //emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Some Subject");
                //emailIntent.putExtra(Intent.EXTRA_TEXT, "Some body");
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });*/
    }

    public void addResults(SearchResults results) {
        Log.d(TAG, "addResults start");
        mResults.push(results);

        mListArray.add("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + getResources().getString(R.string.frgListView_source, results.getSource()) + "\n");
        mListArrayIndexForSeatchInterface.add(CASE_NOT_A_MENU_OPTION);

        List<MediaFile> onlineFiles = results.getResults();

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
                mListArrayIndexForSeatchInterface.add(CASE_FILE_IN_FRAGMENT);
                mListArrayIndexForSearchResults.put(mListArrayIndexForSeatchInterface.size() - 1, cntr);
                cntr2++;
            }
        }

        //mListArray.add("See All Video");
        //mListArrayIndexForSeatchInterface.add(CASE_SEE_ALL_VIDEO);

        mListArray.add("\n");
        mListArrayIndexForSeatchInterface.add(CASE_NOT_A_MENU_OPTION);

        mAdapter.notifyDataSetChanged();

        Log.d(TAG, "addResults finish");
    }

    public List<SearchResults> getResults() {
        return mResults;
    }

    public void finishSearch() {
        // TODO: Add logic
    }

    public MediaFile getMediaFile() {
        //TODO: make work with multiple results
        return mResults.get(0).getResults().get(mResultIndex);
    }
}
/*
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="edu.uco.phodgden.p5peterh.ListViewFragment">

    <!-- TODO: Update blank fragment layout -->
    <TextView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:text="@string/hello_blank_fragment" />

</FrameLayout>

 */

package edu.uco.map2016.mediaplayer;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PlaylistListViewFragment extends ListFragment {

    public TextView mListView;

    private static final String TAG = "TitlesFragment";
    private ListSelectionListener mListener = null;

    public interface ListSelectionListener {
        public void onListSelection(int index);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {

        ((PlaylistListActivity)getActivity()).resultIndex = pos;
        PlaylistItemDialogFragment d = new PlaylistItemDialogFragment();
        d.show(getFragmentManager(), "colors");

        //getListView().setItemChecked(pos, true);
        //mListener.onListSelection(pos);

        //Toast.makeText(getActivity().getApplicationContext(), Integer.toString(pos), Toast.LENGTH_SHORT).show();

        //AddToPlaylistActivity activity = (AddToPlaylistActivity) getActivity();


        /*((AddToPlaylistActivity) getActivity()).setResultShown(Integer.toString(pos));
        ((AddToPlaylistActivity) getActivity()).finish();*/

        /*
        if ((AddToPlaylistActivity.mListArrayIndexForSeatchInterface.get(pos) >= 0) && (AddToPlaylistActivity.mListArrayIndexForSeatchInterface.get(pos) < AddToPlaylistActivity.mListArray.size())) {
            Toast.makeText(getActivity().getApplicationContext(), pos, Toast.LENGTH_SHORT).show();

        }
        else {
            switch (AddToPlaylistActivity.mListArrayIndexForSeatchInterface.get(pos)) {
                case AddToPlaylistActivity.CASE_NOT_A_MENU_OPTION:
                    //do nothing
                    break;
                case AddToPlaylistActivity.CASE_SEE_ALL_MUSIC:
                    //open SeeAllMusic Activity
                    break;
                case AddToPlaylistActivity.CASE_SEE_ALL_VIDEO:
                    //open SeeAllVideo Activity
                    break;
            }
        }
        */

        //Toast.makeText(getActivity().getApplicationContext(), Integer.toString(SearchListActivity.mListArray.size()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        setListAdapter(new ArrayAdapter<String>(getActivity(),R.layout.fragment_playlist_list_view, PlaylistListActivity.mListArray));
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
}
/*
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="edu.uco.phodgden.p5peterh.SearchListViewFragment">

    <!-- TODO: Update blank fragment layout -->
    <TextView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:text="@string/hello_blank_fragment" />

</FrameLayout>

 */

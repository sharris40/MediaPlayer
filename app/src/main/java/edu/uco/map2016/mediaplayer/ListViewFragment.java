package edu.uco.map2016.mediaplayer;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewFragment extends ListFragment {

    public TextView mListView;

    private static final String TAG = "TitlesFragment";
    private ListSelectionListener mListener = null;

    public interface ListSelectionListener {
        public void onListSelection(int index);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        //getListView().setItemChecked(pos, true);
        //mListener.onListSelection(pos);
        if ((ListActivity.mListArrayIndexForSeatchInterface.get(pos) >= 0) && (ListActivity.mListArrayIndexForSeatchInterface.get(pos) < ListActivity.mListArray.size())) {
            Toast.makeText(getActivity().getApplicationContext(), ListActivity.searchInterface.getListResult().get(ListActivity.mListArrayIndexForSeatchInterface.get(pos)).getFileLocationAddress().toString(), Toast.LENGTH_SHORT).show();
        }
        else {
            switch (ListActivity.mListArrayIndexForSeatchInterface.get(pos)) {
                case ListActivity.CASE_NOT_A_MENU_OPTION:
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

        setListAdapter(new ArrayAdapter<String>(getActivity(),R.layout.fragment_list_view, ListActivity.mListArray));
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
    tools:context="edu.uco.phodgden.p5peterh.ListViewFragment">

    <!-- TODO: Update blank fragment layout -->
    <TextView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:text="@string/hello_blank_fragment" />

</FrameLayout>

 */

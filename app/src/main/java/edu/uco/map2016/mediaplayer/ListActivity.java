package edu.uco.map2016.mediaplayer;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.Vector;

import edu.uco.map2016.mediaplayer.ListViewFragment.ListSelectionListener;
import edu.uco.map2016.mediaplayer.api.SearchInterface;

public class ListActivity extends Activity implements ListSelectionListener {

    //TextView searchResultView;

    public static SearchInterface searchInterface;

    //public static String[] mListArray;
    public static Vector<String> mListArray = new Vector<String>();

    public static int contactIndex;

    //private final DetailsViewFragment mQuoteFragment = new DetailsViewFragment();
    private FragmentManager mFragmentManager;
    private FrameLayout mTitleFrameLayout, mQuotesFrameLayout;

    private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final String TAG = "QuoteViewerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        final String textToSearch = getIntent().getStringExtra("SEARCH");

        /*mListArray = new String[20];
        for (int cntr = 0; cntr < 20; cntr++) {
            mListArray[cntr] = "Hello World" + Integer.toString(cntr);
        }*/



        //searchResultView = (TextView) findViewById(R.id.layout_list_searh_result_view);

        searchInterface = new SearchInterface();

        searchInterface.search(textToSearch);

        for (int cntr = 0; cntr < searchInterface.getListResult().size(); cntr++) {
            mListArray.add(searchInterface.getListResult().get(cntr).getName());
        }


        /*
        String display = "";
        for (int cntr = 0; cntr < searchInterface.getListResult().size(); cntr++) {
            display = "" + display + searchInterface.getListResult().get(cntr).getName() + "\n";
        }
        searchResultView.setText(display);
        */

        setContentView(R.layout.activity_list);



        mTitleFrameLayout = (FrameLayout) findViewById(R.id.listViewMyContactsLayout);
        //mQuotesFrameLayout = (FrameLayout) findViewById(R.id.detailsViewMyContactsLayout);

        mFragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager
                .beginTransaction();
        fragmentTransaction.add(R.id.listViewMyContactsLayout,
                new ListViewFragment());
        fragmentTransaction.commit();

        mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                setLayout();
            }
        });

    }

    private void setLayout() {
        //if (!mQuoteFragment.isAdded()) {
            mTitleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    MATCH_PARENT, MATCH_PARENT)); // width, height
            mQuotesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                    MATCH_PARENT));
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

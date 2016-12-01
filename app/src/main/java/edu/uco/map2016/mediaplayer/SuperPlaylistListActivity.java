package edu.uco.map2016.mediaplayer;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Vector;

import edu.uco.map2016.mediaplayer.DeletePlaylistDialogueFragment.PickDeletePlaylistListener;
import edu.uco.map2016.mediaplayer.SuperPlaylistListViewFragment.ListSelectionListener;

import static edu.uco.map2016.mediaplayer.MainActivity.playlistInterface;

public class SuperPlaylistListActivity extends Activity implements ListSelectionListener, PickDeletePlaylistListener {

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

    private static final int CASE_PLAY_PLAYLIST = 0;
    private static final int CASE_VIEW_PLAYLIST = 1;
    private static final int CASE_SHUFFLE_PLAYLIST = 2;
    private static final int CASE_DELETE_PLAYLIST = 3;
    private static final int CASE_CANCEL = 4;

    public int resultIndex;

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
        setContentView(R.layout.activity_super_playlist_list);

        playlistInterface.retrieveListOfPlaylists();

        mListArray.removeAllElements();


        for (int cntr = 0; cntr < playlistInterface.getListOfPlaylists().size(); cntr++) {
            if (playlistInterface.getListOfPlaylists().get(cntr).getName().length() > STRING_CAPACITY) {
                mListArray.add(playlistInterface.getListOfPlaylists().get(cntr).getName().substring(0,STRING_CAPACITY) + "\n");
            }
            else {
                mListArray.add(playlistInterface.getListOfPlaylists().get(cntr).getName() + "\n");
            }
        }

        /*
        for (int cntr = 0; cntr < searchInterface.getListResult().size(); cntr++) {
            mListArray.add(searchInterface.getListResult().get(cntr).getName());
        }*/

        setContentView(R.layout.activity_super_playlist_list);

        mTitleFrameLayout = (FrameLayout) findViewById(R.id.listViewSuperPlaylistListLayout);
        //mQuotesFrameLayout = (FrameLayout) findViewById(R.id.detailsViewMyContactsLayout);

        mFragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.listViewSuperPlaylistListLayout, new SuperPlaylistListViewFragment());
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

    @Override
    public void onPickDeletePlaylistClick(int optionIndex, DialogFragment dialog) {
        switch(optionIndex) {
            case CASE_PLAY_PLAYLIST:

                break;
            case CASE_VIEW_PLAYLIST:
                Intent activityPlayListList = new Intent(SuperPlaylistListActivity.this, PlaylistListActivity.class); //getApplicationContext()
                //mListArray.removeAllElements();
                //startActivityForResult(activityAddToPlayList, REQUEST_CODE);
                activityPlayListList.putExtra("PLAYLIST", Integer.toString(resultIndex));
                startActivity(activityPlayListList);
                break;
            case CASE_SHUFFLE_PLAYLIST:
                Toast.makeText(getApplicationContext(), "Shuffled Playlist: " + MainActivity.playlistInterface.getListOfPlaylists().get(resultIndex).getName(), Toast.LENGTH_SHORT).show();
                playlistInterface.shufflePlaylist(resultIndex);
                finish();
                break;
            case CASE_DELETE_PLAYLIST:
                Toast.makeText(getApplicationContext(), "Deleted Playlist: " + MainActivity.playlistInterface.getListOfPlaylists().get(resultIndex).getName(), Toast.LENGTH_SHORT).show();
                playlistInterface.removePlaylist(resultIndex);
                finish();
                break;
            case CASE_CANCEL:

                break;
        }
        /*//String color = getResources().getStringArray(R.array.colors_array)[colorIndex];
        //textview.setText(color + " has picked!");
        //Toast.makeText(getApplicationContext(), "It Worked!", Toast.LENGTH_SHORT).show();

        //Uri uri = Uri.parse(urlArray[colorIndex]); // missing 'http://' will cause crashed
        //Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        //startActivity(intent);

        // Define the Notification's expanded message and Intent:

        //mContentView.setTextViewText(R.id.text, contentText + " (" + ++mNotificationCount + ")");

        // Build the Notification

        Notification.Builder notificationBuilder = new Notification.Builder(
                getApplicationContext())
                //.setTicker(tickerText)
                .setSmallIcon(android.R.drawable.stat_sys_warning)
                .setAutoCancel(true)
                .setContentIntent(mContentIntent);

        //.setSound(soundURI)
        //.setVibrate(mVibratePattern)
        //.setContent(mContentView);

        //Intent activityAdd = new Intent(MainActivity.this, DepartmentInfoActivity.class); //getApplicationContext()

        //Toast.makeText(getApplicationContext(), Integer.toString(colorIndex), Toast.LENGTH_SHORT).show();

        //mNotificationIntent.putExtra("INDEX", Integer.toString(colorIndex));

        // Pass the Notification to the NotificationManager:
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MY_NOTIFICATION_ID,
                notificationBuilder.build());

        //mNotificationIntent.putExtra("INDEX", Integer.toString(colorIndex));

        index = colorIndex;
        */
    }

}

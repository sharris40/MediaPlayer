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

import edu.uco.map2016.mediaplayer.PlaylistListViewFragment.ListSelectionListener;
import edu.uco.map2016.mediaplayer.RemoveFromPlaylistDialogueFragment.PickRemoveFromPlaylistListener;

import static edu.uco.map2016.mediaplayer.MainActivity.playlistInterface;

public class PlaylistListActivity extends Activity implements ListSelectionListener, PickRemoveFromPlaylistListener {

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

    private static final int CASE_PLAY_FILE = 0;
    private static final int CASE_REMOVE_FILE_FROM_PLAYLIST = 1;
    private static final int CASE_MOVE_UP_IN_PLAYLIST = 2;
    private static final int CASE_MOVE_DOWN_IN_PLAYLIST = 3;
    private static final int CASE_CANCEL = 4;

    public int resultIndex;
    public String playlistToView;

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
        setContentView(R.layout.activity_playlist_list);

        //playlistInterface.retrieveListOfPlaylists();

        mListArray.removeAllElements();

        playlistToView = getIntent().getStringExtra("PLAYLIST");

        for (int cntr = 0; cntr < playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().size(); cntr++) {
            if (MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(cntr).getName().length() > STRING_CAPACITY) {
                if (MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(cntr).getDetails().length() > STRING_CAPACITY) {
                    mListArray.add(MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(cntr).getName().substring(0,STRING_CAPACITY) + "..." + "\n" + MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(cntr).getDetails().substring(0,STRING_CAPACITY) + "...");
                }
                else {
                    mListArray.add(MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(cntr).getName().substring(0,STRING_CAPACITY) + "..." + "\n" + MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(cntr).getDetails());
                }
            }
            else {
                if (MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(cntr).getDetails().length() > STRING_CAPACITY) {
                    mListArray.add(MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(cntr).getName() + "\n" + MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(cntr).getDetails().substring(0,STRING_CAPACITY) + "...");
                }
                else {
                    mListArray.add(MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(cntr).getName() + "\n" + MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(cntr).getDetails());
                }
            }
        }
        /*
        for (int cntr = 0; cntr < searchInterface.getListResult().size(); cntr++) {
            mListArray.add(searchInterface.getListResult().get(cntr).getName());
        }*/

        setContentView(R.layout.activity_playlist_list);

        mTitleFrameLayout = (FrameLayout) findViewById(R.id.listViewPlaylistListLayout);
        //mQuotesFrameLayout = (FrameLayout) findViewById(R.id.detailsViewMyContactsLayout);

        mFragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.listViewPlaylistListLayout, new PlaylistListViewFragment());
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
    public void onPickRemoveFromPlaylistClick(int optionIndex, DialogFragment dialog) {
        switch(optionIndex) {
            case CASE_PLAY_FILE:

                break;
            case CASE_REMOVE_FILE_FROM_PLAYLIST:
                Toast.makeText(getApplicationContext(), "Removed File: " + MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(resultIndex).getName() + " From Playlist: " + MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getName(), Toast.LENGTH_SHORT).show();
                playlistInterface.removeMediaFile(Integer.parseInt(playlistToView),resultIndex);
                finish();
                break;
            case CASE_MOVE_UP_IN_PLAYLIST:
                Toast.makeText(getApplicationContext(), "Moved File Up: " + MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(resultIndex).getName() + " From Playlist: " + MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getName(), Toast.LENGTH_SHORT).show();
                playlistInterface.moveUpMediaFile(Integer.parseInt(playlistToView),resultIndex);
                finish();
                break;
            case CASE_MOVE_DOWN_IN_PLAYLIST:
                Toast.makeText(getApplicationContext(), "Moved File Down: " + MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getListOfMediaFiles().get(resultIndex).getName() + " From Playlist: " + MainActivity.playlistInterface.getListOfPlaylists().get(Integer.parseInt(playlistToView)).getName(), Toast.LENGTH_SHORT).show();
                playlistInterface.moveDownMediaFile(Integer.parseInt(playlistToView),resultIndex);
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

package edu.uco.map2016.mediaplayer;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.View;
import android.widget.RemoteViews;

import static edu.uco.map2016.mediaplayer.MusicActivity.mediaPlayer;

public class NotificationService extends Service {

    Notification status;
    private NotificationManager mNotificationManager;
    private final String LOG_TAG = "NotificationService";
    private static final int FORWARD_TIME = 2000;

    private RemoteViews views;
    private RemoteViews bigViews;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null)
            mediaPlayer.dispose();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            showNotification();
           // Toast.makeText(this, "Player Started", Toast.LENGTH_SHORT).show();



        } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
            long position = mediaPlayer.getPosition();
            if ((position - FORWARD_TIME) > 0) {
                mediaPlayer.seek(position - FORWARD_TIME);
            } else {
                mediaPlayer.seek(0);
            }

        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
            //Toast.makeText(this, "Clicked Play", Toast.LENGTH_SHORT).show();
           // Log.i(LOG_TAG, "Clicked Play");
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                views.setImageViewResource(R.id.status_bar_play,
                        R.drawable.apollo_holo_dark_play);
                bigViews.setImageViewResource(R.id.status_bar_play,
                        R.drawable.apollo_holo_dark_play);
            } else {
                mediaPlayer.play();
                views.setImageViewResource(R.id.status_bar_play,
                        R.drawable.apollo_holo_dark_pause);
                bigViews.setImageViewResource(R.id.status_bar_play,
                        R.drawable.apollo_holo_dark_pause);
            }
            mNotificationManager.notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);

        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
            long position = mediaPlayer.getPosition();
            if ((position + FORWARD_TIME) <= mediaPlayer.getDuration()) {
                mediaPlayer.seek(position + FORWARD_TIME);
            }

        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {
           // Log.i(LOG_TAG, "Received Stop Foreground Intent");
            //Toast.makeText(this, "Service Stoped", Toast.LENGTH_SHORT).show();
            stopForeground(true);
            stopSelf();
            if (mediaPlayer != null) {
                mediaPlayer.dispose();
                mediaPlayer = null;
            }
        }
        return START_STICKY;
    }

    private void showNotification() {
// Using RemoteViews to bind custom layouts into Notification
        views = new RemoteViews(getPackageName(),
                R.layout.status_bar);
        bigViews = new RemoteViews(getPackageName(),
                R.layout.status_bar_expanded);

// showing default album image
        views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        views.setViewVisibility(R.id.status_bar_album_art, View.GONE);
        bigViews.setImageViewBitmap(R.id.status_bar_album_art,
                Constants.getDefaultAlbumArt(this));

        Intent notificationIntent = new Intent(this, MusicActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, NotificationService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, NotificationService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, NotificationService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent closeIntent = new Intent(this, NotificationService.class);
        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);

        views.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);

        views.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);

        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        views.setImageViewResource(R.id.status_bar_play,
                R.drawable.apollo_holo_dark_play);
        bigViews.setImageViewResource(R.id.status_bar_play,
                R.drawable.apollo_holo_dark_play);

        views.setTextViewText(R.id.status_bar_track_name, "Song Title");
        bigViews.setTextViewText(R.id.status_bar_track_name, "Song Title");

        views.setTextViewText(R.id.status_bar_artist_name, "Artist Name");
        bigViews.setTextViewText(R.id.status_bar_artist_name, "Artist Name");

        bigViews.setTextViewText(R.id.status_bar_album_name, "Album Name");

        mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        status = new Notification.Builder(this).build();
        status.contentView = views;
        status.bigContentView = bigViews;
        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.drawable.ic_launcher;
        status.contentIntent = pendingIntent;
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
    }

}



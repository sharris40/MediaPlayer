package edu.uco.map2016.mediaplayer.api;

import android.util.Log;

import java.util.List;

public abstract class AbstractMediaPlayer {
    public interface OnPreparedListener {
        void onPrepared(AbstractMediaPlayer player, boolean isPlaying);
    }

    public interface TrackChangeListener {
        void onTrackChange(MediaFile track, int index);
    }

    protected Playlist playlist = new Playlist("Now Playing");
    protected int playlistIndex = -1;
    private boolean repeat = false;
    private static final String LOG_TAG = "AbstractMediaPlayer";

    /**
     * Loads a song in the background, but does not play it. The song becomes active, and any
     * currently playing song will stop when the song is loaded.
     * @param file
     */
    protected abstract void load(MediaFile file);

    /**
     * Loads a song in the background then plays it.
     * @param file
     */
    protected abstract void play(MediaFile file);

    /**
     * Plays the currently active song at its current position. If it is already playing, this does
     * nothing.
     */
    protected abstract void playCurrent();

    /**
     * Stops playback, but keeps the current position.
     */
    public abstract void pause();

    /**
     * Stops playback and resets the position. The song is still active and loaded.
     */
    public abstract void stop();

    /**
     * Stops playback. After this call, there will be no active song.
     */
    public abstract void endPlayback();

    public abstract long getDuration();

    /**
     * Gets the current playback position
     * @return the current position
     */
    public abstract long getPosition();

    /**
     * Seeks to the given timecode. If no song is active, this method does nothing.
     * @param timeInMilliseconds
     */
    public abstract void seek(long timeInMilliseconds);

    public abstract boolean isPlaying();

    /**
     * Returns true if a song is currently loaded or being loaded. This should only return false if
     * the queue is empty or playback has reached the end of the queue.
     * @return true if a song is active; false otherwise.
     */
    public abstract boolean isSongActive();

    /**
     * Resets the Now Playing list and queues the file. Any currently playing song will stop when
     * the load finishes.
     * @param file
     */
    public void loadFile(MediaFile file) {
        Log.d(LOG_TAG, "loadFile");
        clearQueue();
        playlist.addMediaFile(file);
        playlistIndex = 0;
        load(file);
    }

    /**
     * Resets the Now Playing list and plays the file.
     * @param file
     */
    public void playFile(MediaFile file) {
        clearQueue();
        playlist.addMediaFile(file);
        playlistIndex = 0;
        play(file);
    }

    /**
     * Plays the current file.
     * <br/>
     * If the current file is paused, playback resumes from the current point.
     * <br/>
     * If the current file is stopped, playback resumes from the start of the file.
     * <br/>
     * If there is no active file, the next song in the queue is played.
     */
    public void play() {
        if (isSongActive())
            playCurrent();
        else if (isSongQueued())
            play(playlist.getListOfMediaFiles().get(playlistIndex));
        else if (playlist.getListOfMediaFiles().size() > 0) {
            playlistIndex = 0;
            play(playlist.getListOfMediaFiles().get(0));
        }
    }

    /**
     * Adds a file to the queue but does not play it. isSongQueued will return true after this call
     * finishes.
     * @param file
     */
    public void queue(MediaFile file) {
        playlist.addMediaFile(file);
        if (!isSongActive()) {
            playlistIndex = playlist.getListOfMediaFiles().size() - 1;
            load(file);
        }
    }

    /**
     * Replaces the Now Playing list with the supplied list and plays it from the beginning.
     * @param playlist
     */
    public void playList(Playlist playlist) {
        clearQueue();
        List<MediaFile> files = playlist.getListOfMediaFiles();
        for (MediaFile file : files) {
            playlist.addMediaFile(file);
        }
        if (files.size() > 0) {
            playlistIndex = 0;
            play(files.get(0));
        }
    }

    /**
     * Adds the supplied list to the end of the Now Playing list. If the list is empty, the first
     * song in the new list will be queued.
     * @param playlist
     */
    public void queueList(Playlist playlist) {
        List<MediaFile> files = playlist.getListOfMediaFiles();
        for (MediaFile file : files) {
            playlist.addMediaFile(file);
        }
        if (!isSongActive() && playlistIndex < 0 && files.size() > 0) {
            playlistIndex = 0;
            load(files.get(0));
        }
    }

    /**
     * Goes to the next song in the list. If isRepeating is true, the list is considered circular.
     * @return true if there is another song, or the list is circular; false otherwise.
     */
    public boolean forward() {
        ++playlistIndex;
        if (playlistIndex >= playlist.getListOfMediaFiles().size()) {
            if (repeat) {
                playlistIndex = 0;
                if (playlist.getListOfMediaFiles().size() == 0) {
                    return false;
                }
            } else {
                stop();
                return false;
            }
        }
        play(playlist.getListOfMediaFiles().get(playlistIndex));
        return true;
    }

    /**
     * Goes to the previous song in the list. If isRepeating is true, the list is considered
     * circular.
     * @return true if there is a previous song, or the list is circular; false otherwise.
     */
    public boolean back() {
        --playlistIndex;
        if (playlistIndex < 0) {
            if (repeat) {
                playlistIndex = playlist.getListOfMediaFiles().size() - 1;
                if (playlist.getListOfMediaFiles().size() == 0) {
                    return false;
                }
            } else {
                stop();
                return false;
            }
        }
        play(playlist.getListOfMediaFiles().get(playlistIndex));
        return true;
    }

    /**
     * Goes to the previous song in the list. If isRepeating is true, the list is considered
     * circular.
     * @param restartSongTimeout The amount of time that must pass before the previous song is
     *                           selected. If this time has not passed, the current song is
     *                           restarted.
     * @return true if there is a previous song, or the list is circular; false otherwise.
     */
    public boolean back(int restartSongTimeout) {
        if (restartSongTimeout > getPosition()) {
            return back();
        } else {
            restartSong();
            return isSongQueued();
        }
    }

    /**
     * Restarts the current song and plays it.
     */
    public void restartSong() {
        if (isSongActive()) {
            seek(0);
        } else {
            play();
        }
    }

    /**
     * Stops playback and goes to the beginning of the queue.
     */
    public void resetQueue() {
        if (isSongActive()) {
            endPlayback();
        }
        playlistIndex = 0;
        if (playlist.getListOfMediaFiles().size() > 0) {
            load(playlist.getListOfMediaFiles().get(0));
        }
    }

    /**
     * Stops playback and clears the Now Playing list.
     */
    public void clearQueue() {
        if (isSongActive()) {
            endPlayback();
        }
        playlistIndex = -1;
        playlist = new Playlist("Now Playing");
    }

    public boolean isSongQueued() {
        return playlistIndex > -1 && playlistIndex < playlist.getListOfMediaFiles().size();
    }

    public boolean isRepeating() {
        return repeat;
    }

    public void setRepeating(boolean repeating) {
        this.repeat = repeating;
    }

    private OnPreparedListener mListener;
    private TrackChangeListener mTrackListener;

    public void setOnPreparedListener(OnPreparedListener listener) {
        mListener = listener;
    }

    /**
     * Call this when the media player is ready to load songs. No songs should be playing before
     * this is called. Only call this if the configuration changes.
     */
    protected void ready() {
        if (mListener != null)
            mListener.onPrepared(this, isPlaying());
    }

    public void setTrackChangeListener(TrackChangeListener listener) {
        mTrackListener = listener;
    }

    /**
     * Call this when the media player finishes loading a song.
     */
    protected void trackChange() {
        if (mTrackListener != null)
            mTrackListener.onTrackChange(playlist.getListOfMediaFiles().get(playlistIndex), playlistIndex);
    }

    /**
     * Disposes the media player. This must be called in order to avoid leaking resources.
     */
    public abstract void dispose();
}

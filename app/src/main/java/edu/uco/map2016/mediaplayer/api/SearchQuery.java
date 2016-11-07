package edu.uco.map2016.mediaplayer.api;

import java.util.LinkedList;

public class SearchQuery {
    private LinkedList<SearchQuery> mAnd = null;
    private LinkedList<SearchQuery> mOr = null;
    private boolean mNot = false;

    private String mQuery = null;
    private String mTitle = null;
    private String mArtist = null;
    private String mAlbum = null;
    private String mChannel = null;
    private String mPlaylist = null;
    private String mKeyword = null;

    private String mArtistId = null;
    private String mAlbumId = null;
    private String mChannelId = null;
    private String mPlaylistId = null;

    public void setNested(boolean enabled, boolean acceptAny) {
        if (enabled) {
            if (acceptAny) {
                if (mAnd != null) {
                    mOr = mAnd;
                    mAnd = null;
                } else {
                    mOr = new LinkedList<>();
                }
            } else {
                if (mOr != null) {
                    mAnd = mOr;
                    mOr = null;
                } else {
                    mAnd = new LinkedList<>();
                }
            }
        } else {
            mAnd = null;
            mOr = null;
        }
    }

    public boolean isNegated() {
        return mNot;
    }

    public void setNegated(boolean negated) {
        mNot = negated;
    }

    public String getQuery() {
        return mQuery;
    }

    public void setQuery(String query) {
        this.mQuery = query;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        this.mArtist = artist;
    }

    public String getAlbum() {
        return mAlbum;
    }

    public void setAlbum(String album) {
        this.mAlbum = album;
    }

    public String getChannel() {
        return mChannel;
    }

    public void setChannel(String channel) {
        this.mChannel = channel;
    }

    public String getPlaylist() {
        return mPlaylist;
    }

    public void setPlaylist(String playlist) {
        this.mPlaylist = playlist;
    }

    public String getKeyword() {
        return mKeyword;
    }

    public void setKeyword(String keyword) {
        this.mKeyword = keyword;
    }

    public String getArtistId() {
        return mArtistId;
    }

    public void setArtistId(String artistId) {
        this.mArtistId = artistId;
    }

    public String getAlbumId() {
        return mAlbumId;
    }

    public void setAlbumId(String albumId) {
        this.mAlbumId = albumId;
    }

    public String getChannelId() {
        return mChannelId;
    }

    public void setChannelId(String channelId) {
        this.mChannelId = channelId;
    }

    public String getPlaylistId() {
        return mPlaylistId;
    }

    public void setPlaylistId(String playlistId) {
        this.mPlaylistId = playlistId;
    }

    public void addNestedQuery(SearchQuery nestedQuery) {
        if (mOr != null) {
            mOr.push(nestedQuery);
        } else {
            mAnd.push(nestedQuery);
        }
    }

    public boolean isNested() {
        return mAnd != null || mOr != null;
    }

    public boolean isAcceptingAnyNested() {
        return mOr != null;
    }
}

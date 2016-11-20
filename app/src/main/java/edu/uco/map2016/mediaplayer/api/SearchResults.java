package edu.uco.map2016.mediaplayer.api;

import java.util.List;

public interface SearchResults {
    public interface SearchUpdateListener {
        void onSearchUpdated(boolean hasMore);
        void onSearchFailed();
    }
    List<MediaFile> getResults();
    int getCount();
    int getTotalEstimate();
    void nextResults(SearchUpdateListener listener);
    String getSource();
}

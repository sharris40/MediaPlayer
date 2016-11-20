package edu.uco.map2016.mediaplayer.services.providers.spotify;

import java.util.List;
import java.util.Vector;

import edu.uco.map2016.mediaplayer.api.MediaFile;
import edu.uco.map2016.mediaplayer.api.SearchResults;

class SpotifySearchResults implements SearchResults {
    Vector<MediaFile> results;
    int total;
    String nextQuery;
    int requestCode;
    SearchUpdateListener currentListener;

    private SpotifyService mService;

    SpotifySearchResults(SpotifyService service) {
        mService = service;
    }

    @Override
    public List<MediaFile> getResults() {
        return results;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public int getTotalEstimate() {
        return total;
    }

    @Override
    public void nextResults(SearchUpdateListener listener) {
        if (currentListener == null) {
            currentListener = listener;
            mService.search(requestCode, nextQuery, true);
        } else {
            currentListener = listener;
        }
    }

    @Override
    public String getSource() {
        return "Spotify";
    }
}

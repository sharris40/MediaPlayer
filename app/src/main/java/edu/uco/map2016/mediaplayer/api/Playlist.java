package edu.uco.map2016.mediaplayer.api;

import java.util.Vector;

public class Playlist {
    private String name;
    private Vector<MediaFile> listOfMediaFiles = new Vector<MediaFile>();
    public Playlist(String a) {
        name = a;
    }
    public String getName() {
        return name;
    }
    public Vector<MediaFile> getListOfMediaFiles() {
        return listOfMediaFiles;
    }
    public void addMediaFile(MediaFile a) {
        listOfMediaFiles.add(a);
    }
    public void removeMediaFile(int index) {
        listOfMediaFiles.remove(index);
    }
    public void moveUpMediaFile(int index) {

    }
    public void moveDownMediaFile(int index) {

    }
}

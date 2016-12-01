package edu.uco.map2016.mediaplayer.api;

import java.util.Random;
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
        if (!(index-1 < 0)) {
            MediaFile temp = listOfMediaFiles.get(index);
            listOfMediaFiles.set(index, listOfMediaFiles.get(index - 1));
            listOfMediaFiles.set(index - 1, temp);
        }
    }
    public void moveDownMediaFile(int index) {
        if (!(index+1 >= listOfMediaFiles.size())) {
            MediaFile temp = listOfMediaFiles.get(index + 1);
            listOfMediaFiles.set(index + 1, listOfMediaFiles.get(index));
            listOfMediaFiles.set(index, temp);
        }
    }
    public void shuffleMediaFiles() {
        Vector<MediaFile> tempListOfMediaFiles = listOfMediaFiles;
        for (int cntr = 0; cntr < listOfMediaFiles.size(); cntr++) {
            listOfMediaFiles.set(cntr, tempListOfMediaFiles.get((new Random()).nextInt(tempListOfMediaFiles.size())));
            tempListOfMediaFiles.remove(cntr);
        }
    }
}

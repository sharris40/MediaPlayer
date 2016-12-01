package edu.uco.map2016.mediaplayer.api;

import android.net.Uri;

import java.util.Vector;

public class PlaylistInterface {
    private Vector<Playlist> listOfPlaylists = new Vector<Playlist>();
    public PlaylistInterface() {
        Playlist demo1 = new Playlist("Demo 1 Playlist");
        demo1.addMediaFile(new MediaFile("This Is How We Do", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "3:24","Katy Perry", "PRISM (Deluxe)", "2013"));
        demo1.addMediaFile(new MediaFile("My Songs Know What You Did In The Dark (Light Em Up)", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "3:07","Fall Out Boy", "Save Rock And Roll", "2013"));
        demo1.addMediaFile(new MediaFile("Counting Stars", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "4:17","OneRepublic", "Native (Deluxe)", "2013"));
        Playlist demo2 = new Playlist("Demo 2 Playlist");
        demo2.addMediaFile(new MediaFile("Still Into You", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "3:36","Paramore", "Paramore", "2013"));
        demo2.addMediaFile(new MediaFile("Lights - Single Version", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "3:31","Ellie Goulding", "Bright Lights", "2010"));
        demo2.addMediaFile(new MediaFile("Tongue Tied", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "3:38","Grouplove", "Never Trust A Happy Song", "2011"));
        Playlist demo3 = new Playlist("Demo 3 Playlist");
        demo3.addMediaFile(new MediaFile("On Top Of The World", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "3:10","Imagine Dragons", "Night Visions (Deluxe)", "2012"));
        demo3.addMediaFile(new MediaFile("Alive", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "4:51","Krewella", "Play Hard EP", "2012"));
        demo3.addMediaFile(new MediaFile("Stay The Night - featuring Hayley Williams of Paramore", Uri.parse("http://www.example.com"), Uri.parse("http://www.example.com"), MediaFile.TYPE_AUDIO, "3:57","Zedd", "Stay The Night - featuring Hayley Williams of Paramore", "2013"));
        addPlaylist(demo1);
        addPlaylist(demo2);
        addPlaylist(demo3);
    }

    public Vector<Playlist> getListOfPlaylists() {
        return listOfPlaylists;
    }

    public void addPlaylist(Playlist a) {
        if (listOfPlaylists.size() > 0) {
            if (a.getName().compareTo(listOfPlaylists.get(listOfPlaylists.size()-1).getName()) > 0) {
                listOfPlaylists.add(listOfPlaylists.size(), a);
            }
            else {
                for (int cntr = 0; cntr < listOfPlaylists.size(); cntr++) {
                    if (a.getName().compareTo(listOfPlaylists.get(cntr).getName()) < 0) {
                        listOfPlaylists.add(cntr, a);
                        break;
                    }
                }
            }
        }
        else {
            listOfPlaylists.add(a);
        }



    }
    public void removePlaylist(int index) {
        listOfPlaylists.remove(index);
    }

    public void retrieveListOfPlaylists() {

    }
    public void saveListOfPlaylists() {

    }

    public void addMediaFile(int playlistIndex, MediaFile a) {
        listOfPlaylists.get(playlistIndex).addMediaFile(a);
    }
    public void removeMediaFile(int playlistIndex, int mediaFileIndex) {
        listOfPlaylists.get(playlistIndex).removeMediaFile(mediaFileIndex);
    }
    public void moveUpMediaFile(int playlistIndex, int mediaFileIndex) {
        listOfPlaylists.get(playlistIndex).moveUpMediaFile(mediaFileIndex);
    }
    public void moveDownMediaFile(int playlistIndex, int mediaFileIndex) {
        listOfPlaylists.get(playlistIndex).moveDownMediaFile(mediaFileIndex);
    }
    public void shufflePlaylist(int playlistIndex) {
        listOfPlaylists.get(playlistIndex).shuffleMediaFiles();
    }
}

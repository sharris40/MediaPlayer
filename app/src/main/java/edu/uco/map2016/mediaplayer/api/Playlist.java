package edu.uco.map2016.mediaplayer.api;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class Playlist implements Parcelable {
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
    public void addMediaFile(@NonNull MediaFile a) {
        if (listOfMediaFiles.isEmpty()) {
            listOfMediaFiles.add(a);
        } else {
            MediaFile first = listOfMediaFiles.firstElement();
            if (first.isLocal() && a.isLocal()
                    || first.getProvider() == null && a.getProvider() == null
                    || first.getProvider() != null && first.getProvider().equals(a.getProvider())) {
                listOfMediaFiles.add(a);
            }
        }
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
        Vector<MediaFile> tempListOfMediaFiles = new Vector<>(listOfMediaFiles);
        boolean[] mapping = new boolean[tempListOfMediaFiles.size()];
        Arrays.fill(mapping, false);
        for (int cntr = 0; cntr < listOfMediaFiles.size(); cntr++) {
            int temp;
            do {
                temp = (new Random()).nextInt(mapping.length);
            } while (mapping[temp]);
            mapping[temp] = true;
            listOfMediaFiles.set(cntr, tempListOfMediaFiles.get(temp));
        }
    }

    protected Playlist(Parcel in) {
        name = in.readString();
        int size = in.readInt();
        listOfMediaFiles.ensureCapacity(size);
        while (size-- > 0) {
            listOfMediaFiles.add((MediaFile)in.readParcelable(MediaFile.class.getClassLoader()));
        }
    }

    public static final Creator<Playlist> CREATOR = new Creator<Playlist>() {
        @Override
        public Playlist createFromParcel(Parcel in) {
            return new Playlist(in);
        }

        @Override
        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(listOfMediaFiles.size());
        for (MediaFile file : listOfMediaFiles) {
            dest.writeParcelable(file, flags);
        }
    }
}

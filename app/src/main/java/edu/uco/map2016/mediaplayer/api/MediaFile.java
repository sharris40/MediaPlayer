package edu.uco.map2016.mediaplayer.api;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class MediaFile implements Parcelable {

    public static final int TYPE_NONE = -1;
    public static final int TYPE_AUDIO = 0;
    public static final int TYPE_VIDEO = 1;

    private static final int IMAGE_PRESENT = 0x01;
    private static final int LENGTH_PRESENT = 0x02;
    private static final int ARTIST_PRESENT = 0x04;
    private static final int ALBUM_PRESENT = 0x08;
    private static final int YEAR_PRESENT = 0x10;

    private String name;
    private Uri fileAddress;
    private Uri imageAddress;
    private int type;
    private String length; //in MM:SS format
    private String artist;
    private String album;
    private String yearPublished;

    public MediaFile() {
        /*fileAddress = new String();
        imageAddress = new String();
        type = TYPE_NONE;
        length = new String();
        artist = new String();
        album = new String();
        yearPublished = new String();*/
    }
    public MediaFile(@NonNull String name, @NonNull Uri fileAddress, int type) {
        this.name = name;
        this.fileAddress = fileAddress;
        this.type = type;
    }
    public MediaFile(@NonNull String name, @NonNull Uri fileAddress, Uri imageAddress, int type, String length, String artist, String album, String yearPublished) {
        this.name = name;
        this.fileAddress = fileAddress;
        this.imageAddress = imageAddress;
        this.type = type;
        this.length = length;
        this.artist = artist;
        this.album = album;
        this.yearPublished = yearPublished;
    }

    protected MediaFile(Parcel in) {
        name = in.readString();
        fileAddress = in.readParcelable(Uri.class.getClassLoader());
        type = in.readInt();
        int optionalParts = in.readInt();
        if ((optionalParts & IMAGE_PRESENT) == IMAGE_PRESENT)
            imageAddress = in.readParcelable(Uri.class.getClassLoader());
        if ((optionalParts & LENGTH_PRESENT) == LENGTH_PRESENT)
            length = in.readString();
        if ((optionalParts & ARTIST_PRESENT) == ARTIST_PRESENT)
            artist = in.readString();
        if ((optionalParts & ALBUM_PRESENT) == ALBUM_PRESENT)
            album = in.readString();
        if ((optionalParts & YEAR_PRESENT) == YEAR_PRESENT)
            yearPublished = in.readString();
    }

    public static final Creator<MediaFile> CREATOR = new Creator<MediaFile>() {
        @Override
        public MediaFile createFromParcel(Parcel in) {
            return new MediaFile(in);
        }

        @Override
        public MediaFile[] newArray(int size) {
            return new MediaFile[size];
        }
    };

    public String getName() {
        return name;
    }
    public Uri getFileLocationAddress() {
        return fileAddress;
    }
    public Uri getImageLocationAddress() {
        return imageAddress;
    }
    public int getType() {
        return type;
    }
    public String getLengthOfFile() {
        return length;
    }
    public String getArtist() {
        return artist;
    }
    public String getAlbum() {
        return album;
    }
    public String getYearPublished() {
        return yearPublished;
    }

    public void setName(@NonNull String a) {
        name = a;
    }
    public void setFileLocationAddress(@NonNull Uri a) {
        fileAddress = a;
    }
    public void setImageLocationAddress(Uri a) {
        imageAddress = a;
    }
    public void setType(int a) {
        type = a;
    }
    public void setLengthOfFile(String a) {
        length = a;
    }
    public void setArtist(String a) {
        artist = a;
    }
    public void setAlbum(String a) {
        album = a;
    }
    public void setYearPublished(String a) {
        yearPublished = a;
    }

    public String getDetails() {
        if (type == TYPE_AUDIO) {
            return "Audio | " + length + " | " + artist + " | " + album;
        }
        else if (type == TYPE_VIDEO) {
            return "Video | " + length + " | " + artist;
        }
        else {
            return "";
        }
    }

    public boolean isLocal() {
        String scheme = fileAddress.getScheme();
        return scheme == null
                || scheme.equalsIgnoreCase("content")
                || scheme.equalsIgnoreCase("file");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeParcelable(fileAddress, i);
        parcel.writeInt(type);
        int optionalParts = 0;
        if (imageAddress != null)
            optionalParts |= IMAGE_PRESENT;
        if (length != null)
            optionalParts |= LENGTH_PRESENT;
        if (artist != null)
            optionalParts |= ARTIST_PRESENT;
        if (album != null)
            optionalParts |= ALBUM_PRESENT;
        if (yearPublished != null)
            optionalParts |= YEAR_PRESENT;
        parcel.writeInt(optionalParts);
        if (imageAddress != null)
            parcel.writeParcelable(imageAddress, i);
        if (length != null)
            parcel.writeString(length);
        if (artist != null)
            parcel.writeString(artist);
        if (album != null)
            parcel.writeString(album);
        if (yearPublished != null)
            parcel.writeString(yearPublished);
    }
}

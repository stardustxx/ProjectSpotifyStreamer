package com.sdust.projectspotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

public class Track implements Parcelable {
    String imageURL, trackName, albumName, musicURL, artistName, bigImageURL;

    public Track(String imageURL, String trackName, String albumName, String musicURL, String artistName, String bigImageURL) {
        this.imageURL = imageURL;
        this.trackName = trackName;
        this.albumName = albumName;
        this.musicURL = musicURL;
        this.artistName = artistName;
        this.bigImageURL = bigImageURL;
    }

    private Track(Parcel in){
        imageURL = in.readString();
        trackName = in.readString();
        albumName = in.readString();
        musicURL = in.readString();
        artistName = in.readString();
        bigImageURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageURL);
        dest.writeString(trackName);
        dest.writeString(albumName);
        dest.writeString(musicURL);
        dest.writeString(artistName);
        dest.writeString(bigImageURL);
    }

    public static final Parcelable.Creator<Track> CREATOR = new Parcelable.Creator<Track>(){
        @Override
        public Track createFromParcel(Parcel source) {
            return new Track(source);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };
}

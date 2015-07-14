package com.sdust.projectspotifystreamer;

public class Track {
    String imageURL, trackName, albumName, musicURL, artistName, bigImageURL;

    public Track(String imageURL, String trackName, String albumName, String musicURL, String artistName, String bigImageURL) {
        this.imageURL = imageURL;
        this.trackName = trackName;
        this.albumName = albumName;
        this.musicURL = musicURL;
        this.artistName = artistName;
        this.bigImageURL = bigImageURL;
    }
}

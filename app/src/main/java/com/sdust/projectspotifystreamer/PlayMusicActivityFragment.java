package com.sdust.projectspotifystreamer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class PlayMusicActivityFragment extends Fragment {

    public static final String MUSIC_INFO = "music demo";
    public static final String MUSIC_TRACK_NUMBER = "track number";
    private MediaPlayer mediaPlayer;
    private int trackPosition;
    private List<Track> tracksData;

    // Initialize all the view components
    TextView albumName, artistName, trackName;
    ImageView trackImage;
    ImageButton playBtn, previousBtn, nextBtn;

    public PlayMusicActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_play_music, container, false);
        albumName = (TextView) rootView.findViewById(R.id.musicAlbumName);
        artistName = (TextView) rootView.findViewById(R.id.musicArtistName);
        trackName = (TextView) rootView.findViewById(R.id.musicTrackName);
        trackImage = (ImageView) rootView.findViewById(R.id.musicTrackImage);
        playBtn = (ImageButton) rootView.findViewById(R.id.playTrackBtn);
        previousBtn = (ImageButton) rootView.findViewById(R.id.previousTrackBtn);
        nextBtn = (ImageButton) rootView.findViewById(R.id.nextTrackBtn);

        Bundle args = getArguments();
        trackPosition = args.getInt(MUSIC_TRACK_NUMBER);
        tracksData = args.getParcelableArrayList(MUSIC_INFO);

        updateTrackInfo();

        // Mediaplayer
        initMusic(tracksData.get(trackPosition).musicURL, playBtn);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        playBtn.setImageResource(R.drawable.ic_play_circle_fill_black_48dp);
                    } else {
                        mediaPlayer.start();
                        playBtn.setImageResource(R.drawable.ic_pause_circle_outline_black_48dp);
                    }
                } else {
                    initMusic(tracksData.get(trackPosition).musicURL, playBtn);
                }
            }
        });

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trackPosition == 0){
                    trackPosition = tracksData.size() - 1;
                }
                else {
                    trackPosition -= 1;
                }
                updateTrackInfo();
                initMusic(tracksData.get(trackPosition).musicURL, playBtn);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trackPosition == tracksData.size() - 1){
                    trackPosition = 0;
                }
                else {
                    trackPosition += 1;
                }
                updateTrackInfo();
                initMusic(tracksData.get(trackPosition).musicURL, playBtn);
            }
        });

        return rootView;
    }

    private void initMusic(String trackURL, final ImageButton musicPlay){
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(trackURL);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    musicPlay.setImageResource(R.drawable.ic_pause_circle_outline_black_48dp);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error in playback", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTrackInfo(){
        artistName.setText(tracksData.get(trackPosition).artistName);
        albumName.setText(tracksData.get(trackPosition).albumName);
        Picasso.with(getActivity()).load(tracksData.get(trackPosition).bigImageURL).into(trackImage);
        trackName.setText(tracksData.get(trackPosition).trackName);
    }

    @Override
    public void onStop() {
        super.onStop();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}

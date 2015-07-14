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

public class PlayMusicActivityFragment extends Fragment {

    public static final String MUSIC_URL_TAG = "music demo";
    private MediaPlayer mediaPlayer;
    private String[] trackInfo;

    public PlayMusicActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_play_music, container, false);
        TextView albumName = (TextView) rootView.findViewById(R.id.musicAlbumName);
        TextView artistName = (TextView) rootView.findViewById(R.id.musicArtistName);
        TextView trackName = (TextView) rootView.findViewById(R.id.musicTrackName);
        ImageView trackImage = (ImageView) rootView.findViewById(R.id.musicTrackImage);
        final ImageButton playBtn = (ImageButton) rootView.findViewById(R.id.playTrackBtn);
        ImageButton previoudBtn = (ImageButton) rootView.findViewById(R.id.previousTrackBtn);
        ImageButton nextBtn = (ImageButton) rootView.findViewById(R.id.nextTrackBtn);

        Bundle args = getArguments();
        trackInfo = args.getStringArray(MUSIC_URL_TAG);

        artistName.setText(trackInfo[0]);
        albumName.setText(trackInfo[1]);
        Picasso.with(getActivity()).load(trackInfo[2]).into(trackImage);
        trackName.setText(trackInfo[3]);

        // Mediaplayer
        initMusic(trackInfo[4], playBtn);

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
                }
                else {
                    initMusic(trackInfo[4], playBtn);
                }
            }
        });

        return rootView;
    }

    private void initMusic(String trackURL, final ImageButton musicPlay){
        if (mediaPlayer == null){
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
    }

    @Override
    public void onStop() {
        super.onStop();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}

package com.sdust.projectspotifystreamer;

import android.app.Dialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class PlayMusicActivityFragment extends DialogFragment {

    public static final String MUSIC_INFO = "music demo";
    public static final String MUSIC_TRACK_NUMBER = "track number";
    private MediaPlayer mediaPlayer;
    private int trackPosition;
    private ArrayList<Track> tracksData;
    private boolean asyncRunning = false;
    private RunPlayBar runPlayBar;
    private Boolean isTrackEnded = false;

    // Initialize all the view components
    TextView albumName, artistName, trackName;
    ImageView trackImage;
    ImageButton playBtn, previousBtn, nextBtn;
    SeekBar playBar;

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        Log.d("saving state", "true");
//        outState.putInt(MUSIC_TRACK_NUMBER, trackPosition);
//        outState.putParcelableArrayList(MUSIC_INFO, tracksData);
//    }

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
        playBar = (SeekBar) rootView.findViewById(R.id.playBar);

        Bundle args = getArguments();
//        if (savedInstanceState != null){
//            trackPosition = savedInstanceState.getInt(MUSIC_TRACK_NUMBER);
//            tracksData = savedInstanceState.getParcelableArrayList(MUSIC_INFO);
//            Log.d("trackPosition", Integer.toString(trackPosition));
//            Log.d("tracksData", tracksData.toString());
//        }
//        else
        if (args != null){
            trackPosition = args.getInt(MUSIC_TRACK_NUMBER);
            tracksData = args.getParcelableArrayList(MUSIC_INFO);
            Log.d("args", "true");
        }

        updateTrackInfo();

        // Mediaplayer
        initMusic(tracksData.get(trackPosition).musicURL);

        // Use PlayBar to control music track position
        playBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);
                    new RunPlayBar().execute();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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
                        if (isTrackEnded) {
                            Log.d("end track", "true");
                            playBar.setProgress(0);
                            isTrackEnded = false;
                        }
                        runPlayBar = new RunPlayBar();
                        runPlayBar.execute();
                    }
                } else {
                    initMusic(tracksData.get(trackPosition).musicURL);
                }
            }
        });

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTrackEnded = false;
                if (trackPosition == 0){
                    trackPosition = tracksData.size() - 1;
                }
                else {
                    trackPosition -= 1;
                }
                updateTrackInfo();
                initMusic(tracksData.get(trackPosition).musicURL);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTrackEnded = false;
                if (trackPosition == tracksData.size() - 1){
                    trackPosition = 0;
                }
                else {
                    trackPosition += 1;
                }
                updateTrackInfo();
                initMusic(tracksData.get(trackPosition).musicURL);
            }
        });

        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void initMusic(String trackURL){
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
            playBar.setEnabled(false);
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(trackURL);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    runPlayBar = new RunPlayBar();
                    mediaPlayer.start();
                    playBar.setEnabled(true);
                    playBar.setMax(mediaPlayer.getDuration());
                    asyncRunning = true;
                    runPlayBar.execute();
                    playBtn.setImageResource(R.drawable.ic_pause_circle_outline_black_48dp);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error in playback", Toast.LENGTH_SHORT).show();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d("track end", "true");
                isTrackEnded = true;
            }
        });
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
        asyncRunning = false;
    }

    private class RunPlayBar extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            try {
                int currentTrackPosition = mediaPlayer.getCurrentPosition();
                int trackLength = mediaPlayer.getDuration();

                while (mediaPlayer != null && currentTrackPosition < trackLength && asyncRunning && mediaPlayer.isPlaying()) {
                    currentTrackPosition = mediaPlayer.getCurrentPosition();
                    publishProgress(currentTrackPosition);
                }
            }

            catch (Exception e){
                Log.d("playback error", e.toString());
            }

            // If this asyncTask is no longer needed, we cancel it to stop running
            if (!asyncRunning){
                this.cancel(true);
            }

            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            playBtn.setImageResource(R.drawable.ic_play_circle_fill_black_48dp);
        }

        private void publishProgress(int trackTime){
            playBar.setProgress(trackTime);
        }
    }
}

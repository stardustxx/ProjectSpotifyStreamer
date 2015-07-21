package com.sdust.projectspotifystreamer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class PlayMusicActivity extends AppCompatActivity {

    public static final String MUSICFRAGMENT_TAG = "MFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        PlayMusicActivityFragment musicFragment = new PlayMusicActivityFragment();
        ArrayList<Track> tracksData;
        int trackPosition;

        if (savedInstanceState == null){
            Bundle extra = this.getIntent().getExtras();
            if (extra != null){
                tracksData = extra.getParcelableArrayList(PlayMusicActivityFragment.MUSIC_INFO);
                trackPosition = extra.getInt(PlayMusicActivityFragment.MUSIC_TRACK_NUMBER);
                Bundle args = new Bundle();
                args.putParcelableArrayList(PlayMusicActivityFragment.MUSIC_INFO, tracksData);
                args.putInt(PlayMusicActivityFragment.MUSIC_TRACK_NUMBER, trackPosition);
                musicFragment.setArguments(args);
            }
        }

        getFragmentManager().beginTransaction().add(R.id.musicFragment, musicFragment, MUSICFRAGMENT_TAG).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play_music, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

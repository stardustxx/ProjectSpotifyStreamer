package com.sdust.projectspotifystreamer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.CallBack{

    private static final String ARTISTFRAG_TAG = "AFTAG";
    private static final String TRACKFRAG_TAG = "TFTAG";

    private Boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if the app is running in one panel or two panel mode
        // Meaning it's checking it's a phone or tablet
        if (findViewById(R.id.tracksFragment) != null){
            twoPane = true;
            if (savedInstanceState == null){
                getSupportFragmentManager().beginTransaction().replace(R.id.tracksFragment, new Top10tracksFragment(), TRACKFRAG_TAG).commit();
            }
        }
        else {
            twoPane = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onItemSelected(String artistID) {
        // We decide how to present the layoyt
        // If it's running on tablet, we create the fragment and implement it
        // since in tablet, Activity Main XML does have tracksFragment layout
        if (twoPane){
            Top10tracksFragment tracksFragment = new Top10tracksFragment();

            Bundle args = new Bundle();
            args.putString(Top10tracksFragment.ARTIST_ID, artistID);
            tracksFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.tracksFragment, tracksFragment, TRACKFRAG_TAG).commit();
        }
        // However, if it's running on phone, we start new activity for tracks
        else {
            Intent intent = new Intent(MainActivity.this, Top10tracks.class);
            intent.putExtra(Top10tracksFragment.ARTIST_ID, artistID);
            startActivity(intent);
        }
    }
}

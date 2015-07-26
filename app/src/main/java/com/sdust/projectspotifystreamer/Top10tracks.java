package com.sdust.projectspotifystreamer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Top10tracks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top10tracks);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // If nothing is saved, we grab the data that was sent along the intent
        // We take the data and add it to tracksFragment that we want to commit
        if (savedInstanceState == null){
            Bundle extra = this.getIntent().getExtras();
            if (extra != null){
                Top10tracksFragment tracksFragment = new Top10tracksFragment();
                Bundle args = new Bundle();
                String artistID = extra.getString(Top10tracksFragment.ARTIST_ID);
                Boolean twoPanel = extra.getBoolean(Top10tracksFragment.TWO_PANEL);
                args.putString(Top10tracksFragment.ARTIST_ID, artistID);
                args.putBoolean(Top10tracksFragment.TWO_PANEL, twoPanel);
                tracksFragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().add(R.id.tracksFragment, tracksFragment).commit();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top10tracks, menu);
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

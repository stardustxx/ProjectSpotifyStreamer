package com.sdust.projectspotifystreamer;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;

public class Top10tracksFragment extends Fragment {
    public static final String ARTIST_ID = "Artist ID";

    TracksAdapter tracksAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top10tracks, container, false);
        String artistid;
        TracksFinder tracksFinder = new TracksFinder();
        ListView tracksList = (ListView) rootView.findViewById(R.id.top10List);
        List<Track> tracksData = new ArrayList<>();

        tracksAdapter = new TracksAdapter(getActivity(), tracksData);
        tracksList.setAdapter(tracksAdapter);

        // Store the artist id that is passed through intern from parent activity
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null){
            artistid = extras.getString(ARTIST_ID);
            tracksFinder.execute(artistid);
            Log.d("tracksfinder", "grabbing stuffs");
        }

        return rootView;
    }

    private void updateView(List<Track> tracks){
        tracksAdapter.clear();
        tracksAdapter.addAll(tracks);
        tracksAdapter.notifyDataSetChanged();
        Log.d("updateView", "done update");
    }

    public class TracksFinder extends AsyncTask<String, Void, List<Track>>{

        @Override
        protected List<Track> doInBackground(String... params) {
            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService spotifyService = spotifyApi.getService();
            Map<String, Object> options = new HashMap<>();
            options.put("country", "US");

            try {
                Tracks tracks = spotifyService.getArtistTopTrack(params[0], options);

                List<Track> trackList = new ArrayList<>();

                // It is my bad that I named my own object to be the same as the Spotify function name
                // Somehow when I refactor/rename my class name, Andoid Studio complains
                for (kaaes.spotify.webapi.android.models.Track track : tracks.tracks){
                    Track track1 = new Track(track.album.images.get(1).url, track.name, track.album.name);
                    trackList.add(track1);
                }

                return trackList;
            }
            catch (RetrofitError e){
                Toast.makeText(getActivity(), "Unexpected error, please try again later", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Track> tracks) {
            super.onPostExecute(tracks);
            updateView(tracks);
        }
    }
}


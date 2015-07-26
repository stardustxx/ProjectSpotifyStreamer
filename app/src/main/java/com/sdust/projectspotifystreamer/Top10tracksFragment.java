package com.sdust.projectspotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;

public class Top10tracksFragment extends Fragment {
    public static final String ARTIST_ID = "Artist ID";
    public static final String TWO_PANEL = "Two Panel";

    TracksAdapter tracksAdapter;
    ArrayList<Track> tracksData;

    Boolean twoPanel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top10tracks, container, false);
        String artistid;
        TracksFinder tracksFinder = new TracksFinder();
        ListView tracksList = (ListView) rootView.findViewById(R.id.top10List);
        tracksData = new ArrayList<>();

        tracksAdapter = new TracksAdapter(getActivity(), tracksData);
        tracksList.setAdapter(tracksAdapter);

        // We grab data that is passed when fragment is implemented
        Bundle args = getArguments();
        if (args != null){
            artistid = args.getString(Top10tracksFragment.ARTIST_ID);
            twoPanel = args.getBoolean(Top10tracksFragment.TWO_PANEL);
            tracksFinder.execute(artistid);
        }

        tracksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (twoPanel){
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    PlayMusicActivityFragment playMusicActivityFragment = new PlayMusicActivityFragment();
                    Bundle args = new Bundle();
                    args.putParcelableArrayList(PlayMusicActivityFragment.MUSIC_INFO, tracksData);
                    args.putInt(PlayMusicActivityFragment.MUSIC_TRACK_NUMBER, position);
                    playMusicActivityFragment.setArguments(args);
                    playMusicActivityFragment.show(fragmentManager, "dialog");
                }
                else {
                    Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
                    intent.putExtra(PlayMusicActivityFragment.MUSIC_TRACK_NUMBER, position);
                    intent.putParcelableArrayListExtra(PlayMusicActivityFragment.MUSIC_INFO, tracksData);
                    startActivity(intent);
                }
                Log.d("twoPanel", twoPanel.toString());
            }
        });

        return rootView;
    }

    private void updateView(ArrayList<Track> tracks){
        if (tracks.size() == 0){
            Toast.makeText(getActivity(), "No result found", Toast.LENGTH_SHORT).show();
        }
        tracksData = tracks;
        tracksAdapter.clear();
        tracksAdapter.addAll(tracks);
        tracksAdapter.notifyDataSetChanged();
    }

    public class TracksFinder extends AsyncTask<String, Void, ArrayList<Track>>{

        @Override
        protected ArrayList<Track> doInBackground(String... params) {
            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService spotifyService = spotifyApi.getService();
            Map<String, Object> options = new HashMap<>();
            options.put("country", "US");

            try {
                Tracks tracks = spotifyService.getArtistTopTrack(params[0], options);

                ArrayList<Track> trackList = new ArrayList<>();

                // It is my bad that I named my own object to be the same as the Spotify function name
                // Somehow when I refactor/rename my class name, Andoid Studio complains
                for (kaaes.spotify.webapi.android.models.Track track : tracks.tracks){
                    Track track1 = new Track(track.album.images.get(2).url, track.name, track.album.name, track.preview_url, track.artists.get(0).name, track.album.images.get(0).url);
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
        protected void onPostExecute(ArrayList<Track> tracks) {
            super.onPostExecute(tracks);
            updateView(tracks);
        }
    }
}


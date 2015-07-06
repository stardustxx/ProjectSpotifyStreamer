package com.sdust.projectspotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

public class MainActivityFragment extends Fragment {

    ArtistSearchAdapter artistSearchAdapter;
    ListView listView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ImageButton searchBtn = (ImageButton) rootView.findViewById(R.id.searchBtn);
        listView = (ListView) rootView.findViewById(R.id.searchTracksList);
        final EditText searchInput = (EditText) rootView.findViewById(R.id.searchText);

        // Declare my data and variable to store the search result
        List<Celebrity> arrayData = new ArrayList<>();
        // Define the array adapter that makes use of the result data
        artistSearchAdapter = new ArtistSearchAdapter(getActivity(), arrayData);
        // Have the listView to use this adapter
        listView.setAdapter(artistSearchAdapter);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchInputText = searchInput.getText().toString();
                // Upon searching, if no text is found in search box, toast a warning
                if (searchInputText.length() == 0){
                    Toast.makeText(getActivity(), "Please type something", Toast.LENGTH_SHORT).show();
                }
                // Have searchWord asynctask to perform searching
                else if (checkNetworkConnection()){
                    GetSpotifyArtists getSpotifyArtists = new GetSpotifyArtists();
                    getSpotifyArtists.execute(searchInputText);
                    // Disable and enable the search box to leave the focus on search box
                    searchInput.setEnabled(false);
                    searchInput.setEnabled(true);
                }
                else {
                    Toast.makeText(getActivity(), "No network connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Upon clicking on an item in listView, create new intent and start new activity to track result
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (checkNetworkConnection()){
                    Intent intent = new Intent(getActivity(), Top10tracks.class);
                    intent.putExtra(Top10tracksFragment.ARTIST_ID, artistSearchAdapter.getItem(position).artistID);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getActivity(), "No network connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    // Function that updates the content of adapter and prompts it to use the update
    private void updateDataAdapter(List<Celebrity> celebrities) {
        artistSearchAdapter.clear();
        artistSearchAdapter.addAll(celebrities);
        artistSearchAdapter.notifyDataSetChanged();
        listView.smoothScrollToPosition(0);
        if (celebrities.size() == 0){
            Toast.makeText(getActivity(), "No result found", Toast.LENGTH_SHORT).show();
        }
    }

    // Code snippet from Google Developer site "Determining and Monitoring the Connectivity Status"
    // http://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html
    private boolean checkNetworkConnection(){
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public class GetSpotifyArtists extends AsyncTask<String, Void, List<Celebrity>>{

        @Override
        protected List<Celebrity> doInBackground(String... params) {
            // Sometimes some artists don't have photo
            // So used Spotify picture as default image
            String spotifyURL = "https://play.spotify.edgekey.net/site/adc3363/images/favicon.png";
            String imageURL;

            // Prepare SpotifyAPI to search
            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService spotifyService = spotifyApi.getService();
            ArtistsPager artistsResult = spotifyService.searchArtists(params[0]);

            List<Artist> artistResult = artistsResult.artists.items;
            List<Celebrity> celebrities = new ArrayList<>();

            for (Artist artist : artistResult){
                if (!artist.images.isEmpty()){
                    imageURL = artist.images.get(1).url;
                }
                else {
                    imageURL = spotifyURL;
                }
                Celebrity celebrity = new Celebrity(artist.name, imageURL, artist.id);
                celebrities.add(celebrity);
            }

            return celebrities;
        }

        @Override
        protected void onPostExecute(List<Celebrity> celebrities) {
            super.onPostExecute(celebrities);
            updateDataAdapter(celebrities);
        }
    }

}

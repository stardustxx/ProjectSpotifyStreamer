package com.sdust.projectspotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TracksAdapter extends ArrayAdapter<Track> {

    Context context;

    public TracksAdapter(Context context, List<Track> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TracksViewHolder tracksViewHolder;
        Track track = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_tracks, parent, false);
            tracksViewHolder = new TracksViewHolder(convertView);
            convertView.setTag(tracksViewHolder);

            return convertView;
        }
        else {
            tracksViewHolder = (TracksViewHolder) convertView.getTag();
        }

        Picasso.with(context).load(track.imageURL).into(tracksViewHolder.image);
        tracksViewHolder.trackName.setText(track.trackName);
        tracksViewHolder.albumName.setText(track.albumName);

        return convertView;
    }

    class TracksViewHolder {
        ImageView image;
        TextView trackName;
        TextView albumName;

        public TracksViewHolder(View v){
            this.image = (ImageView) v.findViewById(R.id.trackPic);
            this.trackName = (TextView) v.findViewById(R.id.trackName);
            this.albumName = (TextView) v.findViewById(R.id.albumName);
        }

    }
}

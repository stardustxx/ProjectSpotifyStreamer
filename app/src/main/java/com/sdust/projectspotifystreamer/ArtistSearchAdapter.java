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
import java.util.zip.Inflater;

public class ArtistSearchAdapter extends ArrayAdapter<Celebrity> {

    Context context;

    public ArtistSearchAdapter(Context context, List<Celebrity> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Celebrity celebrity = getItem(position);
//        View rootView = LayoutInflater.from(context).inflate(R.layout.list_search_artists, parent, false);

        MyViewHolder myViewHolder;

        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.list_search_artists, parent, false);
//            convertView = LayoutInflater.from(context).inflate(R.layout.list_search_artists, parent,false);
            myViewHolder = new MyViewHolder(convertView);

            convertView.setTag(myViewHolder);
        }
        else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }

        Picasso.with(context).load(celebrity.imageURL).into(myViewHolder.image);
        myViewHolder.name.setText(celebrity.name);

        return convertView;
    }
}

class MyViewHolder{
    ImageView image;
    TextView name;

    public MyViewHolder(View v) {
        this.image = (ImageView) v.findViewById(R.id.artistPic);
        this.name = (TextView) v.findViewById(R.id.artistName);
    }
}

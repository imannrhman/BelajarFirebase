package com.belajar.belajarfirebase;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TrackList extends ArrayAdapter<Track> {
    private Activity context;
    private List<Track> tracks;

    public TrackList(Activity context, List<Track> tracks) {
        super(context, R.layout.list_track_item,tracks);
        this.context = context;
        this.tracks= tracks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_track_item,null,true);
        TextView textViewName = listViewItem.findViewById(R.id.tv_name);
        TextView textViewRating = listViewItem.findViewById(R.id.tv_rating);
//
        Track track = tracks.get(position);
        textViewRating.setText(String.valueOf(track.getTrackRating()));
        textViewName.setText(track.getTrackName());
        return listViewItem  ;
    }
}

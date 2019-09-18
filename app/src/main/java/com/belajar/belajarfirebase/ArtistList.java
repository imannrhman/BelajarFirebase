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

public class ArtistList extends ArrayAdapter<Artist> {
    private Activity context;
    private List<Artist> artistList;

    public ArtistList(Activity context, List<Artist> artistList) {
        super(context, R.layout.list_item,artistList);
        this.context = context;
        this.artistList = artistList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_item,null,true);
        TextView textViewName = listViewItem.findViewById(R.id.tv_name);
        TextView textViewGenre = listViewItem.findViewById(R.id.tv_genre);

        Artist artist = artistList.get(position);
        textViewGenre.setText(artist.getArtistGenre());
         textViewName.setText(artist.getArtistName());
        return listViewItem  ;
    }
}

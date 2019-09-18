package com.belajar.belajarfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
EditText editTextName;
Button buttonAdd;
Spinner spinnerGenres;

DatabaseReference databaseArtists;
 ListView listViewArtists;
 List<Artist> artists;
 public  static final String ARTIST_NAME = "artistname";
 public static  final String ARTIST_ID = "artistid";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseArtists = FirebaseDatabase.getInstance().getReference("artists");
        listViewArtists = findViewById(R.id.listview_artist);
        artists = new ArrayList<>();
        editTextName = findViewById(R.id.edt_name);
        buttonAdd = findViewById(R.id.btn_add_artist);
        spinnerGenres = findViewById(R.id.spinner_genres);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    addArtist();
                   }
        });
        listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Artist artist = artists.get(i);

                Intent intent = new Intent( getApplicationContext(),AddTrackActivity.class);
                intent.putExtra(ARTIST_ID,artist.getArtistId());
                intent.putExtra(ARTIST_NAME,artist.getArtistName());
                startActivity(intent);
            }
        });
       listViewArtists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
               Artist artist  = artists.get(i);
               showUpdateDialog(artist.getArtistId(),artist.artistName);
               return false;
           }
       });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseArtists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                artists.clear();
                for (DataSnapshot artistsSnapshot : dataSnapshot.getChildren() ){
                      Artist artist   = artistsSnapshot.getValue(Artist.class);
                      artists.add(artist);
                }
                ArtistList adapter = new ArtistList(MainActivity.this,artists);
                listViewArtists.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private  void showUpdateDialog(final String artistId, String artistName){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog,null);

        dialogBuilder.setView(dialogView);
        final EditText editTextName = dialogView.findViewById(R.id.edt_name_update);
         final Button buttonUpdate = dialogView.findViewById(R.id.btn_update);
         final Spinner spinnerGenres = dialogView.findViewById(R.id.spinner_genres_update);
         dialogBuilder.setTitle("Updating Artist "+artistName);
         editTextName.setText(artistName);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
         buttonUpdate.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                    String name = editTextName.getText().toString().trim();
                    String genre = spinnerGenres.getSelectedItem().toString();
                    if (TextUtils.isEmpty(name)){
                        editTextName.setError("Name Required");
                        return;
                    }
                    updateArtist(artistId,name,genre);
                 alertDialog.dismiss();
             }
         });
        final  Button buttonDelete = dialogView.findViewById(R.id.btn_delete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 deleteArtist(artistId);
                 alertDialog.dismiss();
             }
         });

    }

    private void deleteArtist(String artistId) {
        DatabaseReference dbArtist = FirebaseDatabase.getInstance().getReference("artists").child(artistId);
        DatabaseReference dbTrack = FirebaseDatabase.getInstance().getReference("tracks").child(artistId);

        dbArtist.removeValue();
        dbTrack.removeValue();
        Toast.makeText(this,"Artis is deleted",Toast.LENGTH_LONG).show();
    }

    private boolean updateArtist(String id,String name ,String genre){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("artists").child(id);
        Artist artist = new Artist(id,name,genre);
        databaseReference.setValue(artist);
        Toast.makeText(this,"Artist Update Successfully",Toast.LENGTH_LONG).show();
        return true;
    }
    private  void addArtist(){
        String name = editTextName.getText().toString().trim();
        String genre = spinnerGenres.getSelectedItem().toString();
        if(!TextUtils.isEmpty(name)){
            String id =databaseArtists.push().getKey();
            Artist artist = new Artist(id,name,genre);
            databaseArtists.child(id).setValue(artist);
            Toast.makeText(this,"Artist Added",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"You should enter a name ",Toast.LENGTH_LONG).show();
        }

    }
}

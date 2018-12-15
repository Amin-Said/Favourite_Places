package com.example.amin.favspot;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.amin.favspot.adapter.SavedPlacesAdapter;
import com.example.amin.favspot.helper.DatabaseHelper;
import com.example.amin.favspot.helper.RecyclerTouchListener;
import com.example.amin.favspot.model.Place;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<Place> placeArrayList = new ArrayList<Place>();
    private final int num_list_Items = 100;
    public static SavedPlacesAdapter mAdapter;
    private RecyclerView mNumberLists;
    Place place;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNumberLists = findViewById(R.id.places_list);

        db = new DatabaseHelper(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        placeArrayList.clear();
        getAllplaces();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_location_menu:

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("location", "a");
                intent.putExtra("place", 0);

                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getAllplaces() {

        db = new DatabaseHelper(this);

        placeArrayList.addAll(db.getAllPlaces());

        for (Place place : placeArrayList) {
            mAdapter = new SavedPlacesAdapter(this, placeArrayList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mNumberLists.setLayoutManager(layoutManager);
            mNumberLists.setHasFixedSize(true);
            mNumberLists.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

        mNumberLists.addOnItemTouchListener(new RecyclerTouchListener(this,
                mNumberLists, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("place", position);
                intent.putExtra("location", "null");
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, final int position) {

                showActionsDialog(position);

            }
        }));
    }

    private void deletePlace(int position) {

        db.deletePlace(placeArrayList.get(position));
        placeArrayList.remove(position);
        mAdapter.notifyItemRemoved(position);

    }

    private void showActionsDialog(final int position) {
        CharSequence choices[] = new CharSequence[]{"Yep, Delete it", "Nope, Cancel that"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are u sure you wanna delete this?");
        final AlertDialog ad = builder.show();
        builder.setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    deletePlace(position);
                    ad.dismiss();
                } else {
                    ad.dismiss();
                }
            }
        });
        builder.show();

    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

}
package com.example.amin.favspot.helper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.amin.favspot.model.Place;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "places_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Place.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Place.TABLE_NAME);
        onCreate(db);
    }

    public long insertPlace(String place,Double latitude , Double longitude) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Place.COLUMN_ADDRESS, place);
        values.put(Place.COLUMN_LATITUDE,latitude);
        values.put(Place.COLUMN_LONGITUDE,longitude);

        long id = db.insert(Place.TABLE_NAME, null, values);

        db.close();
        return id;
    }

    public Place getPlace(long id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Place.TABLE_NAME,
                new String[]{Place.COLUMN_ID, Place.COLUMN_ADDRESS , Place.COLUMN_LATITUDE,
                        Place.COLUMN_LONGITUDE, Place.COLUMN_TIMESTAMP},
                Place.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Place place = new Place(
                cursor.getInt(cursor.getColumnIndex(Place.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Place.COLUMN_ADDRESS)),
                cursor.getDouble(cursor.getColumnIndex(Place.COLUMN_LATITUDE)),
                cursor.getDouble(cursor.getColumnIndex(Place.COLUMN_LONGITUDE)),
                cursor.getString(cursor.getColumnIndex(Place.COLUMN_TIMESTAMP)));

        cursor.close();

        return place;
    }

    public List<Place> getAllPlaces() {
        List<Place> places = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Place.TABLE_NAME + " ORDER BY " +
                Place.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Place place = new Place();
                place.setId(cursor.getInt(cursor.getColumnIndex(Place.COLUMN_ID)));
                place.setAddress(cursor.getString(cursor.getColumnIndex(Place.COLUMN_ADDRESS)));
                place.setLatitude(cursor.getDouble(cursor.getColumnIndex(Place.COLUMN_LATITUDE)));
                place.setLongitude(cursor.getDouble(cursor.getColumnIndex(Place.COLUMN_LONGITUDE)));
                place.setTimestamp(cursor.getString(cursor.getColumnIndex(Place.COLUMN_TIMESTAMP)));

                places.add(place);
            } while (cursor.moveToNext());
        }
        db.close();
        return places;
    }

    public int getPlacesCount() {
        String countQuery = "SELECT  * FROM " + Place.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int updatePlace(Place place) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Place.COLUMN_ADDRESS, place.getAddress());
        values.put(Place.COLUMN_LONGITUDE, place.getLatitude());
        values.put(Place.COLUMN_LATITUDE, place.getLongitude());

        return db.update(Place.TABLE_NAME, values, Place.COLUMN_ID + " = ?",
                new String[]{String.valueOf(place.getId())});
    }

    public void deletePlace(Place place) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Place.TABLE_NAME, Place.COLUMN_ID + " = ?",
                new String[]{String.valueOf(place.getId())});
        db.close();
    }
}
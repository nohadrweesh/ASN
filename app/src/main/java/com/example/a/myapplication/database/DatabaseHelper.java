package com.example.a.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.a.myapplication.database.model.Offer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Speed on 28/06/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "offers_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Offer.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ Offer.TABLE_NAME);
        onCreate(db);

    }

    public long insertOffer(int offer_id,int center_id,
             String offer_title,String offer_content,String expiry_date,String center_name) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Offer.COLUMN_OFFER_ID,offer_id);
        values.put(Offer.COLUMN_CENTER_ID,center_id);
        values.put(Offer.COLUMN_OFFER_TITLE,offer_title);
        values.put(Offer.COLUMN_OFFER_CONTENT,offer_content);
        values.put(Offer.COLUMN_OFFER_EXPIRY_DATE,expiry_date);
        values.put(Offer.COLUMN_CENTER_NAME,center_name);

        // insert row
        long id = db.insert(Offer.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Offer getOffer(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Offer.TABLE_NAME,
                new String[]{Offer.COLUMN_OFFER_ID, Offer.COLUMN_CENTER_ID, Offer.COLUMN_OFFER_TITLE, Offer.COLUMN_OFFER_CONTENT, Offer.COLUMN_OFFER_EXPIRY_DATE},
                Offer.COLUMN_OFFER_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Offer offer = new Offer(
                cursor.getInt(cursor.getColumnIndex(Offer.COLUMN_OFFER_ID)),
                cursor.getInt(cursor.getColumnIndex(Offer.COLUMN_CENTER_ID)),
                cursor.getString(cursor.getColumnIndex(Offer.COLUMN_OFFER_TITLE)),
                cursor.getString(cursor.getColumnIndex(Offer.COLUMN_OFFER_CONTENT)),
                cursor.getString(cursor.getColumnIndex(Offer.COLUMN_OFFER_EXPIRY_DATE)),
                cursor.getString(cursor.getColumnIndex(Offer.COLUMN_CENTER_NAME)));

        // close the db connection
        cursor.close();

        return offer;
    }

    public List<Offer> getAllOffers() {
        List<Offer> offers = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Offer.TABLE_NAME + " ORDER BY " +
                Offer.COLUMN_OFFER_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Offer offer = new Offer();
                offer.setOffer_id(cursor.getInt(cursor.getColumnIndex(Offer.COLUMN_OFFER_ID)));
                offer.setCenter_id(cursor.getInt(cursor.getColumnIndex(Offer.COLUMN_CENTER_ID)));
                offer.setOffer_title(cursor.getString(cursor.getColumnIndex(Offer.COLUMN_OFFER_TITLE)));
                offer.setOffer_content(cursor.getString(cursor.getColumnIndex(Offer.COLUMN_OFFER_CONTENT)));
                offer.setExpiry_date(cursor.getString(cursor.getColumnIndex(Offer.COLUMN_OFFER_EXPIRY_DATE)));
                offer.setCenter_name(cursor.getString(cursor.getColumnIndex(Offer.COLUMN_CENTER_NAME)));

                offers.add(offer);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();
        cursor.close();

        // return notes list
        return offers;
    }

    public int getOffersCount() {
        String countQuery = "SELECT  * FROM " + Offer.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }
//TODO :Will delete EXpiring date  offers
    public void deleteOffer(Offer offer) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Offer.TABLE_NAME, Offer.COLUMN_OFFER_ID + " = ?",
                new String[]{String.valueOf(offer.getOffer_id())});
        db.close();
    }
}

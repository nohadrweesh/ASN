package com.example.a.myapplication.database.model;

/**
 * Created by Speed on 28/06/2018.
 */

public class Offer {
    public static final String TABLE_NAME ="offers";
    public static final String COLUMN_OFFER_ID="id";
    public static final String COLUMN_CENTER_ID="center_id";
    public static final String COLUMN_CENTER_NAME="center_name";
    public static final String COLUMN_OFFER_TITLE="offer_title";
    public static final String  COLUMN_OFFER_CONTENT="offer_content";
    public static final String COLUMN_OFFER_EXPIRY_DATE="expiry_date";

    private  int offer_id,center_id;
    private  String offer_title,offer_content,expiry_date,center_name;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_OFFER_ID + " INTEGER PRIMARY KEY ,"
                    + COLUMN_CENTER_ID + " INTEGER ,"
                    + COLUMN_OFFER_TITLE + " TEXT ,"
                    +COLUMN_OFFER_CONTENT +" TEXT ,"
                    +COLUMN_CENTER_NAME +" TEXT ,"
                    +COLUMN_OFFER_EXPIRY_DATE+" DATETIME DEFAULT NULL"
                    + ")";


    public Offer(){}
    public Offer(int offer_id, int center_id, String offer_title, String offer_content, String expiry_date,String center_name) {
        this.offer_id = offer_id;
        this.center_id = center_id;
        this.offer_title = offer_title;
        this.offer_content = offer_content;
        this.expiry_date = expiry_date;
        this.center_name=center_name;
    }

    public int getOffer_id() {
        return offer_id;
    }

    public int getCenter_id() {
        return center_id;
    }

    public String getOffer_title() {
        return offer_title;
    }

    public String getOffer_content() {
        return offer_content;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setOffer_id(int offer_id) {
        this.offer_id = offer_id;
    }

    public void setCenter_id(int center_id) {
        this.center_id = center_id;
    }

    public void setOffer_title(String offer_title) {
        this.offer_title = offer_title;
    }

    public void setOffer_content(String offer_content) {
        this.offer_content = offer_content;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    @Override
    public String toString() {
        return "title: "+offer_title+
                " content: "+offer_content+
                " expDate: "+expiry_date+
                " offerId: "+String.valueOf(offer_id)+
                " centerId: "+String.valueOf(center_id);
    }

    public String getCenter_name() {
        return center_name;
    }

    public void setCenter_name(String center_name) {
        this.center_name = center_name;
    }
}

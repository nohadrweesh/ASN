package com.example.a.myapplication;

public class AdvertisementClass {

    private String title;
    private String description;
    private String imgURL;
    private String expirationDate = null;
    private int ID;
    private int ownerID;
    private String ownerName;


    AdvertisementClass(int ID, int ownerID, String ownerName ,String title, String description, String imgPath){
        this.ID = ID;
        this.ownerID = ownerID;
        this.title = title;
        this.description = description;
        this.imgURL = imgPath;
        this.ownerName = ownerName;
    }
    AdvertisementClass(int ID, int ownerID, String ownerName,String title, String description, String imgPath, String expirationDate){
        this.ID = ID;
        this.ownerID = ownerID;
        this.title = title;
        this.description = description;
        this.imgURL = imgPath;
        this.expirationDate = expirationDate;
        this.ownerName = ownerName;
    }

    public void setID(int ID) { this.ID = ID; }
    public void setOwnerID(int ownerID) { this.ownerID = ownerID; }
    public void setTitle(String title){
        this.title = title;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setImgURL(String imgURL){
        this.imgURL = imgURL;
    }
    public void setExpirationDate(String expirationDate){ this.expirationDate = expirationDate; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public int getID() { return ID; }
    public int getOwnerID() { return ownerID; }
    public String getDescription() {
        return description;
    }
    public String getTitle() {
        return title;
    }
    public String getImgURL() {
        return imgURL;
    }
    public String getExpirationDate() { return expirationDate; }
    public String getOwnerName() { return ownerName; }
}

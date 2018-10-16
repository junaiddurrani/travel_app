package com.android.app.travelapp;

public class Tours {
    private String place, image;

    public Tours() {
    }

    public Tours(String place, String image) {
        this.place = place;
        this.image = image;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

package com.example.user.viewpager_fragment.Item;

/**
 * Created by user on 2017-07-18.
 */

public class PlaceLoad {
    private double latitude;
    private double longitude;

    public PlaceLoad(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

package com.example.user.viewpager_fragment.Item;

import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by user on 2017-07-19.
 */

public class MarkerItem {
    double lat;
    double lon;
    String place;
    String num;


    public  MarkerItem(double lat, double lon, String place, String num) {
        this.lat = lat;
        this.lon = lon;
        this.place = place;
        this.num = num;
    }

    public String getNum(){
        return num;
    }

    public void setNum(String num){
        this.num = num;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getplace() {
        return place;
    }

    public void setplace(String place) {
        this.place = place;
    }


}

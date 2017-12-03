package com.example.user.viewpager_fragment.Util;

import android.content.Context;
import android.text.TextUtils;

import com.example.user.viewpager_fragment.Item.CustomMarker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by user on 2017-07-18.
 */

public class MapControler {

    private Context mContext;
    private GoogleMap mMap;

    public MapControler(Context mContext, GoogleMap mMap) {
        this.mContext = mContext;
        this.mMap = mMap;
    }

    public CustomMarker drawMarker(CustomMarker customMarker, boolean isCamera, float markerColor){
        if(mMap == null){
            return null;
        }

        LatLng placeLocation = new LatLng(customMarker.location.latitude, customMarker.location.longitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(placeLocation);
        markerOptions.draggable(true);
        if(TextUtils.isEmpty(customMarker.title)) {
            markerOptions.title(customMarker.title);
        }
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(markerColor));

        customMarker.marker = mMap.addMarker(markerOptions);
        if(isCamera){
            moveCamera(placeLocation, 16);
        }

        return customMarker;
    }

    public void moveCamera(LatLng location, int zoomLevel){
        if(mMap == null){
            return;
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));
    }
}

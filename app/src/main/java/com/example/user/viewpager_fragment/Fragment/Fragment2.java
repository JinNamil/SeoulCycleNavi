package com.example.user.viewpager_fragment.Fragment;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.viewpager_fragment.Item.CurrentLoad;
import com.example.user.viewpager_fragment.Item.CustomMarker;
import com.example.user.viewpager_fragment.Item.MarkerItem;
import com.example.user.viewpager_fragment.Item.MyVolley;
import com.example.user.viewpager_fragment.Item.PlaceLoad;
import com.example.user.viewpager_fragment.R;
import com.example.user.viewpager_fragment.Util.MapControler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import noman.googleplaces.Place;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;
//현재위치에서 대여소까지 길찾기
/**
 * Created by user on 2017-07-16.
 */

public class Fragment2 extends Fragment implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, PlacesListener {
    private static final LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;
    private static final int UPDATE_INTERVAL_MS = 15000;
    private static final int FASTEST_UPDATE_INTERVAL_MS = 15000;
    private MapControler mControler;
    private CustomMarker mCycleMarker;
    private static final String TAG = "googlemap_example";
    private MapView mapView = null;
    private Marker currentMarker = null;
    private GoogleApiClient googleApiClient = null;
    private GoogleMap googleMap = null;
    private final String server_url = "https://www.bikeseoul.com/app/station/getStationRealtimeStatus.do";
    StringBuilder sb = new StringBuilder();

    private CustomMarker mCurrentMarker;
    private CustomMarker mPlaceMarker;

    private ArrayList<CustomMarker> mCycleList;


    private CurrentLoad currentLocation;
    private PlaceLoad placeLocation;

    private boolean isVergin = true;
    //final RequestQueue qe = Volley.newRequestQueue(getActivity().getApplicationContext());

    public Fragment2() {
        //Required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final RequestQueue qe = Volley.newRequestQueue(getActivity().getApplicationContext());
        ViewGroup layout =(ViewGroup) inflater.inflate(R.layout.fragment_fragment2, container, false);
        //Button home_btn = (Button) layout.findViewById(R.id.home_button);
        mapView = (MapView) layout.findViewById(R.id.map);
        mapView.getMapAsync(this);
        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();

        if (googleApiClient != null && googleApiClient.isConnected())
            googleApiClient.disconnect();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        if (googleApiClient != null)
            googleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();

        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();

        if (googleApiClient != null) {
            googleApiClient.unregisterConnectionCallbacks(this);
            googleApiClient.unregisterConnectionFailedListener(this);

            if (googleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
                googleApiClient.disconnect();
            }
        }
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();

        android.app.FragmentManager fm = getActivity().getFragmentManager();
        android.app.Fragment fragment = fm.findFragmentById(R.id.place_autocomplete_fragment);
        android.app.FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

//    public void onDestroyView() {
//        super.onDestroyView();
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        Fragment fragment = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment);
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.remove(fragment);
//        fragmentTransaction.commit();
//    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }
//        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//
//            @Override
//            public void onPlaceSelected(com.google.android.gms.location.places.Place place) {
//                Log.w(TAG, "onPlaceSelected");
//
//
//                LatLng location = place.getLatLng();
//
//                mPlaceMarker = new CustomMarker();
//                mPlaceMarker.location = location;
//                mPlaceMarker.title = place.getName().toString();
//                mPlaceMarker = mControler.drawMarker(mPlaceMarker, true, BitmapDescriptorFactory.HUE_RED);
//
////                setCurrentLocation(location, place.getName().toString(), place.getAddress().toString());
//                Log.d(TAG, "setCurrentLocation");
//
////액티비티가 처음 생성될 때 실행되는 함수
//
//                if (mapView != null) {
//                    mapView.onCreate(savedInstanceState);
//                }
//            }
//
//            @Override
//            public void onError(Status status) {
//                Log.i(TAG, "An error occurred: " + status);
//            }
//        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged call");
        if (mCurrentMarker != null && mCurrentMarker.marker != null) {
            mCurrentMarker.marker.remove();
            mCurrentMarker = null;
        }
        mCurrentMarker = new CustomMarker();
        mCurrentMarker.location = new LatLng(location.getLatitude(), location.getLongitude());
//        mCurrentMarker = mControler.drawMarker(mCurrentMarker, isVergin, BitmapDescriptorFactory.HUE_BLUE);
        isVergin = false;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentMarker.location, 12));

        //currentLocation = new CurrentLoad(location.getLatitude(), location.getLongitude());
    }

//    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {
//        if (currentMarker != null) currentMarker.remove();
//        Log.d(TAG, "MarkRemove");
//
//        if (location != null) {
//            //현재위치의 위도 경도 가져옴
//            double latitude = location.getLatitude();
//            double longitude = location.getLongitude();
//            currentLocation = new CurrentLoad(latitude, longitude);
//
//            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
//
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(currentLocation);
//            markerOptions.title(markerTitle);
//            markerOptions.snippet(markerSnippet);
//            markerOptions.draggable(true);
//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//            currentMarker = this.googleMap.addMarker(markerOptions);
//
//
//            this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 21));
//            Log.d(TAG, "CurrentCamera");
//            return;
//        }
//    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        getSampleMarkerItems();
        mControler = new MapControler(getActivity(), googleMap);

        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        //  API 23 이상이면 런타임 퍼미션 처리 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 사용권한체크
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);

            if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED) {
                //사용권한이 없을경우
                //권한 재요청
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            } else {
                //사용권한이 있는경우
                if (googleApiClient == null) {
                    buildGoogleApiClient();
                }

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                }
            }
        } else {

            if (googleApiClient == null) {
                buildGoogleApiClient();
            }

            googleMap.setMyLocationEnabled(true);
        }


        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            String daum = "net.daum.android.map";

            private boolean isInstalled(String daum) {
                boolean services = false;
                try {
                    getActivity().getPackageManager().getApplicationInfo(daum, 0);
                    services = true;
                } catch (PackageManager.NameNotFoundException e) {
                    services = false;
                }
                return services;
            }

            private void installPlayService() {
                String name = "https://play.google.com/store/apps/details?id=net.daum.android.map";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(name));
                startActivity(intent);
            }

            @Override
            public boolean onMarkerClick(Marker marker) {
                CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
                googleMap.animateCamera(center);

                try{if(!isInstalled(daum))
                    installPlayService();}catch(Exception e){}
                double currentlatitude = mCurrentMarker.location.latitude;
                double currentlongitude = mCurrentMarker.location.longitude;
                String url = "daummaps://route?sp=" + currentlatitude + ", " + currentlongitude + "&ep=" + marker.getPosition().latitude + ", " + marker.getPosition().longitude + "&by=BICYCLE";
                Log.w("TEST", "url : " + url);
//                url = "http://map.daum.net/link/to/카카오판교오피스,37.402056,127.108212";
                try{
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);}catch(Exception e){e.printStackTrace();}


                return true;
            }

        });
    }


    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), 2, this)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (!checkLocationServicesStatus()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("위치 서비스 비활성화");
            builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n" +
                    "위치 설정을 수정하십시오.");
            builder.setCancelable(true);
            builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent callGPSSettingIntent =
                            new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
                }
            });
            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL_MS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                LocationServices.FusedLocationApi
                        .requestLocationUpdates(googleApiClient, locationRequest, this);
            }
        } else {
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(googleApiClient, locationRequest, this);

            this.googleMap.getUiSettings().setCompassEnabled(true);
            this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        if (cause == CAUSE_NETWORK_LOST)
            Log.e(TAG, "onConnectionSuspended(): Google Play services " +
                    "connection lost.  Cause: network lost.");
        else if (cause == CAUSE_SERVICE_DISCONNECTED)
            Log.e(TAG, "onConnectionSuspended():  Google Play services " +
                    "connection lost.  Cause: service disconnected");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Location location = new Location("");
        location.setLatitude(DEFAULT_LOCATION.latitude);
        location.setLongitude((DEFAULT_LOCATION.longitude));
    }

    @Override
    public void onPlacesFailure(PlacesException e) {

    }

    @Override
    public void onPlacesStart() {

    }

    @Override
    public void onPlacesSuccess(List<Place> places) {

    }

    @Override
    public void onPlacesFinished() {

    }

    private void getSampleMarkerItems() {
        Location location;
        final RequestQueue qe = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, server_url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("realtimeList");
                            mCycleList = new ArrayList<>();
                            Log.w("TEST", " array " + array.toString());
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject item = array.getJSONObject(i);

                                final double B_latitude = Double.parseDouble(item.getString("stationLatitude"));
                                final double B_longitude = Double.parseDouble(item.getString("stationLongitude"));
                                final String bikestation_name = item.getString("stationName");
                                CustomMarker cycleItem = new CustomMarker();
                                cycleItem.location = new LatLng(B_latitude, B_longitude);
                                cycleItem.title = bikestation_name;
//                                cycleItem.marker = addMarker(new MarkerItem(B_latitude, B_longitude, bikestation_name));

                                mCycleList.add(cycleItem);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w("TEST", error.toString());
                    }
                });
        qe.add(request);
    }


    private Marker addMarker(MarkerItem markerItem) {
        LatLng position = new LatLng(markerItem.getLat(), markerItem.getLon());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(position);
        markerOptions.title("stationName");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        return googleMap.addMarker(markerOptions);
    }


//    private Marker addMarker(Marker marker) {
//        double lat = marker.getPosition().latitude;
//        double lon = marker.getPosition().longitude;
//        String place = String.valueOf(String.valueOf("대여소"));
//        MarkerItem temp = new MarkerItem(lat, lon, place);
//        return addMarker(temp);
//    }
    }

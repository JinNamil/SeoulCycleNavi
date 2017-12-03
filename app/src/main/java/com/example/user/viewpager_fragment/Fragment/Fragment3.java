package com.example.user.viewpager_fragment.Fragment;
//현재위치에서 목적지, 대여소까지의 길찾기
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.LocaleDisplayNames;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.viewpager_fragment.Activity.MainActivity;
import com.example.user.viewpager_fragment.Item.CurrentLoad;
import com.example.user.viewpager_fragment.Item.CustomMarker;
import com.example.user.viewpager_fragment.Item.MarkerItem;
import com.example.user.viewpager_fragment.Item.MyVolley;
import com.example.user.viewpager_fragment.Item.PlaceLoad;
import com.example.user.viewpager_fragment.R;
import com.example.user.viewpager_fragment.Util.MapControler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;
import com.google.gson.JsonObject;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.ui.BubbleIconFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

/**
 * Created by user on 2017-07-16.
 */

public class Fragment3 extends Fragment implements OnMapReadyCallback,
        LocationListener, PlacesListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;
    private static final int UPDATE_INTERVAL_MS = 15000;
    private static final int FASTEST_UPDATE_INTERVAL_MS = 15000;

    Location mCurrentLocation;
    double lat = 0, lng = 0;
    private final String server_url = "https://www.bikeseoul.com/app/station/getStationRealtimeStatus.do";
    private GoogleMap googleMap = null;
    private MapControler mControler;
    private MapView mapView = null;
    private GoogleApiClient googleApiClient = null;
    private Marker currentMarker = null;
    private Marker placeMarker = null;

    private CustomMarker mCurrentMarker;
    private CustomMarker mPlaceMarker;
    private ArrayList<CustomMarker> mCycleList;


    private final static int MAXENTRIES = 5;
    private String[] LikelyPlaceNames = null;
    private String[] LikelyAddresses = null;
    private String[] LikelyAttributions = null;
    private LatLng[] LikelyLatLngs = null;
    private CurrentLoad currentLocation;
    private PlaceLoad placeLocation;

    private boolean isVergin = true;

    public Fragment3() {
        //Required
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
//            this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
//            Log.d(TAG, "CurrentCamera");
//            return;
//        }
//    }

    StringBuilder sb = new StringBuilder();
    TextView home_text = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_fragment3, container, false);

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
        Log.d("TEST", "Stop_2");
        super.onStop();
        mapView.onStop();

        if (googleApiClient != null && googleApiClient.isConnected())
            googleApiClient.disconnect();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
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
        Log.d("Test", "Pause_2");
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
        android.app.Fragment fragment = fm.findFragmentById(R.id.place_autocomplete_fragment3);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //액티비티가 처음 생성될 때 실행되는 함수
        MapsInitializer.initialize(getActivity().getApplicationContext());

        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().
                findFragmentById(R.id.place_autocomplete_fragment3);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {
                Log.w(TAG, "onPlaceSelected");


                LatLng location = place.getLatLng();

                mPlaceMarker = new CustomMarker();
                mPlaceMarker.location = location;
                mPlaceMarker.title = place.getName().toString();
                mPlaceMarker = mControler.drawMarker(mPlaceMarker, true, BitmapDescriptorFactory.HUE_RED);

//                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mPlaceMarker.location, 12));

                //setCurrentLocation(location, place.getName().toString(), place.getAddress().toString());
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        getSampleMarkerItems();
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
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
            public void onInfoWindowClick(Marker marker) {
                CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
                googleMap.animateCamera(center);


                try {
                    if (!isInstalled(daum))
                        installPlayService();
                } catch (Exception e) {
                }
                double currentlatitude = mCurrentMarker.location.latitude;
                double currentlongitude = mCurrentMarker.location.longitude;
//                double placelatitude = mPlaceMarker.location.latitude;
//                double placelongitude = mPlaceMarker.location.longitude;
                String url = "daummaps://route?sp=" + currentlatitude + ", " + currentlongitude + "&ep=" + marker.getPosition().latitude + ", " + marker.getPosition().longitude + "&by=BICYCLE";
                Log.w("TEST", "url : " + url);
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
//        mCurrentMarker = new CustomMarker();
        mControler = new MapControler(getActivity(), googleMap);

//        Marker marker = googleMap.addMarker(new MarkerOptions().title(markerItem.getplace()).position(position).icon(BitmapDescriptorFactory.fromResource(R.drawable.bikestation_marker)));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(, 12));
//        LatLng latLng = new LatLng(mCurrentMarker.location.latitude, mCurrentMarker.location.longitude);
//        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
//        mCurrentMarker.location = new LatLng(location.getLatitude(), location.getLongitude());

        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에 지도의 초기위치를 서울로 이동
//        setCurrentLocation(null, "위치정보 가져올 수 없음", "위치 퍼미션과 GPS 활성 여부 확인");


//        LatLng latLng = new LatLng();
        googleMap.getUiSettings().setCompassEnabled(true);
//        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentMarker.location, 12));

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


//        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//
//            String daum = "net.daum.android.map";
//
//            private boolean isInstalled(String daum) {
//                boolean services = false;
//                try {
//                    getActivity().getPackageManager().getApplicationInfo(daum, 0);
//                    services = true;
//                } catch (PackageManager.NameNotFoundException e) {
//                    services = false;
//                }
//                return services;
//            }
//
//            private void installPlayService() {
//                String name = "https://play.google.com/store/apps/details?id=net.daum.android.map";
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(name));
//                startActivity(intent);
//            }
//
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
//                googleMap.animateCamera(center);
//
//
//                try {
//                    if (!isInstalled(daum))
//                        installPlayService();
//                } catch (Exception e) {
//                }
//                double currentlatitude = mCurrentMarker.location.latitude;
//                double currentlongitude = mCurrentMarker.location.longitude;
////                double placelatitude = mPlaceMarker.location.latitude;
////                double placelongitude = mPlaceMarker.location.longitude;
//                String url = "daummaps://route?sp=" + currentlatitude + ", " + currentlongitude + "&ep=" + marker.getPosition().latitude + ", " + marker.getPosition().longitude + "&by=BICYCLE";
//                Log.w("TEST", "url : " + url);
//                try {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//                return true;
//            }
//
//        });
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
        mCurrentMarker = mControler.drawMarker(mCurrentMarker, isVergin, BitmapDescriptorFactory.HUE_BLUE);
        isVergin = false;
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

        Log.w(TAG, "currentLocation");
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
                .enableAutoManage(getActivity(), 1, this)
                .build();
        googleApiClient.connect();
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

    private void getSampleMarkerItems() {

//        final Marker marker = null;
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
//                                final double bike_num = Double.parseDouble("parkingBikeTotCnt");
                                final String bike_num = item.getString("parkingBikeTotCnt");

                                CustomMarker cycleItem = new CustomMarker();
                                cycleItem.location = new LatLng(B_latitude, B_longitude);
                                cycleItem.title = bikestation_name;
                                cycleItem.marker = addMarker(new MarkerItem(B_latitude, B_longitude, bikestation_name, bike_num));
//                                Marker marker = googleMap.addMarker(new MarkerOptions().title(bikestation_name).position(cycleItem.location).icon(BitmapDescriptorFactory.fromResource(R.drawable.bikestation_marker)));
//
//                                marker.showInfoWindow();
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
        LatLng position = new LatLng(markerItem.getLat(), (markerItem.getLon()));
        MarkerOptions markerOptions = new MarkerOptions();
//        Marker marker = googleMap.addMarker(new MarkerOptions().title(markerItem.getplace()).position(markerItem.setLat(markerItem.getLat()),markerItem.setLon(markerItem.getLon())).icon(BitmapDescriptorFactory.fromResource(R.drawable.bikestation_marker)));
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bsp));
//        markerOptions.alpha(0.5f);
        markerOptions.title(markerItem.getplace());
        markerOptions.snippet("대여가능 따릉이 : "+markerItem.getNum());
//        return marker.showInfoWindow();
        return googleMap.addMarker(markerOptions);

    }



//    private Marker addMarker(Marker marker) {
//        double lat = marker.getPosition().latitude;
//        double lon = marker.getPosition().longitude;
//        String place = String.valueOf(String.valueOf("대여소"));
//        MarkerItem temp = new MarkerItem(lat, lon, place);
//        return addMarker(temp);
//    }

//    static View v;
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (v != null) {
//            ViewGroup parent = (ViewGroup) v.getParent();
//            if (parent != null) {
//                parent.removeView(v);
//            }
//        }
//    }

    @Override
    public void onPlacesFailure(PlacesException e) {

    }

    @Override
    public void onPlacesStart() {

    }

    @Override
    public void onPlacesSuccess(List<noman.googleplaces.Place> places) {

    }

    @Override
    public void onPlacesFinished() {

    }
}
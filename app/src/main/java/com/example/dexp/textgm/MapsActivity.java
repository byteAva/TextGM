package com.example.dexp.textgm;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double markLat,markLon;
    double targLat,targLon;
    double gldist;
    Polyline poly;
    TextView txtTask,txtKm;
    boolean marker = false;
    boolean buttNext = false;
    Marker mMarker;
    double sumresult = 0;
    Button btnNext;
    /////////////////////////////////////////////////////////////////
    int id=0;

    String [] targmas = {"Трафик","Кирилл","Некит","Топ кодер"};
    double [] latmas = {45.134290,45.190445,45.031929,45.122265};
    double [] lngmas = {33.603298,33.366861,35.382429,34.986569};
//////////////////////////////////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        txtTask = (TextView)findViewById(R.id.txtTask);
        txtKm = (TextView)findViewById(R.id.txtKm);
        btnNext = (Button) findViewById(R.id.btn_next);
        mapFragment.getMapAsync(this);
    }

    public void onButtonNextClick(View view)
    {
        // выводим сообщение
        //Toast.makeText(this, "Зачем вы нажали?", Toast.LENGTH_SHORT).show();
        mMarker.setVisible(false);
        marker = false;
        poly.remove();
        setTarget();
        btnNext.setVisibility(View.INVISIBLE);
        buttNext = false;

    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // Sets the map type to be "hybrid"
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        LatLng start = new LatLng(0, 0);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
        //45.13390497482353,33.60509466379881

        setTarget();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                //save current location
                if (buttNext == false) {
                    System.out.println("Rоординаты : " + point);
                    markLat = point.latitude;
                    markLon = point.longitude;

                    if (marker == false) {
                        mMarker = mMap.addMarker(new MarkerOptions()
                                .draggable(true)
                                .visible(true)
                                // .icon(BitmapDescriptorFactory.fromResource(R.drawable.trff))
                                .position(point));
                        marker = true;
                    } else {
                        mMarker.setPosition(point);
                    }
                }
               // chkPidr();
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                if (buttNext == false) {
                    distBetween(markLat, markLon, targLat, targLon);
                    poly = mMap.addPolyline(new PolylineOptions().geodesic(true)
                            .width(5)
                            .add(new LatLng(markLat, markLon))
                            .add(new LatLng(targLat, targLon))
                    );
                    getResult();
                }
                return true;
            }
        });
    }


    private void distBetween(double latA,double lngA,double latB,double lngB ) {

        Location locationA = new Location("point A");

        locationA.setLatitude(latA);
        locationA.setLongitude(lngA);

        Location locationB = new Location("point B");

        locationB.setLatitude(latB);
        locationB.setLongitude(lngB);

        double distance = locationA.distanceTo(locationB);
       // System.out.println("Расстояние : "+ distance);
        gldist = distance;

    }

    public void setTarget(){

        txtTask.setText("Где живёт "+ targmas[id] +"?");
        targLat = latmas[id];
        targLon = lngmas[id];
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(targLat, targLon))
                .visible(false)
                .title("lol"));
    }

    public void getResult(){

        double result;
        double rounddist = gldist/1000;
        result = 40075-rounddist;
        sumresult = result + sumresult;
       // txtTask.setText("Ошибка на: "+ Math.round(rounddist)+ "км");
        txtKm.setText("Баллы: "+Math.round(sumresult));
        id++;

        btnNext.setVisibility(View.VISIBLE);
        buttNext = true;
    }
}


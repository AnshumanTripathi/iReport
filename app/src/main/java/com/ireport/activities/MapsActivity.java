package com.ireport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ireport.R;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ArrayList<String> idList = getIntent().getExtras().getStringArrayList("idList");
        ArrayList<String> latList = getIntent().getExtras().getStringArrayList("latList");
        ArrayList<String> lngList = getIntent().getExtras().getStringArrayList("lngList");
        for(int i=0;i<idList.size();i++){
            LatLng markerLatLng = new LatLng(Double.parseDouble(latList.get(i)),
                    Double.parseDouble(lngList.get(i)));
            Marker marker = mMap.addMarker(new MarkerOptions().position(markerLatLng));
            marker.setTag(idList.get(i));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(markerLatLng));
            mMap.setOnMarkerClickListener(this);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent intent = new Intent(MapsActivity.this,ViewReportActivity.class);
        intent.putExtra("report_id_in_mongo",marker.getTag().toString());
        startActivity(intent);
        return false;
    }
}

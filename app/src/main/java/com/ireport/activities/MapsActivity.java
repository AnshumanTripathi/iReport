package com.ireport.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.ireport.R;
import com.ireport.controller.utils.Constants;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        findViewById(R.id.goToHeatMap).setVisibility(View.VISIBLE);
        findViewById(R.id.goToMap).setVisibility(View.GONE);
        findViewById(R.id.goToHeatMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this,HeatMapActivity.class);

                ArrayList<String> idList = getIntent().getExtras().getStringArrayList("idList");
                ArrayList<String> latList = getIntent().getExtras().getStringArrayList("latList");
                ArrayList<String> lngList = getIntent().getExtras().getStringArrayList("lngList");

                intent.putStringArrayListExtra("idList",idList);
                intent.putStringArrayListExtra("latList",latList);
                intent.putStringArrayListExtra("lngList",lngList);

                startActivity(intent);
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    private ArrayList<LatLng> readItems() {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        list.add(new LatLng(37.3382,121.8863));
        list.add(new LatLng(37.3688,122.0363));
        return list;
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
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Constants.DEF_LAT,Constants.DEF_LNG),9));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MapsActivity.this,ListReportsActivity.class);
        startActivity(intent);
    }
}

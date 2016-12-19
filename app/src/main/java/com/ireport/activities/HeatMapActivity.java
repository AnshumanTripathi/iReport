package com.ireport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.ireport.R;
import com.ireport.controller.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class HeatMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        findViewById(R.id.goToHeatMap).setVisibility(View.GONE);
        findViewById(R.id.goToMap).setVisibility(View.VISIBLE);
        findViewById(R.id.goToMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeatMapActivity.this,MapsActivity.class);

                ArrayList<String> idList = getIntent().getExtras().getStringArrayList("idList");
                ArrayList<String> latList = getIntent().getExtras().getStringArrayList("latList");
                ArrayList<String> lngList = getIntent().getExtras().getStringArrayList("lngList");

                intent.putStringArrayListExtra("idList",idList);
                intent.putStringArrayListExtra("latList",latList);
                intent.putStringArrayListExtra("lngList",lngList);

                startActivity(intent);
            }
        });

    }

    private ArrayList<LatLng> readItems() {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        ArrayList<String> idList = getIntent().getExtras().getStringArrayList("idList");
        ArrayList<String> latList = getIntent().getExtras().getStringArrayList("latList");
        ArrayList<String> lngList = getIntent().getExtras().getStringArrayList("lngList");
        if (idList.size() != 0) {
            for (int i = 0; i < idList.size(); i++) {
                list.add(new LatLng(Double.parseDouble(latList.get(i)),
                        Double.parseDouble(lngList.get(i))));
            }
            return list;
        } else {
            return null;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        List<LatLng> list = readItems();

        if (list != null) {
            HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                    .data(list)
                    .build();

            mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(Constants.DEF_LAT,Constants.DEF_LNG),10));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(HeatMapActivity.this,ListReportsActivity.class);
        startActivity(intent);
    }

}


package com.example.helpgenic;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.helpgenic.databinding.ActivityMaps2Binding;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMaps2Binding binding;
    Double latts = 0.0, longs = 0.0;
    String clinicName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMaps2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        latts = getIntent().getDoubleExtra("latts",0.0);
        longs = getIntent().getDoubleExtra("longs",0.0);
        clinicName = getIntent().getStringExtra("clinicName");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(latts, longs);
        mMap.addMarker(new MarkerOptions().position(location).title(clinicName));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20000.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }
}
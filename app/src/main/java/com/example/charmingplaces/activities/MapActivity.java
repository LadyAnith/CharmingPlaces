package com.example.charmingplaces.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.charmingplaces.R;
import com.example.charmingplaces.client.CharmingPlacesApi;
import com.example.charmingplaces.pojo.GeoPoint;
import com.example.charmingplaces.pojo.PlacesInsideAreaRequestDto;
import com.example.charmingplaces.pojo.PlacesNearRequestDto;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Button btnCentrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Fragment fragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameMap, fragment).commit();
        btnCentrar = findViewById(R.id.btnCentrar);
        btnCentrar.setOnClickListener(v -> {
            MapFragment.gmap.findNearMarkersFromUser();
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }
/*
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;



        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));

        LatLng latLng = new LatLng(0, 0);
        float zoom = 15;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.moveCamera(cameraUpdate);
    }

    private static GoogleMapOptions obtenerConfigInicial(){
        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_NORMAL)
                .compassEnabled(false)
                .rotateGesturesEnabled(false)
                .tiltGesturesEnabled(false);

        return options;
    }*/
}

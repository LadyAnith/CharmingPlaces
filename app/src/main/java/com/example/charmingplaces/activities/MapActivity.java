package com.example.charmingplaces.activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.charmingplaces.R;
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
}

package com.example.charmingplaces.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.charmingplaces.R;
import com.example.charmingplaces.client.CharmingPlacesApi;
import com.example.charmingplaces.logic.GMap;
import com.example.charmingplaces.logic.Gps;
import com.example.charmingplaces.pojo.PlacesNearRequestDto;
import com.example.charmingplaces.pojo.PlacesListResponseDto;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {

    public static GMap gmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                gmap = new GMap(googleMap, new CharmingPlacesApi(getContext()), new Gps(getActivity()));

                gmap.getInstance().setOnCameraIdleListener(() -> gmap.findAreaMarkers());

                gmap.findNearMarkersFromUser();

            }
        });

        return view;
    }
}
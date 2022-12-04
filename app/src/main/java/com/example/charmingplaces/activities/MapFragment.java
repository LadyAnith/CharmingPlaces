package com.example.charmingplaces.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.charmingplaces.R;
import com.example.charmingplaces.logic.GMap;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

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

                gmap = new GMap(googleMap, getActivity());
                gmap.findNearMarkersFromUser();
            }
        });

        return view;
    }
}
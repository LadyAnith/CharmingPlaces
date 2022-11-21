package com.example.charmingplaces.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.example.charmingplaces.R;
import com.example.charmingplaces.client.CharmingPlacesApi;
import com.example.charmingplaces.logic.Gps;
import com.example.charmingplaces.pojo.PlacesNearRequestDto;
import com.example.charmingplaces.pojo.PlacesNearResponseDto;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {
    private GoogleMap gmap;

    private CharmingPlacesApi charmingPlacesApi;
    private Gps gps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        charmingPlacesApi = new CharmingPlacesApi(getContext());
        gps = new Gps(getActivity());

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {


                gmap = googleMap;

                gps.getLocation(gpsLocation -> {

                    gpsLocation.setLatitude(39.8581000).setLonguitude(-4.0227700);

                    gmap.clear();
                    gmap.getUiSettings().setZoomControlsEnabled(true);
                    gmap.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(false);
                    gmap.getUiSettings().setRotateGesturesEnabled(false);
                    gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsLocation.getLatitude(), gpsLocation.getLonguitude()), 13));

                    PlacesNearRequestDto placesNearRequestDto = new PlacesNearRequestDto(gpsLocation);

                    charmingPlacesApi.findInterestingPoint(placesNearRequestDto, response -> {
                        for (PlacesNearResponseDto.PlacesDto responseDto : response.getData()) {
                            agregarMarcador(responseDto);
                        }

                    }, error-> Log.d("ERROR", "Error al recibir los lugares", error));

                });



                /*
                                LatLng latLng = new LatLng(0, 0);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));


                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title(latLng.latitude + " KG" +latLng.longitude);
                        googleMap.clear();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));
                        googleMap.addMarker(markerOptions);
                    }
                });
*/
            }
        });

        return view;
    }


    private void agregarMarcador(PlacesNearResponseDto.PlacesDto responseDto) {
        agregarMarcador(responseDto.getYcoord(), responseDto.getXcoord(), responseDto.getName());
    }

    private void agregarMarcador(double lat, double lon, String titulo) {
        gmap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title(titulo));
    }
}
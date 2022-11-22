package com.example.charmingplaces.logic;

import android.util.Log;

import com.example.charmingplaces.activities.MapFragment;
import com.example.charmingplaces.client.CharmingPlacesApi;
import com.example.charmingplaces.pojo.GeoPoint;
import com.example.charmingplaces.pojo.PlacesInsideAreaRequestDto;
import com.example.charmingplaces.pojo.PlacesListResponseDto;
import com.example.charmingplaces.pojo.PlacesNearRequestDto;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GMap {
    private GoogleMap gmapInstance;
    private Gps gps;
    private CharmingPlacesApi charmingPlacesApi;

    public GoogleMap getInstance() {
        return gmapInstance;
    }

    public GMap(GoogleMap googleMap, CharmingPlacesApi charmingPlacesApi, Gps gps) {
        this.gmapInstance = googleMap;
        this.gps = gps;
        this.charmingPlacesApi = charmingPlacesApi;

        gmapInstance.clear();
        gmapInstance.getUiSettings().setZoomControlsEnabled(true);
        gmapInstance.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(false);
        gmapInstance.getUiSettings().setRotateGesturesEnabled(false);

    }


    public void findNearMarkersFromUser() {
        gps.getLocation(gpsLocation -> {

            gpsLocation.setLatitude(39.8581000).setLonguitude(-4.0227700);

            gmapInstance.clear();
            gmapInstance.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsLocation.getLatitude(), gpsLocation.getLonguitude()), 15));

            PlacesNearRequestDto placesNearRequestDto = new PlacesNearRequestDto(gpsLocation);

            charmingPlacesApi.findNearInterestingPoint(placesNearRequestDto,
                    response -> {
                        for (PlacesListResponseDto.PlacesDto responseDto : response.getData()) {
                            addMarker(responseDto);
                        }
                    },
                    error -> Log.d("ERROR", "Error al recibir los lugares", error));

        });
    }

    public void findAreaMarkers() {

        gmapInstance.clear();

        GeoPoint geoPoint = new GeoPoint(gmapInstance.getProjection().getVisibleRegion().farLeft);
        GeoPoint geoPoint2 = new GeoPoint(gmapInstance.getProjection().getVisibleRegion().nearRight);

        PlacesInsideAreaRequestDto placesInsideAreaRequestDto = new PlacesInsideAreaRequestDto();
        placesInsideAreaRequestDto.setGeoPointTopLeft(geoPoint);
        placesInsideAreaRequestDto.setGeoPointBottomRight(geoPoint2);

        charmingPlacesApi.findPlacesInsideArea(placesInsideAreaRequestDto,
                response -> {
                    for (PlacesListResponseDto.PlacesDto placesDto:response.getData()) {
                        addMarker(placesDto);
                    }
                },
                error -> Log.d("ERROR", "Error al recibir los lugares", error));
    }

    private void addMarker(PlacesListResponseDto.PlacesDto responseDto) {
        addMarker(responseDto.getYcoord(), responseDto.getXcoord(), responseDto.getName());
    }

    private void addMarker(GeoPoint geoPoint) {
        gmapInstance.addMarker(new MarkerOptions()
                        .position(new LatLng(geoPoint.getYcoord(), geoPoint.getXcoord())));
    }

    private void addMarker(double lat, double lon, String titulo) {
        gmapInstance.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title(titulo));
    }

}

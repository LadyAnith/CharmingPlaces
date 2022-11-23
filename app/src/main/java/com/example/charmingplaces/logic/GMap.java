package com.example.charmingplaces.logic;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.charmingplaces.R;
import com.example.charmingplaces.client.CharmingPlacesApi;
import com.example.charmingplaces.pojo.GeoPoint;
import com.example.charmingplaces.pojo.PlacesDto;
import com.example.charmingplaces.pojo.PlacesInsideAreaRequestDto;
import com.example.charmingplaces.pojo.PlacesNearRequestDto;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

public class GMap {
    private GoogleMap gmapInstance;
    private Gps gps;
    private CharmingPlacesApi charmingPlacesApi;
    private boolean isMarkerClicked = false;
    private Map<Marker, PlacesDto> markersMap = new HashMap<>();

    private PopupWindow popupWindow;
    private View popupView;

    public GoogleMap getInstance() {
        return gmapInstance;
    }

    public GMap(GoogleMap googleMap, Activity activity) {
        this.gmapInstance = googleMap;
        this.gps = new Gps(activity);
        this.charmingPlacesApi = new CharmingPlacesApi(activity);
        this.popupView = LayoutInflater.from(activity).inflate(R.layout.place_info_window_popup, null, false);
        popupWindow = new PopupWindow(popupView, 1000, 1500, false);


        gmapInstance.clear();
        gmapInstance.getUiSettings().setZoomControlsEnabled(true);
        gmapInstance.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(false);
        gmapInstance.getUiSettings().setRotateGesturesEnabled(false);
        gmapInstance.setOnCameraIdleListener(() -> {
            if (!isMarkerClicked) {
                findAreaMarkers();
            }

            isMarkerClicked = false;
        });
        gmapInstance.setOnMarkerClickListener(eventMarker -> {
            Log.d("INFO", eventMarker.getTitle());

            PlacesDto placesDto = markersMap.get(eventMarker);

            ((TextView) this.popupView.findViewById(R.id.txtName)).setText(placesDto.getName());
            popupWindow.showAtLocation(new View(activity), Gravity.CENTER,0,0);

            isMarkerClicked = true;
            return false;
        });

        popupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    public void findNearMarkersFromUser() {
        gps.getLocation(gpsLocation -> {

            gpsLocation.setLatitude(39.8581000).setLonguitude(-4.0227700);

            gmapInstance.clear();
            gmapInstance.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsLocation.getLatitude(), gpsLocation.getLonguitude()), 15));

            PlacesNearRequestDto placesNearRequestDto = new PlacesNearRequestDto(gpsLocation);

            charmingPlacesApi.findNearInterestingPoint(placesNearRequestDto,
                    response -> {
                        for (PlacesDto responseDto : response.getData()) {
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
                    for (PlacesDto placesDto : response.getData()) {
                        addMarker(placesDto);
                    }
                },
                error -> Log.d("ERROR", "Error al recibir los lugares", error));
    }

    private void addMarker(PlacesDto responseDto) {
        Marker m = addMarker(responseDto.getYcoord(), responseDto.getXcoord(), responseDto.getName());
        markersMap.put(m, responseDto);
    }

    private void addMarker(GeoPoint geoPoint) {
        gmapInstance.addMarker(new MarkerOptions()
                .position(new LatLng(geoPoint.getYcoord(), geoPoint.getXcoord())));
    }

    private Marker addMarker(double lat, double lon, String title) {
        return gmapInstance.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title(title));

    }


}

package com.example.charmingplaces.logic;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.charmingplaces.R;
import com.example.charmingplaces.client.PlacesApi;
import com.example.charmingplaces.pojo.GeoPoint;
import com.example.charmingplaces.pojo.PlacesDto;
import com.example.charmingplaces.pojo.PlacesInsideAreaRequestDto;
import com.example.charmingplaces.pojo.PlacesNearRequestDto;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GMap {
    private Activity context;
    private GoogleMap gmapInstance;
    private Gps gps;
    private PlacesApi charmingPlacesApi;

    private PopupWindow popupWindow;
    private View popupView;

    private boolean isMarkerClicked = false;
    private Map<Marker, PlacesDto> markersMap = new HashMap<>();

    public GMap(GoogleMap googleMap, Activity context) {
        this.context = context;
        this.gmapInstance = googleMap;
        this.gps = new Gps(context);
        this.charmingPlacesApi = new PlacesApi(context);
        this.popupView = LayoutInflater.from(context).inflate(R.layout.place_info_window_popup, null, false);
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
            ImageUtils.setImage(this.popupView.findViewById(R.id.imgBBDD), placesDto.getImageContent());
            setDirections(this.popupView.findViewById(R.id.btnLlegar), placesDto);

            popupWindow.showAtLocation(new View(context), Gravity.CENTER, 0, 0);

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



    public void setDirections(Button link, PlacesDto placeData) {
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps.getLocation(gpsLocation -> {
                    String template = "https://www.google.es/maps/dir/%s,%s/%s,%s";
                    String url = String.format(template, placeData.getYcoord(), placeData.getXcoord(), gpsLocation.getLatitude(), gpsLocation.getLonguitude());
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                });

            }
        });


    }


    public void findNearMarkersFromUser() {
        gps.getLocation(gpsLocation -> {

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

        float color = 0 + new Random().nextFloat() * (330 - 0);

        return gmapInstance.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .icon(BitmapDescriptorFactory.defaultMarker(color))
                .title(title));
    }
}

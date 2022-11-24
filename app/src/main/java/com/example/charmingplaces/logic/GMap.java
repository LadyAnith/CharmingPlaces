package com.example.charmingplaces.logic;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.util.Linkify;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
            setImage(this.popupView.findViewById(R.id.imgBBDD), placesDto.getUrl());
            setDirections(this.popupView.findViewById(R.id.btnLlegar), placesDto);

            popupWindow.showAtLocation(new View(activity), Gravity.CENTER, 0, 0);

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

    private void setImage(ImageView imageView, String encodedImage) {

        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(decodedByte);

    }

    private void setDirections(Button link, PlacesDto placeData) {
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps.getLocation(gpsLocation -> {
                    String template = "https://www.google.es/maps/dir/%s,%s/%s,%s";
                    String uri = String.format(template, placeData.getYcoord(), placeData.getXcoord(), gpsLocation.getLatitude(), gpsLocation.getLonguitude());
                    link.setText(uri);
                    Linkify.addLinks(link, Linkify.WEB_URLS);
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
        return gmapInstance.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title(title));

    }
}

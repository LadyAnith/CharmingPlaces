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

/**
 * Clase encargada de manipular un mapa en Google Maps
 */
public class GMap {
    private Activity context;
    private GoogleMap gmapInstance;
    private Gps gps;
    private PlacesApi charmingPlacesApi;

    private PopupWindow popupWindow;
    private View popupView;

    private boolean isMarkerClicked = false;
    //HashMap que relaciona Marcadores con la información del su lugar
    private Map<Marker, PlacesDto> markersMap = new HashMap<>();

    /**
     * Constructor de la clase que configura el mapa para que pueda permitirse el zoom y que no se rote.
     * Además establece los eventos de los marcadores, arrastre de mapa y apertura de pop ups
     *
     * @param googleMap
     * @param context
     */
    public GMap(GoogleMap googleMap, Activity context) {
        this.context = context;
        this.gmapInstance = googleMap;
        this.gps = new Gps(context);
        this.charmingPlacesApi = new PlacesApi(context);
        this.popupView = LayoutInflater.from(context).inflate(R.layout.place_info_window_popup, null, false);
        popupWindow = new PopupWindow(popupView, 1000, 1500, false);

        //Configurar el mapa
        gmapInstance.clear();
        gmapInstance.getUiSettings().setZoomControlsEnabled(true);
        gmapInstance.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(false);
        gmapInstance.getUiSettings().setRotateGesturesEnabled(false);

        //Evento al ejecutar cuando se arrastra el mapa. Ejecutará el método de encontrar marcadores en ese área.
        gmapInstance.setOnCameraIdleListener(() -> {
            if (!isMarkerClicked) {
                findAreaMarkers();
            }
            isMarkerClicked = false;
        });

        //Evento que muestra el pop up con la información del lugar de interés que se aloja en el marcador
        gmapInstance.setOnMarkerClickListener(clickedMarker -> {
            Log.d("INFO", clickedMarker.getTitle());

            //Consulta la información del lugar del marcador que ha pulsado
            PlacesDto placesDto = markersMap.get(clickedMarker);

            //Muestra la información del lugar en el pop up
            ((TextView) this.popupView.findViewById(R.id.txtName)).setText(placesDto.getName());
            ImageUtils.setImage(this.popupView.findViewById(R.id.imgBBDD), placesDto.getImageContent());
            setDirections(this.popupView.findViewById(R.id.btnLlegar), placesDto);

            popupWindow.showAtLocation(new View(context), Gravity.CENTER, 0, 0);

            isMarkerClicked = true;
            return false;
        });

        /**
         * Evento que cierra el pop up
         */
        popupView.setOnClickListener(v ->
                popupWindow.dismiss()
        );
    }


    /**
     * Método que indicará al usuario como llegar al lugar tomando la ubicaciión del usuario con el GPS
     * y abriendo el navegador con la direcciones de Google Maps
     *
     * @param button boton que ejecuta el evento
     * @param placeData coordenadas del lugar
     */
    public void setDirections(Button button, PlacesDto placeData) {
        button.setOnClickListener(v-> {
                gps.getLocation(gpsLocation -> {
                    String template = "https://www.google.es/maps/dir/%s,%s/%s,%s";
                    String url = String.format(template, placeData.getYcoord(), placeData.getXcoord(), gpsLocation.getLatitude(), gpsLocation.getLonguitude());
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                });
        });


    }


    /**
     * Método encargado de mostrar los marcadores cerca de la ubicación del usuario
     */
    public void findNearMarkersFromUser() {
        gps.getLocation(gpsLocation -> {

            gmapInstance.clear();
            //Hace zoom y mueve la cámara del mapa
            gmapInstance.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsLocation.getLatitude(), gpsLocation.getLonguitude()), 15));

            PlacesNearRequestDto placesNearRequestDto = new PlacesNearRequestDto(gpsLocation);
            //Ejecuta el método que consulta en el backen los puntos cercanos a esa ubicación
            charmingPlacesApi.findNearInterestingPoint(placesNearRequestDto,
                    response -> {
                        for (PlacesDto responseDto : response.getData()) {
                            //Muestra los marcadores en el mapa
                            addMarker(responseDto);
                        }
                    },
                    error -> Log.d("ERROR", "Error al recibir los lugares", error));

        });
    }

    /**
     * Método encargado de mostrar los marcadores dentro de un área visible del dispositivo
     */
    public void findAreaMarkers() {

        gmapInstance.clear();

        // Guarda los puntos visibles de la pantalla (izquierda arriba, derecha abajo)
        GeoPoint geoPoint = new GeoPoint(gmapInstance.getProjection().getVisibleRegion().farLeft);
        GeoPoint geoPoint2 = new GeoPoint(gmapInstance.getProjection().getVisibleRegion().nearRight);

        PlacesInsideAreaRequestDto placesInsideAreaRequestDto = new PlacesInsideAreaRequestDto();
        placesInsideAreaRequestDto.setGeoPointTopLeft(geoPoint);
        placesInsideAreaRequestDto.setGeoPointBottomRight(geoPoint2);

        //Manda como parámetro el área visible de la pantalla, y recibe los marcadores que se encuentran en ese área
        charmingPlacesApi.findPlacesInsideArea(placesInsideAreaRequestDto,
                response -> {
                    for (PlacesDto placesDto : response.getData()) {
                        //Pinta en el mapa el marcador
                        addMarker(placesDto);
                    }
                },
                error -> Log.d("ERROR", "Error al recibir los lugares", error));
    }

    /**
     * Método encargado de añadir marcadores al mapa y al Hashmap
     * @param responseDto
     */
    private void addMarker(PlacesDto responseDto) {
        Marker m = addMarker(responseDto.getYcoord(), responseDto.getXcoord(), responseDto.getName());
        markersMap.put(m, responseDto);
    }

    /**
     * Añade un marcador al mapa y le pinta con un color aleatorio
     *
     * @param lat latitud
     * @param lon longuitud
     * @param title titulo del marcador
     * @return el marcador creado
     */
    private Marker addMarker(double lat, double lon, String title) {

        float color = 0 + new Random().nextFloat() * (330 - 0);

        return gmapInstance.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .icon(BitmapDescriptorFactory.defaultMarker(color))
                .title(title));
    }
}

package com.example.charmingplaces.logic;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.charmingplaces.pojo.GpsLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Clase para manipular el gps del dispositivo
 */
public class Gps {

    /**
     * Interfaz que permite escribir un callback como parámetro en las llamadas del gps
     */
    public interface GetLocationCallback {
        void execute(GpsLocation gpsLocation);
    }

    private final static int REQUEST_CODE = 100;

    private Activity context;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public Gps(Activity context) {
        this.context = context;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    /**
     * Este método extrae la ubicación del usuario, necesita permisos de ubicación, los cuales serán pedidos si el usuario no los tiene
     *
     * @param callback función a ejecutar cuando se recupere la ubicación del usuario
     */
    public void getLocation(GetLocationCallback callback) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            Geocoder geocoder = new Geocoder(context, Locale.getDefault());

                            try {
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                /*Variables en las que guardo las coordenadas actuales del dispositivo*/
                                GpsLocation gpsLocation = new GpsLocation()
                                        .setAddress(addresses.get(0).getAddressLine(0))
                                        .setCountry(addresses.get(0).getCountryName())
                                        .setCity(addresses.get(0).getCountryName())
                                        .setLatitude(addresses.get(0).getLatitude())
                                        .setLonguitude(addresses.get(0).getLongitude());


                                //TODO BORRAR ESTA TRAMPA
                                gpsLocation.setLatitude(39.64663975887039).setLonguitude(-4.275421116794311);

                                callback.execute(gpsLocation);

                            } catch (IOException e) {
                                Log.e("ERROR", "GPS Error", e);
                            }

                        }
                    });

        } else {
            pedirPermisoLocation();
        }
    }

    /**
     * Método encargado de pedir el permiso de localización por gps
     */
    public void pedirPermisoLocation() {
        ActivityCompat.requestPermissions(this.context, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

    }

}

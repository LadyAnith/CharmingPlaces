package com.example.charmingplaces.logic;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.charmingplaces.pojo.GpsLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Gps {

    public interface GetLocationCallback{
        void execute(GpsLocation gpsLocation);
    }

    private final static int REQUEST_CODE = 100;

    private Activity context;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public Gps(Activity context){
        this.context = context;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void getLocation(GetLocationCallback callback) {
        GpsLocation gpsLocation = new GpsLocation().setCity("AnithLand");
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                Geocoder geocoder = new Geocoder(context, Locale.getDefault());

                                try {
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                                    /*Variables en las que guardo las coordenadas actuales del dispositivo*/
                                    gpsLocation
                                            .setAddress(addresses.get(0).getAddressLine(0))
                                            .setCountry(addresses.get(0).getCountryName())
                                            .setCity(addresses.get(0).getCountryName())
                                            .setLatitude(addresses.get(0).getLatitude())
                                            .setLonguitude(addresses.get(0).getLongitude());

                                    callback.execute(gpsLocation);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });

        }else{
            pedirPermisoLocation();
        }
    }

    public void pedirPermisoLocation() {
        ActivityCompat.requestPermissions(this.context, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

    }

}

package com.example.charmingplaces;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

public class CapturePlace extends AppCompatActivity {
    private static final int PERMISION_CODE = 1234;
    private static final int CAPTURE_CODE = 1001;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView gps;
    TextView pais;
    TextView ciudad;
    TextView latitud;
    TextView longuitud;
    Button play;
    ImageView foto;
    private final static int REQUEST_CODE = 100;
    String address, country, city;
    double latitude, longuitude;
    Bitmap fotoBitmap;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_place);

        gps = findViewById(R.id.txtUbicacion);
        pais = findViewById(R.id.txtPais);
        ciudad = findViewById(R.id.txtCiudad);
        latitud = findViewById(R.id.txtLatitud);
        longuitud = findViewById(R.id.txtLonguitud);
        play = findViewById(R.id.btnPlay);
        foto = findViewById(R.id.imgCamara);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getLocation();
                pedirPermisosCamara();

            }

        });


    }


    ActivityResultLauncher<Intent> camaraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Bundle extras = result.getData().getExtras();
                fotoBitmap = (Bitmap) extras.get("data");
                foto.setImageBitmap(fotoBitmap);
            }
        }
    });

    /**
     * Convertimos la foto en un array de bytes para poder enviarlo al microservicio
     * @param bitmap bitmap de la foto
     * @return bytearray del contenido de la imagen
     */
    private byte[] bitmapToByteArray(Bitmap bitmap){
        //Montamos un outputstream para almacenar la info de la foto
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //guardamos la info de la foto con compresión JPEG y calidad del 100% para no perder más calidad de imagen
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        //reconvertimos el outputstream a su cadena de bytes
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    //Método que se encarga de activar la cámara
    private void camara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent, 1);
        }
    }

    private void getLocation() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                Geocoder geocoder = new Geocoder(CapturePlace.this, Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                                    /*Variables en las que guardo las coordenadas actuales del dispositivo*/
                                    address = addresses.get(0).getAddressLine(0);
                                    country = addresses.get(0).getCountryName();
                                    city = addresses.get(0).getCountryName();
                                    latitude = addresses.get(0).getLatitude();
                                    longuitude= addresses.get(0).getLongitude();

                                    latitud.setText("Latitud: "+ latitude);
                                    longuitud.setText("Longuitud: " + longuitude);
                                    gps.setText("Dirección: " + address);
                                    ciudad.setText("Ciudad: "+ city);
                                    pais.setText("Pais: " + country);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });

        }else{
            pedirPermisos();
        }
    }

    private void pedirPermisos() {
        ActivityCompat.requestPermissions(CapturePlace.this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

    }

    private void pedirPermisosCamara() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.CAMERA)==
            PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                            PackageManager.PERMISSION_DENIED){

                String[]permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISION_CODE);
            } else{
                //abrirCamara();
                camaraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
            }
        } else{
            //abrirCamara();
            camaraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
        }
    }
/*
    private void abrirCamara() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "new image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAPTURE_CODE);
    }

 */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    getLocation();
                }else{
                    Toast.makeText(this, "Necesita aceptar los Permisos", Toast.LENGTH_SHORT).show();
                }
                break;
            case PERMISION_CODE:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //abrirCamara();
                    camaraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
                }else{
                    Toast.makeText(this, "Necesita aceptar los Permisos de la Cámara", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        /*
        if(requestCode == REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                getLocation();
            }else{
                Toast.makeText(this, "Necesita aceptar los Permisos", Toast.LENGTH_SHORT).show();
            }
        }

         */


    }
/*
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            foto.setImageURI(imageUri);
        }
    }

 */
}
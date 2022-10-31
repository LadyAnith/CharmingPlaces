package com.example.charmingplaces;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.charmingplaces.POJO.Photo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CapturePlace extends AppCompatActivity {
    private static final int PERMISION_CODE = 1234;;
    private final static int REQUEST_CODE = 100;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView gps;
    TextView pais;
    TextView ciudad;
    TextView latitud;
    TextView longuitud;
    Button play;
    Button send;
    ImageView foto;
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
        send = findViewById(R.id.btnSend);
        foto = findViewById(R.id.imgCamara);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getLocation();
                pedirPermisosCamara();

            }

        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject datos = buildRequestData();
                crearPuntoDeInteres(datos);
                //test();
            }
        });

    }

    /**
     * Recopila la información necesaria para ser enviada al controller de crear entrada
     *
     * @return objeto con los datos a añadir
     */
    public JSONObject buildRequestData() {
        //esto lo usaré para convertir mi objeto java en JSON para enviarlo al micro
        Gson jsonParser = new Gson();

        Photo photo = new Photo();
        byte[] imagen = bitmapToByteArray(fotoBitmap);
        photo.setImage(imagen);
        photo.setXcoord(longuitude);
        photo.setXcoord(latitude);

        String json = jsonParser.toJson(photo);
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }
    }

    public void crearPuntoDeInteres(JSONObject request) {
        String url = "http://192.168.1.104:8080/lugares/img";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        Log.d("PETARDAZO", "Error al conectar con lugaresconencanto.com", e);
                    }
                });

        // Esto añade la request que enviamos hacia el controller a una cola de peticiones y la manda cuando tenga disponibilidad
        RequestQueue.getInstance(this).addToRequestQueue(jsonObjectRequest);
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
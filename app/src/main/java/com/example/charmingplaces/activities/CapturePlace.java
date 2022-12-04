package com.example.charmingplaces.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charmingplaces.client.PlacesApi;
import com.example.charmingplaces.logic.Gps;
import com.example.charmingplaces.pojo.GpsLocation;
import com.example.charmingplaces.pojo.PhotoCreatePlaceRequestDto;
import com.example.charmingplaces.R;

import java.io.ByteArrayOutputStream;

public class CapturePlace extends AppCompatActivity {
    private static final int REQUEST_CODE_CAMERA = 1234;

    private final static int REQUEST_CODE_GPS = 100;

    TextView ubicacion;
    TextView pais;
    TextView ciudad;
    TextView latitud;
    TextView longuitud;
    EditText nombre;

    Button play;
    Button send;
    Button cancel;

    ImageView foto;

    Bitmap fotoBitmap;

    private GpsLocation gpsLocation;

    //Services
    private Gps gps;
    private PlacesApi placesApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_place);

        ubicacion = findViewById(R.id.txtName);
        play = findViewById(R.id.btnPlay);
        send = findViewById(R.id.btnSend);
        cancel = findViewById(R.id.btnCancel);
        foto = findViewById(R.id.imgBBDD);
        nombre = findViewById(R.id.txtNamePlace);

        gps = new Gps(this);
        placesApi = new PlacesApi(this);

        play.setOnClickListener(evento -> {
            play.setError(null);
            gps.getLocation(this::gpsLocationCallback);
            pedirPermisosCamara();

        });

        send.setOnClickListener(evento -> {
            play.setError(null);
            String resultadoNombre = nombre.getText().toString();
            if (resultadoNombre.isEmpty()) {
                nombre.setError("El nombre no puede ir vacío");
                return;
            }
            if (fotoBitmap == null) {
                play.setError("Se debe realizar una foto");
                Toast.makeText(this, "Debes de tomar una foto", Toast.LENGTH_LONG);
                return;
            }

            PhotoCreatePlaceRequestDto photo = buildRequestData();
            placesApi.createInterestingPoint(photo,
                    responseSuccess -> Log.d("DEBUG", "Va todo OKey: " + responseSuccess),
                    error -> Log.d("ERROR", error.getMessage())
            );

            nombre.setText("");
            foto.setImageResource(R.drawable.picture);
            //test();
        });

        cancel.setOnClickListener(evento -> {
            nombre.setText("");
            foto.setImageResource(R.drawable.picture);
        });

    }

    /**
     * Recopila la información necesaria para ser enviada al controller de crear entrada
     *
     * @return objeto con los datos a añadir
     */
    public PhotoCreatePlaceRequestDto buildRequestData() {

        byte[] imagen = bitmapToByteArray(fotoBitmap);
        return new PhotoCreatePlaceRequestDto()
                .setImage(imagen)
                .setXcoord(gpsLocation.getLonguitude())
                .setYcoord(gpsLocation.getLatitude())
                .setName(nombre.getText().toString())
                .setCity(gpsLocation.getCity())
                .setAddress(gpsLocation.getAddress());
    }

    ActivityResultLauncher<Intent> camaraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Bundle extras = result.getData().getExtras();
                    fotoBitmap = (Bitmap) extras.get("data");
                    foto.setImageBitmap(fotoBitmap);
                }
            });

    /**
     * Convertimos la foto en un array de bytes para poder enviarlo al microservicio
     *
     * @param bitmap bitmap de la foto
     * @return bytearray del contenido de la imagen
     */
    private byte[] bitmapToByteArray(Bitmap bitmap) {
        //Montamos un outputstream para almacenar la info de la foto
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //guardamos la info de la foto con compresión JPEG y calidad del 100% para no perder más calidad de imagen
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        //reconvertimos el outputstream a su cadena de bytes
        return stream.toByteArray();
    }

    private void pedirPermisosCamara() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED) {

                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, REQUEST_CODE_CAMERA);
            } else {
                //abrirCamara();
                camaraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
            }
        } else {
            //abrirCamara();
            camaraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_GPS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    gps.getLocation(response -> gpsLocationCallback(response));
                } else {
                    Toast.makeText(this, "Necesita aceptar los Permisos", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //abrirCamara();
                    camaraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
                } else {
                    Toast.makeText(this, "Necesita aceptar los Permisos de la Cámara", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void gpsLocationCallback(GpsLocation gpsLocation) {
        Toast.makeText(this, "FUNCIONA" + gpsLocation.getCity(), Toast.LENGTH_SHORT).show();
        this.gpsLocation = gpsLocation;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_back:
                Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(i);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
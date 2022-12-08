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
import android.widget.Toast;

import com.example.charmingplaces.client.PlacesApi;
import com.example.charmingplaces.logic.Gps;
import com.example.charmingplaces.pojo.GpsLocation;
import com.example.charmingplaces.pojo.PhotoCreatePlaceRequestDto;
import com.example.charmingplaces.R;

import java.io.ByteArrayOutputStream;

/**
 * Este Activity es el enncargado de hacer captura de la imagen y guardar las coordenadas de la ubicación del usuario
 */
public class CapturePlace extends AppCompatActivity {
    private static final int REQUEST_CODE_CAMERA = 1234;
    private final static int REQUEST_CODE_GPS = 100;

    private EditText txtName;

    private Button btnCapture;
    private Button btnSend;
    private Button btnCancel;

    private ImageView imgVPhoto;
    private Bitmap photoBitmap;

    private GpsLocation gpsLocation;

    //Services
    private Gps gps;
    private PlacesApi placesApi;
    private ActivityResultLauncher<Intent> camaraLauncher;

    /**
     * Método que se ejecuta al cargar la pantalla, se encarga de pintar el layout y de
     * instanciar las variables de los input y servicios que se utilizarán en este activity.
     * Da funcionalidad a los botones de la vista.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_place);

        btnCapture = findViewById(R.id.btnPlay);
        btnSend = findViewById(R.id.btnSend);
        btnCancel = findViewById(R.id.btnCancel);
        imgVPhoto = findViewById(R.id.imgBBDD);
        txtName = findViewById(R.id.txtNamePlace);

        gps = new Gps(this);
        placesApi = new PlacesApi(this);

        /*
        * Botón que pide los permisos para utilizar la cámara y la abre
        */
        btnCapture.setOnClickListener(event -> {
            btnCapture.setError(null);
            gps.getLocation(response -> this.gpsLocation = response);
            requestPermissionsAndOpenCamera();

        });

        /*
        * Botón que  registra un nuevo lugar de interés siempre y cuando se hayan rellenado todos los campos
        */
        btnSend.setOnClickListener(event -> {
            btnCapture.setError(null);
            String resultName = txtName.getText().toString();
            if (resultName.isEmpty()) {
                txtName.setError("El nombre no puede ir vacío");
                return;
            }
            if (photoBitmap == null) {
                btnCapture.setError("Se debe realizar una foto");
                Toast.makeText(this, "Debes de tomar una foto", Toast.LENGTH_LONG).show();
                return;
            }

            PhotoCreatePlaceRequestDto photo = buildRequestData();
            placesApi.createInterestingPoint(photo,
                    responseSuccess -> {
                        Log.d("DEBUG", "Va todo OKey");
                        Toast.makeText(this, "El Lugar se ha registrado correctamente", Toast.LENGTH_LONG).show();
                    },
                    error -> Log.d("ERROR", error.getMessage())

            );

            txtName.setText("");
            imgVPhoto.setImageResource(R.drawable.picture);
        });

        /*
        * Botón que limpia los datos de la pantalla, dejando el texto vacío y limpiando la imagen
         */
        btnCancel.setOnClickListener(evento -> {
            txtName.setText("");
            imgVPhoto.setImageResource(R.drawable.picture);
        });

        //Clase que recupera la información de la captura realizada
        camaraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        photoBitmap = (Bitmap) extras.get("data");
                        imgVPhoto.setImageBitmap(photoBitmap);
                    }
                });

    }

    /**
     * Recopila la información necesaria para ser enviada al controller de crear entrada
     *
     * @return objeto con los datos a añadir
     */
    public PhotoCreatePlaceRequestDto buildRequestData() {

        byte[] image = bitmapToByteArray(photoBitmap);
        return new PhotoCreatePlaceRequestDto()
                .setImage(image)
                .setXcoord(gpsLocation.getLonguitude())
                .setYcoord(gpsLocation.getLatitude())
                .setName(txtName.getText().toString())
                .setCity(gpsLocation.getCity())
                .setAddress(gpsLocation.getAddress());
    }




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

    /**
     * Método que en caso de no tener los permisos aceptados para abrir la cámara, te los solicite.
     * Una vez aceptados los permisos, te abre la cámara del dispositivo.
     */
    private void requestPermissionsAndOpenCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED) {

                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, REQUEST_CODE_CAMERA);
            } else {
                camaraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
            }
        } else {
            camaraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
        }
    }

    /**
     * Método encargado de verificar que se han aceptado los permisos de la cámara y del gps.
     * En el caso de que estén aceptados ejecuta las acciones para cada herramienta.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_GPS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    gps.getLocation(response -> this.gpsLocation = response);
                } else {
                    Toast.makeText(this, "Necesita aceptar los Permisos", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    camaraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
                } else {
                    Toast.makeText(this, "Necesita aceptar los Permisos de la Cámara", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * Método para mostrar el menú en la vista
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Método que crea un evento al clickar el item del menú
     * @param item
     * @return
     */
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
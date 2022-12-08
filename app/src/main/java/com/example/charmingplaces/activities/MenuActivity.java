package com.example.charmingplaces.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.charmingplaces.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Pantalla de la página principal de la aplicación, contiene el menú de opciones
 */
public class MenuActivity extends AppCompatActivity {
    private ConstraintLayout btnCapture;
    private ConstraintLayout btnMap;
    private ConstraintLayout btnListPlaces;
    private CircleImageView profileImage;

    /**
     * Método que se ejecuta al cargar la pantalla, se encarga de pintar el layout y de
     * instanciar las variables de los input y servicios que se utilizarán en este activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        profileImage = findViewById(R.id.profile_image);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //Tomo el usuario logueado para mostrar su imagen en el menú
        if (currentUser != null) {
            Glide
                    .with(this)
                    .load(currentUser.getPhotoUrl().toString())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(profileImage);
            Log.i("EMAIL", currentUser.getUid());
        }
        // Verifico el token del usuario para imprimirlo por consola
        currentUser.getIdToken(true)
                .addOnCompleteListener(command -> {
                    String token = command.getResult().getToken();
                    Log.i("token", token);
                });

        //Botón que abre la pantalla de Capture
        btnCapture = findViewById(R.id.btnCapturePlaces);
        btnCapture.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), CapturePlace.class);
            startActivity(i);

        });

        //Botón que abre la pantalla del Mapa
        btnMap = findViewById(R.id.btnMap);
        btnMap.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), MapActivity.class);
            startActivity(i);

        });

        //Botón que abre la pantalla del Listado de lugares
        btnListPlaces = findViewById(R.id.btnListPlaces);
        btnListPlaces.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), PlacesListActivity.class);
            startActivity(i);
        });
    }

    /**
     * Método para mostrar el menú en la vista
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Método que crea un evento al clickar el item del menú
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Log.d("DESLOGUEADO", "Usuario Desconectado");
                signOut();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Método encargado de cerrar sesión
     */
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
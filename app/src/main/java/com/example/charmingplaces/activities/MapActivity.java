package com.example.charmingplaces.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.charmingplaces.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Pantalla encargada de mostrar el mapa con la ubicación del usuario y los lugares de interés
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Button btnCentrer;

    /**
     * Método que se ejecuta al cargar la pantalla, se encarga de pintar el layout y de
     * instanciar las variables de los input y servicios que se utilizarán en este activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Carga el mapa en un fragmento
        setContentView(R.layout.activity_map);
        Fragment fragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameMap, fragment).commit();
        //Botón encargado de mover la cámara del mapa al punto donde se encuentra el usuario
        btnCentrer = findViewById(R.id.btnCentrar);
        btnCentrer.setOnClickListener(v -> {
            MapFragment.gmap.findNearMarkersFromUser();
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
    }

    /**
     * Método para mostrar el menú en la vista
     *
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

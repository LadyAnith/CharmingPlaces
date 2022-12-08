package com.example.charmingplaces.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charmingplaces.R;
import com.example.charmingplaces.client.PlacesApi;
import com.example.charmingplaces.logic.AdapterPlaces;

/**
 * Pantalla que muestra el listado de lugares de interés y el listado de lugares favoritos del ususario
 */
public class PlacesListActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private PlacesApi charmingPlacesApi;
    public static final int OPTION_LIST= R.id.action_list;
    public static final int OPTION_FAVORITE= R.id.action_favoriteList;

    /**
     * Método que se ejecuta al cargar la pantalla, se encarga de pintar el layout y de
     * instanciar las variables de los input y servicios que se utilizarán en este activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);

        recycler = findViewById(R.id.listPlaces);
        charmingPlacesApi = new PlacesApi(this);

        //Añade al adaptador el listado de lugares de interés
        charmingPlacesApi.findAll(
                response -> {
                    recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    AdapterPlaces adaptador = new AdapterPlaces(this, response, OPTION_LIST);
                    recycler.setAdapter(adaptador);
                },
                null);
    }


    /**
     * Método para mostrar el menú en la vista
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu3, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Método que crea un evento al clickar uno de los items del menú
     *
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
            case R.id.action_list:
                //Añade al adaptador el listado de lugares de interés
                charmingPlacesApi.findAll(
                        response -> {
                            recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                            AdapterPlaces adaptador = new AdapterPlaces(this, response, OPTION_LIST);
                            recycler.setAdapter(adaptador);
                        },
                        null);
                break;
            case R.id.action_favoriteList:
                //Añade al adaptador el listado de lugares favoritos
                charmingPlacesApi.findFavorites(
                        response -> {
                            recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                            AdapterPlaces adaptador = new AdapterPlaces(this, response, OPTION_FAVORITE);
                            recycler.setAdapter(adaptador);

                        },
                        null);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}

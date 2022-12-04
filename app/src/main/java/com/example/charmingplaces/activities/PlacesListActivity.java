package com.example.charmingplaces.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charmingplaces.R;
import com.example.charmingplaces.client.PlacesApi;
import com.example.charmingplaces.logic.AdapterPlaces;
import com.example.charmingplaces.pojo.PlacesListResponseDto;

import java.util.ArrayList;

public class PlacesListActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private PlacesApi charmingPlacesApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);

        recycler = (RecyclerView) findViewById(R.id.listPlaces);
        charmingPlacesApi = new PlacesApi(this);

        charmingPlacesApi.findAll(
                response -> {
                    recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    AdapterPlaces adaptador = new AdapterPlaces(this, response);
                    recycler.setAdapter(adaptador);
                },
                null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu3, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_back:
                Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(i);
                break;
            case R.id.action_list:
                charmingPlacesApi.findAll(
                        response -> {
                            recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                            AdapterPlaces adaptador = new AdapterPlaces(this, response);
                            recycler.setAdapter(adaptador);
                        },
                        null);
                break;
            case R.id.action_favoriteList:
                charmingPlacesApi.findFavorites(
                        response -> {
                            recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                            AdapterPlaces adaptador = new AdapterPlaces(this, response);
                            recycler.setAdapter(adaptador);
                        },
                        null);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}

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
import com.example.charmingplaces.client.CharmingPlacesApi;
import com.example.charmingplaces.logic.AdapterPlaces;
import com.example.charmingplaces.logic.Gps;
import com.example.charmingplaces.pojo.PlacesDto;
import com.example.charmingplaces.pojo.PlacesListResponseDto;
import com.example.charmingplaces.pojo.PlacesNearRequestDto;

import java.util.ArrayList;
import java.util.List;

public class PlacesListActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private CharmingPlacesApi charmingPlacesApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);

        recycler = (RecyclerView) findViewById(R.id.listPlaces);
        charmingPlacesApi = new CharmingPlacesApi(this);

        charmingPlacesApi.findAll(
                response -> {
                    recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

                    AdapterPlaces adaptador = new AdapterPlaces(response);
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

                break;
            case R.id.action_favoriteList:

                break;

        }
        return super.onOptionsItemSelected(item);
    }

}

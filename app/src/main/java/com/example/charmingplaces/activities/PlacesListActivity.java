package com.example.charmingplaces.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charmingplaces.R;
import com.example.charmingplaces.logic.AdapterPlaces;
import com.example.charmingplaces.pojo.PlacesDto;
import com.example.charmingplaces.pojo.PlacesListResponseDto;

import java.util.ArrayList;
import java.util.List;

public class PlacesListActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private PlacesListResponseDto listadoLugares = new PlacesListResponseDto();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);

        PlacesDto placesDto = new PlacesDto("holi","una cosa bonita", 3435.6, 35353.7,"fs");
        PlacesDto placesDto2 = new PlacesDto("holi","una cosa bonita", 3435.6, 35353.7,"fs");
        List<PlacesDto> list = new ArrayList<>();
        list.add(placesDto);
        list.add(placesDto2);
        listadoLugares.setData(list);


            recycler = (RecyclerView) findViewById(R.id.listPlaces);
            recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

            AdapterPlaces adaptador = new AdapterPlaces(listadoLugares);
            recycler.setAdapter(adaptador);

        }
    }

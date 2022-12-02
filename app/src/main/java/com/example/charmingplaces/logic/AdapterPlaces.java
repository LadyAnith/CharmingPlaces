package com.example.charmingplaces.logic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charmingplaces.R;
import com.example.charmingplaces.activities.PlacesListActivity;
import com.example.charmingplaces.pojo.PlacesDto;
import com.example.charmingplaces.pojo.PlacesListResponseDto;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;

public class AdapterPlaces extends RecyclerView.Adapter<AdapterPlaces.ViewHolder> {
    PlacesListResponseDto placesDtoList;
    GoogleMap googleMap;

    public AdapterPlaces(PlacesListResponseDto placesDtoList) {
        this.placesDtoList = placesDtoList;
    }

    @NonNull
    @Override
    public AdapterPlaces.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null, false);
            return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPlaces.ViewHolder holder, int position) {
        ImageUtils.setImage(holder.imgPhoto, placesDtoList.getData().get(position).getUrl());

        holder.txtName.setText(placesDtoList.getData().get(position).getName());
        holder.btnButtonHowGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GMap gMap = new GMap(googleMap, new PlacesListActivity());
                gMap.setDirections(holder.btnButtonHowGet,placesDtoList.getData().get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return placesDtoList.getData().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private ImageView imgPhoto;
        private Button btnButtonHowGet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName =  itemView.findViewById(R.id.txtNamePlaceBBDD);
            btnButtonHowGet = itemView.findViewById(R.id.btnHowToGet);
            imgPhoto = itemView.findViewById(R.id.imagePhoto);

        }
    }
}

package com.example.charmingplaces.logic;

import static com.example.charmingplaces.activities.PlacesListActivity.OPTION_FAVORITE;
import static com.example.charmingplaces.activities.PlacesListActivity.OPTION_LIST;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charmingplaces.R;
import com.example.charmingplaces.client.VotesApi;
import com.example.charmingplaces.pojo.PlacesDto;
import com.example.charmingplaces.pojo.PlacesListResponseDto;
import com.example.charmingplaces.pojo.VoteRequestDto;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Adaptador del recycler view del listado de lugares de interés
 */
public class AdapterPlaces extends RecyclerView.Adapter<AdapterPlaces.ViewHolder> {
    PlacesListResponseDto placesDtoList;
    VotesApi votesApi;
    private Gps gps;
    private Activity context;
    private int menuOption;


    public AdapterPlaces(Activity context, PlacesListResponseDto placesDtoList, int menuOption) {
        this.context = context;
        this.menuOption = menuOption;
        this.placesDtoList = placesDtoList;
        votesApi = new VotesApi(context);
        gps = new Gps(context);
    }

    @NonNull
    @Override
    public AdapterPlaces.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPlaces.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ImageUtils.setImage(holder.imgPhoto, placesDtoList.getData().get(position).getImageContent());

        PlacesDto placeData = placesDtoList.getData().get(position);
        holder.txtName.setText(placeData.getName());
        holder.txtLikes.setText("Likes: " + placeData.getVotes());

        setVotesResponse(holder, placeData, placeData);

        /* Botón al que al hacer click, indicará al usuario como llegar al lugar tomando la ubicaciión del usuario con el GPS
         * y abriendo el navegador con la direcciones de Google Maps
         */
        holder.btnButtonHowGet.setOnClickListener(v -> {
            gps.getLocation(gpsLocation -> {
                String template = "https://www.google.es/maps/dir/%s,%s/%s,%s";
                String url = String.format(template, placeData.getYcoord(), placeData.getXcoord(), gpsLocation.getLatitude(), gpsLocation.getLonguitude());
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            });
        });

        /*
         Botón encargado de dar un like o eliminar like al lugar.
         Muestra dependiendo si el usuario ha dado un like (mostrará un icono de una estrella) o si se lo ha eliminado
         o no ha vatado, aparecerá el icono del dedito para arriba.
         */
        holder.btnLike.setOnClickListener(v -> {
            VoteRequestDto voteRequestDto = new VoteRequestDto();
            voteRequestDto.setPlaceId(placeData.getId());
            voteRequestDto.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());

            if (placeData.isVoted()) {
                votesApi.deleteVote(
                        voteRequestDto,
                        response -> setVotesResponse(holder, placeData, response)
                );
            } else {
                votesApi.addVote(
                        voteRequestDto,
                        response -> setVotesResponse(holder, placeData, response)
                );
            }

        });
    }

    @Override
    public int getItemCount() {
        return placesDtoList.getData().size();
    }

    public void setVotesResponse(ViewHolder holder, PlacesDto placeData, PlacesDto response) {
        placeData.setVoted(response.isVoted());
        placeData.setVotes(response.getVotes());

        holder.txtLikes.setText("Likes: " + placeData.getVotes());
        if (placeData.isVoted()) {
            holder.btnLike.setImageResource(R.drawable.estrella);
        } else {
            holder.btnLike.setImageResource(R.drawable.me_gusta);
        }

        //Si el usuario consulta el listado de favoritos, se ocultará el icono de votar
        if (menuOption == OPTION_FAVORITE) {
            holder.btnLike.setVisibility(View.INVISIBLE);
        }
        if (menuOption == OPTION_LIST) {
            holder.btnLike.setVisibility(View.VISIBLE);
        }

    }

    // AUXILIAR CLASS

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private ImageView imgPhoto;
        private Button btnButtonHowGet;
        private ImageView btnLike;
        private TextView txtLikes;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtNamePlaceBBDD);
            btnButtonHowGet = itemView.findViewById(R.id.btnHowToGet);
            imgPhoto = itemView.findViewById(R.id.imagePhoto);
            btnLike = itemView.findViewById(R.id.like);
            txtLikes = itemView.findViewById(R.id.txtLikes);

        }
    }
}

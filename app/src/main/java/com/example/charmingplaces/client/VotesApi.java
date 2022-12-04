package com.example.charmingplaces.client;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.example.charmingplaces.pojo.PhotoCreatePlaceRequestDto;
import com.example.charmingplaces.pojo.PlacesDto;
import com.example.charmingplaces.pojo.PlacesInsideAreaRequestDto;
import com.example.charmingplaces.pojo.PlacesListResponseDto;
import com.example.charmingplaces.pojo.PlacesNearRequestDto;
import com.example.charmingplaces.pojo.VoteRequestDto;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;

public class VotesApi extends AbstractCharmingPlacesApi{

    public VotesApi(Context context){
        super(context);
    }


    private static final String URL_BASE = "http://192.168.1.104:8080/votes";
    private static final String POST_UP_VOTE_ENDPOINT = "/upVote";
    private static final String POST_DOWN_VOTE_ENDPOINT = "/downVote";


    public void addVote(VoteRequestDto data, Response.Listener<PlacesDto> successCallback) {
        String url = URL_BASE + POST_UP_VOTE_ENDPOINT;
        //Convierto el objeto VoteRequestDto a formato JSON
        JSONObject request = objectToJSON(data);

        //Indicamos el tipo de respuesta para convertir desde el micro a un objeto que podamos manejar
        Type typeList = new TypeToken<PlacesDto>() {}.getType();
        Response.Listener<JSONObject> success = (result -> successCallback.onResponse(jsonToObject(result, typeList)));

        executeCall(Request.Method.POST, url, request, success,  null);

    }

    public void deleteVote(VoteRequestDto data, Response.Listener<PlacesDto> successCallback) {
        String url = URL_BASE + POST_DOWN_VOTE_ENDPOINT;
        //Convierto el objeto VoteRequestDto a formato JSON
        JSONObject request = objectToJSON(data);

        //Indicamos el tipo de respuesta para convertir desde el micro a un objeto que podamos manejar
        Type typeList = new TypeToken<PlacesDto>() {}.getType();
        Response.Listener<JSONObject> success = (result -> successCallback.onResponse(jsonToObject(result, typeList)));

        executeCall(Request.Method.POST, url, request, success,  null);

    }



}

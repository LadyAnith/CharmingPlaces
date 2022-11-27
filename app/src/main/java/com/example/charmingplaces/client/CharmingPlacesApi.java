package com.example.charmingplaces.client;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.charmingplaces.pojo.PhotoCreatePlaceRequestDto;
import com.example.charmingplaces.pojo.PlacesInsideAreaRequestDto;
import com.example.charmingplaces.pojo.PlacesNearRequestDto;
import com.example.charmingplaces.pojo.PlacesListResponseDto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CharmingPlacesApi extends AbstractCharmingPlacesApi{

    public CharmingPlacesApi(Context context){
        super(context);
    }


    private static final String URL_BASE = "http://192.168.1.104:8080/lugares";
    private static final String CREATE_PLACE_ENDPOINT = "/img";
    private static final String GET_PLACES_NEAR_ENDPOINT = "/findNear?xcoord=%s&ycoord=%s";
    private static final String GET_PLACES_AREA_ENDPOINT = "/placesInsideArea";

    /**
     * Crea un punto de interés en el sistema enviando los datos del punto de interés por REST al microservicio spring
     *
     * @param data datos del punto de interés a crear
     * @param successCallback qué función ejecutar si va OK
     * @param errorCallback qué función ejecutar si hay un error al insertar el lugar de interés
     */
    public void createInterestingPoint(PhotoCreatePlaceRequestDto data, Response.Listener<PhotoCreatePlaceRequestDto> successCallback, Response.ErrorListener errorCallback) {
        String url = URL_BASE + CREATE_PLACE_ENDPOINT;
        //Convierto el objeto PhotoCreatePlaceRequestDto a formato JSON
        JSONObject request = objectToJSON(data);

        //Indicamos el tipo de respuesta para convertir desde el micro a un objeto que podamos manejar
        Type typeList = new TypeToken<PhotoCreatePlaceRequestDto>() {}.getType();
        Response.Listener<JSONObject> success = (result -> successCallback.onResponse(jsonToObject(result, typeList)));

        executeCall(Request.Method.POST, url, request, success,  errorCallback);

    }



    /**
     * Busca puntos de interés cercanos
     *
     * @param data Coordenadas para buscar lugares cercanos a ellas
     * @param successCallback qué función ejecutar si va OK
     * @param errorCallback qué función ejecutar si hay un error al insertar el lugar de interés
     */
    public void findNearInterestingPoint(PlacesNearRequestDto data, Response.Listener<PlacesListResponseDto> successCallback, Response.ErrorListener errorCallback) {
        String urlBase = URL_BASE + GET_PLACES_NEAR_ENDPOINT;
        String url = String.format(urlBase, data.getGeoPoint().getXcoord(), data.getGeoPoint().getYcoord());

        //Indicamos el tipo de respuesta para convertir desde el micro a un objeto que podamos manejar
        Type typeList = new TypeToken<PlacesListResponseDto>() {}.getType();

        //Construimos un listener que ejecutará lo que indiquemos como parámetro tras convertir nuestro JSON a Objeto java
        Response.Listener<JSONObject> success = result -> {
            successCallback.onResponse(jsonToObject(result, typeList));
        };

        executeCall(Request.Method.GET, url, null, success,  errorCallback);

    }

    public void findPlacesInsideArea(PlacesInsideAreaRequestDto data, Response.Listener<PlacesListResponseDto> successCallback, Response.ErrorListener errorCallback) {
        String url = URL_BASE + GET_PLACES_AREA_ENDPOINT;
        //Convierto el objeto PlacesInsideAreaRequestDto a formato JSON
        JSONObject request = objectToJSON(data);

        //Indicamos el tipo de respuesta para convertir desde el micro a un objeto que podamos manejar
        Type typeList = new TypeToken<PlacesListResponseDto>() {}.getType();
        Response.Listener<JSONObject> success = (result -> successCallback.onResponse(jsonToObject(result, typeList)));

        executeCall(Request.Method.POST, url, request, success,  errorCallback);
    }


}

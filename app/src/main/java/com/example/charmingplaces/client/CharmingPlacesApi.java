package com.example.charmingplaces.client;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.charmingplaces.pojo.PhotoCreatePlaceRequestDto;
import com.example.charmingplaces.pojo.PlacesNearRequestDto;
import com.example.charmingplaces.pojo.PlacesNearResponseDto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CharmingPlacesApi {

    public CharmingPlacesApi(Context context){
        this.context = context;
    }

    private Context context;

    private static final String URL_BASE = "http://192.168.1.104:8080/lugares";
    private static final String CREATE_PLACE_ENDPOINT = "/img";
    private static final String GET_PLACES_NEAR_ENDPOINT = "/findNear?xcoord=%s&ycoord=%s";

    /**
     * Crea un punto de interés en el sistema enviando los datos del punto de interés por REST al microservicio spring
     *
     * @param data datos del punto de interés a crear
     * @param successCallback qué función ejecutar si va OK
     * @param errorCallback qué función ejecutar si hay un error al insertar el lugar de interés
     */
    public void createInterestingPoint(PhotoCreatePlaceRequestDto data, Response.Listener<PhotoCreatePlaceRequestDto> successCallback, Response.ErrorListener errorCallback) {
        String url = URL_BASE + CREATE_PLACE_ENDPOINT;
        JSONObject request = objectToJSON(data);

        //Indicamos el tipo de respuesta para convertir desde el micro a un objeto que podamos manejar
        Type typeList = new TypeToken<PhotoCreatePlaceRequestDto>() {}.getType();
        Response.Listener<JSONObject> success = (result -> successCallback.onResponse(jsonToObject(result, typeList)));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                request,
                success,
                errorCallback);

        // Esto añade la request que enviamos hacia el controller a una cola de peticiones y la manda cuando tenga disponibilidad
        RequestQueue.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Busca puntos de interés cercanos
     *
     * @param data Coordenadas para buscar lugares cercanos a ellas
     * @param successCallback qué función ejecutar si va OK
     * @param errorCallback qué función ejecutar si hay un error al insertar el lugar de interés
     */
    public void findInterestingPoint(PlacesNearRequestDto data, Response.Listener<PlacesNearResponseDto> successCallback, Response.ErrorListener errorCallback) {
        String urlBase = URL_BASE + GET_PLACES_NEAR_ENDPOINT;
        String url = String.format(urlBase, data.getXcoord(), data.getYcoord());

        //Indicamos el tipo de respuesta para convertir desde el micro a un objeto que podamos manejar
        Type typeList = new TypeToken<PlacesNearResponseDto>() {}.getType();

        //Construimos un listener que ejecutará lo que indiquemos como parámetro tras convertir nuestro JSON a Objeto java
        Response.Listener<JSONObject> success = result -> {
            successCallback.onResponse(jsonToObject(result, typeList));
        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                url,
                success,
                errorCallback);

        // Esto añade la request que enviamos hacia el controller a una cola de peticiones y la manda cuando tenga disponibilidad
        RequestQueue.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Convierte mi objeto e un JSON para enviarlo al micro
     *
     * @return objeto con los datos a convertir en JSON
     */
    private JSONObject objectToJSON(Object data) {
        //esto lo usaré para convertir mi objeto java en JSON para enviarlo al micro
        Gson jsonParser = new Gson();
        String json = jsonParser.toJson(data);
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            Log.d("ERROR", "No se ha podido transformar el Objeto a JSon");
            return null;
        }
    }

    /**
     * Convierte mi objeto e un JSON para enviarlo al micro
     *
     * @return objeto con los datos a convertir en JSON
     */
    private <T> T jsonToObject(JSONObject json, Type clazz) {
        //esto lo usaré para convertir mi objeto java en JSON para enviarlo al micro
        Gson jsonParser = new Gson();
        try {
            return jsonParser.fromJson(json.toString(), clazz);
        }catch (Exception e){
            Log.d("ERROR", "No se ha podido transformar el Json a Objeto");
            return null;
        }
    }
}

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

/**
 * Api encargada de hacer peticiones al Backend referida a los lugares de interés
 */
public class PlacesApi extends AbstractCharmingPlacesApi{

    public PlacesApi(Context context){
        super(context);
    }

    @Override
    public String getEndpointPath(String path) {
        return URL_BASE + ENDPOINT_PATH + path;
    }

    private static final String ENDPOINT_PATH = "/places";
    private static final String GET_PLACES_NEAR_ENDPOINT = "/findNear?xcoord=%s&ycoord=%s";
    private static final String GET_PLACES_AREA_ENDPOINT = "/placesInsideArea";
    private static final String GET_FAVORITE_PLACES_ENDPOINT = "/findFavorites";


    /**
     * Crea un punto de interés en el sistema enviando los datos del punto de interés por REST al microservicio spring
     *
     * @param data datos del punto de interés a crear
     * @param successCallback qué función ejecutar si va OK
     * @param errorCallback qué función ejecutar si hay un error al insertar el lugar de interés
     */
    public void createInterestingPoint(PhotoCreatePlaceRequestDto data, Response.Listener<Void> successCallback, Response.ErrorListener errorCallback) {
        String url = getEndpointPath("");
        //Convierto el objeto PhotoCreatePlaceRequestDto a formato JSON
        JSONObject request = objectToJSON(data);

        Response.Listener<JSONObject> success = (result -> successCallback.onResponse(null));

        executeCall(Request.Method.POST, url, request, success,  errorCallback);

    }


    /**
     * Busca lugares de interés cercanos a la ubicación del usuario
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

    /**
     * Busca lugares de interés dentro de un área
     *
     * @param data contiene los puntos de un área, el superior izquierdo y el inferior derecho
     * @param successCallback qué función ejecutar si va OK
     * @param errorCallback qué función ejecutar si hay un error al cargar los lugares de interés en un área
     */
    public void findPlacesInsideArea(PlacesInsideAreaRequestDto data, Response.Listener<PlacesListResponseDto> successCallback, Response.ErrorListener errorCallback) {
        String url = getEndpointPath(GET_PLACES_AREA_ENDPOINT);
        //Convierto el objeto PlacesInsideAreaRequestDto a formato JSON
        JSONObject request = objectToJSON(data);

        //Indicamos el tipo de respuesta para convertir desde el micro a un objeto que podamos manejar
        Type typeList = new TypeToken<PlacesListResponseDto>() {}.getType();
        Response.Listener<JSONObject> success = (result -> successCallback.onResponse(jsonToObject(result, typeList)));

        executeCall(Request.Method.POST, url, request, success,  errorCallback);
    }

    /**
     * Busca el listado completo de todos los lugares de interés
     *
     * @param successCallback qué función ejecutar si va bien
     * @param errorCallback qué función ejecutar si hay un error al cargar el listado de los lugares de interés
     */
    public void findAll(Response.Listener<PlacesListResponseDto> successCallback, Response.ErrorListener errorCallback) {
        String urlBase = getEndpointPath("");

        //Indicamos el tipo de respuesta para convertir desde el micro a un objeto que podamos manejar
        Type typeList = new TypeToken<PlacesListResponseDto>() {}.getType();

        //Construimos un listener que ejecutará lo que indiquemos como parámetro tras convertir nuestro JSON a Objeto java
        Response.Listener<JSONObject> success = result -> {
            successCallback.onResponse(jsonToObject(result, typeList));
        };

        executeCall(Request.Method.GET, urlBase, null, success,  errorCallback);

    }

    /**
     * Busca el listado de los lugares de interés favoritos del usuario
     *
     * @param successCallback qué función ejecutar si va bien
     * @param errorCallback qué función ejecutar si hay un error al cargar el listado de los lugares favoritos del usuario
     */
    public void findFavorites(Response.Listener<PlacesListResponseDto> successCallback, Response.ErrorListener errorCallback) {
        String urlBase = getEndpointPath(GET_FAVORITE_PLACES_ENDPOINT);

        //Indicamos el tipo de respuesta para convertir desde el micro a un objeto que podamos manejar
        Type typeList = new TypeToken<PlacesListResponseDto>() {}.getType();

        //Construimos un listener que ejecutará lo que indiquemos como parámetro tras convertir nuestro JSON a Objeto java
        Response.Listener<JSONObject> success = result -> {
            successCallback.onResponse(jsonToObject(result, typeList));
        };

        executeCall(Request.Method.GET, urlBase, null, success,  errorCallback);

    }
}

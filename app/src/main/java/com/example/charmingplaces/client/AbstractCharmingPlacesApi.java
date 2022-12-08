package com.example.charmingplaces.client;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase abstracta con la lógica común para todas las clases que se comunican con la Api
 */
public abstract class AbstractCharmingPlacesApi {

    protected Context context;
    protected static final String URL_BASE = "http://192.168.1.104:8080";

    //Método constructor
    public AbstractCharmingPlacesApi(Context context) {
        this.context = context;
    }


    /**
     * Método para hacer llamadas genéricas a la Api (POST,PUT,DELETE,GET), que se encarga de añadir a las cabeceras el id del usuario
     *
     * @param method tipo de petición (POST,PUT,DELETE,GET)
     * @param url endpoint del microservicio
     * @param request la información que el usuario manda al microservicio
     * @param successCallback la función que se ejecutará si la petición es exitosa
     * @param errorCallback la función que se ejecutará si la petición da error
     */
    protected void executeCall(int method, String url, JSONObject request, Response.Listener<JSONObject> successCallback, Response.ErrorListener errorCallback) {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                method,
                url,
                request,
                successCallback,
                errorCallback) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("user-id", userId);
                return headers;
            }
        };

        // Esto añade la request que enviamos hacia el controller a una cola de peticiones y la manda cuando tenga disponibilidad
        RequestQueue.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    /**
     * Convierte mi objeto e un JSON para enviarlo al micro
     *
     * @param data Objeto Java a convertir en JSON
     * @return objeto con los datos a convertir en JSON
     */
    protected JSONObject objectToJSON(Object data) {
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
     * Convierte mi JSON a Objeto java que le pase como parámetro
     *
     * @param json El JSON a convertir en Objeto de Java
     * @param clazz el tipo de clase Java a la que voy a convertir el JSON
     * @return objeto con los datos a convertir en JSON
     */
    protected <T> T jsonToObject(JSONObject json, Type clazz) {
        //esto lo usaré para convertir mi objeto java en JSON para enviarlo al micro
        Gson jsonParser = new Gson();
        try {
            return jsonParser.fromJson(json.toString(), clazz);
        } catch (Exception e) {
            Log.d("ERROR", "No se ha podido transformar el Json a Objeto");
            return null;
        }
    }

    public abstract String getEndpointPath(String path);
}

package com.example.charmingplaces.client;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class AbstractCharmingPlacesApi {

    protected Context context;

    public AbstractCharmingPlacesApi(Context context) {
        this.context = context;
    }


    protected void executeCall(int method, String url, JSONObject request, Response.Listener<JSONObject> success, Response.ErrorListener errorCallback) {

        FirebaseAuth.getInstance().getCurrentUser()
                .getIdToken(true)
                .addOnCompleteListener(task -> {
                    String token = "Bearer " + task.getResult().getToken();

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            method,
                            url,
                            request,
                            success,
                            errorCallback) {
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> headers = new HashMap<>();
                            headers.put("Authorization", token);
                            return headers;
                        }
                    };

                    // Esto añade la request que enviamos hacia el controller a una cola de peticiones y la manda cuando tenga disponibilidad
                    RequestQueue.getInstance(context).addToRequestQueue(jsonObjectRequest);
                });
    }

    /**
     * Convierte mi objeto e un JSON para enviarlo al micro
     *
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
     * Convierte mi objeto e un JSON para enviarlo al micro
     *
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
}

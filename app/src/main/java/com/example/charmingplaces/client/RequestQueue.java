package com.example.charmingplaces.client;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

/**
 * Clase encargada de lanzar las peticiones REST
 */
public class RequestQueue {
    private  static RequestQueue instance;
    private com.android.volley.RequestQueue requestQueue;
    private Context ctx;

    private RequestQueue(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized RequestQueue getInstance(Context context) {
        if (instance == null) {
            instance = new RequestQueue(context);
        }
        return instance;
    }

    public com.android.volley.RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
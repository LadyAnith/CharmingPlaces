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

/**
 * Api encargada de hacer peticiones al Backend referida a los votos del usuario
 */
public class VotesApi extends AbstractCharmingPlacesApi{

    //Constructor
    public VotesApi(Context context){
        super(context);
    }

    @Override
    public String getEndpointPath(String path) {
        return URL_BASE + ENDPOINT_PATH + path;
    }

    private static final String ENDPOINT_PATH = "/votes";
    private static final String POST_UP_VOTE_ENDPOINT = "/upVote";
    private static final String POST_DOWN_VOTE_ENDPOINT = "/downVote";

    /**
     * Método encarga de añadir un nuevo voto al lugar.
     *
     * @param data contiene el id del usuario y el id del lugar que está votando
     * @param successCallback accion que ejecuta si la respuesta es Ok, el callback tiene la información del lugar votado actualizado
     */
    public void addVote(VoteRequestDto data, Response.Listener<PlacesDto> successCallback) {
        String url = getEndpointPath(POST_UP_VOTE_ENDPOINT);
        //Convierto el objeto VoteRequestDto a formato JSON
        JSONObject request = objectToJSON(data);

        //Indicamos el tipo de respuesta para convertir desde el micro a un objeto que podamos manejar
        Type typeList = new TypeToken<PlacesDto>() {}.getType();
        Response.Listener<JSONObject> success = (result -> successCallback.onResponse(jsonToObject(result, typeList)));

        executeCall(Request.Method.POST, url, request, success,  null);

    }

    /**
     * Método encargado de eliminar un voto
     *
     * @param data contiene el id del usuario y el id del lugar que está quitando el voto
     * @param successCallback accion que ejecuta si la respuesta es Ok, el callback tiene la información del lugar votado actualizado
     */
    public void deleteVote(VoteRequestDto data, Response.Listener<PlacesDto> successCallback) {
        String url = getEndpointPath(POST_DOWN_VOTE_ENDPOINT);
        //Convierto el objeto VoteRequestDto a formato JSON
        JSONObject request = objectToJSON(data);

        //Indicamos el tipo de respuesta para convertir desde el micro a un objeto que podamos manejar
        Type typeList = new TypeToken<PlacesDto>() {}.getType();
        Response.Listener<JSONObject> success = (result -> successCallback.onResponse(jsonToObject(result, typeList)));

        executeCall(Request.Method.POST, url, request, success,  null);

    }



}

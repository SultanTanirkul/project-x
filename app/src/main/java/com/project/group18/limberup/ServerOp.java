package com.project.group18.limberup;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

class ServerOp {
    private static ServerOp serverOp;
    private RequestQueue requestQueue;
    private static Context ctx;

    private ServerOp(Context context){
        ctx = context;
        requestQueue = getRequestQueue();
    }

    interface Predicate<String> {
        void respond(String response);
    }

    public StringRequest getRequest(String url, Map params, Predicate<String> responder){
        final StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        responder.respond(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                responder.respond(error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        return getRequest;
    }

    public StringRequest postRequest(String url, Map params, Predicate<String> responder){
        final StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        responder.respond(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("---->", "onErrorResponse: " + error.toString());
                responder.respond(error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }


        };
        return postRequest;
    }



    public static synchronized ServerOp getInstance(Context context) {
        if(serverOp == null){
            serverOp = new ServerOp(context);
        }
        return serverOp;
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(ctx);
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }



}

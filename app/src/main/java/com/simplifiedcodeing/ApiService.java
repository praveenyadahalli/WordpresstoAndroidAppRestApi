package com.simplifiedcodeing;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class ApiService {

    private final RequestQueue requestQueue;

    public ApiService(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public void fetchPost(String url, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(Request.Method.GET, url, successListener, errorListener);
        requestQueue.add(request);
    }
}

package com.simplifiedcodeing;

import com.android.volley.RequestQueue;

public class RequestManager {

    private final ApiService apiService;

    public static int postPerPage = 12;
    public static final String BASE_URL = "https://www.simplifiedcoding.net/wp-json/wp/v2/";

    public static String getPostsEndpoint() {
        return BASE_URL + "posts?per_page=" + postPerPage;
    }

    public static String getPostEndpoint(int postId) {
        return BASE_URL + "posts/" + postId + "?";
    }
    public RequestManager(RequestQueue requestQueue) {
        apiService = new ApiService(requestQueue);
    }

}

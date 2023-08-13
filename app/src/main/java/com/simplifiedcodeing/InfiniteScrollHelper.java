package com.simplifiedcodeing;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InfiniteScrollHelper {
    private Context context;
    private ListView listView;
    private int currentPage = 1;
    private boolean isLoading = false;
    private PostAdapter postAdapter;

    private ProgressDialog progressDialog;

    List<Map<String, Object>> postListData;

    public InfiniteScrollHelper(Context context, ListView listView, PostAdapter postAdapter) {
        this.context = context;
        this.listView = listView;
        this.postAdapter = postAdapter;

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        setupScrollListener();
        loadPosts();
    }

    private void setupScrollListener() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastVisibleItem = firstVisibleItem + visibleItemCount;
                if (lastVisibleItem == totalItemCount && !isLoading) {
                    loadMorePosts();
                }
            }
        });
    }

    private void loadPosts() {
        progressDialog.show(); // Show progress dialog

        String url = RequestManager.getPostsEndpoint(currentPage);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                postListData = gson.fromJson(s, List.class);

                List<PostModel> postModels = new ArrayList<>();
                for (Map<String, Object> post : postListData) {
                    String title = ((Map<String, Object>) post.get("title")).get("rendered").toString();
                    double postIdDouble = (double) post.get("id");
                    int postId = (int) postIdDouble;

                    PostModel postModel = new PostModel((Map<String, Object>) post.get("title"), (Map<String, Object>) post.get("content"), postId);
                    postModels.add(postModel);
                }

                postAdapter.addAll(postModels);
                postAdapter.notifyDataSetChanged();

                progressDialog.dismiss(); // Dismiss progress dialog
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss(); // Dismiss progress dialog
                Toast.makeText(context, "Some error occurred", Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }

    private void loadMorePosts() {
        isLoading = true;
        currentPage++;

        String url = RequestManager.getPostsEndpoint(currentPage);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                postListData = gson.fromJson(s, List.class);

                List<PostModel> postModels = new ArrayList<>();
                for (Map<String, Object> post : postListData) {
                    String title = ((Map<String, Object>) post.get("title")).get("rendered").toString();
                    double postIdDouble = (double) post.get("id");
                    int postId = (int) postIdDouble;

                    PostModel postModel = new PostModel((Map<String, Object>) post.get("title"), (Map<String, Object>) post.get("content"), postId);
                    postModels.add(postModel);
                }

                postAdapter.addAll(postModels);
                postAdapter.notifyDataSetChanged();

                isLoading = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                isLoading = false;
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }
}

package com.simplifiedcodeing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    ListView postListView;
    List<Map<String, Object>> postListData;
    PostAdapter postAdapter;
    int currentPage = 1;
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postListView = findViewById(R.id.postList);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        postAdapter = new PostAdapter(MainActivity.this, R.layout.list_item_post, new ArrayList<>());
        postListView.setAdapter(postAdapter);

        loadPosts();

        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (postAdapter != null && position < postAdapter.getCount()) {
                    PostModel selectedPost = postAdapter.getItem(position);
                    int postId = selectedPost.getPostId();

                    Intent intent = new Intent(MainActivity.this, PostActivity.class);
                    intent.putExtra("id", postId);
                    startActivity(intent);
                }
            }
        });

        postListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Some error occurred", Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
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
                // Handle error
                isLoading = false;
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);
    }
}

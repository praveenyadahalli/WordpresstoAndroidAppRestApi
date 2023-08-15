package com.simplifiedcodeing;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.Map;

public class PostActivity extends AppCompatActivity {

    TextView title;
    WebView content;
    Dialog customProgressDialog;
    Gson gson;
    Map<String, Object> mapPost;
    Map<String, Object> mapTitle;
    Map<String, Object> mapContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        final int id = getIntent().getIntExtra("id", -1);
        if (id == -1) {
            // Handle the case where "id" is missing
            finish();
            return;
        }

        title = findViewById(R.id.title);
        content = findViewById(R.id.content);

        customProgressDialog = new Dialog(this);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setContentView(R.layout.custom_progress_bar);
        customProgressDialog.show();

        String url = RequestManager.getPostEndpoint(id);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                gson = new Gson();
                mapPost = gson.fromJson(s, Map.class);
                mapTitle = (Map<String, Object>) mapPost.get("title");
                mapContent = (Map<String, Object>) mapPost.get("content");

                title.setText(mapTitle.get("rendered").toString());

                // Initialize WebView
                content.getSettings().setJavaScriptEnabled(true);
                content.setWebViewClient(new WebViewClient());
                content.loadDataWithBaseURL(null, mapContent.get("rendered").toString(), "text/html", "UTF-8", null);

                if (customProgressDialog != null && customProgressDialog.isShowing()) {
                    customProgressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (customProgressDialog != null && customProgressDialog.isShowing()) {
                    customProgressDialog.dismiss();
                }
                Toast.makeText(PostActivity.this, "Error loading post", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (content != null) {
            content.destroy(); // Release WebView resources
        }
    }
}
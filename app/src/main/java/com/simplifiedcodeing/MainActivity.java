package com.simplifiedcodeing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ListView postListView;
    PostAdapter postAdapter;
    InfiniteScrollHelper scrollHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postListView = findViewById(R.id.postList);

        // Initialize the postAdapter and set it to the ListView
        postAdapter = new PostAdapter(MainActivity.this, R.layout.list_item_post, new ArrayList<>());
        postListView.setAdapter(postAdapter);

        // Initialize InfiniteScrollHelper with context, listView, and postAdapter
        scrollHelper = new InfiniteScrollHelper(MainActivity.this, postListView, postAdapter);

        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click
                handleItemClick(position);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Dismiss the progress dialog and handle back button press
        scrollHelper.dismissProgressDialog();
        super.onBackPressed();
    }

    private void handleItemClick(int position) {
        if (postAdapter != null && position < postAdapter.getCount()) {
            PostModel selectedPost = postAdapter.getItem(position);
            int postId = selectedPost.getPostId();

            Intent intent = new Intent(MainActivity.this, PostActivity.class);
            intent.putExtra("id", postId);
            startActivity(intent);
        }
    }
}

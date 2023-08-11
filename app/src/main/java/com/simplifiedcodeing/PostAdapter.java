package com.simplifiedcodeing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class PostAdapter extends ArrayAdapter<PostModel> {

    private final LayoutInflater inflater;
    private final int layoutResource;

    public PostAdapter(@NonNull Context context, int resource, @NonNull List<PostModel> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        layoutResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(layoutResource, parent, false);
        }

        PostModel post = getItem(position);
        if (post != null) {
            TextView titleTextView = view.findViewById(R.id.title);
            titleTextView.setText(post.getTitle().get("rendered").toString());
        }

        return view;
    }
}

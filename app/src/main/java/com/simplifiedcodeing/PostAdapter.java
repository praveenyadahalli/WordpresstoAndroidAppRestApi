package com.simplifiedcodeing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;

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
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = view.findViewById(R.id.title); // Replace with the correct ID
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        PostModel post = getItem(position);
        if (post != null) {
            Object titleObject = post.getTitle();
            if (titleObject instanceof Map) {
                Map<String, Object> titleMap = (Map<String, Object>) titleObject;
                Object renderedTitleObject = titleMap.get("rendered");
                if (renderedTitleObject != null) {
                    String renderedTitle = renderedTitleObject.toString();
                    holder.titleTextView.setText(renderedTitle);
                } else {
                    holder.titleTextView.setText("No Title");
                }
            } else {
                holder.titleTextView.setText("Invalid Title Format");
            }
        } else {
            holder.titleTextView.setText("No Post Data");
        }

        return view;
    }

    private static class ViewHolder {
        TextView titleTextView;
    }
}

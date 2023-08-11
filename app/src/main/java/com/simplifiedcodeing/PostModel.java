package com.simplifiedcodeing;

import java.util.Map;

public class PostModel {
    private int postId;
    private Map<String, Object> title;
    private Map<String, Object> content;

    public Map<String, Object> getTitle() {
        return title;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public int getPostId() {
        return postId;
    }
}

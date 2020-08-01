package com.example.mydribbble.model;

import android.text.Html;

import java.util.HashMap;
import java.util.List;

public class Shot {
    public static final String IMAGE_NORMAL = "normal";
    public static final String IMAGE_HIDPI = "hidpi";

    public boolean animated;
    public String description;
    public int height;
    public int id;
    public HashMap<String, String> images;
    public boolean low_profile;
    public List<String> tags;
    public String title;
    public int width;
    public String start;
    public String end;
    public List<String> attachments;
    public List<String> projects;
    public String video;
//    public int view_count;
//    public int likes_count;
//    public int buckets_count;
    public String getImageUrl() {
        if (images == null) {
            return null;
        } else if (animated) {
            return images.get(IMAGE_NORMAL);
        }

        return images.get(IMAGE_HIDPI) != null
                ? images.get(IMAGE_HIDPI)
                : images.get(IMAGE_NORMAL);
    }
}

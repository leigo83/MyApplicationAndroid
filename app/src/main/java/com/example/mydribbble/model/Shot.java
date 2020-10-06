package com.example.mydribbble.model;

import android.text.Html;

import java.util.HashMap;
import java.util.List;

public class Shot {
    public static final String IMAGE_NORMAL = "normal";
    public static final String IMAGE_HIDPI = "hidpi";

    public String id;
    public String created_at;
    public String updated_at;
    public int width;
    public int height;
    public String color;
    public int likes;
    public boolean liked_by_user;
    public String description;
    public User user;
    public List<CurrentUserCollections> currentUserCollections;
    HashMap<String, String> urls;
    HashMap<String, String> links;

    public class User {
        public String id;
        public String username;
        public String name;
        public String portfolio_url;
        public String bio;
        public String location;
        public int total_likes;
        public int total_photos;
        public int total_collections;
        public String instagram_username;
        public String twitter_username;
        public HashMap<String, String> profile_image;
        public HashMap<String, String> links;
    }

    public String getImageUrl() {
        if (urls == null) {
            return null;
        } else if (urls.size() == 0) {
            return null;
        }
        if (!urls.containsKey("small") && !urls.containsKey("regular")) {
            return null;
        }
        return urls.get("regular") != null
                ? urls.get("regular")
                : urls.get("small");
    }

    public String getUserImageUrl() {
        if (this.user.profile_image.size() == 0) {
            return null;
        }
        return this.user.profile_image.get("small") != null
                ? this.user.profile_image.get("medium")
                : this.user.profile_image.get("large");
    }
}



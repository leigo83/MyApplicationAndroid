package com.example.mydribbble.model;

import java.util.HashMap;

public class Collection {
    public String id;
    public String title;
    public String description;
    public String published_at;
    public String last_collected_at;
    public String updated_at;
    public String total_photos;
    public String share_key;
    public Shot cover_photo;
    public User user;
    public HashMap<String, String> links;

    public String getImageUrl() {
        if (cover_photo == null) return null;
        if (cover_photo.urls == null) {
            return null;
        }
        return cover_photo.urls.get("small") != null
                ? cover_photo.urls.get("regular")
                : cover_photo.urls.get("thumb");
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













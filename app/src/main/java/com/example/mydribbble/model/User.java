package com.example.mydribbble.model;

import java.util.HashMap;

public class User {
    public String id;
    public String updated_at;
    public String username;
    public String name;
    public HashMap<String, String> profile_image;
    public String downloads;
    public String total_likes;
    public String total_photos;
    public String total_collections;
    public String followers_count;
    public String following_count;

    public String getUserImageUrl() {
        if (this.profile_image.size() == 0) {
            return null;
        }
        if (this.profile_image.get("large") != null) {
            return this.profile_image.get("large");
        } else if (this.profile_image.get("medium") != null) {
            return this.profile_image.get("medium");
        } else if (this.profile_image.get("small") != null) {
            return this.profile_image.get("small");
        } else {
            return null;
        }
    }
}

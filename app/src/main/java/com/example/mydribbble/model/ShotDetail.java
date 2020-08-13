package com.example.mydribbble.model;

import java.util.HashMap;
import java.util.List;

public class ShotDetail {
    public String id;
    public String created_at;
    public String updated_at;
    public int width;
    public int height;
    public String color;
    public int downloads;
    public int likes;
    public boolean liked_by_user;
    public String description;
    public HashMap<String, String> exif;
    public String city;
    public String country;
    public String latitude;
    public String longitude;
    public List<String> tags;
    public List<CurrentUserCollections> currentUserCollections;
    public HashMap<String, String> urls;
    public HashMap<String, String> links;

    public String userId;
    public String userUpdatedAt;
    public String username;
    public String name;
    public String portfolio_url;
    public String bio;
    public String location;
    public int total_likes;
    public int total_photos;
    public int total_collections;
    public HashMap<String, String> userLinks;

}




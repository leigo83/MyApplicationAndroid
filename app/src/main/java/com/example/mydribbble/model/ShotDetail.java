package com.example.mydribbble.model;

import java.util.ArrayList;
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
    public String alt_description;
    public Exif exif;
    public Location location;
    public Urls urls;
    public Links links;
    public User user;


    public class User {
        public String id;
        public String updated_at;
        public String username;
        public String name;
        public String portfolio_url;
        public String bio;
        public String location;
        public float total_likes;
        public float total_photos;
        public float total_collections;
        UserLinks LinksObject;
    }
    public class UserLinks {
        public String self;
        public String html;
        public String photos;
        public String likes;
        public String portfolio;
    }
    public class Links {
        public String self;
        public String html;
        public String download;
        public String download_location;
    }
    public class Urls {
        public String raw;
        public String full;
        public String regular;
        public String small;
        public String thumb;
    }
    public class Location {
        public String city;
        public String country;
        Position PositionObject;
    }
    public class Position {
        public float latitude;
        public float longitude;
    }
    public class Exif {
        public String make;
        public String model;
        public String exposure_time;
        public String aperture;
        public String focal_length;
        public float iso;
    }

    public String getImageUrl() {
        if (urls == null) {
            return null;
        }
        return urls.regular != null
                ? urls.small
                : urls.thumb;
    }

//    public String getUserImageUrl() {
//        if (this.user.profile_image.size() == 0) {
//            return null;
//        }
//        return this.user.profile_image.get("small") != null
//                ? this.user.profile_image.get("medium")
//                : this.user.profile_image.get("large");
//    }
}










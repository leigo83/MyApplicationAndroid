package com.example.mydribbble.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//
//public class ShotDetail {
//    public String id;
//    public String created_at;
//    public String updated_at;
//    public String promoted_at = null;
//    public int width;
//    public int height;
//    public String color;
//    public String description = null;
//    public String alt_description;
//    public Urls urls;
//    public Links shotLinks;
////    public HashMap<String, String> urls;
////    public HashMap<String, String> shotLink;
//    public List <String> categories;
//    public int likes;
//    public boolean liked_by_user;
//    public ArrayList <String> current_user_collections = new ArrayList <> ();
//    public Sponsorship SponsorshipObject = new Sponsorship();
//    public User UserObject = new User ();
//    public Exif ExifObject = new Exif();
//    public Location LocationObject = new Location();
//    public Meta MetaObject = new Meta();
//    public ArrayList < Object > tags = new ArrayList < Object > ();
//    public Related_collections Related_collectionsObject = new Related_collections();
//    public float views;
//    public float downloads;
//
//    public class Related_collections {
//        public float total;
//        public String type;
//        ArrayList < Object > results = new ArrayList < Object > ();
//    }
//    public class Meta {
//        public boolean index;
//    }
//    public class Location {
//        public String title = null;
//        public String name = null;
//        public String city = null;
//        public String country = null;
//        Position PositionObject;
//
//    }
//    public class Position {
//        public String latitude = null;
//        public String longitude = null;
//    }
//    public class Exif {
//        public String make = null;
//        public String model = null;
//        public String exposure_time = null;
//        public String aperture = null;
//        public String focal_length = null;
//        public String iso = null;
//    }
//    public class User {
//        public String id;
//        public String updated_at;
//        public String username;
//        public String name;
//        public String first_name;
//        public String last_name = null;
//        public String twitter_username;
//        public String portfolio_url;
//        public String bio;
//        public String location = null;
//        public HashMap<String, String> links;
//        Profile_image Profile_imageObject;
//        public String instagram_username;
//        public float total_collections;
//        public float total_likes;
//        public float total_photos;
//        public boolean accepted_tos;
//    }
//    public class Profile_image {
//        public String small;
//        public String medium;
//        public String large;
//    }
//
//    public class Sponsorship {
//        ArrayList <String> impression_urls = new ArrayList < > ();
//        public String tagline;
//        public String tagline_url;
//        Sponsor SponsorObject;
//
//    }
//    public class Sponsor {
//        public String id;
//        public String updated_at;
//        public String username;
//        public String name;
//        public String first_name;
//        public String last_name = null;
//        public String twitter_username;
//        public String portfolio_url;
//        public String bio;
//        public String location = null;
//        Links LinksObject;
//        Profile_image Profile_imageObject;
//        public String instagram_username;
//        public float total_collections;
//        public float total_likes;
//        public float total_photos;
//        public boolean accepted_tos;
//    }
//    public class UserLinks {
//        public String self;
//        public String html;
//        public String photos;
//        public String likes;
//        public String portfolio;
//        public String following;
//        public String followers;
//    }
//    public class Links {
//        public String self;
//        public String html;
//        public String download;
//        public String download_location;
//    }
//    public class Urls {
//        public String raw;
//        public String full;
//        public String regular;
//        public String small;
//        public String thumb;
//    }
//
//    public String getImageUrl() {
//        if (urls == null) {
//            return null;
//        }
//        return urls.regular != null
//                ? urls.small
//                : urls.thumb;
//    }
//}



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
    public Exif exlf;
    public Location location;
    public ArrayList < Object > tags = new ArrayList < Object > ();
    public ArrayList < Object > current_user_collections = new ArrayList < Object > ();
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
}










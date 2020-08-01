package com.example.mydribbble.utils;

import android.net.Uri;

import com.example.mydribbble.model.Shot;
import com.facebook.drawee.view.SimpleDraweeView;

public class ImageUtils {
    public static void loadShotImage(Shot shot, SimpleDraweeView imageView) {
        String imageUrl = shot.getImageUrl();
        if (imageUrl != null) {
            Uri imageUri = Uri.parse(imageUrl);
            imageView.setImageURI(imageUri);
        }
    }
}

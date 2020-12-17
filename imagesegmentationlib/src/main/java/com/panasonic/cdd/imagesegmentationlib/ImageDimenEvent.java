package com.panasonic.cdd.imagesegmentationlib;

import android.net.Uri;


import androidx.annotation.NonNull;

import java.util.Locale;

public class ImageDimenEvent {

    public Uri imageUri;
    public int width;
    public int height;

    public ImageDimenEvent(Uri uri, int w, int h) {
        imageUri = uri;
        width = w;
        height = h;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.US, "image dimen: %s, [%d x %d]", imageUri, width, height);
    }

}

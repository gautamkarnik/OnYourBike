package com.example.gautamkarnik.onyourbike.helpers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by gautamkarnik on 2015-06-23.
 */
public class Share {

    private static String CLASS_NAME;

    private Activity activity;

    public Share(Activity activity) {
        CLASS_NAME = getClass().getName();
        this.activity = activity;
    }

    public void share(Intent intent) {
        activity.startActivity(Intent.createChooser(intent, "Share via"));
    }

    public void addText(Intent intent, String text) {
        Log.d(CLASS_NAME, "addText");
        intent.setAction(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
    }

    public void addPhoto(Intent intent, Bitmap bitmap) {
        Log.d(CLASS_NAME, "addPhoto");
        if (bitmap != null) {
            intent.setAction(android.content.Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, bitmap);
        }
    }

}

package com.example.gautamkarnik.onyourbike.helpers;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by gautamkarnik on 2015-06-22.
 */
public class Camera {

    private static String CLASS_NAME;
    private Activity activity;

    public static final int PHOTO_TAKEN = 1001;

    public Camera(Activity activity) {
        CLASS_NAME = getClass().getName();
        this.activity = activity;
    }

    public void takePhoto() {
        Log.d(CLASS_NAME, "takePhoto");

        Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(takePhoto, PHOTO_TAKEN);
    }

    public boolean hasCamera() {
        Log.d(CLASS_NAME, "hasCamera");
        PackageManager manager = activity.getPackageManager();
        return manager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public boolean hasCameraApplication() {
        Log.d(CLASS_NAME, "hasCameraApplication");
        PackageManager manager = activity.getPackageManager();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list = manager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public void displayPhoto(ImageView image, Intent intent) {
        Log.d(CLASS_NAME, "displayPhoto");

        Bundle extra = intent.getExtras();
        Bitmap bitmap = (Bitmap) extra.get("data");
        if (bitmap != null) {
            image.setImageBitmap(bitmap);
        }
    }

}

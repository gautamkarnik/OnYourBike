package com.example.gautamkarnik.onyourbike.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.gautamkarnik.onyourbike.R;
import com.example.gautamkarnik.onyourbike.helpers.Camera;
import com.example.gautamkarnik.onyourbike.helpers.Share;

public class PhotoActivity extends BaseActivity {

    private static String CLASS_NAME;

    private Button takePhoto;
    private ImageView image;
    private Camera camera;

    public PhotoActivity() {
        CLASS_NAME = getClass().getName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        setupActionBar();
        takePhoto = (Button) findViewById(R.id.photo_button);
        image = (ImageView) findViewById(R.id.photo_image);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(CLASS_NAME, "onStart");
        camera = new Camera(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(CLASS_NAME, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(CLASS_NAME, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(CLASS_NAME, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(CLASS_NAME, "onDestroy");
        camera = null;
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d(CLASS_NAME, "onRestart");
    }

    public void takePhoto(View view) {
        Log.d(CLASS_NAME, "takePhoto");
        if(camera.hasCamera() && camera.hasCameraApplication()) {
            camera.takePhoto();
        }
    }

    public void displayPhoto(Intent intent) {
        Log.d(CLASS_NAME, "displayPhoto");
        camera.displayPhoto(image, intent);
    }

    @Override
    public void shareActivity() {
        Share share = new Share(this);
        Intent intent = new Intent();

        share.addText(intent, "On your bike photo!");
        share.addPhoto(intent, getPhoto());

        share.share(intent);
    }

    public Bitmap getPhoto() {
        Log.d(CLASS_NAME, "getPhoto");
        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = null;
        if (drawable != null) {
            bitmap = drawable.getBitmap();
        }
        return bitmap;
    }

}

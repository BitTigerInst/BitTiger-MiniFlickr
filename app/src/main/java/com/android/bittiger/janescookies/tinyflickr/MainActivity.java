package com.android.bittiger.janescookies.tinyflickr;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int KEEP_TIME = 2000;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "----------onCreate----------");

        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,  GalleryActivity.class);

                Log.d(TAG, "----------startActivityIntent----------");
                startActivity(intent);
            }
        }, KEEP_TIME);
    }
}

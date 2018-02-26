package com.example.anshul.speed;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by aashish on 26/10/17.
 */

public class splash extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;


    @Override
    public void onCreate(Bundle savedInstaneState) {
        super.onCreate(savedInstaneState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(splash.this);
                String username = prefs.getString("username", "1");
                String password = prefs.getString("password", "");
                if (username.equals("1")) {
                    Intent mainIntent = new Intent(splash.this, login.class);
                    splash.this.startActivity(mainIntent);
                    splash.this.finish();
                } else {
                    Intent mainIntent = new Intent(splash.this, sppeed.class);
                    splash.this.startActivity(mainIntent);
                    splash.this.finish();
                }
            }
        },SPLASH_DISPLAY_LENGTH);
    }
    }



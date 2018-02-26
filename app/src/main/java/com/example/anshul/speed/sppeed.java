package com.example.anshul.speed;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class sppeed extends AppCompatActivity implements LocationListener {
    TextView t1,warning;
    ImageView needle;
    Button logout;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    String speed,email,exceed;
    private static String url_update_speed = "https://www.darshansharma.me/speed/update_speed.php";
    float cspeed,kmph,speed_angle,needle_angle,current_angle;
    int count=0,exceed_count=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sppeed);
        t1=(TextView)findViewById(R.id.t1);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(sppeed.this);
        email=prefs.getString("username", "1");
        logout=(Button)findViewById(R.id.logout);

        needle=(ImageView)findViewById(R.id.needle);
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        this.onLocationChanged(null);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(sppeed.this);
                SharedPreferences.Editor ed = prefs.edit();
                ed.clear();
                ed.commit();
                Intent i1 = new Intent(sppeed.this,login.class);
                startActivity(i1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        int backButtonCount=0;
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }


    @Override
    public void onLocationChanged(Location location) {
        MediaPlayer mp=MediaPlayer.create(this, R.raw.aaa);
      if(location==null)
      {
        t1.setText("--m/s");

      }
      else
      {
          cspeed=location.getSpeed();
          kmph = cspeed * 18 / 5;
          /*kmph=70;*/
          kmph=round(kmph,2);
          speed=Float.toString(kmph);
          t1.setText(kmph + "Km/h");


          speed_angle = (kmph / 180) * 100;
          needle_angle = 270 * speed_angle / 100;
          RotateAnimation needlerotate = new RotateAnimation(current_angle, needle_angle, 360, 360);
          needlerotate.setDuration(900);
          current_angle=needle_angle;
          needlerotate.setFillAfter(true);
          needle.startAnimation(needlerotate);
          needle.refreshDrawableState();

          new sppeed.CreateNewProduct().execute();

          if (kmph>60) {
              exceed_count++;
              exceed=Integer.toString(exceed_count);
              warning = (TextView) findViewById(R.id.warning);
              warning.setTextColor(getResources().getColor(R.color.red));
              warning.setText("LIMIT EXCEEDED");
              manageBlinkEffect();
              mp.start();
              cspeed=location.getSpeed();
              kmph = cspeed * 18 / 5;
                /*kmph=70;*/
              kmph=round(kmph,2);
              t1.setText(kmph + "Km/h");




          }
          else {
              warning = (TextView) findViewById(R.id.warning);
              warning.setTextColor(getResources().getColor(R.color.colorPrimary));
              warning.setText("LIMIT: 60 Km/h");
              stopBlinkEffect();

              if(mp.isPlaying()){
                  mp.stop();
              }
          }


      }

    }

    class CreateNewProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("speed", speed));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("exceed_count", exceed));


            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_speed,
                    "POST", params);

            // check log cat fro response


            // check for success tag
            /*try {
                int success = json.getInt("success");

                if (success == 1) {
                    // successfully created product
                    Toast.makeText(sppeed.this, "updating...", Toast.LENGTH_SHORT).show();
                    // closing this screen

                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }*/

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done

        }

    }

    @SuppressLint("WrongConstant")
    public void manageBlinkEffect(){

        ObjectAnimator anim= ObjectAnimator.ofInt(warning,"textColor",Color.RED,Color.YELLOW,Color.RED);

        anim.setDuration(800);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();



    }
    @SuppressLint("WrongConstant")
    private void stopBlinkEffect(){
        ObjectAnimator anim= ObjectAnimator.ofInt(warning,"textColor",Color.BLUE,Color.BLUE,Color.BLUE);

        anim.setDuration(800);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    /*private void manageBlinkEffect()
    {
        ObjectAnimator anim=ObjectAnimator.ofInt(t1,"backgroundColor", Color.WHITE, Color.RED,Color.WHITE);
        anim.setDuration(200);
        anim.start();
    }*/
}

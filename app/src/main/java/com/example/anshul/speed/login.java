package com.example.anshul.speed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.view.View.VISIBLE;

/**
 * Created by Aashish on 12/19/2017.
 */

public class login extends AppCompatActivity {

    Button login, signup;
    EditText usr, psd;
    int flag=0;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    View v;
    String pass_check,email_check,pass;
    private static String url_email_check = "https://www.darshansharma.me/speed/email_check.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);



        usr = (EditText) findViewById(R.id.usr);
        psd = (EditText) findViewById(R.id.password);
        signup = (Button) findViewById(R.id.signup_button);
        login = (Button) findViewById(R.id.login_button);

        final String url_email_check = "https://www.darshansharma.me/speed/email_check.php?email="+email_check;

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usr.getVisibility() == VISIBLE)
                {
                    flag=0;
                    email_check = usr.getText().toString();
                    pass = psd.getText().toString();
                    if (email_check.equals("")) {
                        Toast.makeText(login.this, "Enter a valid email", Toast.LENGTH_LONG).show();
                        flag=1;
                    }
                    if (pass.equals("")) {
                        Toast.makeText(login.this, "Enter the password", Toast.LENGTH_LONG).show();
                        flag=1;
                    }
                    if (flag == 0) {

                        getJSON("https://www.darshansharma.me/speed/email_check.php?email="+email_check);

                    }

                }
                else
                {
                    login.setY(login.getY() + 360);
                    usr.setVisibility(VISIBLE);
                    psd.setVisibility(VISIBLE);
                    signup.setVisibility(View.GONE);
                }


                }

        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(com.example.anshul.speed.login.this,register.class);
                com.example.anshul.speed.login.this.startActivity(mainIntent);
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

    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if(s.equals(null)){
                    Toast.makeText(login.this, "Connect to Internet and Try Again...", Toast.LENGTH_SHORT).show();
                }
                pass_check=s;
                if(pass.equals(pass_check))
                {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(login.this);
                    SharedPreferences.Editor ed = prefs.edit();
                    ed.putString("username", email_check);
                    ed.putString("password", pass);
                    ed.commit();
                    Intent intent=new Intent(login.this,sppeed.class);
                    intent.putExtra("email",email_check);
                    email_check="";
                    pass="";
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(login.this, "Email password does not match", Toast.LENGTH_SHORT).show();
                }

                /*Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();*/
                /*try {
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }


}

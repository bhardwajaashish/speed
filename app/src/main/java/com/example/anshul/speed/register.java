package com.example.anshul.speed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class register extends AppCompatActivity {

    EditText em, user, pswd, cpswd, vnam, vnum;
    String email, username, password, cpassword, vname, vnumber;
    Button register;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static String url_create_product = "https://www.darshansharma.me/speed/create_product.php";
    private static final String TAG_SUCCESS = "success";
    /*private static final String DB_URL="jdbc:localhost:3306/id4090377_speeddata";
    private static final String USER="id4090377_abhiroop";
    private static final String PASS="fifty5050";*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        em = (EditText) findViewById(R.id.email);

        user = (EditText) findViewById(R.id.user);

        pswd = (EditText) findViewById(R.id.pswd);

        cpswd = (EditText) findViewById(R.id.cpswd);

        vnam = (EditText) findViewById(R.id.vname);

        vnum = (EditText) findViewById(R.id.vnumber);
        register = (Button) findViewById(R.id.register_button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = em.getText().toString();
                username = user.getText().toString();
                password = pswd.getText().toString();
                cpassword = cpswd.getText().toString();
                vname = vnam.getText().toString();
                vnumber = vnum.getText().toString();
                int flag = 0;
                if (!password.equals(cpassword)) {
                    Toast.makeText(register.this, "Password do not match with Confirm password", Toast.LENGTH_LONG).show();
                    flag = 1;
                }
                if (!email.contains("@")) {
                    Toast.makeText(register.this, "Invalid email", Toast.LENGTH_LONG).show();
                    flag = 1;
                }
                if (flag == 0) {
                    /*new Send().execute();*/
                    new CreateNewProduct().execute();
                    Toast.makeText(register.this, "Registered successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(com.example.anshul.speed.register.this, login.class);
                    startActivity(intent);
                }


            }
        });


    }
    class CreateNewProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(register.this);
            pDialog.setMessage("Registering...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            email = em.getText().toString();
            username = user.getText().toString();
            password = pswd.getText().toString();
            cpassword = cpswd.getText().toString();
            vname = vnam.getText().toString();
            vnumber = vnum.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("vehicle_name", vname));
            params.add(new BasicNameValuePair("vehicle_number", vnumber));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), login.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }

}

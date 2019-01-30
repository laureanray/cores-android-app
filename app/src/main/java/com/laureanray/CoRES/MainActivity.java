

package com.laureanray.CoRES;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static String IPADDRESS;
    int MY_PERMISSIONS = 0;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                requestPermission();
                Toast toast = Toast.makeText(this, "Please allow permission. ", Toast.LENGTH_LONG);
                toast.show();
            } else {
                requestPermission();
            }
        } else {
            // Permission has already been granted
        }
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                MY_PERMISSIONS);
    }

    private void FO_LOGIN(){
        final ProgressBar bar = findViewById(R.id.loginProgressBar);
        final Button button = findViewById(R.id.loginBtn);
        button.setClickable(false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bar.animate().alpha(1).setDuration(400);
                button.animate().alpha(0).setDuration(400);
            }
        });
    }

    private void FI_LOGIN(){
        final ProgressBar bar = findViewById(R.id.loginProgressBar);
        final Button button = findViewById(R.id.loginBtn);
        button.setClickable(true);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bar.animate().alpha(0).setDuration(400);
                button.animate().alpha(1).setDuration(400);
            }
        });
    }


    public void login(final View view) {
        FO_LOGIN();
        Log.d("MAIN_ACTIVITY", "Login Clicked");
        final EditText email = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);
        final EditText ip = findViewById(R.id.ip);
        IPADDRESS = ip.getText().toString().trim();
        Log.d("MAIN_ACTIVITY", MainActivity.IPADDRESS);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://" + ip.getText().toString().trim() + "/portal/login/admin";

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("MAIN_ACTIVITY", response);
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
                        Log.d("MAIN_ACTIVITY", jsonObject.get("status").toString());
                        if (jsonObject.get("status").toString().equals("\"success\"")) {
                            launchDashboardActivity();
                        } else {
                            Context context = getApplicationContext();
                            CharSequence text = "Invalid Login Credentials";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                            FI_LOGIN();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("MAIN_ACTIVITY", error.toString());
                        Context context = getApplicationContext();
                        CharSequence text = "Invalid IP address!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        FI_LOGIN();
//                        ip.setText("");

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email.getText().toString().trim());
                params.put("password", password.getText().toString().trim());


                return params;
            }
        };


        Log.d(TAG, "login: ");
        requestQueue.add(postRequest);

    }


    public void launchDashboardActivity() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
        Log.d(TAG, "launchDashboardActivity: ");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}

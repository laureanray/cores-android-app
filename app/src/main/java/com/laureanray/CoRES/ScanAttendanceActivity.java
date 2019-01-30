package com.laureanray.CoRES;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ScanAttendanceActivity extends AppCompatActivity {

    private static final String TAG = "ScanAttendanceActivity";

    private static final String API_KEY = "a7DFe2Er56s";
    private static String seminar;

    TextView textView;
    TextView name;

    public static void setSeminar(String seminar) {
        ScanAttendanceActivity.seminar = seminar;
    }

    public static String getSeminar() {
        return seminar;
    }

    private String parseSeminarText(String seminarText){
        return seminarText.replaceAll(" ", "_");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_attendance);
        textView = findViewById(R.id.statusCode);
        name = findViewById(R.id.name);
        TextView seminarTextView = findViewById(R.id.seminar);
        seminarTextView.setText(seminar);
        Log.d(TAG, "onCreate: " + seminar);
    }

    @Override
    public void onBackPressed(){
        startActivity( new Intent(this, AttendanceActivity.class) );
        finish();
        Log.d(TAG, "onBackPressed: ");
    }

    public void scan(View view){
        Intent intent = new Intent(this, ScanActivity.class);
        startActivityForResult(intent, 0);
        textView.setText("");
        Log.d(TAG, "scan: ");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: /");
        if(requestCode == 0){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data != null){
                    //
                    Barcode barcode = data.getParcelableExtra("barcode");
                    Log.d(TAG, "onActivityResult: " + barcode.displayValue);
                    checkAttendance(barcode.displayValue, seminar);
                    // barcode.displayValue();
                }else {
                    // NO BARCODE FOUND
                }
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
            Log.d(TAG, "onActivityResult: /else");

        }
    }
    
    

    private void onResponsePOST(int status){
        textView.setText("");
        textView.setAlpha(0);
        name.setText("");
        textView.setTranslationY(0);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.animate().alpha(1.0f).translationY(-100).setDuration(250);
            }
        });
        if(status == 200){
            // ok
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("OK");
                }
            });
        }else if(status == 100 || status == 101){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("ERROR");
                }
            });
            // not exist
        }else if(status == 201){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("WARNING");
                }
            });
        }else if(status == 404){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("NOT FOUND");
                }
            });
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("UNKNOWN");
                }
            });
        }
    }

    private void displayInformation(String response){
        Gson gson = new Gson();
        final JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        final String lastName = jsonObject.get("lastName").toString().replaceAll("\"", "");
        final String firstName = jsonObject.get("firstName").toString().replaceAll("\"", "");
        final String middleInitial = jsonObject.get("middleInitial").toString().replaceAll("\"", "");



        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                name.animate().alpha(1).setDuration(500);
                name.setText(lastName + ", " + firstName + " " + middleInitial);
            }
        });
    }

    private void checkAttendance(String _id, String seminarStr){
        String URL = "http://" + MainActivity.IPADDRESS + "/admin/" + API_KEY + '/' + _id.trim() + '/' + parseSeminarText(seminarStr);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.d(TAG, "checkAttendance: URL: " + URL);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        final JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
                        Log.d(TAG, "onResponse: " + response);
                        if(Integer.parseInt(jsonObject.get("status").toString()) == 200){
                            onResponsePOST(200);
                            displayInformation(response);
                            Log.d(TAG, "onResponse: 200");
                        }else if(Integer.parseInt(jsonObject.get("status").toString()) == 100 || Integer.parseInt(jsonObject.get("status").toString()) == 101){
                            onResponsePOST(100);

                            Log.d(TAG, "onResponse: 100/101");
                        }else if(Integer.parseInt(jsonObject.get("status").toString()) == 201){
                            onResponsePOST(201);

                            Log.d(TAG, "onResponse: 201");
                        }else if(Integer.parseInt(jsonObject.get("status").toString()) == 404){
                            onResponsePOST(404);

                            Log.d(TAG, "onResponse: 404");
                        }else{
                            Log.d(TAG, "onResponse: ELSE");
                            onResponsePOST(500);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });


        requestQueue.add(postRequest);
        System.gc();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }
}

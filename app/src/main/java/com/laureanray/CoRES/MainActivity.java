

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
import android.widget.EditText;
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

    ProgressDialog dialog;

    public static String IPADDRESS;
    private final int REQUEST_PERMISSION_CAMERA=1;
    int MY_PERMISSIONS = 0;


    @Override
    protected void onPostResume() {
        Log.d("ON", "Post Resume");
        super.onPostResume();
    }


    @Override
    protected void onResume() {
        if(dialog != null){
            dialog.dismiss();
        }


        Log.d("ON", "Resume");
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                requestPermission();


                Toast toast = Toast.makeText(this, "Please allow permission. ", Toast.LENGTH_LONG);
                toast.show();
            } else {
                // No explanation needed; request the permission


              requestPermission();

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
        setContentView(R.layout.activity_main);

    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                MY_PERMISSIONS);

    }




    public void login(View view){
        Log.d("MAIN_ACTIVITY", "Login Clicked");
        dialog = ProgressDialog.show(MainActivity.this, "",
                "Logging In... Teka lang. ", false);
        final EditText email = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);
        final EditText ip = findViewById(R.id.ip);

        IPADDRESS = ip.getText().toString().trim();
        Log.d("MAIN_ACTIVITY", MainActivity.IPADDRESS);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://" + ip.getText().toString().trim() + "/portal/login/admin";

        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("MAIN_ACTIVITY", response);
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
                        Log.d("MAIN_ACTIVITY", jsonObject.get("status").toString());
                        if(jsonObject.get("status").toString().equals("\"success\"")){
                            launchCameraActivity();
                        }else{
                            Context context = getApplicationContext();
                            CharSequence text = "Invalid Login Credentials";
                            int duration = Toast.LENGTH_SHORT;
                            dialog.dismiss();
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("MAIN_ACTIVITY", error.toString());
                        Context context = getApplicationContext();
                        CharSequence text = "Invalid IP address!";
                        int duration = Toast.LENGTH_SHORT;
                        dialog.dismiss();
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
//                        ip.setText("");

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email", email.getText().toString().trim());
                params.put("password", password.getText().toString().trim());


                return params;
            }
        };



        requestQueue.add(postRequest);

    }






    public void launchCameraActivity(){
        Intent intent = new Intent(this, SelectDayActivity.class);
        startActivity(intent);

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//
//        if (requestCode == 0) {
//            if(resultCode == CommonStatusCodes.SUCCESS){
//                if(data != null){
//
//                    Barcode barcode = data.getParcelableExtra("barcode");
//                    barcodeResult.setText("Barcode Value: " +  barcode.displayValue);
//                }else{
//                    barcodeResult.setText("No Barcode Found");
//                }
//            }
//        }else{
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//
//    }
}

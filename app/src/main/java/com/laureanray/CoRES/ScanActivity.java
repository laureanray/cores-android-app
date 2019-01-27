package com.laureanray.CoRES;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.lang.reflect.Array;

public class ScanActivity extends AppCompatActivity {

    SurfaceView cameraPreview;
    public static String toScan;
    private static String API_KEY = "a7DFe2Er56s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        cameraPreview = (SurfaceView) findViewById(R.id.camera_preview);
        createCameraSource();

        final TextView toScanText = findViewById(R.id.toScanText);

        Log.d("toscantext", toScan);


                    toScanText.setText(toScan);




    }

    public static void setToScan(final String toScanText) {
        toScan = toScanText;
    }



    private String stringParser(String s){
        switch(s){
            case "Zero Human Interface":
                return "Zero_Human_Interface";

            case "Blockchain":
                return s;

            case "Robotic Process Automation":
                return "Robotic_Process_Automation";

            case "Web Developer Skillset":
                return "Web_Developer_Skillset";

            default:
                return "NULL";
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, SelectDayActivity.class));
    }

    private void createCameraSource() {
        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build();
        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1280, 720)
                .build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider callingD
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {


            }
        });



        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override

            public void release() {
                Log.d("Release", "Camera");

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {



                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

//

                if(barcodeDetector.isOperational()) {


                    if (barcodes.size() > 0) {
                        Log.d("QR", "is this called once?");
//                        Intent intent = new Intent(ScanActivity.this, QRScanResultActivity.class);
//                        QRScanResultActivity.ResultQR(barcodes.valueAt(0).displayValue);
//                        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
//                        startActivity(intent);
                        onScanned(barcodes.valueAt(0).displayValue);
                        cameraSource.release();
                        cameraSource.stop();
                        barcodeDetector.release();


                    }
                }

            }

        });




    }



    private void onScanned(String _id){
        final Button scanButton = findViewById(R.id.scanNewButton);
        final TextView scanPromptTxt = findViewById(R.id.scanPromptTxt);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scanPromptTxt.animate().alpha(0.0f).setDuration(400);
                scanButton.animate().translationY(-75f).alpha(1.0f).setDuration(500);
                cameraPreview.animate().alpha(0.2f).setDuration(1000);
//                scanButton.setVisibility(View.VISIBLE);

            }
        });
        Log.d("onScanned", _id);


//        createCameraSource();


        String seminarOrWorkshop = stringParser(toScan);

        if(!seminarOrWorkshop.equals("NULL")) {

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://" + MainActivity.IPADDRESS + "/admin/" + API_KEY + '/' + _id.trim() + '/' + seminarOrWorkshop;

            Log.d("URL", URL);
            StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response", response);
                            Gson gson = new Gson();
                            JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
                            Log.d("STATUS", jsonObject.get("status").toString());
//                            if (jsonObject.get("status").toString().equals("\"success\"")) {
//
//                            } else {
//
//                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.toString());
                            Context context = getApplicationContext();
                            CharSequence text = "Invalid IP address!";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
//                        ip.setText("");

                        }
                    });

                requestQueue.add(postRequest);
                Log.d("breakpoint", "test");
        }else{
            Log.e("ERROR", "NULL!!");
        }
    }

    public void scanAgain(View v){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                cameraPreview.animate().alpha(1f).setDuration(1000);
//                scanButton.setVisibility(View.VISIBLE);

            }
        });
        finish();
        Intent intent = new Intent(ScanActivity.this, ScanActivity.class);
                                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);

        startActivity(intent);
    }


}

package com.laureanray.CoRES;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ScanIDCheckActivity extends AppCompatActivity {

    TextView textView;
    TextView name;
    TextView cys;
    TextView school;
    TextView email;
    TextView mobile;
    TextView seminars;
    TextView seminarsTitle;
    TextView workshops;
    TextView workshopsTitle;

    private static final String TAG = "ScanIDCheckActivity";
        
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_idcheck);
        textView = findViewById(R.id.statusCode);
        name = findViewById(R.id.name_check);
        cys = findViewById(R.id.cys_check);
        school = findViewById(R.id.school_check);
        email = findViewById(R.id.email_check);
        mobile = findViewById(R.id.mobile_check);
        seminars = findViewById(R.id.seminar_check);
        seminarsTitle = findViewById(R.id.seminarTitle);
        workshops = findViewById(R.id.workshop_check);
        workshopsTitle = findViewById(R.id.workshopTitle);
    }

    @Override
    public void onBackPressed(){
        startActivity( new Intent(this, DashboardActivity.class) );
        finish();
    }


    public void scan(View view){
        ScanActivity.setSeminarName("Check ID");
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
                    checkID(barcode.displayValue);

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

    private void displayError(final     String err){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                name.setText(err);
                name.animate().alpha(1).setDuration(200);
                school.setText("");
                cys.setText("");
                email.setText("");
                mobile.setText("");
                workshops.setText("");
                seminars.setText("");
                workshopsTitle.setText("");
                seminarsTitle.setText("");
            }
        });

    }

    private void displayResponse(String response){
        workshops.setText("");
        seminars.setText("");
        workshopsTitle.setAlpha(0);
        seminarsTitle.setAlpha(0);
        Gson gson = new Gson();
        final JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        final String lastName = jsonObject.get("lastName").toString().replaceAll("\"", "");
        final String firstName = jsonObject.get("firstName").toString().replaceAll("\"", "");
        String mi;
        if(jsonObject.get("middleInitial").toString().replaceAll("\"", "").equals("NA") || jsonObject.get("middleInitial").toString().replaceAll("\"", "").equals("NA.")){
            mi = "";
        }else{
            mi = jsonObject.get("middleInitial").toString().replaceAll("\"", "");
        }

        final String course = jsonObject.get("course").toString().replace("\"", "");
        final String year = jsonObject.get("year").toString().replace("\"" ,"");
        final String section = jsonObject.get("section").toString().replace("\"", "");
        final String school_text = jsonObject.get("school").toString().replace("\"", "");
        final String emailText = jsonObject.get("email").toString().replace("\"", "");
        final String mobileText = jsonObject.get("mobile").toString().replace("\"", "");
        final String middleInitial = mi;
        Log.d(TAG, "displayResponse: " + jsonObject.get("seminar").toString());
        String seminarString  = jsonObject.get("seminar").toString().replace("[", "");
        String workshopString = jsonObject.get("workshop").toString().replace("[", "").replace("]", "");

        seminarString = seminarString.replace("]", "");
        Log.d(TAG, "displayResponse: " + seminarString);

        final String[] workshopArray = workshopString.split(",");
        final String[] seminarsArray = seminarString.split(",");
        String finalSeminarString = "";
        String finalWorkshopString = "";
        if(seminarsArray.length > 0){
            for(int i = 0; i < seminarsArray.length; i++){
                seminarsArray[i] = seminarsArray[i].replace("\"", "");
                if(seminarsArray[i].equals("Zero Human Interface")){
                    seminarsArray[i] += " (Wed, Bul, AM)";
                }else if(seminarsArray[i].equals("Blockchain")){
                    seminarsArray[i] += " (Wed, Bul, PM)";
                }else if(seminarsArray[i].equals("Robotic Process Automation")){
                    seminarsArray[i] += " (Wed, Recto, PM)";
                }else if(seminarsArray[i].equals("Web Developer Skillset")){
                    seminarsArray[i] += " (Wed, COC, PM)";
                }else if(seminarsArray[i].equals("VR Engineering")){
                    seminarsArray[i] += " (Thu, Bul, AM)";
                }else if(seminarsArray[i].equals("Software-Defined Architecture")){
                    seminarsArray[i] += " (Thu, IT, AM)";
                }else if(seminarsArray[i].equals("Game Development")){
                    seminarsArray[i] += " (Thu, Recto, AM)";
                }else if(seminarsArray[i].equals("5G Network")){
                    seminarsArray[i] += " (Thu, Bul, PM)";
                }else if(seminarsArray[i].equals("3D Metal Printing")){
                    seminarsArray[i] += " (Thu, IT, PM)";
                }else if(seminarsArray[i].equals("Cyber Defense")){
                    seminarsArray[i] += " (Thu, Recto, PM)";
                }else if(seminarsArray[i].equals("Salesforce Marketing Cloud")){
                    seminarsArray[i] += " (Fri, Recto, AM)";
                }else if(seminarsArray[i].equals("Cloud Auto ML")){
                    seminarsArray[i] += " (Fri, Claro, PM)";
                }else if(seminarsArray[i].equals("Ethical Hacking")){
                    seminarsArray[i] += " (Sat, COC, AM)";
                }else if(seminarsArray[i].equals("Digital Twin")){
                    seminarsArray[i] += " (Sat, COC, PM)";
                }

                finalSeminarString += (seminarsArray[i] + "\n");
            }
        }

        if(workshopArray.length > 0){
            for(int i = 0; i < workshopArray.length; i++){
                workshopArray[i] = workshopArray[i].replace("\"", "");
                finalWorkshopString += (workshopArray[i] + "\n");
            }
        }

        final String finalWORKSHOP = finalWorkshopString;
        final String finalSTRING = finalSeminarString;
        Log.d(TAG, "array" + Arrays.toString(workshopArray) + " and " + Arrays.toString(seminarsArray) );
        Log.d(TAG, "seminar: " + seminarsArray.length + " workshop: " + workshopArray.length);
        Log.d(TAG, "coneent of array: " + workshopArray[0]);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                name.animate().alpha(1).setDuration(200);
                name.setText(lastName + ", " + firstName + " " + middleInitial);
                school.animate().alpha(1).setDuration(200);
                school.setText(school_text);
                cys.animate().alpha(1).setDuration(200);
                cys.setText(course + " " + year +" " + section);
                email.animate().alpha(1).setDuration(200);
                email.setText(emailText);
                mobile.animate().alpha(1).setDuration(200);
                mobile.setText(mobileText);

                if(!(workshopArray.length == 1 && workshopArray[0].equals(""))){
                    workshopsTitle.animate().alpha(1).setDuration(200);
                    workshops.animate().alpha(1).setDuration(200);
                    workshops.setText(finalWORKSHOP);
                }


                if(!(seminarsArray.length == 1 && seminarsArray[0].equals(""))){
                    seminarsTitle.animate().alpha(1).setDuration(200);
                    seminars.animate().alpha(1).setDuration(200);
                    seminars.setText( finalSTRING);
                }


            }
        });
    }

    private void checkID(final String _id){
        String URL = "http://" + MainActivity.IPADDRESS + "/utils/searchByID";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.d(TAG, "checkAttendance: URL: " + URL);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        final JsonObject jsonObject = gson.fromJson(response, JsonObject.class);

                         try {
                             String status = jsonObject.get("status").toString().replaceAll("\"", "");
                             Log.d(TAG, "onResponse: " + Integer.parseInt(status));
                             if(Integer.parseInt(status) == 404){
                                 displayError("Not found on server. \nConsult administrator.");
                             }
                         }catch(NullPointerException e){
                             displayResponse(response);
                         }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        displayError("Check WIFI/Server");
                        Log.d(TAG, "onErrorResponse: " + error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("searchQuery", _id.trim());
                return params;
            }
        };

        requestQueue.add(postRequest);
        System.gc();
    }

}

package com.laureanray.CoRES;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class DashboardActivity extends AppCompatActivity {


    private static final String TAG = "DashboardActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    public void attendance(View v){
        Intent intent = new Intent(this, AttendanceActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        startActivity( new Intent(this, MainActivity.class) );
        finish();
    }

    public void idCheck(View v){
        Intent intent = new Intent(this, ScanIDCheckActivity.class);
        startActivity(intent);
        finish();
    }

}

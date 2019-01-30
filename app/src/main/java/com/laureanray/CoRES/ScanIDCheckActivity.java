package com.laureanray.CoRES;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ScanIDCheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_idcheck);
    }

    @Override
    public void onBackPressed(){
        startActivity( new Intent(this, DashboardActivity.class) );
        finish();
    }
}

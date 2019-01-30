package com.laureanray.CoRES;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AttendanceActivity extends AppCompatActivity {

    private static final String TAG = "AttendanceActivity";
    RadioGroup rGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        rGroup = (RadioGroup) findViewById(R.id.seminarRadioGroup);
        final Button nextButton = findViewById(R.id.nextButton);;

        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checked = (RadioButton) group.findViewById(checkedId);
                boolean isChecked = checked.isChecked();

                if(isChecked){
                    Log.d(TAG, "onCheckedChanged: " + checked.getText().toString());
                    nextButton.setVisibility(View.VISIBLE);
                }
            }
        });


    }


    public void next(View v){
        RadioButton checked = (RadioButton) rGroup.findViewById(rGroup.getCheckedRadioButtonId());
        ScanAttendanceActivity.setSeminar(checked.getText().toString());
        Log.d(TAG, "next: " + checked.getText().toString());
        Intent intent = new Intent(this, ScanAttendanceActivity.class);
        startActivity(intent);
        finish();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }
}

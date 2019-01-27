package com.laureanray.CoRES;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Day1Select extends AppCompatActivity {


    private String selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day1_select);

        RadioGroup day1RadioGroup = findViewById(R.id.day1RadioGroup);
        final Button proceedButton = findViewById(R.id.proceedButton);


        day1RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = group.findViewById(checkedId);

                boolean isChecked = checkedRadioButton.isChecked();

                if(isChecked){
                    selected = checkedRadioButton.getText().toString();
                    Log.d("Radio Button", checkedRadioButton.getText().toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            proceedButton.animate().alpha(1.0f).translationY(-40f).setDuration(500);
                        }
                    });
                }
            }
        });

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Day1Select.this, ScanActivity.class);
                ScanActivity.setToScan(selected);
                finish();
                startActivity(intent);
            }
        });
    }
}

package com.laureanray.CoRES;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectDayActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_day);

        Button day1 = findViewById(R.id.day1);
        Button day2 = findViewById(R.id.day2);
        Button day3 = findViewById(R.id.day3);
        Button day4 = findViewById(R.id.day4);


        day1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectDayActivity.this, Day1Select.class);
                startActivity(intent);
            }
        });

        day2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectDayActivity.this, Day2Select.class);
                startActivity(intent);
            }
        });

        day3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectDayActivity.this, Day3Select.class);
                startActivity(intent);
            }
        });


        day4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectDayActivity.this, Day4Select.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void startScan(View v){
        Intent intent = new Intent(this, ScanActivity.class);
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

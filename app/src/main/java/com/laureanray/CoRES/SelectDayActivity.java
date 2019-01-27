package com.laureanray.CoRES;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SelectDayActivity extends AppCompatActivity {

    String selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_day);

        RadioGroup rgroup = findViewById(R.id.selectDayGroup);
        final Button selectButton = findViewById(R.id.selectBtn);
        rgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = group.findViewById(checkedId);

                boolean isChecked = checkedRadioButton.isChecked();

                if(isChecked){
                    selected = checkedRadioButton.getText().toString();
                    Log.d("Radio Button", checkedRadioButton.getText().toString());
                    selectButton.animate().alpha(1.0f).translationY(25).setDuration(300);
                }
            }
        });


        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                finish();
                Log.d("Selected", selected);
                if(selected.equals("Day 1")){
                    intent = new Intent(SelectDayActivity.this, Day1Select.class);
                    startActivity(intent);
                    Log.d("Selected", "Is this called?");

                }else if(selected == "Day 2"){
                    intent = new Intent(SelectDayActivity.this, Day2Select.class);
                    startActivity(intent);

                }else if(selected == "Day 3"){
                    intent = new Intent(SelectDayActivity.this, Day3Select.class);
                    startActivity(intent);

                }else if(selected == "Day 4") {
                    intent = new Intent(SelectDayActivity.this, Day4Select.class);
                    startActivity(intent);

                }

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

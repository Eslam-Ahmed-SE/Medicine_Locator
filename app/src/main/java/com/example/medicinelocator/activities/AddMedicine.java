package com.example.medicinelocator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.medicinelocator.AddDrugTask;
import com.example.medicinelocator.R;

public class AddMedicine extends AppCompatActivity {

    private int pharmID;
    private String pharm;
    private String mail;

    EditText medNameEn;
    EditText medNameAr;
    EditText medPrice;

    TextView errorTxt;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        sharedPref = getSharedPreferences("loginData", MODE_PRIVATE);

        pharmID = sharedPref.getInt("pharmIDKey", 0);
        pharm = sharedPref.getString("pharmKey", null);
        mail = sharedPref.getString("pharmMailKey", null);

        medNameEn = findViewById(R.id.drugAddNameEn);
        medNameAr = findViewById(R.id.drugAddNameAr);
        medPrice = findViewById(R.id.drugAddPrice);

        errorTxt = findViewById(R.id.drugAddErrorTxt);





    }

    public void add(View view) {
        if (medNameEn.getText().toString().isEmpty()){
            medNameEn.setError(getResources().getString(R.string.sigup_empty_name));
            medNameEn.requestFocus();
        }
        else if (medNameAr.getText().toString().isEmpty()){
            medNameAr.setError(getResources().getString(R.string.sigup_empty_name));
            medNameAr.requestFocus();
        }
        else if (medPrice.getText().toString().isEmpty()){
            medPrice.setError(getResources().getString(R.string.price_empty));
            medPrice.requestFocus();
        }
        else {
            AddDrugTask addDrugTask = new AddDrugTask(medNameEn.getText().toString(), medNameAr.getText().toString(), medPrice.getText().toString(), String.valueOf(pharmID), mail, pharm) {
                @Override
                public void onResponseReceived(int result) {
                    if (result==20){
                        errorTxt.setText(getResources().getText(R.string.added_successfuly));
                        errorTxt.setTextColor(Color.GREEN);
                        errorTxt.setVisibility(View.VISIBLE);
                    }
                    else {
                        errorTxt.setText(getResources().getText(R.string.general_connection_error));
                        errorTxt.setVisibility(View.VISIBLE);
                    }
                }
            };
            addDrugTask.execute();
        }
    }
}
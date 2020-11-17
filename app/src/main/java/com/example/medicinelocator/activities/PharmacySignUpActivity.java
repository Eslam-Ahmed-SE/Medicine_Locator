package com.example.medicinelocator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.medicinelocator.R;

public class PharmacySignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_sign_up);
    }

    public void signupClicked(View view) {
        Intent i = new Intent(this, PharmacyHomeActivity.class);
        startActivity(i);
    }
}
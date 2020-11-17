package com.example.medicinelocator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.medicinelocator.R;

public class PharmacySignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_sign_in);
    }

    public void signinClicked(View view) {
        Intent i =  new Intent(this, PharmacySignUpActivity.class);
        startActivity(i);
    }
}
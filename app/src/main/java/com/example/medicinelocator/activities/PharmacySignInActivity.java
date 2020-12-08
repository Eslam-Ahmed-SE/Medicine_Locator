package com.example.medicinelocator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.medicinelocator.PharmacyLoginTask;
import com.example.medicinelocator.R;

public class PharmacySignInActivity extends AppCompatActivity {

    private boolean paused;

    private TextView errorMsg;

    SharedPreferences sharedPref;

    EditText signinEmail;
    EditText signinPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_sign_in);

        initComps();


        int pharmID = sharedPref.getInt("pharmIDKey", 0);
        Log.i("inf", "pharmIDKey" + pharmID);

        if (pharmID!=0){
            Intent i =  new Intent(this, PharmacyHomeActivity.class);
            startActivity(i);
            finish();
        }

//        if paused =false;
    }

    private void initComps() {
        sharedPref = getSharedPreferences("loginData", MODE_PRIVATE);

        errorMsg = findViewById(R.id.errorMsg);
        signinEmail = findViewById(R.id.signinEmail);
        signinPassword = findViewById(R.id.signinPassword);

    }

    public void signinClicked(View view) {

        paused = false;

        String mail;
        String pass;

        mail = signinEmail.getText().toString();
        pass = signinPassword.getText().toString();

        Intent i =  new Intent(this, PharmacyHomeActivity.class);

        @SuppressLint("StaticFieldLeak")
        PharmacyLoginTask pharmLoginTask = new PharmacyLoginTask(mail, pass) {

            @Override
            public void onResponseReceived(int result, int id) {
                Log.i("inf", "result code " + result);
                if (result==20){
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("pharmKey", signinPassword.getText().toString());
                    editor.putInt("pharmIDKey", id);
                    editor.commit();

                    startActivity(i);
                    finish();
                }
                else if (result==11){
                    errorMsg.setText(getResources().getText(R.string.emptyFields));
                    errorMsg.setVisibility(View.VISIBLE);
                }
                else {
                    errorMsg.setText(getResources().getText(R.string.incorrectFields));
                    errorMsg.setVisibility(View.VISIBLE);
                }
            }
        };
        pharmLoginTask.execute();

    }

    public void goToSignup(View view) {
        Intent i =  new Intent(this, PharmacySignUpActivity.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (paused) {
            SharedPreferences sharedPref = getSharedPreferences("loginData", MODE_PRIVATE);
            String errorMsgTxt = sharedPref.getString("errorMsgKey", null);
            Log.i("inf", "resume" + errorMsgTxt);

            if (errorMsgTxt!=null){
                errorMsg.setText(errorMsgTxt);
                errorMsg.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }
}


package com.example.medicinelocator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.medicinelocator.FillCityListTask;
import com.example.medicinelocator.FillGovListTask;
import com.example.medicinelocator.PharmacySigupTask;
import com.example.medicinelocator.R;
import com.example.medicinelocator.dataModels.Cities;
import com.example.medicinelocator.dataModels.Govs;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PharmacySignUpActivity extends AppCompatActivity {

    private EditText pharmName;
    private EditText pharmAddress;
    private EditText pharmLandmark;
    private Spinner pharmGovSpinner;
    private Spinner pharmCitySpinner;
    private EditText pharmPhone;
    private EditText pharmEmail;
    private EditText pharmPass;
    private TextView errorTxt;

    private Cities[] cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_sign_up);

        initComps();

        FillGovListTask govTask = new FillGovListTask() {
            @Override
            public void onResponseReceived(Govs[] mGovs) {
                if (mGovs!= null) {
                    String[] govNames = new String[mGovs.length];
                    for (int i = 0; i<mGovs.length; i++) {
                        govNames[i] = Locale.getDefault().getLanguage().equals("ar") ? mGovs[i].getName_ar():mGovs[i].getName_en();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(PharmacySignUpActivity.this, android.R.layout.simple_spinner_item, govNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    pharmGovSpinner.setAdapter(adapter);


                }
            }
        };
        govTask.execute();

        FillCityListTask cityTask = new FillCityListTask() {
            @Override
            public void onResponseReceived(Cities[] mCities) {
                if (mCities!= null) {
                    cities = mCities;
                    String[] cityNames = new String[mCities.length];
                    for (int i = 0; i<mCities.length; i++) {
                        cityNames[i] = Locale.getDefault().getLanguage().equals("ar") ? mCities[i].getName_ar():mCities[i].getName_en();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(PharmacySignUpActivity.this, android.R.layout.simple_spinner_item, cityNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    pharmCitySpinner.setAdapter(adapter);

                    pharmCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    pharmGovSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            filterCities();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    filterCities();
                }
            }
        };
        cityTask.execute();
    }

    private void filterCities() {

        List<String> availCitiesNames = new ArrayList<String>();
        for (int i=0; i<cities.length; i++){
            if (cities[i].getGov_id() == pharmGovSpinner.getSelectedItemPosition()+1){
                availCitiesNames.add(Locale.getDefault().getLanguage().equals("ar") ? cities[i].getName_ar():cities[i].getName_en());
            }
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PharmacySignUpActivity.this, android.R.layout.simple_spinner_item, availCitiesNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pharmCitySpinner.setAdapter(adapter);

    }

    private void initComps() {
        pharmName = findViewById(R.id.sigupPharmName);
        pharmAddress = findViewById(R.id.sigupPharmAddress);
        pharmLandmark = findViewById(R.id.sigupPharmLandmark);
        pharmGovSpinner = findViewById(R.id.sigupPharmGovSpinner);
        pharmCitySpinner = findViewById(R.id.sigupPharmCitySpinner);
        pharmPhone = findViewById(R.id.sigupPharmPhone);
        pharmEmail = findViewById(R.id.sigupPharmEmail);
        pharmPass = findViewById(R.id.sigupPharmPass);
        errorTxt = findViewById(R.id.signupPharmError);
    }

    public void signupClicked(View view) {
        if (pharmName.getText().toString().isEmpty()){
            pharmName.setError(getResources().getText(R.string.sigup_empty_name));
            pharmName.requestFocus();
        }
        else if (pharmAddress.getText().toString().isEmpty()){
            pharmAddress.setError(getResources().getText(R.string.sigup_empty_address));
            pharmAddress.requestFocus();
        }
        else if (pharmLandmark.getText().toString().isEmpty()){
            pharmLandmark.setError(getResources().getText(R.string.sigup_empty_landmark));
            pharmLandmark.requestFocus();
        }
        else if (pharmPhone.getText().toString().isEmpty()){
            pharmPhone.setError(getResources().getText(R.string.sigup_empty_phone));
            pharmPhone.requestFocus();
        }
        else if (pharmEmail.getText().toString().isEmpty()){
            pharmEmail.setError(getResources().getText(R.string.sigup_empty_email));
            pharmEmail.requestFocus();
        }
        else if (pharmPass.getText().toString().isEmpty()){
            pharmPass.setError(getResources().getText(R.string.sigup_empty_pass));
            pharmPass.requestFocus();
        }
        else {
            Log.i("inf", "all inserted");
            PharmacySigupTask sigupTask = new PharmacySigupTask(
                    pharmName.getText().toString(), pharmAddress.getText().toString(), pharmLandmark.getText().toString(), pharmGovSpinner.getSelectedItemPosition()+1,
                    pharmCitySpinner.getSelectedItemPosition()+1, pharmPhone.getText().toString(), pharmEmail.getText().toString(), pharmPass.getText().toString()
            ) {
                @Override
                public void onResponseReceived(int result) {
                    if (result==10){
                        errorTxt.setText(getResources().getText(R.string.sigup_email_exists));
                        errorTxt.setVisibility(View.VISIBLE);
                    }
                    else if (result==11||result==12){
                        errorTxt.setText(getResources().getText(R.string.general_connection_error));
                        errorTxt.setVisibility(View.VISIBLE);
                    }
                    else if (result==20){
                        errorTxt.setText(getResources().getText(R.string.sigup_done));
                        errorTxt.setTextColor(Color.GREEN);
                        errorTxt.setVisibility(View.VISIBLE);
                        finish();
                    }
                    Log.i("inf", "sigup done with: " + result);
                }
            };
            sigupTask.execute();


        }


    }
}
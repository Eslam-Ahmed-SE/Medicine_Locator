package com.example.medicinelocator.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.medicinelocator.FillMedicineListTask;
import com.example.medicinelocator.FillPharmacyListTask;
import com.example.medicinelocator.MakeAvailableUnavailableTask;
import com.example.medicinelocator.MedicineListAdapter;
import com.example.medicinelocator.R;
import com.example.medicinelocator.dataModels.Medicine;
import com.example.medicinelocator.dataModels.Pharmacy;

import java.util.List;

public class PharmacyHomeActivity extends AppCompatActivity {

    private List<Medicine> medicines;
    private RecyclerView medicineList;

    private int pharmID;
    private String pharm;
    private String mail;

    TextView pharmHomeName;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_home);

        initComps();

        sharedPref = getSharedPreferences("loginData", MODE_PRIVATE);
        pharmID = sharedPref.getInt("pharmIDKey", 0);
        pharm = sharedPref.getString("pharmKey", null);
//        final String[] mail = {null};

        Log.i("inf", "pharmIDKey" + pharmID);





    }

    @Override
    protected void onResume() {
        super.onResume();

        if (pharmID==0){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("errorMsgKey", "Please login first");
            editor.commit();
            Intent i =  new Intent(this, PharmacySignInActivity.class);
            startActivity(i);
            finish();
        }
        else {


            FillPharmacyListTask pharmacyListTask = new FillPharmacyListTask( String.valueOf(pharmID), this) {
                @Override
                public void onResponseReceived(List<Pharmacy> mPharms) {
                    Log.i("inf", "post executed");
                    if ( mPharms.size() == 1 ){
                        pharmHomeName.setText(mPharms.get(0).getName());
                        mail = mPharms.get(0).getMail();
                    }
                }
            };
//
            pharmacyListTask.execute();

            @SuppressLint("StaticFieldLeak")
            FillMedicineListTask medicineListTask =  new FillMedicineListTask()
            {
                @Override
                public void onResponseReceived(List<Medicine> mMedicine) {
                    medicines = mMedicine;
                    Log.i("inf", "task response");
                    if (medicines != null) {
                        Log.i("inf", "not null at home");
                        MedicineListAdapter mAdapter = new MedicineListAdapter(medicines, PharmacyHomeActivity.this, true, pharmID) {
                            @Override
                            public void onItemClick(itemViewHolder item, View clickedView) {
                                Log.i("inf", "drug id " + item.drugID);
                                if (item.pharmAvailability.isChecked()){
                                    MakeAvailableUnavailableTask makeAvailableUnavailableTask = new MakeAvailableUnavailableTask(mail, pharm, item.drugID, false) {
                                        @Override
                                        public void onResponseReceived(int result) {
                                            //        codes:
                                            //          errors:
                                            //              10: User name or password are incorect
                                            //              11: User mail or password is empty
                                            //              12: Drug id is empty
                                            //              13: Sql error
                                            //              14: no record found with this drug id
                                            //          successs:
                                            //              20: Updated availability
                                            if (result==20){
                                                item.pharmAvailability.setChecked(false);
                                            }
                                        }
                                    };
                                    makeAvailableUnavailableTask.execute();
                                }
                                else {
                                    MakeAvailableUnavailableTask makeAvailableUnavailableTask = new MakeAvailableUnavailableTask(mail, pharm, item.drugID, true) {
                                        @Override
                                        public void onResponseReceived(int result) {
                                            //        codes:
                                            //          errors:
                                            //              10: User name or password are incorect
                                            //              11: User mail or password is empty
                                            //              12: Drug id is empty
                                            //              13: Sql error
                                            //              14: no record found with this drug id
                                            //          successs:
                                            //              20: Updated availability
                                            if (result==20){
                                                item.pharmAvailability.setChecked(true);
                                            }

                                        }
                                    };
                                    makeAvailableUnavailableTask.execute();

                                }


//                                item.drugID

                            }
                        };
                        medicineList.setAdapter(mAdapter);

                        showItemContainer();
                    } else {
                        Log.i("inf", "null at home");

                    }
                }
            };
            medicineListTask.execute();
        }

    }

    private void initComps() {

        medicineList = findViewById(R.id.medicineList);
        medicineList.setLayoutManager(new LinearLayoutManager(this));
        medicineList.setItemAnimator(new DefaultItemAnimator());

        pharmHomeName = findViewById(R.id.pharmHomeName);
    }

    public void showItemContainer(){
        medicineList.setVisibility(View.VISIBLE);
//        medicines_loading.setVisibility(View.GONE);
    }

    public void logout(View view) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("pharmIDKey", 0);
        editor.putString("pharmKey", null);
        editor.commit();
        finish();
    }

    public void addNow(View view) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("pharmMailKey", mail);
        editor.commit();
        Intent i =  new Intent(this, AddMedicine.class);
        startActivity(i);

    }
}
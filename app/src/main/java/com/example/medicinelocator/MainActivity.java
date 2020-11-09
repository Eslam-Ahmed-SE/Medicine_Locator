package com.example.medicinelocator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.shimmer.ShimmerFrameLayout;

public class MainActivity extends AppCompatActivity {

    private RecyclerView medicineList;
    private ConstraintLayout item_container;
    private ShimmerFrameLayout medicines_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();


        FillMedicineListTask medicineListTask = new FillMedicineListTask(medicineList, this);
        medicineListTask.execute();
//        if(medicineListTask.getStatus() == AsyncTask.Status.PENDING){
//            // My AsyncTask has not started yet
//        }
//
//        if(medicineListTask.getStatus() == AsyncTask.Status.RUNNING){
//            // My AsyncTask is currently doing work in doInBackground()
//        }
//
//        if(medicineListTask.getStatus() == AsyncTask.Status.FINISHED){
//
//        }
    }

    private void initComponents() {
        medicineList = findViewById(R.id.medList);
        item_container = findViewById(R.id.recent_container);
        medicines_loading = findViewById(R.id.medicines_loading);

        medicineList.setLayoutManager(new LinearLayoutManager(this));
        medicineList.setItemAnimator(new DefaultItemAnimator());
        Log.i("inf", "init comps");
    }

    public void showItemContaier(){
        item_container.setVisibility(View.VISIBLE);
        medicines_loading.setVisibility(View.GONE);
    }

}
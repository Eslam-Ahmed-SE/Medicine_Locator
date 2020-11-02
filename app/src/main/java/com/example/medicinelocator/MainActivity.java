package com.example.medicinelocator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    RecyclerView medicineList;
    ConstraintLayout recent_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
//comment
        FillMedicineListTask medicineListTask = new FillMedicineListTask(medicineList, recent_container);
//
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
        recent_container = findViewById(R.id.recent_container);

        medicineList.setLayoutManager(new LinearLayoutManager(this));
        medicineList.setItemAnimator(new DefaultItemAnimator());
        Log.i("inf", "init comps");
    }

}
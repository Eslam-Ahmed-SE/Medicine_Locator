package com.example.medicinelocator;

import android.content.Context;
import android.os.AsyncTask;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FillMedicineListTask extends AsyncTask<Void,Void,Void> {
        RecyclerView medicineList;
        Medicine[] medicines;
        Context context;
        MainActivity mainActivity;

        public FillMedicineListTask(RecyclerView mMedicineList, MainActivity mContext) {
            medicineList = mMedicineList;
            context = mContext;
            mainActivity = mContext;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.i("inf", "task started");
            String url = "http://medprices.c1.biz/api/getDrugs.php";
            HttpURLConnection con;

            try {
                Log.i("inf", "try started");
                URL urlObj = new URL(url);
                con = (HttpURLConnection) urlObj.openConnection();
                Log.i("inf", "connect");
                con.connect();
                Log.i("inf", "connected");
                InputStream is = con.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ( (line = br.readLine()) != null ){
                    sb.append(line);
                }

                parseJson(sb.toString());
                Log.i("inf", "try done");
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
//                con.cl
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("inf", "post executed");
            if ( medicines !=null ){
                MedicineListAdapter mAdapter = new MedicineListAdapter(medicines,context);
                medicineList.setAdapter(mAdapter);

//                Transition transition = new Slide(Gravity.TOP);
//                transition.setDuration(3000);
//                transition.addTarget(recent_container);
//                TransitionManager.beginDelayedTransition((ViewGroup) recent_container.getParent(), transition);

                mainActivity.showItemContaier();

//                id.setText(contacts[0].id);
//                name.setText(contacts[0].name);
//                mail.setText(contacts[0].email);
//                mobile.setText(contacts[0].mobile);
            }


        }

        private void parseJson(String JSONString) {


            try {
                JSONObject jsonObj = new JSONObject(JSONString);
                JSONArray jsonArr = jsonObj.getJSONArray("Drugs");
                medicines = new Medicine[jsonArr.length()];
                for (int i=0; i<jsonArr.length(); i++){
                    JSONObject currentObj = jsonArr.getJSONObject(i);

                    String id = currentObj.getString("id");
                    String name = currentObj.getString("name");
                    String price = currentObj.getString("price");
                    String availability = currentObj.getString("availability");

                    medicines[i] = new Medicine(id, name, price, availability);
                    Log.i("inf", "try parse json");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



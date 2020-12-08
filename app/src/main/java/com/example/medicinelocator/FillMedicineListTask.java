package com.example.medicinelocator;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinelocator.activities.MainActivity;
import com.example.medicinelocator.dataModels.Medicine;
import com.example.medicinelocator.dataModels.Pharmacy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class FillMedicineListTask extends AsyncTask<Void,Void,List<Medicine>> {
        List<Medicine> medicines;

    public FillMedicineListTask() {
        medicines = new ArrayList<Medicine>();
    }

    @Override
        protected List<Medicine> doInBackground(Void... voids) {
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

            return medicines;
        }

        @Override
        protected void onPostExecute(List<Medicine> mMedicine) {
            super.onPostExecute(mMedicine);
            Log.i("inf", "post executed");
            onResponseReceived(mMedicine);


        }

    public abstract void onResponseReceived(List<Medicine> mMedicine);

    private void parseJson(String JSONString) {


            try {
                JSONObject jsonObj = new JSONObject(JSONString);
                JSONArray jsonArr = jsonObj.getJSONArray("Drugs");
//                medicines = new Medicine[jsonArr.length()];
                for (int i=0; i<jsonArr.length(); i++){
                    JSONObject currentObj = jsonArr.getJSONObject(i);

                    String id = currentObj.getString("id");
                    String name_en = currentObj.getString("name_en");
                    String name_ar = currentObj.getString("name_ar");
                    String price = currentObj.getString("price");
                    String availability = currentObj.getString("availability");

                    medicines.add( new Medicine(id, name_en, name_ar, price, availability) );
                    Log.i("inf", "try parse json");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



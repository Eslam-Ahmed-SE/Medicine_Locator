package com.example.medicinelocator;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinelocator.activities.MedicineItemViewActivity;
import com.example.medicinelocator.dataModels.Cities;
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
import java.util.StringTokenizer;

public abstract class FillPharmacyListTask extends AsyncTask<Void,Void,Void> {
    List<Pharmacy> pharms;
    String pharmIDs;
    Context context;

    int counter;

    public FillPharmacyListTask(String mPharmIDs, Context mContext) {
        pharmIDs = mPharmIDs;
        context = mContext;

        counter = 0;
        pharms = new ArrayList<Pharmacy>();

    }

    @Override
    protected Void doInBackground(Void... voids) {

        StringTokenizer st = new StringTokenizer(pharmIDs,"|");
        while (st.hasMoreTokens()) {
            Log.i("inf", "task started");
            String url = "http://medprices.c1.biz/api/getPharmacies.php?id="+st.nextToken();
            HttpURLConnection con;
            Log.i("inf", "url: " + url);

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
        }




        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        onResponseReceived(pharms);


    }

    public abstract void onResponseReceived(List<Pharmacy> mPharms);

    private void parseJson(String JSONString) {


        try {
            JSONObject jsonObj = new JSONObject(JSONString);
            JSONArray jsonArr = jsonObj.getJSONArray("Pharmacies");
//            pharms = new Pharmacy[arrLength];
//            for (; counter<jsonArr.length(); counter++){
                JSONObject currentObj = jsonArr.getJSONObject(0);

                String id = currentObj.getString("id");
                String name = currentObj.getString("name");
                Log.i("inf", "name: " + name);
                String phone = currentObj.getString("phone");
                String address = currentObj.getString("address");
                String landmark = currentObj.getString("landmark");
                String mail = currentObj.getString("mail");
                String gov = currentObj.getString("gov");
                String city = currentObj.getString("city");

                pharms.add(counter, new Pharmacy(id, name, phone, address, landmark, mail, gov, city));
                Log.i("inf", "try parse json");
                counter++;
            Log.i("inf", "counter " + counter);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}



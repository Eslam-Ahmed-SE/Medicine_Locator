package com.example.medicinelocator;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

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
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class FillPharmacyListTask extends AsyncTask<Void,Void,Void> {
    RecyclerView pharmList;
    List<Pharmacy> pharms;
    String pharmIDs;
    medicineItemView context;

    int counter;

    public FillPharmacyListTask(RecyclerView mPharmList, String mPharmIDs, medicineItemView mContext) {
        pharmList = mPharmList;
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
        Log.i("inf", "post executed");
        if ( pharms !=null ){
            PharmacyListAdapter mAdapter = new PharmacyListAdapter(pharms);
            pharmList.setAdapter(mAdapter);
            context.showPharmList();

//                Transition transition = new Slide(Gravity.TOP);
//                transition.setDuration(3000);
//                transition.addTarget(recent_container);
//                TransitionManager.beginDelayedTransition((ViewGroup) recent_container.getParent(), transition);

//            recent_container.setVisibility(View.VISIBLE);


//                id.setText(contacts[0].id);
//                name.setText(contacts[0].name);
//                mail.setText(contacts[0].email);
//                mobile.setText(contacts[0].mobile);
        }


    }

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



package com.example.medicinelocator;

import android.os.AsyncTask;
import android.util.Log;

import com.example.medicinelocator.dataModels.Govs;
import com.example.medicinelocator.dataModels.Medicine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class FillGovListTask extends AsyncTask<Void, Void, Govs[]> {

    Govs[] govs;

    @Override
    protected Govs[] doInBackground(Void... voids) {
        String url = "http://medprices.c1.biz/api/getGov.php";
        HttpURLConnection con;

        try {
            URL urlObj = new URL(url);
            con = (HttpURLConnection) urlObj.openConnection();
            con.connect();
            InputStream is = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ( (line = br.readLine()) != null ){
                sb.append(line);
            }

            fillGovs(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
//                con.cl
        }

        return govs;
    }

    @Override
    protected void onPostExecute(Govs[] mGovs) {
        super.onPostExecute(mGovs);
        onResponseReceived(mGovs);
    }

    public abstract void onResponseReceived(Govs[] mGovs);

    private void fillGovs(String JSONString) {

        try {
            JSONObject jsonObj = new JSONObject(JSONString);
            JSONArray jsonArr = jsonObj.getJSONArray("Governerates");
            govs = new Govs[jsonArr.length()];
            for (int i=0; i<jsonArr.length(); i++){
                JSONObject currentObj = jsonArr.getJSONObject(i);

                int id = currentObj.getInt("id");
                String name_en = currentObj.getString("governorate_name_en");
                String name_ar = currentObj.getString("governorate_name");

                govs[i] = new Govs(id, name_en, name_ar);;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}

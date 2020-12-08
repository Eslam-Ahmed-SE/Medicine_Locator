package com.example.medicinelocator;

import android.os.AsyncTask;
import android.util.Log;

import com.example.medicinelocator.dataModels.Cities;
import com.example.medicinelocator.dataModels.Govs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class FillCityListTask extends AsyncTask<Void, Void, Cities[]> {

    Cities[] cities;

    @Override
    protected Cities[] doInBackground(Void... voids) {
        String url = "http://medprices.c1.biz/api/getCity.php";
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

        return cities;
    }

    @Override
    protected void onPostExecute(Cities[] mCities) {
        super.onPostExecute(mCities);
        onResponseReceived(mCities);
    }

    public abstract void onResponseReceived(Cities[] mCities);

    private void fillGovs(String JSONString) {

        try {
            JSONObject jsonObj = new JSONObject(JSONString);
            JSONArray jsonArr = jsonObj.getJSONArray("Cities");
            cities = new Cities[jsonArr.length()];
            for (int i=0; i<jsonArr.length(); i++){
                JSONObject currentObj = jsonArr.getJSONObject(i);

                int id = currentObj.getInt("id");
                int govId = currentObj.getInt("gov_id");
                String name_en = currentObj.getString("city_name_en");
                String name_ar = currentObj.getString("city_name");

                cities[i] = new Cities(id, govId, name_en, name_ar);;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}

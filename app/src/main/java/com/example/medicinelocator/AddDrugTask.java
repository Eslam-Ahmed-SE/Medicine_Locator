package com.example.medicinelocator;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public abstract class AddDrugTask extends AsyncTask<Void, Void, Void> {


    private final String drugName_en;
    private final String drugName_ar;
    private final String drugPrice;
    private final String pharmID;
    private final String pharmMail;
    private final String pharmPass;

    private int result;

    public AddDrugTask(String drugName_en, String drugName_ar, String drugPrice, String pharmID, String pharmMail, String pharmPass) {
        this.drugName_en = drugName_en;
        this.drugName_ar = drugName_ar;
        this.drugPrice = drugPrice;
        this.pharmID = pharmID;
        this.pharmMail = pharmMail;
        this.pharmPass = pharmPass;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mailCred", pharmMail);
        params.put("passCred", pharmPass);
        params.put("medName_en", drugName_en);
        params.put("medName_ar", drugName_ar);
        params.put("medPrice", drugPrice);
        params.put("pharmID", pharmID);
        Log.i("inf", "mail: " + pharmMail);
        Log.i("inf", "pass: " + pharmPass);

        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0){
                    sbParams.append("&");
                }
                sbParams.append(key).append("=")
                        .append(URLEncoder.encode(params.get(key), "UTF-8"));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }

        Log.i("inf", "task started");
        String url = "http://medprices.c1.biz/api/addDrug.php";
        HttpURLConnection conn = null;

        try{
            URL urlObj = new URL(url);
            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF-8");

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);

            conn.connect();

            String paramsString = sbParams.toString();

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(paramsString);
            wr.flush();
            wr.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            Log.d("test", "result from server: " + result.toString());
            parseJson(result.toString());

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void voids) {
        super.onPostExecute(voids);
        Log.i("inf", "post executed");
        onResponseReceived(result);


    }

    public abstract void onResponseReceived(int result);

    private void parseJson(String JSONString) {


        try {
            JSONObject jsonObj = new JSONObject(JSONString);
            JSONArray jsonArr;
            if (jsonObj.has("success")){
                jsonArr = jsonObj.getJSONArray("success");
            }
            else {
                jsonArr = jsonObj.getJSONArray("error");
            }

            for (int i=0; i<jsonArr.length(); i++){
                JSONObject currentObj = jsonArr.getJSONObject(i);

                int resultCode = currentObj.getInt("code");
                result = resultCode;

                Log.i("inf", "try parse json code: " + resultCode);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}



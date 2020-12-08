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

public abstract class PharmacySigupTask extends AsyncTask<Void, Void, Void> {


    private final String name;
    private final String address;
    private final String landmark;
    private final int gov;
    private final int city;
    private final String phone;
    private final String mail;
    private final String pass;


    private int result;

    public PharmacySigupTask(String name, String address, String landmark, int gov, int city, String phone, String mail, String pass) {
        this.name = name;
        this.address = address;
        this.landmark = landmark;
        this.gov = gov;
        this.city = city;
        this.phone = phone;
        this.mail = mail;
        this.pass = pass;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        HashMap<String, String> params = new HashMap<>();
        params.put("pharmName", name);
        params.put("pharmAddress", address);
        params.put("pharmNear", landmark);
        params.put("pharmGov", String.valueOf(gov));
        params.put("pharmCity", String.valueOf(city));
        params.put("pharmPhone", String.valueOf(phone));
        params.put("pharmMail", mail);
        params.put("pharmPass", pass);
        Log.i("inf", "mail: " + mail);
        Log.i("inf", "pass: " + pass);

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
        String url = "http://medprices.c1.biz/api/addPharmacy.php";
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



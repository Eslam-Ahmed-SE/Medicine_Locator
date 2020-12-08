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

public abstract class MakeAvailableUnavailableTask extends AsyncTask<Void, Void, Void> {

    private final String mail;
    private final String pass;
    private final String drugID;
    private final boolean makeAvail;

    private int result;

    public MakeAvailableUnavailableTask(String mail, String pass, String drugID, boolean makeAvail) {
        this.mail = mail;
        this.pass = pass;
        this.drugID = drugID;
        this.makeAvail = makeAvail;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mail", mail);
        params.put("pass", pass);
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
        String url = (makeAvail) ? "http://medprices.c1.biz/api/makeAvail.php?id="+drugID : "http://medprices.c1.biz/api/makeUnAvail.php?id="+drugID;
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



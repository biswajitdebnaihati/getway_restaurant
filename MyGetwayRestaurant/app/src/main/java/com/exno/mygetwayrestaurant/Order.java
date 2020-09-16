package com.exno.mygetwayrestaurant;



import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Order extends AsyncTask<String, Void, String> {
    HttpURLConnection conn;
    URL url = null;
    String data;
    Context context;
    public Data rData=null;
    static  String cnumber;
    public Order(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params)
    {
        try
        {
            String durl=params[0];
            cnumber=params[3];
            url = new URL(durl);
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
            return "exception";
        }
        try
        {

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("data", params[1])
            .appendQueryParameter("query", params[2]);

            String query = builder.build().getEncodedQuery();
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
        } catch (IOException e1)
        {
            Log.d("drdrd","ass "+e1.getMessage());
            e1.printStackTrace();
            return "false2";
        }

        try {

            InputStream input = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
            {
                result.append(line);
            }
            data = result.toString();

        } catch (IOException e)
        {
            e.printStackTrace();
            return "false1";
        } finally
        {
            conn.disconnect();
        }
        Log.d("Ck_check","B ");

        return data;
    }



    @Override
    protected void onPostExecute(String result)
    {
        result = result.trim();

        try {
            JSONObject jsonRootObject2 = new JSONObject(result);
            jsonRootObject2.put("callNo",cnumber);
            rData.showData(jsonRootObject2.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}


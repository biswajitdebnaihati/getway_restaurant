package com.exno.mygetwayrestaurant;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiCall extends AsyncTask<String, Void, String> {
        HttpURLConnection conn;
        URL url = null;
        String data;
        Context context;
        public ApiInterface_a rData=null;
        public ApiCall(Context context)
        {
        this.context = context;
        rData= (ApiInterface_a) context;
        Log.d("printcontext",""+this.context);
        }

@Override
protected void onPreExecute() {
        super.onPreExecute();


        }

@Override
protected String doInBackground(String... params) {
    OutputStream os = null;
    InputStream is = null;
    HttpURLConnection conn = null;
    try {
        URL url = new URL(context.getString(R.string.baseurl)+params[0]);
        Log.d("sssssssQQQ","url "+url);
        String message = params[1];
        conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout( 10000 );
        conn.setConnectTimeout( 15000 );
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setFixedLengthStreamingMode(message.getBytes().length);
        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        conn.connect();
        Log.d("sssssssQQQ","CALL JSON "+message);
        os = new BufferedOutputStream(conn.getOutputStream());
        os.write(message.getBytes());
        os.flush();
        is = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
        {
            result.append(line);
        }
        data=result.toString();

    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        //clean up
        try
        {
            os.close();
            try {
                is.close();
            }catch (Exception e){}
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        conn.disconnect();
        }
        Log.d("aaaaaree",""+data);
        return data;
        }



@Override
protected void onPostExecute(String result)
        {


        try {
        rData.ApiMethod(result);

        } catch (JSONException e)

        {
            e.printStackTrace();
        }
        }
        }


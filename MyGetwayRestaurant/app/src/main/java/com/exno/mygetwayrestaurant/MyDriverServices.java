package com.exno.mygetwayrestaurant;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;

public class MyDriverServices extends Service {
    String data="";

    public MyDriverServices()
    {

    }

    @Override
    public IBinder onBind(Intent intent)
    {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        CurrentDriverId.handler= new Handler();
        CurrentDriverId.runnable = new Runnable()
        {
            @Override
            public void run()
            {

                try
                {
                    if(!(CurrentDriverId.did.equals("")))
                    getDriverDetails();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                CurrentDriverId.handler.postDelayed(CurrentDriverId.runnable, 2000);
            }
        };
        CurrentDriverId.handler.post(CurrentDriverId.runnable);




        Log.d("ServiceKolkataSS","Ok");
    }
    void getDriverDetails() throws JSONException
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("did", CurrentDriverId.did);
        Log.d("OpdataCall",""+jsonObject.toString());
        RequestQueue requestQueue = Volley.newRequestQueue(this);


        String url="https://youaudio.in/food2/customerapi/get_deliveryboy_current_location";
        Log.d("OpdataCall","URL : "+url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST,url, jsonObject, new com.android.volley.Response.Listener<JSONObject>()
                {

                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("OpdataCall","Response : "+response.toString());
                        try
                        {
                            FileOutputStream fOut = openFileOutput(getString(R.string.file_name),MODE_PRIVATE);
                            fOut.write(response.toString().getBytes());
                            fOut.close();

                        }
                        catch (Exception e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }




                    }
                }, new com.android.volley.Response.ErrorListener()
                {

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();

                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
}

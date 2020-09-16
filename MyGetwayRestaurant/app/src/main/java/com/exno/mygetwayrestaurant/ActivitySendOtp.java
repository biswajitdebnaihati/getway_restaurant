package com.exno.mygetwayrestaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class ActivitySendOtp extends AppCompatActivity {
    PinEntryEditText pinEntry ;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String otp_data="-",otp_ph="-";
    TextView resend_t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);
        pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.SESSION), 0);
        editor = pref.edit();
        if(!(new CheckInternet(this).getInternetStatus()))
        {

            startActivity(new Intent(this, ActivityNoActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        resend_t=(TextView)findViewById(R.id.resend);
        otp_data= pref.getString("otp_data", "-");
        otp_ph= pref.getString("otp_ph", "-");
        if (otp_data.equals("-"))
        {

            startActivity(new Intent(ActivitySendOtp.this,ActivityPasswordReSet.class));
            finish();
        }
        else {

            StorageData.OTP=otp_data;
            pinEntry = (PinEntryEditText) findViewById(R.id.txt_pin_entry);
            pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    if (StorageData.OTP.trim().equals(pinEntry.getText().toString().trim()))
                    {
                        startActivity(new Intent(ActivitySendOtp.this, ActivityNewPassword.class));
                        editor.putString("otp_data", "-");
                        editor.apply();
                        finish();
                    }
                    else{
                        Toasty.error(ActivitySendOtp.this,"Invalid OTP",Toasty.LENGTH_LONG).show();
                    }

                }
            });
        }
        resend_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getPasswordReset();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ActivitySendOtp.this,LoginActivity.class));
    }

    void getPasswordReset() throws JSONException
    {


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone",otp_ph);

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        String url=getString(R.string.baseurl)+"forget_pass";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST,url, jsonObject, new com.android.volley.Response.Listener<JSONObject>()
                {

                    @Override
                    public void onResponse(JSONObject response)
                    {

                        try {
                            if (response.getInt("status")==1){
                                Toasty.success(ActivitySendOtp.this,"OTP Reset",Toasty.LENGTH_LONG).show();

                                JSONArray jsonArray=response.getJSONArray("data");
                                JSONObject jsonObject1=jsonArray.getJSONObject(0);
                                StorageData.OTP=jsonObject1.getString("otp");
                                editor.putString("otp_data", jsonObject1.getString("otp"));
                                editor.apply();

                            }
                            else {
                                Toast.makeText(ActivitySendOtp.this, "Invalid ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
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

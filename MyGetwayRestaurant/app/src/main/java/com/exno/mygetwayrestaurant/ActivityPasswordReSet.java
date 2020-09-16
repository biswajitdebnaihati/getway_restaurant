package com.exno.mygetwayrestaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class ActivityPasswordReSet extends AppCompatActivity {
    int cc=0;
    EditText userid;
    Button signin;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    TextView ph_t;
    Button send_otp;
    JsonObject jsonObject=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_re_set);
        pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.SESSION), 0);
        editor = pref.edit();
        if(!(new CheckInternet(this).getInternetStatus()))
        {

            startActivity(new Intent(this, ActivityNoActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        init();
        Action();
    }

    private void Action()
    {
        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (((ph_t.getText().toString().length())>9) && ((ph_t.getText().toString().length())<16) )
                {

                    try
                    {
                        getPasswordReset();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
                else
                    {
                        Toasty.info(ActivityPasswordReSet.this,"Invalid Phone No.",Toasty.LENGTH_LONG).show();
                    }
            }
        });
        ph_t.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    send_otp.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    private void init() {
        send_otp=(Button)findViewById(R.id.send_otp);
        ph_t=(TextView)findViewById(R.id.ph_t);
    }


    void getPasswordReset() throws JSONException
    {


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone",ph_t.getText().toString());

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
                                JSONArray jsonArray=response.getJSONArray("data");
                                JSONObject jsonObject1=jsonArray.getJSONObject(0);
                                editor.putString("otp_data", jsonObject1.getString("otp"));
                                editor.putString("otp_ph", ph_t.getText().toString().trim());
                                editor.apply();
                                startActivity(new Intent(ActivityPasswordReSet.this,ActivitySendOtp.class));
                                finish();
                            }
                            else {
                                Toast.makeText(ActivityPasswordReSet.this, "Invalid Phone No.", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ActivityPasswordReSet.this,LoginActivity.class));
    }
}

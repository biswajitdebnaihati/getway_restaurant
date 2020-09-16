package com.exno.mygetwayrestaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityNewPassword extends AppCompatActivity {
    EditText pass_e,cpass_e;
    Button submit_b;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    static String otp_ph="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.SESSION), 0);
        editor = pref.edit();
        if(!(new CheckInternet(this).getInternetStatus()))
        {

            startActivity(new Intent(this, ActivityNoActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        otp_ph= pref.getString("otp_ph", "-");
        if (pref.getBoolean("isLogin", false)==true)
        {
            otp_ph= pref.getString(getString(R.string.Contact_no), "-");
        }
        else {
            otp_ph= pref.getString("otp_ph", "-");
        }

        init();
        ActionCall();
    }

    private void ActionCall()
    {
        submit_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
              if (isValidate())
              {
                  //
                  try {
                      getPasswordResetCall();
                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
              }
            }
        });
    }

    private boolean isValidate()
    {
        if (!(pass_e.getText().toString().length()>3))
        {
            pass_e.setError("Between 4 and 20 alphanumeric characters");
            return false;
        }
        else {
            if (pass_e.getText().toString().trim().equals(cpass_e.getText().toString().trim())){
                return  true;
            }
            else {
                cpass_e.setError("Password not match");
                return false;
            }

        }
    }

    private void init()
    {
        pass_e=(EditText)findViewById(R.id.pass) ;
        cpass_e=(EditText)findViewById(R.id.cpass) ;
        submit_b=(Button) findViewById(R.id.submit) ;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ActivityNewPassword.this,LoginActivity.class));
        finish();
    }
    void getPasswordResetCall() throws JSONException
    {


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone",otp_ph);
        jsonObject.put("pass",pass_e.getText().toString());

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        String url=getString(R.string.baseurl)+"reset_pass";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST,url, jsonObject, new com.android.volley.Response.Listener<JSONObject>()
                {

                    @Override
                    public void onResponse(JSONObject response)
                    {

                        try {
                            if (response.getInt("status")==1){

                                startActivity(new Intent(ActivityNewPassword.this,LoginActivity.class));
                                finish();
                            }
                            else {
                                Toast.makeText(ActivityNewPassword.this, "Invalid Phone No.", Toast.LENGTH_SHORT).show();
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

package com.exno.mygetwayrestaurant;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity
{
String TAG="aaaaa";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  startActivity(new Intent(MainActivity.this, ActivityDatabase.class));


        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        }, 2000);

        FirebaseInstanceId.getInstance().getInstanceId()


                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task)
                    {
                        if (!task.isSuccessful())
                        {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                       // openWhatsApp(token);
                        /*try {
                            getPasswordResetCall(token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, token);

                    }
                });
      //  startActivity(new Intent(MainActivity.this,ActivityDashBoard.class));

    //    startActivity(new Intent(this,LoginActivity.class));

    }



    public void openWhatsApp(String s){
        PackageManager pm=getPackageManager();
        try {
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = s;

            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e)
        {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }
        catch(Exception e)
        {

        }

    }


    /*void getPasswordResetCall(String token) throws JSONException
    {


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone","111");
        jsonObject.put("pass",token);

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        String url="http://77.104.161.68/~getwayin/food_delivery/apk/php_testing/index.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST,url, jsonObject, new com.android.volley.Response.Listener<JSONObject>()
                {

                    @Override
                    public void onResponse(JSONObject response)
                    {

                        try {
                            if (response.getInt("status")==1){

                                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                                finish();
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Invalid Phone No.", Toast.LENGTH_SHORT).show();
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
    }*/
}

package com.exno.mygetwayrestaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.exno.mygetwayrestaurant.model.Testingdata;
import com.exno.mygetwayrestaurant.rest.ApiClient;
import com.exno.mygetwayrestaurant.rest.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText _emailText;
    EditText _passwordText;
    Button _loginButton;
    TextView _link_forgot_password;
    static String TAG="kkkkk";
    private static final int REQUEST_SIGNUP = 0;
    RequestBody bodyRequest;
    Call<Testingdata> cell=null;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    boolean doubleBackToExitPressedOnce = false;
    Boolean isLogin=false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.SESSION), 0);
        editor = pref.edit();

        _emailText=(EditText)findViewById(R.id.input_email);
        _passwordText=(EditText)findViewById(R.id.input_password);;
        _loginButton=(Button)findViewById(R.id.btn_login);;
        _link_forgot_password=(TextView)findViewById(R.id.link_forgot_password);;
        _loginButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                login();
                _loginButton.setEnabled(false);
            }
        });

        _link_forgot_password.setOnClickListener(new View.OnClickListener()
            {

            @Override
            public void onClick(View v)
            {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), ActivityPasswordReSet.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
            }
            });

        _passwordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    login();
                    handled = true;
                }
                return handled;
            }
        });

        if(!(new CheckInternet(LoginActivity.this).getInternetStatus()))
        {

            startActivity(new Intent(this, ActivityNoActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        isLogin= pref.getBoolean("isLogin", false);
        if (isLogin)
        {
            startActivity(new Intent(LoginActivity.this,ActivityDashBoard.class));
            finish();
        }

    }

    public void login()
    {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);
        onLoginSuccess();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically

            }
        }
    }

    /*@Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }*/

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);


        callLogin();
       // finish();
    }

    private void callLogin() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", _emailText.getText().toString().toLowerCase());
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        try {
            jsonObject.put("password", _passwordText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        cell=apiService.savePost(bodyRequest);
        cell.enqueue(new Callback<Testingdata>()
        {
            @Override
            public void onResponse(Call<Testingdata> call, Response<Testingdata> response)
            {
                _loginButton.setEnabled(true);
                Log.d("alldata",""+response.body().getStatus()+"\n");
                Log.d("alldata",""+response.body().getData()+"\n");
                /*Log.d("alldata",""+response.body().getStatus()+"\n");
                Log.d("alldata","call "+call+"\n");
                Log.d("alldata","JSON SIZE "+data.size()+"\n");*/

                List<LoginPojo> data=response.body().getData();
                if (response.body().getStatus()==1)
                {
                    for (int n=0;n<data.size();n++)
                    {


                        editor.putString(getString(R.string.Rid), data.get(n).getRid());
                        editor.putString(getString(R.string.Res_auth_id), data.get(n).getRes_auth_id());
                        editor.putString(getString(R.string.Rname), data.get(n).getRname());
                        editor.putString(getString(R.string.Email), data.get(n).getEmail());
                        editor.putString(getString(R.string.Contact_no), data.get(n).getContact_no());
                        editor.putString(getString(R.string.Last_login), data.get(n).getLast_login());
                        editor.putString(getString(R.string.on_off_status), data.get(n).getOn_off());
                        editor.putBoolean("isLogin", true);
                        editor.apply();


                    }
                    startActivity(new Intent(LoginActivity.this,ActivityDashBoard.class));
                    finish();

                }
                else {
                    Toast.makeText(LoginActivity.this, "Invalid User name or password", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Testingdata> call, Throwable t)
            {
                Log.d("alldata","ERROR : "+t.toString()+"\n");
            }
        });
    }

    public void onLoginFailed()
    {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        /*if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        }
        */
        if (email.isEmpty() || !(email.length()>9 && (email.length()<16))) {
            _emailText.setError("enter a valid Phone No.");
            valid = false;
        }
        else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            _passwordText.setError("between 4 and 20 alphanumeric characters");
            valid = false;
        } else
        {
            _passwordText.setError(null);
        }

        return valid;
    }

    @Override
    public void onBackPressed()
    {
        if (doubleBackToExitPressedOnce)
        {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        Toasty.info(LoginActivity.this,"Please click BACK again to exit",Toasty.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
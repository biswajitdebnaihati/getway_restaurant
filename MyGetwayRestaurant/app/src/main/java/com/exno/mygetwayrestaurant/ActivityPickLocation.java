package com.exno.mygetwayrestaurant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ActivityPickLocation extends AppCompatActivity implements ApiInterface_a {



    static String urlLink;
    private WebView mWebView;
    static SharedPreferences pref;
    static SharedPreferences.Editor editor;
    static WebView webView;
    static TextView addressselect;
    static Button slocation,more;
    static Boolean bool=false;
    static JSONArray jsonArray=null;
    static  String userchk;
    static  String address;
    List<Address> addresses;
    static  String rid;


    private static final int REQUEST_LOCATION = 1;
    Button btnGetLocation;
    TextView showLocation;
    LocationManager locationManager;
    String latitude, longitude;



    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_location);
        //startService(new Intent(this,LocationTrack.class));
        addressselect=(TextView)findViewById(R.id.addressselect);
        pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.SESSION), 0);
        editor = pref.edit();
        rid = pref.getString(getString(R.string.Rid), "no data");

       /* try
        {
            callServerlink();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }*/

        //webView.loadUrl("javascript:callFromActivity(\"" + "Bjfnvjfnjfnjfngjfdngjfnjfnvjdfzvnvjfdn vbjfdn bjfdnb" + "\")");


        ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        //showLocation = findViewById(R.id.showLocation);
        //btnGetLocation = findViewById(R.id.btnGetLocation);
        try{
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                OnGPS();
            }
            else
                {
                getLocation();
            }
        }
        catch (Exception e){
            getLocation();
        }


    }


    //*********
    private void callServerlink() throws IOException
    {
        FileInputStream fin = null;
        try {
            fin = openFileInput("locationfilename");
            int c;
            String temp = "";
            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
            Log.d("GETLOCATION:" + "FILE", temp);


            String[] separated = temp.split("~");
            try {
                Double lati_1 = 22.5749d;
                Double longi_2 = 88.4339d;
                urlLink=getString(R.string.MapAPIurlcall)+"mapapi/location.php?api=AIzaSyBpI3ZQp-FROiguTAksLUe2s9Il4a-N3A8&lat="+lati_1+"&lng="+longi_2+"";

            } catch (Exception e) {

            }
        }
        catch (Exception e) {

        }
    }

    @Override
    public void ApiMethod(String s) throws JSONException
    {
        try{
            JSONObject jsonObject1=new JSONObject(s);
            if (jsonObject1.getInt("status")==1){
                Toast.makeText(this, "Your location update", Toast.LENGTH_SHORT).show();
                finish();
            }
            else {
                Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
                finish();
            }


        }catch (Exception e){

        }


    }


    //*************


    /*@Override
    public void ApiMethod(String s) throws JSONException
    {

    }*/

    public class WebViewJavaScriptInterface{

        Context mContext;

        /** Instantiate the interface and set the context */
        WebViewJavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface   // must be added for API 17 or higher
        public void showToast(final String toast) {



            String[] latlngdata=null;
            try
            {
                Toast.makeText(mContext, ""+toast, Toast.LENGTH_SHORT).show();
                latlngdata = toast.split("~");
            }
            catch (Exception e)
            {

            }
            try
            {
                Log.d("printalldataabcd",Double.parseDouble(latlngdata[0])+":"+Double.parseDouble(latlngdata[1]));
                /*setLocationData(Double.parseDouble(latlngdata[0]),Double.parseDouble(latlngdata[1]),1);
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("did",did+"");
                jsonObject.put("home_lat",Double.parseDouble(latlngdata[0])+"");
                jsonObject.put("home_long",Double.parseDouble(latlngdata[0])+"");
                new ApiCall(ActivityPickLocation.this).execute("api/deliveryboy_home_lat_long_update",jsonObject.toString());
                startActivity(new Intent(ActivityPickLocation.this,MainActivity.class));*/
               // finish();

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        @JavascriptInterface   // must be added for API 17 or higher
        public void showToastCurrent(final String toast)
        {

            Log.d("locationchkdatarr","B "+toast);
            String[] latlngdata=null;
            try
            {
                latlngdata = toast.split("~");
            }
            catch (Exception e)
            {

            }
            try
            {
                JSONObject jsonObject1=new JSONObject();
                jsonObject1.put("rid",rid);
                jsonObject1.put("lat",Double.parseDouble(latlngdata[0]));
                jsonObject1.put("lon",Double.parseDouble(latlngdata[1]));
                //getAllOrders(rid,);
                //************************123
                ApiCall apiCall= (ApiCall) new ApiCall(ActivityPickLocation.this).execute("restaurant_lat_long_update",jsonObject1.toString());
                apiCall.rData=ActivityPickLocation.this;

                //setLocationData(Double.parseDouble(latlngdata[0]),Double.parseDouble(latlngdata[1]),0);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            //webView.loadUrl("javascript:testFun('MUMBAI')");
            /*editor.putString("currentaddress", toast);
            editor.putString("knownname", toast.substring(0,toast.indexOf(",")));
            editor.putString("latitude", (GetLocationLatLng(toast)).lat+"");
            editor.putString("longitude", (GetLocationLatLng(toast)).lng+"");
            editor.apply();*/

        }
    }

    private void setLocationData(double parseDouble, double parseDouble1, int i) throws IOException, JSONException {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(parseDouble, parseDouble1, 1);
        address = addresses.get(0).getAddressLine(0);
        addressselect.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                addressselect.setText(""+"");
                webView.loadUrl("javascript:callFromActivity(\"" + address + "\")");
            }
        },1000);



    }
    @JavascriptInterface   // must be added for API 17 or higher
    public void showToastCurrent(final String toast) {

        Log.d("locationchkdatarr", "B " + toast);

    }
    @JavascriptInterface   // must be added for API 17 or higher
    public void showToast(final String toast) {

        Log.d("locationchkdatarr","A "+toast);


    }


    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                ActivityPickLocation.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                ActivityPickLocation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                Log.d("printlogdata",""+"Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
                callMap(lat,longi);

            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void callMap(Double lati_1, Double longi_2) {

        /*Double lati_1 =22.5749d;
        Double longi_2 = 88.4339d;*/
        urlLink=getString(R.string.MapAPIurlcall)+"mapapi/location.php?api=AIzaSyBpI3ZQp-FROiguTAksLUe2s9Il4a-N3A8&lat="+lati_1+"&lng="+longi_2+"";



        webView= (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl(urlLink);
        Log.d("printalldata",""+urlLink);


        webView.loadUrl("javascript:testFun()");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.addJavascriptInterface(new WebViewJavaScriptInterface(this), "Android");
    }

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        startActivity(new Intent(ActivityPickLocation.this,ActivityDashBoard.class));
        finish();
    }



}

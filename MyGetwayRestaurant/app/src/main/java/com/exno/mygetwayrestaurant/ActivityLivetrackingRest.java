package com.exno.mygetwayrestaurant;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLivetrackingRest extends AppCompatActivity implements OnMapReadyCallback , LocationListener{

    /*
    Location
     */
    static String dAddress="";
    static String rAddress="";
    LocationManager locationManager;
    static  String CurrentAddress="";

    Double locationLatitude = 0d;
    Double locationLongitude =0d;

    private int mInterval = 3000; // 3 seconds by default, can be changed later
    private Handler mHandler;
    ImageView back;
    Handler handler_;
    Runnable runnable_;
    private LatLng startPosition, endPisition;
    private float v;
    private GoogleMap mMap;
    LatLng sydney;
    static Double dLat=22.8891d;
    static Double dLon=88.4185d;
    IGoogleApi mService;
    static String showTotalTime="";
    private List<LatLng> polylineList;
    private Handler handler;
    private Polyline blackPolyline, greyPolyline;
    private PolylineOptions polylineOptions, blackPolylineOptions;
    private Marker marker;
    private double lat, lng;
    MapFragment mapFragment;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livetracking_rest);

        intent= new Intent(this, MyDriverServices.class);
        startService(intent);
        checkLocationPermission();
        getLocation();
        mapFragment= (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapssss);

        polylineList = new ArrayList<>();
        polylineList = new ArrayList<>();
        mService = Common.getGoogleApi();
        handler = new Handler();
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (CurrentDriverId.handler!=null && CurrentDriverId.runnable!=null)
                {
                    CurrentDriverId.handler.removeCallbacks(CurrentDriverId.runnable);
                }
                stopService(intent);
                finish();
            }
        });
//********
        handler_ = new Handler();


        runnable_ = new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    FileInputStream fin = openFileInput(getString(R.string.file_name));
                    int p;
                    String temp="";
                    while( (p = fin.read()) != -1)
                    {
                        temp = temp + Character.toString((char)p);
                    }
                    //string temp contains all the data of the file.
                    fin.close();

                    JSONObject jsonObject1=new JSONObject(temp);
                    JSONArray jsonArray=jsonObject1.getJSONArray("data");
                    JSONObject jsonObject2=jsonArray.getJSONObject(0);
                    try {
                        dLat = Double.parseDouble(jsonObject2.getString("d_lat"));
                        dLon = Double.parseDouble(jsonObject2.getString("d_long"));
                    }catch (Exception e){

                    }

                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                handler_.postDelayed(runnable_, 2000);
            }
        };
        handler_.post(runnable_);
//************

        //callLocation();
        callCurrentLocation();

    }



    @Override
    public void onMapReady(GoogleMap googleMap)
    {


        mMap = googleMap;
        Log.d("printall",dLat+":"+dLon);
        if (dLon != null && dLat != null) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setTrafficEnabled(false);
            mMap.setIndoorEnabled(false);
            mMap.setBuildingsEnabled(false);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            //final LatLng sydney = new LatLng(22.6213, 88.3984);

            sydney= new LatLng(dLat,dLon );
            // mMap.addMarker(new MarkerOptions().position(sydney).title("My Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                    .target(googleMap.getCameraPosition().target)
                    .zoom(14)
                    .bearing(30)
                    .tilt(45)
                    .build()));
            String requestUrl = null;
            try {
            /*requestUrl = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "mode=drivings&" +
                    "origin=" + sydney.latitude + "," + sydney.longitude + "&" +
                    "destination=" + destination + "&" +
                    "key=" + getString(R.string.google_maps_key);*/
                //requestUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=dum+dum+station,kolkata&destination=jaya+cinema+lake+town,kolkata&waypoints=via:indira+maidan+dum+dum&departure_time=now&key=AIzaSyBpI3ZQp-FROiguTAksLUe2s9Il4a-N3A8";
               // requestUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=dum+dum+station,kolkata&destination=jaya+cinema+lake+town,kolkata&waypoints=via:indira+maidan+dum+dum&departure_time=now&key=AIzaSyBpI3ZQp-FROiguTAksLUe2s9Il4a-N3A8";
                requestUrl = "https://maps.googleapis.com/maps/api/directions/json?origin="+dAddress+"m&destination="+rAddress+"&departure_time=now&key=AIzaSyBpI3ZQp-FROiguTAksLUe2s9Il4a-N3A8";
                Log.d("chkURLdata", "" + requestUrl);
                mService.getDataFromGoogleApi(requestUrl).enqueue(new Callback<String>()
                {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response)
                    {
                        Log.d("responsedata",""+call.toString());
                        JSONArray aa=null;
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            JSONArray jsonArray = jsonObject.getJSONArray("routes");
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject route = jsonArray.getJSONObject(i);
                                JSONObject poly = route.getJSONObject("overview_polyline");
                                aa=route.getJSONArray("legs");

                                JSONObject route2 = aa.getJSONObject(0);
                                JSONObject route3 = route2.getJSONObject("duration_in_traffic");
                                // JSONObject route4 = route3.getJSONObject("text");
                                Log.d("jsondddaaee","A "+route3.toString());
                                //Log.d("jsondddaaee","B "+route4.toString());
                                showTotalTime=route3.toString();
                                String polyline = poly.getString("points");  //duration_in_traffic
                                polylineList= decodePoly(polyline);
                                }


                            //Adjusting bounds
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            for (LatLng latLng : polylineList)
                                builder.include(latLng);
                            LatLngBounds bounds = builder.build();
                            CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
                            mMap.animateCamera(mCameraUpdate);

                            polylineOptions = new PolylineOptions();
                            polylineOptions.color(Color.GRAY);
                            polylineOptions.width(8);
                            polylineOptions.startCap(new SquareCap());
                            polylineOptions.endCap(new SquareCap());
                            polylineOptions.jointType(JointType.ROUND);
                            polylineOptions.addAll(polylineList);
                            greyPolyline = mMap.addPolyline(polylineOptions);


                            blackPolylineOptions = new PolylineOptions();
                            blackPolylineOptions.color(Color.GRAY);
                            blackPolylineOptions.width(5);
                            blackPolylineOptions.startCap(new SquareCap());
                            blackPolylineOptions.endCap(new SquareCap());
                            blackPolylineOptions.jointType(JointType.ROUND);
                            blackPolylineOptions.addAll(polylineList);
                            blackPolyline = mMap.addPolyline(blackPolylineOptions);

                            mMap.addMarker(new MarkerOptions().position(polylineList.get(polylineList.size() - 1)));

                            //Animator

                            ValueAnimator polylineAnimator = ValueAnimator.ofInt(0, 100);
                            polylineAnimator.setDuration(2000);
                            polylineAnimator.setInterpolator(new LinearInterpolator());
                            polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    List<LatLng> points = greyPolyline.getPoints();
                                    int percentValue = (int) animation.getAnimatedValue();
                                    int size = points.size();
                                    int newPoints = (int) (size * (percentValue / 100.0f));
                                    List<LatLng> p = points.subList(0, newPoints);
                                    blackPolyline.setPoints(p);

                                }
                            });
                            polylineAnimator.start();

                            //Add car marker

                            if (marker != null) {
                                marker.remove();
                            }


                            marker = mMap.addMarker(new MarkerOptions().position(sydney)
                                    .flat(true)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.new_bike)));
                            //Car Moving

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                /*if (index<polylineList.size()-1){
                                    index++;
                                    next=index+1;
                                }
                                if (index<polylineList.size()-1){
                                    startPosition=polylineList.get(index);
                                    endPisition=polylineList.get(next);
                                }*/

                               /* startPosition = polylineList.get(index);
                                endPisition = polylineList.get(next);*/

                                    startPosition = new LatLng(dLat,dLon);
                                    endPisition =new LatLng(dLat,dLon);

                                    ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
                                    valueAnimator.setDuration(3000);
                                    valueAnimator.setInterpolator(new LinearInterpolator());
                                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                        @Override
                                        public void onAnimationUpdate(ValueAnimator animation) {
                                            v = animation.getAnimatedFraction();
                                            lng = v * endPisition.longitude + (1 - v) * startPosition.longitude;
                                            lat = v * endPisition.latitude + (1 - v) * startPosition.latitude;

                                            //change latlon data

                                            Log.d("testingdatalatlon", ""+lat);


                                            LatLng newPos = new LatLng(lat, lng);
                                            marker.setPosition(newPos);
                                            marker.setAnchor(0.5f, 0.5f);
                                            // marker.setRotation(getBearing(startPosition, newPos));
                                            // marker.setRotation(getBearing(startPosition, newPos));
                                            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                                                    .target(newPos)
                                                    .zoom(12.5f)
                                                    .build())
                                            );

                                        }
                                    });
                                    valueAnimator.start();
                                    handler.postDelayed(this, 3000);


                                }
                            }, 3000);


                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                        Log.d("trableMSGdataCHK", "" + t.getMessage());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
    private List<LatLng> decodePoly(String encoded) {

        List poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p=new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(p);
        }

        return poly;
    }


    public boolean checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                         new AlertDialog.Builder(this)
                        .setTitle("Testing 1")
                        .setMessage("Testing 1")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(ActivityLivetrackingRest.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    void callCurrentLocation(){
        GPSTracker gpsTracker = new GPSTracker(this);

        if (gpsTracker.getIsGPSTrackingEnabled())
        {
            String stringLatitude = String.valueOf(gpsTracker.latitude);
            String stringLongitude = String.valueOf(gpsTracker.longitude);
            String country = gpsTracker.getCountryName(this);
            String city = gpsTracker.getLocality(this);
            String postalCode = gpsTracker.getPostalCode(this);
            String addressLine = gpsTracker.getAddressLine(this);

            Log.d("stringLongitude",stringLongitude+"\n"+country+"\n"+city+postalCode+addressLine);


        }
        else
        {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }
    }
//***Location
private void callLocation() {
    startRepeatingTask();
   /* //Alert Dialog
    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
            ActivityLivetrackingRest.this);

    // Setting Dialog Title
    alertDialog2.setTitle("Notification");

    // Setting Dialog Message
    String string1 = "Give it 10-15 seconds for your coordinates to update. Keep moving around and you will see coordinates update.";

    alertDialog2.setMessage(string1);

    // Setting Icon to Dialog
    alertDialog2.setIcon(R.drawable.ic_launcher_background);

    // Setting Positive "Yes" Btn
    alertDialog2.setPositiveButton("Continue",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });

    // Showing Alert Dialog
    alertDialog2.show();

    Handler handler2 = new Handler();
    handler2.postDelayed(new Runnable() {
        public void run() {
            mHandler = new Handler();
            startRepeatingTask();
        }
    }, 5000);   //5 seconds*/

    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

    }
}
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {



            try {
                getLocation(); //this function can change value of mInterval.
                Toast.makeText(getApplicationContext(), locationLongitude+"Trying to retrieve coordinates."+locationLatitude, Toast.LENGTH_LONG).show();

            } finally {

                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        //mStatusChecker.run();
        getLocation(); //this function can change value of mInterval.
        Toast.makeText(getApplicationContext(), locationLongitude+"Trying to retrieve coordinates."+locationLatitude, Toast.LENGTH_LONG).show();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, (LocationListener) this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {


        locationLatitude = location.getLatitude();
        locationLongitude = location.getLongitude();
        try
        {
            CurrentAddress=getFullAddress(locationLatitude,locationLongitude);
            CurrentAddress.replaceAll(" ","+");
            rAddress=CurrentAddress;
            setMapMarker();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Log.d("KolkatttLatlon",""+locationLatitude);
    }

    private void setMapMarker()
    {
        try {
            FileInputStream fin = openFileInput(getString(R.string.file_name));
            int p;
            String temp="";
            while( (p = fin.read()) != -1)
            {
                temp = temp + Character.toString((char)p);
            }
            //string temp contains all the data of the file.
            fin.close();
            JSONObject jsonObject1=new JSONObject(temp);
            JSONArray jsonArray=jsonObject1.getJSONArray("data");
            JSONObject jsonObject2=jsonArray.getJSONObject(0);
            try
            {
                dLat = Double.parseDouble(jsonObject2.getString("d_lat"));
                dLon = Double.parseDouble(jsonObject2.getString("d_long"));
                String deliveryboyAddress=getFullAddress(dLat,dLon);
                deliveryboyAddress.replaceAll(" ","+");
                dAddress=deliveryboyAddress;
                mapFragment.getMapAsync(this);

            }
            catch (Exception e)
            {
            }

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private String getFullAddress(Double locationLatitude, Double locationLongitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(locationLatitude, locationLongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
        return address;
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(ActivityLivetrackingRest.this, "Please Enable GPS", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
}

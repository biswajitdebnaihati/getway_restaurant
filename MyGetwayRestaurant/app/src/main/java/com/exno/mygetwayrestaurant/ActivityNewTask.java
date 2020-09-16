package com.exno.mygetwayrestaurant;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.exno.mygetwayrestaurant.database.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class ActivityNewTask extends AppCompatActivity {
 ArrayList<MyListData_task> new_task;
 LinearLayout holdform;
 EditText cancel_details;
 Button cancle_button_details;
 TextView time_set;
 String format="";
 ImageView close_pop_up;
 SharedPreferences pref;
 SharedPreferences.Editor editor;
 static String rid="-";
 DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.SESSION), 0);
        editor = pref.edit();
        if(!(new CheckInternet(this).getInternetStatus()))
        {

            startActivity(new Intent(this, ActivityNoActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        db= new DatabaseHandler(this);

        editor.putBoolean("hasTask",true);
        editor.apply();
        close_pop_up=(ImageView)findViewById(R.id.close_pop_up);
        new_task=new ArrayList<MyListData_task>();
        new_task.clear();
        init();
        Intent intent=getIntent();
        if (intent!=null)
        {


            Log.d("testingTTT","A "+intent.getStringExtra("a"));
            Log.d("testingTTT","B "+intent.getStringExtra("b"));
            //Log.d("testingTTT","C "+intent.getStringExtra("c"));


        }
        else {
            finish();
        }
        try {
            action();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        close_pop_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holdform.setVisibility(View.GONE);
            }
        });

    }

    private void action() throws JSONException {
        rid= pref.getString(getString(R.string.Rid), "-");
        if (rid.equals("-"))
        {
            finish();

        }
        getAllOrders(rid);
        cancle_button_details.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (StorageAll.orderid_store!=null)
                {
                    buildAlertMessageNoGps(StorageAll.orderid_store);
                }

            }
        });

        //startActivity(new Intent(ActivityNewTask.this,ActivityTaskAction.class).putExtra("id",id));
    }

    private void init()
    {
        holdform=(LinearLayout)findViewById(R.id.holdform);
        cancel_details=(EditText)findViewById(R.id.cancel_details);
        cancle_button_details=(Button) findViewById(R.id.cancle_button_details);
        time_set=(TextView)findViewById(R.id.time_set);
    }


    public class MyListData_task
    {
        String order_id;
        String no_of_product;
        String total_product_price;
        String delivered_time;
        String order_date;
        String to_status;
        String customer_id;
        public MyListData_task(String order_id, String no_of_product, String total_product_price, String delivered_time, String order_date, String to_status, String customer_id) {
            this.order_id = order_id;
            this.no_of_product = no_of_product;
            this.total_product_price = total_product_price;
            this.delivered_time = delivered_time;
            this.order_date = order_date;
            this.to_status = to_status;
            this.customer_id=customer_id;
        }

        public String getCustomer_id() {
            return customer_id;
        }

        public String getOrder_id()
        {
            return order_id;
        }

        public String getNo_of_product() {
            return no_of_product;
        }

        public String getTotal_product_price() {
            return total_product_price;
        }

        public String getDelivered_time() {
            return delivered_time;
        }

        public String getOrder_date() {
            return order_date;
        }

        public String getTo_status() {
            return to_status;
        }
    }
    public class MyListAdapter_task extends RecyclerView.Adapter<MyListAdapter_task.ViewHolder>{
        private ArrayList<MyListData_task> listdata;

        // RecyclerView recyclerView;
        public MyListAdapter_task(ArrayList<MyListData_task> listdata)
        {
            this.listdata = listdata;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.list_item_new_task, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position)
        {
            Date   oneWayTripDate =null;
            holder.orderid.setText(listdata.get(position).getOrder_id());
            holder.amt.setText(listdata.get(position).getTotal_product_price());

            holder.total_item.setText(listdata.get(position).getNo_of_product()+" Items");
            String date =listdata.get(position).getOrder_date();
            SimpleDateFormat input = new SimpleDateFormat("Y-m-d H:s");
            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy   H:ma");
            try {
                oneWayTripDate=input.parse(date);                 // parse input

            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.time.setText(output.format(oneWayTripDate));
            holder.hold_t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    holdform.setVisibility(View.VISIBLE);
                    StorageAll.orderid_store=listdata.get(position).getOrder_id();
                }
            });
            holder.confirm_t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    setTimeManually(listdata.get(position).getOrder_id());
                    editor.putString("customerIdclock",listdata.get(position).getCustomer_id()+"");
                    editor.apply();
                }
            });
            /*holder.linearlayout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    buildAlertMessageNoGps();
                    StorageData.order_id=listdata.get(position).getOrder_id();
                    // Toast.makeText(view.getContext(),"click on item: "+myListData.getDescription(),Toast.LENGTH_LONG).show();
                   // startActivity(new Intent(ActivityNewTask.this,ActivityTaskAction.class).putExtra("id",listdata.get(position).getId()));
                }
            });*/
        }


        @Override
        public int getItemCount()
        {
            return listdata.size();
        }

        public  class ViewHolder extends RecyclerView.ViewHolder
        {
            public ImageView imageView;
            public TextView orderid,total_item,time,amt,hold_t,confirm_t;
            public LinearLayout linearlayout;
            public ViewHolder(View itemView)
            {
                super(itemView);
                this.orderid = (TextView) itemView.findViewById(R.id.orderid);
                this.amt = (TextView) itemView.findViewById(R.id.amt);
                this.confirm_t = (TextView) itemView.findViewById(R.id.confirm_t);
                this.hold_t = (TextView) itemView.findViewById(R.id.hold_t);
                this.total_item = (TextView) itemView.findViewById(R.id.total_item);
                this.time = (TextView) itemView.findViewById(R.id.time);
                linearlayout = (LinearLayout)itemView.findViewById(R.id.container_header);
            }
        }
    }

    private void buildAlertMessageNoGps(final String order_id)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure want to request for cancel ?")
                .setTitle("Cancel Order")

                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(final DialogInterface dialog, final int id)
                    {
                       // startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                        try {
                            setCancelRequiest(order_id);
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    public void onClick(final DialogInterface dialog, final int id)
                    {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    void setTimeManually(final String Order_id)
    {
        Calendar mcurrentTime = Calendar.getInstance();

        //int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int hour =0;
        int minute=0;
        //int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                {
                    timePicker.setAccessibilityHeading(true);
                }
                // pptime.setText( selectedHour + ":" + selectedMinute);
                long h=selectedHour*60*60;
                long m=selectedMinute*60;
                long second=h+m;

                String timeSet=""+selectedHour + ":" + selectedMinute;
                time_set.setText(timeSet);

                Date date = new Date();
                long timeMilli = date.getTime();
                long sumSecond=timeMilli+second;

                Log.d("AASSWWWW","A "+timeMilli);
                Log.d("AASSWWWW","B "+second);
                Log.d("AASSWWWW","C "+sumSecond);
                buildAlertMessageNoConfirm(selectedHour+" h : "+selectedMinute+" m "+"",Order_id);


            }
        }
        , hour, minute, true);//Yes 24 hour time



    mTimePicker.setTitle("Select Time");

        mTimePicker.show();
    }


    void getAllOrders(String orderId) throws JSONException
    {


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rid", orderId);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url=getString(R.string.baseurl)+"getnew_order_restaurant";
        Log.d("chkdataAPIdata","Url "+url);
        Log.d("chkdataAPIdata","callJson "+jsonObject);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST,url, jsonObject, new com.android.volley.Response.Listener<JSONObject>()
                {

                    @Override
                    public void onResponse(JSONObject response)
                    {

                        new_task.clear();
                        Log.d("chkdataAPIdataTEST","ALL CALL "+response.toString());
                        try {
                            if (response.getInt("status")==1)
                            {
                                JSONArray jsonArray=response.getJSONArray("data");
                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject1= jsonArray.getJSONObject(i);
                                    new_task.add(new MyListData_task(
                                                    jsonObject1.getString("order_id"),
                                                    jsonObject1.getString("no_of_product"),
                                                    jsonObject1.getString("total_product_price"),
                                                    jsonObject1.getString("delivered_time"),
                                                    jsonObject1.getString("order_date"),
                                                    jsonObject1.getString("to_status"),
                                                    jsonObject1.getString("order_cus_id")


                                            )
                                    );



                                }
                            }
                            else {
                                finish();
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }


                        RecyclerView recyclerView_on_process = (RecyclerView) findViewById(R.id.recyclerView_new_task);
                        MyListAdapter_task adapter_bed = new MyListAdapter_task(new_task);
                        recyclerView_on_process.setHasFixedSize(true);
                        recyclerView_on_process.setLayoutManager(new LinearLayoutManager(ActivityNewTask.this));
                        recyclerView_on_process.setAdapter(adapter_bed);
                        recyclerView_on_process.scrollToPosition(0);

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
    void setConfirm(String time,final String order_id) throws JSONException
    {


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("order_id", order_id);


        jsonObject.put("to_status", "1");
        jsonObject.put("processing_time", time);
        jsonObject.put("user_id", rid);
        jsonObject.put("user_type", "restaurant");
        jsonObject.put("note", "");




        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.d("abcddataLOgData","CALL JSON "+jsonObject.toString());
        String url=getString(R.string.baseurl)+"order_confirm";
        Log.d("abcddataLOgData","URL "+url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST,url, jsonObject, new com.android.volley.Response.Listener<JSONObject>()
                {

                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            if (response.getInt("status")==1)
                            {
                                Toasty.success(ActivityNewTask.this,"Order Confirm",Toasty.LENGTH_LONG).show();
                                editor.putString("orderid_onprocess",order_id);
                                editor.putBoolean("hasTask",true);
                                editor.apply();
                                getDeliveryBoy();
                            }
                            else
                                {
                                    Toasty.warning(ActivityNewTask.this,"Invalid",Toasty.LENGTH_LONG).show();
                                }
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        Log.d("abcddataLOgData","JSON "+response.toString());
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
    void setCancelRequiest(String order_id) throws JSONException
    {



        JSONObject jsonObject = new JSONObject();
        jsonObject.put("order_id", order_id);


        jsonObject.put("to_status", "9");
        jsonObject.put("processing_time", "");
        jsonObject.put("user_id", rid);
        jsonObject.put("user_type", "restaurant");
        jsonObject.put("note", cancel_details.getText().toString());




        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.d("abcddataLOgData","CALL JSON "+jsonObject.toString());
        String url=getString(R.string.baseurl)+"order_confirm";
        Log.d("abcddataLOgData","URL "+url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST,url, jsonObject, new com.android.volley.Response.Listener<JSONObject>()
                {

                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            if (response.getInt("status")==1)
                            {
                                Toasty.success(ActivityNewTask.this,"Order cancel request send",Toasty.LENGTH_LONG).show();
                                finish();
                            }
                            else
                            {
                                Toasty.warning(ActivityNewTask.this,"Invalid",Toasty.LENGTH_LONG).show();
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        Log.d("abcddataLOgData","JSON "+response.toString());
                    }
                }, new com.android.volley.Response.ErrorListener()
                {

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();

                    }
                });

        if (cancel_details.getText().toString().length()>3) {
            requestQueue.add(jsonObjectRequest);
        }
        else
            {
                cancel_details.setError("Reason for cancellation");
                Toasty.info(ActivityNewTask.this,"Reason for cancellation",Toasty.LENGTH_LONG).show();
            }
    }



    private void buildAlertMessageNoConfirm(final  String  sumSecond,final  String Order_id)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure want to Confirm?")
                .setTitle("Confirm order with timing "+sumSecond)

                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(final DialogInterface dialog, final int id)
                    {
                        // startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                       // setCancelRequiest();
                        try {
                            setConfirm(sumSecond+"",Order_id);
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    public void onClick(final DialogInterface dialog, final int id)
                    {

                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    void getDeliveryBoy() throws JSONException
    {
        db.getRemoveAll();



        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rid", rid);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.d("abcddataLOgDataD","CALL A JSON "+jsonObject.toString());
        String url=getString(R.string.baseurl)+"deliveryboy_search_for_workassign";
        Log.d("abcddataLOgDataD","URL "+url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST,url, jsonObject, new com.android.volley.Response.Listener<JSONObject>()
                {

                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("abcddataLOgDataD","Result A "+response.toString());


                        try {
                            if (response.getInt("status")==1)
                            {
                                JSONArray jsonArray=response.getJSONArray("data");
                                for (int y=0;y<jsonArray.length();y++)
                                {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(y);
                                    //list_of_de.add(jsonObject1.getString("did"));
                                    db.addDid(jsonObject1.getString("did"));
                                    Log.d("printalldataAbcdCall","\tDriverId\t"+jsonObject1.getString("did"));

                                }
                                finish();
//300000

                            }
                            else
                            {

                                finish();
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        startActivity(new Intent(ActivityNewTask.this,ActivityDashBoard.class));
                        finish();
                        Log.d("abcddataLOgData","JSON "+response.toString());
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

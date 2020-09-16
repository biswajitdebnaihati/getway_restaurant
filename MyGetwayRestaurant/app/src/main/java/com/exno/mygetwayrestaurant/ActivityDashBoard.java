package com.exno.mygetwayrestaurant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.exno.mygetwayrestaurant.database.DatabaseHandler;
import com.exno.mygetwayrestaurant.database.DatabaseHandler2;
import com.exno.mygetwayrestaurant.database.didStore;
import com.exno.mygetwayrestaurant.model.Testingdata;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.RequestBody;
import retrofit2.Call;

public class ActivityDashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    boolean doubleBackToExitPressedOnce = false;
    TextView r_name_header;
    TextView autho_id;
    RelativeLayout container_driver_details;
    int width = 0;
    int height = 0;
    RelativeLayout.LayoutParams lp,lp2;
    TextView showmore;
    DatabaseHandler db;
    static  DatabaseHandler2 db2;
    private static final String TAG = MainActivity.class.getSimpleName();
    RecyclerView recyclerView_on_process;
    RequestBody bodyRequest;
    Call<Testingdata> cell = null;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    LinearLayout detals;
    static TextView cown_down_new;
    ArrayList<MyListData_time> onprocess;
    CountDownTimer countDownTimer = null;
    static Button rest_btn;
    static String rid = "-";
    static String on_off_status = "-";
    static String token = "";
    static String order_id_on_ = "_";
    static ArrayList<String> list_of_de;
    static ArrayList<String> list_of_de_reject;
    static int count = 0;
    ArrayList<MyListData_task> new_task;
    ImageView did_loader;

    static RelativeLayout container_noti;

    static TextView de_name,de_reg,vehicle_no,contact_no,email_id,order_id;
    static  MyListAdapter_task adapter_bed;
    JSONObject jsonObject=null;
    JSONArray jsonArray=new JSONArray();

    JSONObject jb;


    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_container);
        if(!(new CheckInternet(this).getInternetStatus()))
        {

            startActivity(new Intent(this, ActivityNoActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }

        checkLocationPermission();
        r_name_header=(TextView)findViewById(R.id.r_name_header);
        autho_id=(TextView)findViewById(R.id.autho_id);
        pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.SESSION), 0);
        editor = pref.edit();
        rid = pref.getString(getString(R.string.Rid), "-");
        String rname = pref.getString(getString(R.string.Rname), "-");
        String authoId = pref.getString(getString(R.string.Res_auth_id), "-");
        if (!(rname.equals("-")))
        r_name_header.setText(rname);
        if (!(authoId.equals("-")))
        autho_id.setText("Authentication id "+authoId);
        jsonObject=new JSONObject();
        jsonArray=new JSONArray();
        jb=new JSONObject();
        new_task = new ArrayList<MyListData_task>();


        did_loader = (ImageView) findViewById(R.id.did_loader);

        container_noti = (RelativeLayout) findViewById(R.id.container_noti);
        db = new DatabaseHandler(this);
        db2 = new DatabaseHandler2(this);


        de_name=(TextView) findViewById(R.id.de_name);
        de_reg=(TextView) findViewById(R.id.de_reg);
        order_id=(TextView) findViewById(R.id.order_id);
        vehicle_no=(TextView) findViewById(R.id.vehicle_no);
        contact_no=(TextView) findViewById(R.id.contact_no);
        email_id=(TextView) findViewById(R.id.email_id);
        //ImageLoader imageLoader = ImageLoader.getInstance();
        list_of_de_reject = new ArrayList<String>();
        list_of_de_reject.clear();

        try
        {
            getDeDetails("");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


        detals = (LinearLayout) findViewById(R.id.detals);
        onprocess = new ArrayList<MyListData_time>();
        onprocess.clear();
        init();
        cown_down_new = (TextView) findViewById(R.id.cown_down_new);
        //startActivity(new Intent(ActivityDashBoard.this, ActivityDrawer.class));
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nname);
        TextView authi_id = (TextView) headerView.findViewById(R.id.authi_id);
        navUsername.setText(rname);
        authi_id.setText("Authentication id "+authoId);
        Menu nav_Menu = navigationView.getMenu();
        hidemenu(nav_Menu);
        try
        {
            callOnProcess();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        detals.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                detals.setVisibility(View.GONE);
            }
        });
        container_noti.setVisibility(View.GONE);
        try {
            action();
        }
        catch (Exception e)
        {
          //  Toast.makeText(this, "ERROR 1 " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Glide.with(ActivityDashBoard.this)
                .load(R.raw.tenor)

                .placeholder(R.raw.boy_loader)
                .error(R.raw.boy_loader)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(did_loader);

        if (pref.getBoolean("isSetDriver",false)==true)
        {
            /*de_name.setText("Driver Name"+pref.getString("de_name","-"));
            de_reg.setText("Driver Reg. "+pref.getString("reg_id","-"));
            vehicle_no.setText("Driver vehicle no "+pref.getString("vehicle_no","-"));
            contact_no.setText("Contact no "+pref.getString("contact_no","-"));
            email_id.setText("Email Id "+pref.getString("email_id","-"));
            order_id.setText("Order Id "+pref.getString("orderid","-"));*/
            container_noti.setVisibility(View.VISIBLE);

        }
        else
            {
                container_noti.setVisibility(View.GONE);
            }


        callCurrentDe();

    }



    private void callCurrentDe()
    {

        List<didStore> allAccessDList=db2.getAllDid();
        if (allAccessDList.size()>0)
        {
            StorageAll.driverList.clear();
          //  Toast.makeText(this, "R SIZE : "+allAccessDList.size(), Toast.LENGTH_SHORT).show();
            Log.d("Sizeprint",""+allAccessDList.size());
            for (int y = 0; y < allAccessDList.size(); y++)
            {
                String[] separated = allAccessDList.get(y).getStatus().split("~");
                Log.d("alldataDb", "DB\n" + allAccessDList.get(y).getStatus() + "\n" + allAccessDList.get(y).getDid()+" SIZE "+separated.length);
                String drive_name=separated[0];
                String reg_id=separated[1];
                String vehicle_no=separated[2];
                String contact_no=separated[3];
                String email_id=separated[4];
                String a=separated[5];
                String de_id_data="";
                try {
                    de_id_data = separated[6];
                }catch (Exception e){

                }
                StorageAll.driverList.add(new DriverList
                        (
                        drive_name,
                        reg_id,
                        vehicle_no,
                        contact_no,
                        email_id,
                        a,
                        de_id_data


                ));
            }
            ActiveDriver(1);
        }
        else
            {
                ActiveDriver(0);
            }
    }

    @SuppressLint("ResourceType")
    private void action() throws JSONException {



        on_off_status = pref.getString(getString(R.string.on_off_status), "-");
        if (rid.equals("-"))
        {
            finish();
            //********1
        } else {
            getDashBoard(rid);
        }
        Log.d("Onoffstatus", "" + on_off_status);
        if (on_off_status.trim().equals("1")) {
            rest_btn.setBackgroundColor(Color.parseColor("#a4f09e"));
            rest_btn.setText("Close Restaurant");

        } else {
            rest_btn.setBackgroundColor(Color.parseColor("#f35d3f"));
            rest_btn.setText("Open Restaurant");
        }
        rest_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                try {
                    getRestOpenStatus();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Log.d("asweeeeeeeee", "" + pref.getBoolean("hasTask", false));
        Log.d("asweeeeeeeee", "" + pref.getString("orderid_onprocess", "-"));
        if (pref.getBoolean("hasTask", false) == true) {
            //searching11
            order_id_on_ = pref.getString("orderid_onprocess", "-");
            int secondsDelayed = 300;
            if (!(order_id_on_.equals("-"))) {
                if (StorageAll.desearch_img != null) {
                    StorageAll.desearch_img.setVisibility(View.VISIBLE);
                }
                new Handler().postDelayed(new Runnable()
                {
                    public void run()
                    {

                        List<didStore> aa = db.getDid();
                        for (int y = 0; y < aa.size(); y++)
                        {
                            try
                            {

                                callDe(aa.get(y).getDid());
                                editor.putBoolean("hasTask", false);
                                editor.apply();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("printallaaDSDS", "NNM A : " + aa.get(y).getDid() + " B : " + aa.get(y).getStatus());
                          //  Toast.makeText(ActivityDashBoard.this, "Ok", Toast.LENGTH_SHORT).show();

                        }


                    }
                }, secondsDelayed * 1000);
            }
        }
        Intent i = getIntent();
        if (i != null) {
            if (i.getStringExtra("c") != null) {
                Log.d("RRRRRRRRRRRRRR", " C " + i.getStringExtra("c"));

                if (i.getStringExtra("d") != null) {

                    if (i.getStringExtra("d").trim().equals("0")) {
                        container_noti.setVisibility(View.GONE);
                        did_loader.setVisibility(View.VISIBLE);
                        if (StorageAll.desearch_img != null)
                        {
                            Glide.with(ActivityDashBoard.this)
                                    .load(R.raw.tenor)
                                    .placeholder(R.raw.tenor)
                                    .error(R.raw.tenor)
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .into(StorageAll.desearch_img);
                            StorageAll.desearch_img.setVisibility(View.VISIBLE);
                            Log.d("RRRRRRRRRRRRRR", "SHOW ");
                        }
                        List<didStore> aa = db.getDid();
                        Log.d("printallaaDSDS", "NN A SIZE : " + aa.size());
                        for (int y = 0; y < aa.size(); y++)
                        {
                            // Toast.makeText(this, db.getRemove(aa.get(y).getDid())+"\nOK "+i.getStringExtra("c"), Toast.LENGTH_SHORT).show();
                            db.getRemove(aa.get(y).getDid());
                            calldeAgain();
                            Log.d("printallaaDSDS", "NN A : " + aa.get(y).getDid() + " B : " + aa.get(y).getStatus());

                        }
                    } else {

                        container_noti.setVisibility(View.VISIBLE);
                        did_loader.setVisibility(View.GONE);
                       // getDeDetails(i.getStringExtra("c"));
                        db.getRemoveAll();
                        if (StorageAll.desearch_img != null)
                        {
                            StorageAll.desearch_img.setVisibility(View.GONE);
                            Log.d("RRRRRRRRRRRRRR", "SHOW ");
                        }
                        Log.d("extradataaaaa",""+i.getStringExtra("drive_name"));
                        String orderid=i.getStringExtra("a").trim();

                        String drive_name=i.getStringExtra("drive_name");
                        String reg_id=i.getStringExtra("reg_id");
                        String vehicle_no_s=i.getStringExtra("vehicle_no");
                        String contact_no_s=i.getStringExtra("contact_no");
                        String email_id_s=i.getStringExtra("email_id");
                        String d_id_s=i.getStringExtra("c");
                        int getPosition=-1;
                        Log.d("printlong","ALL \t"+new_task.size());
                        for (int j=0;j<new_task.size();j++)
                        {

                            if (new_task.get(j).getOrder_id().trim().equals(orderid))
                            {
                                Log.d("printlong","TRUE\t"+j);
                                getPosition=j;

                            }

                        }

                        long l=db2.addDid(orderid,""
                                +drive_name+"~"
                                +reg_id+"~"
                                +vehicle_no_s+"~"
                                +contact_no_s+"~"
                                +email_id_s+"~"
                                +orderid+"~"+d_id_s
                        );

                        // +i.getStringExtra("email_id")+"~"







                        /*editor.putString("de_name",i.getStringExtra("de_name"));
                        editor.putString("reg_id",i.getStringExtra("reg_id"));
                        editor.putString("vehicle_no",i.getStringExtra("vehicle_no"));
                        editor.putString("contact_no",i.getStringExtra("contact_no"));
                        editor.putString("email_id",i.getStringExtra("email_id"));
                        editor.putString("orderid",i.getStringExtra("a"));
                        String json1="~";
                        if (!(pref.getString("driver_json","-").equals("-"))){
                            json1=pref.getString("driver_json","-");
                            }
                        editor.putBoolean("isSetDriver",true);
                        editor.apply();
                        editor.putString("driver_json","~"+jsonObject.toString()+"~"+json1);
                        editor.apply();

                        Log.d("extradataaaaa","CC J:  "+pref.getString("driver_json","~"));


                        de_name.setText(i.getStringExtra("drive_name"));
                        de_reg.setText(i.getStringExtra("reg_id"));
                        vehicle_no.setText(i.getStringExtra("vehicle_no"));
                        contact_no.setText(i.getStringExtra("contact_no"));
                        email_id.setText(i.getStringExtra("email_id"));*/





                    }
                    Log.d("RRRRRRRRRRRRRR", "D " + i.getStringExtra("d"));
                }


            }
        }
        else {
            container_noti.setVisibility(View.GONE);
        }


    }

    private void ActiveDriver(int y)
    {

        RecyclerView recyclerView_on_process = (RecyclerView) findViewById(R.id.recyclerView_driver_list);
        MyListAdapter_driver_list adapter_bed = new MyListAdapter_driver_list(StorageAll.driverList);
        recyclerView_on_process.setHasFixedSize(true);
        recyclerView_on_process.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_on_process.setAdapter(adapter_bed);
        recyclerView_on_process.scrollToPosition(0);
        adapter_bed.notifyDataSetChanged();
        if (y==0)
        {
            recyclerView_on_process.setVisibility(View.GONE);
        }
        else
            {
                recyclerView_on_process.setVisibility(View.VISIBLE);
                container_noti.setVisibility(View.VISIBLE);
            }
    }

    private void calldeAgain() throws JSONException {
        List<didStore> aa = db.getDid();
        Log.d("printallaaDSDS", "NN A SIZE : " + aa.size());
        for (int y = 0; y < aa.size(); y++) {
            // Toast.makeText(this, db.getRemove(aa.get(y).getDid())+"\nOK "+i.getStringExtra("c"), Toast.LENGTH_SHORT).show();
            callDe(aa.get(y).getDid());
            Log.d("chkdataabcd", "C : " + aa.get(y).getDid());
            //Log.d("chkdataabcd","B : "+aa.get(y).getDid());
            Log.d("printallaaDSDS", "NN A : " + aa.get(y).getDid() + " B : " + aa.get(y).getStatus());
        }

    }

    private void init() {
        rest_btn = (Button) findViewById(R.id.rest_btn);
        StorageAll.desearch_img = (ImageView) findViewById(R.id.desearch_img);
    }

    private void callOnProcess() throws JSONException {
        getAllOrders();
        /*//onprocess.add(new MyListData_time("Ord0001", "1579101702949"));
        onprocess.add(new MyListData_time("Ord0001", "1579252494422"));
        onprocess.add(new MyListData_time("Ord0002","00000000"));
        RecyclerView recyclerView_on_process = (RecyclerView) findViewById(R.id.recyclerView_on_process);
        MyListAdapter_bed adapter_bed = new MyListAdapter_bed(onprocess);
        recyclerView_on_process.setHasFixedSize(true);
        recyclerView_on_process.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_on_process.setAdapter(adapter_bed);
        recyclerView_on_process.scrollToPosition(0);*/
    }

    private void hidemenu(Menu nav_Menu)
    {


    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.logout) {
            editor.putBoolean("isLogin", false);
            editor.apply();
            startActivity(new Intent(ActivityDashBoard.this, LoginActivity.class));
            finish();
        } else if (id == R.id.change_pass) {
            startActivity(new Intent(ActivityDashBoard.this, ActivityNewPassword.class));
            finish();
        }
        else if (id == R.id.setlocation) {
            startActivity(new Intent(ActivityDashBoard.this, ActivityPickLocation.class));
            finish();
        }
         /*else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class MyListData_time {
        private String description;

        private String time;

        public MyListData_time(String description, String time) {
            this.description = description;

            this.time = time;
        }


        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }


        public String getTime() {
            return time;
        }
    }

    public class MyListAdapter_bed extends RecyclerView.Adapter<MyListAdapter_bed.ViewHolder> {
        private ArrayList<MyListData_time> listdata;

        // RecyclerView recyclerView;
        public MyListAdapter_bed(ArrayList<MyListData_time> listdata) {
            this.listdata = listdata;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.list_item_on_process, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            try {
                Long l = Long.parseLong(listdata.get(position).getTime());
                CountDownTimer countDownTimer = null;
                //callCowndown(holder.cowndown, l,countDownTimer,holder.btn1);

            } catch (Exception e) {
            }
            holder.textView.setText(listdata.get(position).getDescription());

            holder.relativeLayout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    detals.setVisibility(View.VISIBLE);
                    caccData(listdata.get(position).getTime());
                    // Toast.makeText(view.getContext(),"click on item: "+myListData.getDescription(),Toast.LENGTH_LONG).show();
                }
            });
        }


        @Override
        public int getItemCount() {
            return listdata.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView textView;
            public TextView cowndown;

            public TextView btn1;
            public LinearLayout relativeLayout;

            public ViewHolder(View itemView) {
                super(itemView);

                this.textView = (TextView) itemView.findViewById(R.id.textView);

                this.btn1 = (TextView) itemView.findViewById(R.id.btn1);
                this.cowndown = (TextView) itemView.findViewById(R.id.cowndown);
                relativeLayout = (LinearLayout) itemView.findViewById(R.id.relativeLayout);
            }
        }
    }

    private void caccData(String time) {
        /*String h="00:15:00";
        String[] h1=h.split(":");

        int hour=Integer.parseInt(h1[0]);
        int minute=Integer.parseInt(h1[1]);
        int second=Integer.parseInt(h1[2]);

        int temp;
        temp = second + (60 * minute) + (3600 * hour);
        Date date = new Date();
        long timeMilli = date.getTime();*/
        try {

            Date date = new Date();
            long timeMilli = date.getTime();
            long l2 = Long.parseLong(time);
            long l3 = timeMilli - l2;
            Log.d("testingtimedata", "A : " + timeMilli);
            Log.d("testingtimedata", "B : " + l2);
            Log.d("testingtimedata", "C : " + l3);
            if ((l3) > 0)

                callCowndown1(cown_down_new, (l3), countDownTimer, detals);
            else

                Log.d("chkdata", "negative");

        } catch (Exception e) {

        }

        // Log.d("chkmilisecond",""+(1579095565132l-1579095422439l));
    }

    void callCowndown(final TextView textView, final Long l, CountDownTimer countDownTimer, TextView b1)
    {
        countDownTimer = new CountDownTimer(l, 1000) {

            public void onTick(final long millisUntilFinished) {


                textView.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        long seconds = (millisUntilFinished / 1000);
                        long p1 = seconds % 60;
                        long p2 = seconds / 60;
                        long p3 = p2 % 60;
                        p2 = p2 / 60;

                        textView.setText("" + p2 + ":" + p3 + ":" + p1);
                    }
                }, 100);

            }

            public void onFinish() {
                textView.setText("Time Over");
            }
        }.start();
        final CountDownTimer finalCountDownTimer = countDownTimer;
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalCountDownTimer.cancel();
            }
        });

    }


    void callCowndown1(final TextView textView, final Long l, CountDownTimer countDownTimer, final LinearLayout b1) {
        countDownTimer = new CountDownTimer(l, 1000) {

            public void onTick(final long millisUntilFinished) {


                textView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        long seconds = (millisUntilFinished / 1000);
                        long p1 = seconds % 60;
                        long p2 = seconds / 60;
                        long p3 = p2 % 60;
                        p2 = p2 / 60;

                        textView.setText("" + p2 + ":" + p3 + ":" + p1);
                    }
                }, 100);

            }

            public void onFinish() {
                textView.setText("Time Over");
            }
        }.start();
        final CountDownTimer finalCountDownTimer = countDownTimer;
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalCountDownTimer.cancel();
                b1.setVisibility(View.GONE);
            }
        });

    }

    void getRestOpenStatus() throws JSONException {


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            token = "";

                        }
                        token = task.getResult().getToken();
                        Log.d(TAG, token);
                        Log.d("printtokek",""+token);

                    }
                });

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rid", rid);
        on_off_status = pref.getString(getString(R.string.on_off_status), "-");
        if (on_off_status.trim().equals("1")) {
            jsonObject.put("status", "0");
            jsonObject.put("device_token", "");
            editor.putString(getString(R.string.on_off_status), "0");
            editor.apply();
        } else {
            jsonObject.put("status", "1");
            jsonObject.put("device_token", token);
            editor.putString(getString(R.string.on_off_status), "1");
            editor.apply();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.d("abcddataLOgData", "CALL JSON " + jsonObject.toString());
        String url = getString(R.string.baseurl) + "restaurant_on_off";
        Log.d("abcddataLOgData", "URL " + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("abcddataLOgData", "Result " + response.toString());
                        try {
                            if (response.getInt("status") == 1) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                if (jsonObject1.getString("status").trim().equals("1")) {
                                    rest_btn.setBackgroundColor(Color.parseColor("#a4f09e"));
                                    rest_btn.setText("Close Restaurant");

                                } else {
                                    rest_btn.setBackgroundColor(Color.parseColor("#f35d3f"));
                                    rest_btn.setText("Open Restaurant");
                                }

                            } else {
                                rest_btn.setBackgroundColor(Color.parseColor("#f35d3f"));
                                rest_btn.setText("Open Restaurant");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("abcddataLOgData", "JSON " + response.toString());
                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                });
        requestQueue.add(jsonObjectRequest);

    }


    private void callDe(String co) throws JSONException {

        String cid = pref.getString("customerIdclock", "-");
        if (order_id_on_.trim().equals("-")) {
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("driver_id", co);
        jsonObject.put("rest_id", rid);
        jsonObject.put("cus_id", cid);
        jsonObject.put("order_id", order_id_on_);
        jsonObject.put("status", "1");

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.d("chklogindataWW", "TASK BOOK : AAAB" + jsonObject.toString());

        String url = getString(R.string.baseurl) + "deliveryboy_workassign";
        Log.d("dattttttAbcdWW", "TASK URL " + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("dattttttAbcdWW", "TASK " + response.toString());
                        //callAsignorNot();

                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void callAsignorNot() {

    }

    void getAllOrders() throws JSONException {


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rid", rid);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = getString(R.string.baseurl) + "all_accepted_new_order_restaurant";
        Log.d("chkdataAPIdata", "Url " + url);
        Log.d("chkdataAPIdata", "callJson " + jsonObject);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        new_task.clear();
                        Log.d("chkdataAPIdataonprocess", "ALL CALL *" + response.toString());
                        try {
                            if (response.getInt("status") == 1) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    if (!(jsonObject1.getString("to_status").equals("4") || jsonObject1.getString("to_status").equals("5") || jsonObject1.getString("to_status").equals("6") || jsonObject1.getString("to_status").equals("7")  || jsonObject1.getString("to_status").equals("8")  || jsonObject1.getString("to_status").equals("9") ))
                                    {
                                        new_task.add(new MyListData_task
                                                (
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
                                StorageAll.desearch_img.setVisibility(View.GONE);
                            } else {
                                Glide.with(ActivityDashBoard.this)
                                        .load(R.drawable.rest_back)
                                        .placeholder(R.drawable.rest_back)
                                        .error(R.drawable.rest_back)
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .into(StorageAll.desearch_img);
                                StorageAll.desearch_img.setVisibility(View.VISIBLE);
                                //finish();
                                //*******2
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        recyclerView_on_process = (RecyclerView) findViewById(R.id.recyclerView_on_process);
                        if (new_task.size() > 0) {
                            recyclerView_on_process.setVisibility(View.VISIBLE);
                            adapter_bed= new MyListAdapter_task(new_task);
                            recyclerView_on_process.setHasFixedSize(true);
                            recyclerView_on_process.setLayoutManager(new LinearLayoutManager(ActivityDashBoard.this));
                            recyclerView_on_process.setAdapter(adapter_bed);
                            recyclerView_on_process.scrollToPosition(0);
                        } else {
                            recyclerView_on_process.setVisibility(View.GONE);

                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                });
        if (rid.equals("-")) {
        //    Toast.makeText(this, "Error 2", Toast.LENGTH_SHORT).show();
        } else {
            requestQueue.add(jsonObjectRequest);
        }
    }


    public class MyListAdapter_task extends RecyclerView.Adapter<MyListAdapter_task.ViewHolder> {
        private ArrayList<MyListData_task> listdata;

        // RecyclerView recyclerView;
        public MyListAdapter_task(ArrayList<MyListData_task> listdata) {
            this.listdata = listdata;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.list_item_new_task2, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Date oneWayTripDate = null;
            holder.orderid.setText(listdata.get(position).getOrder_id());
            holder.amt.setText(listdata.get(position).getTotal_product_price());

            holder.total_item.setText(listdata.get(position).getNo_of_product() + " Items");
            holder.set_time.setText("Processing Time " + listdata.get(position).getDelivered_time());
            String date = listdata.get(position).getOrder_date();
            holder.pick_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        getFoodReady(listdata.get(position).getOrder_id());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            /*SimpleDateFormat input = new SimpleDateFormat("Y-m-d H:s");
            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy   H:ma");
            try {
                oneWayTripDate=input.parse(date);                 // parse input

            } catch (ParseException e) {
                e.printStackTrace();
            }*/
            /*holder.time.setText(output.format(oneWayTripDate));
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
            });*/
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
        public int getItemCount() {
            return listdata.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView orderid, total_item, time, amt, hold_t, confirm_t, set_time, pick_up;
            public LinearLayout linearlayout;

            public ViewHolder(View itemView) {
                super(itemView);
                this.orderid = (TextView) itemView.findViewById(R.id.orderid);
                this.amt = (TextView) itemView.findViewById(R.id.amt);
                this.pick_up = (TextView) itemView.findViewById(R.id.pick_up);
                this.set_time = (TextView) itemView.findViewById(R.id.set_time);
                this.total_item = (TextView) itemView.findViewById(R.id.total_item);
                this.time = (TextView) itemView.findViewById(R.id.time);
                linearlayout = (LinearLayout) itemView.findViewById(R.id.container_header);
            }
        }
    }

    public class MyListData_task {
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
            this.customer_id = customer_id;
        }

        public String getCustomer_id() {
            return customer_id;
        }

        public String getOrder_id() {
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

    void getDashBoard(String rid) throws JSONException {


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rid", rid);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = getString(R.string.baseurl) + "dashboard_details";
        Log.d("chkdataAPIdata", "Url " + url);
        Log.d("chkdataAPIdata", "callJson " + jsonObject);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt("status") == 1) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                TextView total_earnint_t = (TextView) findViewById(R.id.total_earnint_t);
                                TextView total_due_t = (TextView) findViewById(R.id.total_due_t);
                                TextView t_cancel_t = (TextView) findViewById(R.id.t_cancel_t);
                                TextView t_order_t = (TextView) findViewById(R.id.t_order_t);
                                TextView tdelivert_t = (TextView) findViewById(R.id.tdelivert_t);
                                total_earnint_t.setText("₹ " + jsonObject1.getString("total_earning"));
                                total_due_t.setText("₹ " + jsonObject1.getString("total_due"));
                                t_cancel_t.setText(jsonObject1.getString("total_cancell"));
                                t_order_t.setText(jsonObject1.getString("total_order"));
                                tdelivert_t.setText(jsonObject1.getString("total_delivered"));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    void getFoodReady2(String order_id_on_) throws JSONException {


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("order_id", order_id_on_);
        jsonObject.put("to_status", "4");
        jsonObject.put("user_id", rid);
        jsonObject.put("user_type", "restaurant");
        jsonObject.put("note", "");

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = getString(R.string.baseurl_second) + "order_status_update";
        Log.d("Resultupdate", "Url " + url);
        Log.d("Resultupdate", "callJson " + jsonObject);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getString("status").equals("true")) {
                                Toasty.success(ActivityDashBoard.this, "Food ready", Toasty.LENGTH_LONG).show();
                                startActivity(new Intent(ActivityDashBoard.this, ActivityDashBoard.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    void getFoodReady(String order_id_on_) throws JSONException {
        editor.putBoolean("isSetDriver",false);
        editor.apply();

        Log.d("chkremovedata","A "+order_id_on_);
        db2.getRemoveOrder(order_id_on_);

        callCurrentDe();
        /*for(int i=0;i<StorageAll.driverList.size();i++)
        {
            Log.d("printall","B "+StorageAll.driverList.get(i).getOrder_id());
            if(StorageAll.driverList.get(i).getOrder_id().trim().equals(order_id_on_))
            {
                Log.d("printall","C "+StorageAll.driverList.get(i).getOrder_id());

                *//*StorageAll.driverList.remove(i);
                adapter_bed.notifyDataSetChanged();
                Toast.makeText(this, "R size : "+StorageAll.driverList, Toast.LENGTH_SHORT).show();
                adapter_bed.notifyItemRemoved(i);
                adapter_bed.notifyItemRangeChanged(i, StorageAll.driverList.size());*//*


            }

        }*/
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = getString(R.string.baseurl_second) + "order_status_update";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("order_id", order_id_on_);
            jsonObject.put("to_status", "4");
            jsonObject.put("user_id", rid);
            jsonObject.put("user_type", "restaurant");
            jsonObject.put("note", "");
            Log.d("printjson",""+jsonObject.toString());
            final String requestBody = jsonObject.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    startActivity(new Intent(ActivityDashBoard.this,ActivityDashBoard.class));
                    Log.i("VOLLEYresponse", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    void getDeDetails(String did_id) throws JSONException
    {/*

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("reg_id", "440003");

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.d("chklogindata","BOOK : AAAB"+jsonObject.toString());

        String url=getString(R.string.baseurl_second)+"deliveryboy_details";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST,url, jsonObject, new com.android.volley.Response.Listener<JSONObject>()
                {

                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            Picasso.with(ActivityDashBoard.this).load(getString(R.string.baseurl_img)+response.getString("pancard_img")).into(driver_profile);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            de_name.setText("Delivery boy Name "+response.getString("drive_name"));
                            de_reg.setText("Delivery boy Name "+response.getString("reg_id"));
                            vehicle_no.setText(response.getString("vehicle_no"));
                            contact_no.setText(response.getString("contact_no"));
                            email_id.setText(response.getString("email_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("ServertaDataAbcdCHK",""+response.toString());
                    }
                }, new com.android.volley.Response.ErrorListener()
                {

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();

                    }
                });
        requestQueue.add(jsonObjectRequest);*/
    }


    //*******************45
    public class MyListData_driver_list{
        private String description;
        private int imgId;
        public MyListData_driver_list(String description, int imgId) {
            this.description = description;
            this.imgId = imgId;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public int getImgId() {
            return imgId;
        }
        public void setImgId(int imgId) {
            this.imgId = imgId;
        }
    }
    public class MyListAdapter_driver_list extends RecyclerView.Adapter<MyListAdapter_driver_list.ViewHolder>{
        private ArrayList<DriverList> listdata;

        // RecyclerView recyclerView;
        public MyListAdapter_driver_list(ArrayList<DriverList> listdata)
        {
            this.listdata = listdata;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.list_item_new_task3, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {

            holder.de_name.setText("Driver Name "+listdata.get(position).getDe_name());
            holder.de_reg.setText("Driver Reg. "+listdata.get(position).getDe_reg());
            holder.vehicle_no.setText("Driver vehicle no "+listdata.get(position).getVehicle_no());
            holder.contact_no.setText("Contact no. "+listdata.get(position).getContact_no());
            holder.email_id.setText("Email Id "+listdata.get(position).getEmail_id());
            holder.orderid.setText("Order Id "+listdata.get(position).getOrder_id());
            holder.relativeLayout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    if (holder.cont1.getVisibility() == View.VISIBLE)
                    {
                        holder.cont1.setVisibility(View.GONE);
                    } else {
                        holder.cont1.setVisibility(View.VISIBLE);
                    }


                }
            });
            holder.getDidId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    try {
                        if (listdata.get(position).getDe_id().trim().matches("\\d+(?:\\.\\d+)?")) {
                            CurrentDriverId.did = listdata.get(position).getDe_id().trim();
                            startActivity(new Intent(ActivityDashBoard.this,ActivityLivetrackingRest.class));
                        } else {
                            Toast.makeText(ActivityDashBoard.this, "Invalid", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(ActivityDashBoard.this, "Invalid", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }


        @Override
        public int getItemCount()
        {
            return listdata.size();
        }

        public  class ViewHolder extends RecyclerView.ViewHolder
        {
            public ImageButton getDidId;
            public TextView de_name;
            public TextView de_reg;
            public TextView vehicle_no;
            public TextView contact_no;
            public TextView email_id;
            public TextView orderid;
            public LinearLayout cont1;
            public RelativeLayout relativeLayout;
            public ViewHolder(View itemView)
            {
                super(itemView);
                 getDidId= (ImageButton) itemView.findViewById(R.id.getDidId);
                 de_name= (TextView) itemView.findViewById(R.id.de_name);
                 de_reg= (TextView) itemView.findViewById(R.id.de_reg);
                 vehicle_no= (TextView) itemView.findViewById(R.id.vehicle_no);
                 contact_no= (TextView) itemView.findViewById(R.id.contact_no);
                 email_id= (TextView) itemView.findViewById(R.id.email_id);
                 orderid= (TextView) itemView.findViewById(R.id.orderid);
                 cont1=(LinearLayout)itemView.findViewById(R.id.cont1);
                 relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
            }
        }
    }
    // *******************45

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("")
                        .setMessage("")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(ActivityDashBoard.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
}




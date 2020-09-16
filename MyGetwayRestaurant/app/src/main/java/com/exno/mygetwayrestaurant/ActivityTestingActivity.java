package com.exno.mygetwayrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ActivityTestingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        Intent dialogIntent = new Intent(this, ActivityDashBoard.class);

        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        dialogIntent.putExtra("a","a");
        dialogIntent.putExtra("b","b");
        dialogIntent.putExtra("c","1");
        dialogIntent.putExtra("d","d");


        dialogIntent.putExtra("vehicle_no","e");
        dialogIntent.putExtra("reg_id","f");
        dialogIntent.putExtra("contact_no","g");
        dialogIntent.putExtra("drive_name","h");
        dialogIntent.putExtra("email_id","i");


        startActivity(dialogIntent);
    }
}

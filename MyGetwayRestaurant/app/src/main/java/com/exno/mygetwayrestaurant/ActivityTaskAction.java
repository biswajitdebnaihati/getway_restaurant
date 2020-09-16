package com.exno.mygetwayrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class ActivityTaskAction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_action);
        Intent intent=getIntent();
        if (intent==null){
            finish();
        }
        Toast.makeText(this, ""+intent.getStringExtra("id"), Toast.LENGTH_SHORT).show();
    }
}

package com.exno.mygetwayrestaurant.database;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.exno.mygetwayrestaurant.R;

import java.util.List;

public class ActivityDatabase extends AppCompatActivity {
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        db= new DatabaseHandler(this);
        db.addDid("1");
        db.addDid("2");
        db.addDid("3");
        db.addDid("4");
        db.getRemove("3");
        Toast.makeText(this, "callToast", Toast.LENGTH_SHORT).show();
        Cursor cursor=null;
        List<didStore> aa=db.getDid();
        for (int y=0;y<aa.size();y++)
        {
            Log.d("printallaaDSDS","A : "+aa.get(y).getDid()+" B : "+aa.get(y).getStatus());
        }
            db.getRemoveAll();

        }
    }

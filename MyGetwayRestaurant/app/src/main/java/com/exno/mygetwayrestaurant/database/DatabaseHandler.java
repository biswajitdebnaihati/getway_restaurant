package com.exno.mygetwayrestaurant.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "task_driver_details_database1";
    private static final String TABLE_CONTACTS = "task_driver_details_table1";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "did";
    private static final String KEY_PH_NO = "status";




    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    public void addDid(String did) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, did);
        values.put(KEY_PH_NO, "1");
        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    public List<didStore> getDid() {
        List<didStore> contactList = new ArrayList<didStore>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS+" limit 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                didStore contact = new didStore();
                contact.setDid(cursor.getString(1));
                contact.setStatus(cursor.getString(2));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public void getRemoveAll() {

        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from "+ TABLE_CONTACTS);
        }
        public long getRemove(String id) {

            SQLiteDatabase db = this.getWritableDatabase();
            long l=db.delete(TABLE_CONTACTS, KEY_NAME + " = ?",
                    new String[] { id });
            db.close();
            return l;
        }
}

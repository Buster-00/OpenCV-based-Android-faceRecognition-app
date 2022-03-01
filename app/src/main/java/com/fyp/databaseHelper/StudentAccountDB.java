package com.fyp.databaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.HashMap;

public class StudentAccountDB extends SQLiteOpenHelper {

    private static final int version = 2;

    //Define names
    public static final String DB_NAME = "DB";
    public static final String TABLE_NAME = "studentAccount";

    //Define column NAME

    public static final String COLUMN_1 = "id";
    public static final String COLUMN_2 = "password";
    public static final String COLUMN_3 = "name";
    public static final String COLUMN_4 = "label";

    public StudentAccountDB(@Nullable Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "(id TEXT UNIQUE, password TEXT, name TEXT, label INTEGER PRIMARY KEY AUTOINCREMENT); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }


    //Insert new student account
    public boolean insert(String col_1, String col_2, String col_3){

        //Get database reference
        SQLiteDatabase DB = this.getWritableDatabase();

        //add data into content value
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_1, col_1);
        contentValues.put(COLUMN_2, col_2);
        contentValues.put(COLUMN_3, col_3);

        //insert data into database
        long result = DB.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            Log.e("Database Error", "insert new student account failed");
            return false;
        }

        return true;
    }

    //read student account
    //col_1 equals to student id

    public boolean readById(String id, HashMap<String, String> hm) {

        //get database reference
        SQLiteDatabase DB = this.getReadableDatabase();

        //Retrieve data
        Cursor cursor = DB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_1 + " LIKE ?", new String[]{id});

        //Exists
        if (cursor.moveToFirst()) {
            hm.put("id", cursor.getString(cursor.getColumnIndex("id")));
            hm.put("password", cursor.getString(cursor.getColumnIndex("password")));
            hm.put("name", cursor.getString(cursor.getColumnIndex("name")));

            cursor.close();
            DB.close();

            return true;
        } else {
            //Not exists
            Log.i("log from database", "cannot retrieve requested data");

            cursor.close();
            DB.close();

            return false;
        }
    }

    public String ReadAll(){

        //get database reference
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + "*" + " FROM " + TABLE_NAME, new String[]{});

        String s = new String();
        while(cursor.moveToNext()){
            s += cursor.getString(cursor.getColumnIndex(COLUMN_1));
            s += " label: ";
            s += cursor.getString(cursor.getColumnIndex(COLUMN_4)) + "\n";
        }
        return s;
    }

    public int getLabelByID(String studentID){

        //get database reference
        SQLiteDatabase DB = this.getReadableDatabase();

        //Retrieve data
        Cursor cursor = DB.rawQuery("SELECT " + COLUMN_4 + " FROM " + TABLE_NAME +  " WHERE " + COLUMN_1 + "=?", new String[]{studentID});

        if(cursor.moveToNext()){
            return cursor.getInt(cursor.getColumnIndex(COLUMN_4));
        }
        else {
            return -1;
        }


    }

}

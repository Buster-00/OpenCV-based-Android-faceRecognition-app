package com.fyp.databaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.HashMap;

public class SQLiteStudent extends SQLiteOpenHelper {

    private static final int version = 2;

    //Define names
    String DB_NAME = Student.DB_NAME;
    String TABLE_NAME = Student.TABLE_NAME;

    //Define column NAME
    String COLUMN_1 = Student.COLUMN_1;
    String COLUMN_2 = Student.COLUMN_2;
    String COLUMN_3 = Student.COLUMN_3;
    String COLUMN_4 = Student.COLUMN_4;

    public SQLiteStudent(@Nullable Context context) {
        super(context, Student.DB_NAME, null, version);
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
        DB.close();

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

    public String getNameByLabel(int label){

        //get database reference
        SQLiteDatabase DB = this.getReadableDatabase();

        //Retrieve data
        Cursor cursor = DB.rawQuery("SELECT " + COLUMN_3 +" FROM " + TABLE_NAME + " WHERE " + COLUMN_4 +"=?", new String[]{String.valueOf(label)});
        //Cursor cursor = DB.rawQuery("SELECT name FROM studentAccount WHERE label=1", new String[]{});
        if(cursor.moveToNext()){
            return cursor.getString(cursor.getColumnIndex(COLUMN_3));
        }
        else {
            return "unknown";
        }
    }

    public boolean deleteByID(String ID){

        //get database reference
        SQLiteDatabase DB = this.getWritableDatabase();

        //Delete data
        if(DB.delete(TABLE_NAME, "id=?", new String[]{ID}) != -1){
            return true;
        }
        else{
            return false;
        }

    }

}

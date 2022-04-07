package com.fyp.databaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fyp.invariable.InVar;

import java.sql.Connection;
import java.util.Stack;
import java.util.Vector;

public class StudentLectureDB {

    //Define names
    public static final String DB_NAME = "DB";
    public static final String TABLE_NAME = "lectureOfStudent";

    //Define the columan name
    public static final String COLUMN_1 = "studentID";
    public static final String COLUMN_2 = "lectureID";

    //Context used for local database
    boolean isConnect = InVar.IS_CONNECT_TO_MARIA_DB;
    Context context;
    public SQLiteStudentLectureDB sqLiteStudentLectureDB;

    public StudentLectureDB(Context context){
        if(!isConnect){
            sqLiteStudentLectureDB = new SQLiteStudentLectureDB(context);
        }
    }

    public boolean insert(String studentID, String lectureID){

        if(isConnect){
            return false;
        }
        else{
            return sqLiteStudentLectureDB.insert(studentID, lectureID);
        }
    }

    public Vector<String> getLecturesIDsByStudentID(String studentID){

        if(isConnect){
            return null;
        }
        else
        {
            return sqLiteStudentLectureDB.getLecturesIDsByStudentID(studentID);
        }
    }

    public class SQLiteStudentLectureDB extends SQLiteOpenHelper {


        public SQLiteStudentLectureDB(Context context){
            super(context, DB_NAME, null, InVar.SQLite_version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    "(studentID TEXT, lectureID TEXT, PRIMARY KEY(studentID, lectureID))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    "(studentID TEXT, lectureID TEXT, PRIMARY KEY(studentID, lectureID))");
        }

        public boolean insert(String studentID, String lectureID){
            //get database reference
            SQLiteDatabase DB = getWritableDatabase();

            //add values to content value
            ContentValues contentValues = new ContentValues();
            contentValues.put("studentID", studentID);
            contentValues.put("lectureID", lectureID);

            //insert data into database
            long result = DB.insert(TABLE_NAME, null, contentValues);
            DB.close();

            if(result == -1){
                Log.e("error", "cannot insert student lecture table");
                return false;
            }
            else{
                return true;
            }
        }

        public Vector<String> getLecturesIDsByStudentID(String studentID){
            //get database reference
            SQLiteDatabase DB = getReadableDatabase();

            //Retrieve data
            Cursor cursor = DB.rawQuery("SELECT lectureID FROM " + TABLE_NAME + " WHERE studentID=?", new String[]{studentID});

            Vector<String> lectures = new Vector<>();
            while (cursor.moveToNext()){
                lectures.add(cursor.getString(cursor.getColumnIndex(COLUMN_2)));
            }

            return lectures;
        }
    }

}

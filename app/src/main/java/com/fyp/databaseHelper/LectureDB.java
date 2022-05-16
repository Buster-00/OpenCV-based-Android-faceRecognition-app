package com.fyp.databaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fyp.invariable.InVar;

import org.bytedeco.opencv.presets.opencv_core;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;

public class LectureDB {

    //Define names
    public static final String DB_NAME = "DB";
    public static final String TABLE_NAME = "lecture";

    //Define the column name
    public static final String COLUMN_1 = "lectureID";
    public static final String COLUMN_2 = "lectureName";
    public static final String COLUMN_3 = "lecturerID";
    public static final String COLUMN_4 = "date";
    public static final String COLUMN_5 = "venue";


    //Context used for local database
    boolean isConnect = InVar.IS_CONNECT_TO_MARIA_DB;
    Context context;
    SQLiteLectureDB sqLiteLectureDB;
    MariaLecture mariaLecture;

    public LectureDB(Context context){
        this.context = context;

        if(!isConnect){
            //create local database helper
            sqLiteLectureDB = new SQLiteLectureDB(context);
        }
        else
        {
            mariaLecture = new MariaLecture();
        }
    }

    public boolean insert(String col_1, String col_2, String col_3, String col_4, String col_5){
        if(isConnect){
            return mariaLecture.insert(col_1, col_2, col_3, col_4, col_5);
        }
        else{
            return sqLiteLectureDB.insert(col_1, col_2, col_3, col_4, col_5);
        }
    }

    public Vector<Lecture> getAllLecture(){
        if(isConnect){
            return mariaLecture.getAllLecture();
        }
        else{
            return sqLiteLectureDB.getAllLecture();
        }
    }

    public Vector<Lecture> getLectureByID(String ID){
        if(isConnect){
            //TODO
            return mariaLecture.getLectureByID(ID);
        }
        else{
           return null;
        }
    }

    class SQLiteLectureDB extends SQLiteOpenHelper{

        private static final int version = 2;

        public SQLiteLectureDB(@Nullable Context context) {
            super(context, DB_NAME, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME +
                    "(lectureID TEXT UNIQUE PRIMARY KEY, lectureName TEXT, lecturer TEXT, day TEXT, time TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }

        @Override
        public void onOpen(SQLiteDatabase db){
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    "(lectureID TEXT UNIQUE PRIMARY KEY, lectureName TEXT, lecturer TEXT, day TEXT, time TEXT)");
        }

        public boolean insert(String col_1, String col_2, String col_3, String col_4, String col_5){

            //Get database reference
            SQLiteDatabase DB = this.getWritableDatabase();

            //add data into content value
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_1, col_1);
            contentValues.put(COLUMN_2, col_2);
            contentValues.put(COLUMN_3, col_3);
            contentValues.put(COLUMN_4, col_4);
            contentValues.put(COLUMN_5, col_5);

            //insert data into database
            long result = DB.insert(TABLE_NAME,null, contentValues);
            DB.close();

            if(result == -1){
                Log.e("Database Error", "insert new Lecture failed");
                return false;
            }
            else {
                return true;
            }

        }

        public Vector<Lecture> getAllLecture(){

            Vector<Lecture> lectureVector = new Vector<>();

            //Get database reference
            SQLiteDatabase DB = this.getReadableDatabase();

            //Retrieve all data
            Cursor cursor = DB.rawQuery("SELECT * FROM " + TABLE_NAME, new String[]{});

            while (cursor.moveToNext()){
                String s1 = cursor.getString(cursor.getColumnIndex(COLUMN_1));
                String s2 = cursor.getString(cursor.getColumnIndex(COLUMN_2));
                String s3 = cursor.getString(cursor.getColumnIndex(COLUMN_3));
                String s4 = cursor.getString(cursor.getColumnIndex(COLUMN_4));
                String s5 = cursor.getString(cursor.getColumnIndex(COLUMN_5));
                Lecture lecture = new Lecture(s1, s2, s3, s4, s5);

                lectureVector.add(lecture);
            }

            return lectureVector;
        }

        public Lecture getLectureByID(String ID){

            //Get database reference
            SQLiteDatabase DB = this.getReadableDatabase();

            //Retrieve all data
            Cursor cursor = DB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE lectureID=?", new String[]{ID});

            if (cursor.moveToNext()){
                String s1 = cursor.getString(cursor.getColumnIndex(COLUMN_1));
                String s2 = cursor.getString(cursor.getColumnIndex(COLUMN_2));
                String s3 = cursor.getString(cursor.getColumnIndex(COLUMN_3));
                String s4 = cursor.getString(cursor.getColumnIndex(COLUMN_4));
                String s5 = cursor.getString(cursor.getColumnIndex(COLUMN_5));
                Lecture lecture = new Lecture(s1, s2, s3, s4, s5);
                return lecture;
            }
            else{
                return null;
            }

        }
    }

    class MariaLecture{
        public boolean insert(String col_1, String col_2, String col_3, String col_4, String col_5){
            final boolean[] isSuccess = {false};

            Thread thread = new Thread(){
                @Override
                public void run() {
                    try {
                        Connection mariaCon = new MariaDBconnector().getConnection(new Properties());
                        Statement statement= mariaCon.createStatement();

                        String query = String.format("INSERT INTO %s(%s, %s, %s, %s, %s) VALUES('%s', '%s', '%s', '%s', '%s')",
                                TABLE_NAME, COLUMN_1, COLUMN_2, COLUMN_3, COLUMN_4, COLUMN_5, col_1, col_2, col_3, col_4, col_5);
                        long rs = statement.executeUpdate(query);
                        mariaCon.close();
                        if(rs > 0){
                            isSuccess[0] = true;
                        }
                        else {
                            isSuccess[0] = false;
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            };

            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return isSuccess[0];
        }

        public Vector<Lecture> getAllLecture() {
            return null;
        }

        public Vector<Lecture> getLectureByID(String ID){
            Vector<Lecture> lectureVector = new Vector<>();

            Thread thread = new Thread(){
                public void run(){
                    try {
                        Connection mariaCon = new MariaDBconnector().getConnection(new Properties());
                        Statement statement = mariaCon.createStatement();

                        String query = String.format("SELECT * FROM %s WHERE %s = '%s'",
                                TABLE_NAME, COLUMN_3, ID);

                        ResultSet rs = statement.executeQuery(query);
                        mariaCon.close();
                        while (rs.next()){
                            Lecture lecture = new Lecture(
                                    rs.getString(COLUMN_1),
                                    rs.getString(COLUMN_2),
                                    rs.getString(COLUMN_3),
                                    rs.getString(COLUMN_4),
                                    rs.getString(COLUMN_5));

                            lectureVector.add(lecture);
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            };

            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return lectureVector;
        }
    }
}

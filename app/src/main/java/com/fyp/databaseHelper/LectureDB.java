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

import java.util.Vector;

public class LectureDB {

    //Define names
    public static final String DB_NAME = "DB";
    public static final String TABLE_NAME = "lecture";

    //Define the column name
    public static final String COLUMN_1 = "lectureID";
    public static final String COLUMN_2 = "lectureName";
    public static final String COLUMN_3 = "lecturer";
    public static final String COLUMN_4 = "day";
    public static final String COLUMN_5 = "time";


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

    public Lecture getLectureByID(String ID){
        if(isConnect){
            //TODO
            return null;
        }
        else{
            return sqLiteLectureDB.getLectureByID(ID);
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
            //TODO
            return false;
        }

        public Vector<Lecture> getAllLecture() {
            //TODO
            return null;
        }
    }
}

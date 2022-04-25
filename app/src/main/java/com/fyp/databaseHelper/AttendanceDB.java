package com.fyp.databaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.fyp.invariable.InVar;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class AttendanceDB {

    //Define names
    public static final String DB_NAME = "DB";
    public static final String TABLE_NAME = "attendance";

    //Define the column name
    public static final String COLUMN_1 = "lectureID";
    public static final String COLUMN_2 = "studentID";
    public static final String COLUMN_3 = "date";

    //local database
    SQLiteAttendance sqLiteAttendance;

    public AttendanceDB(Context context){

        //using local database
        if(!InVar.IS_CONNECT_TO_MARIA_DB){
            sqLiteAttendance = new SQLiteAttendance(context);
        }
        else{
            //TODO using remote database
        }

    }

    public boolean insert(String col_1, String col_2, String col_3){

        //using local database
        if(!InVar.IS_CONNECT_TO_MARIA_DB){
            return sqLiteAttendance.insert(col_1, col_2, col_3);
        }
        else{
            //TODO Remote server
            return false;
        }
    }

    public Vector<AttendanceRecord> getAttendanceByStudentID(String studentID){
        //using local database
        if(!InVar.IS_CONNECT_TO_MARIA_DB){
            return sqLiteAttendance.getAttendanceByStudentID(studentID);
        }
        else{
            //TODO Remote server
            return null;
        }
    }

    class SQLiteAttendance extends SQLiteOpenHelper {

        private static final int version = InVar.SQLite_version;

        public SQLiteAttendance(@Nullable Context context) {
            super(context, DB_NAME, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME +
                    "(lectureID TEXT , studentID TEXT, date TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    "(lectureID TEXT , studentID TEXT, date TEXT)");
        }

        public boolean insert(String col_1, String col_2, String col_3){

            //get database reference
            SQLiteDatabase DB = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_1, col_1);
            contentValues.put(COLUMN_2, col_2);
            contentValues.put(COLUMN_3, col_3);

            //insert data into database
            long result = DB.insert(TABLE_NAME, null, contentValues);

            if(result == -1){
                Log.e("Database Error", "insert new attendance failed");
                return false;
            }
            else
            {
                return true;
            }

        }

        public Vector<AttendanceRecord> getAttendanceByStudentID(String studentID){
            Vector<AttendanceRecord> data = new Vector<>();

            //get database reference
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();

            //get data
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT lectureID, studentID, date FROM attendance WHERE studentID=?", new String[]{studentID});
            while (cursor.moveToNext()){
                AttendanceRecord record = new AttendanceRecord(cursor.getString(cursor.getColumnIndex(COLUMN_1)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_2)), cursor.getString(cursor.getColumnIndex(COLUMN_3)));
                data.add(record);
            }

            return data;
        }
    }

    public static class AttendanceRecord{
        String LectureID;
        String StudentID;
        String Date;

        public AttendanceRecord(String lectureID, String studentID, String date) {
            LectureID = lectureID;
            StudentID = studentID;
            Date = date;
        }

        public String getLectureID() {
            return LectureID;
        }

        public void setLectureID(String lectureID) {
            LectureID = lectureID;
        }

        public String getStudentID() {
            return StudentID;
        }

        public void setStudentID(String studentID) {
            StudentID = studentID;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String date) {
            Date = date;
        }
    }
}

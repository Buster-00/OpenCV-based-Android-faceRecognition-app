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
    public static final String COLUMN_4 = "lecturerName";
    public static final String COLUMN_5 = "venue";

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

    //Param
    public boolean insert(String col_1, String col_2, String col_3, String col_4, String col_5){

        //using local database
        if(!InVar.IS_CONNECT_TO_MARIA_DB){
            return sqLiteAttendance.insert(col_1, col_2, col_3, col_4, col_5);
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
                    "(lectureID TEXT , studentID TEXT, date TEXT, lecturerName TEXT, venue TEXT," +
                    " UNIQUE(lectureID, studentID, date, lecturerName, venue))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    "(lectureID TEXT , studentID TEXT, date TEXT, lecturerName TEXT, venue TEXT," +
                    " UNIQUE(lectureID, studentID, date, lecturerName, venue))");
        }

        public boolean insert(String col_1, String col_2, String col_3, String col_4, String col_5){

            //get database reference
            SQLiteDatabase DB = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_1, col_1);
            contentValues.put(COLUMN_2, col_2);
            contentValues.put(COLUMN_3, col_3);
            contentValues.put(COLUMN_4, col_4);
            contentValues.put(COLUMN_5, col_5);

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
                AttendanceRecord record = new AttendanceRecord(
                        cursor.getString(cursor.getColumnIndex(COLUMN_1)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_2)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_3)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_4)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_5)));
                data.add(record);
            }

            return data;
        }
    }

    public static class AttendanceRecord{

        String lectureID;
        String studentID;
        String date;
        String lecturerName;
        String venue;

        public AttendanceRecord(String lectureID, String studentID, String date, String lecturerName, String venue) {
            this.lectureID = lectureID;
            this.studentID = studentID;
            this.date = date;
            this.lecturerName = lecturerName;
            this.venue = venue;
        }

        public String getLecturerName() {
            return lecturerName;
        }

        public void setLecturerName(String lecturerName) {
            this.lecturerName = lecturerName;
        }

        public String getVenue() {
            return venue;
        }

        public void setVenue(String venue) {
            this.venue = venue;
        }

        public String getLectureID() {
            return lectureID;
        }

        public void setLectureID(String lectureID) {
            lectureID = lectureID;
        }

        public String getStudentID() {
            return studentID;
        }

        public void setStudentID(String studentID) {
            studentID = studentID;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            date = date;
        }
    }
}

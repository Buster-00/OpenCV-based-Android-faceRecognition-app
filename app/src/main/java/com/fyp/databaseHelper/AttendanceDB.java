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

public class AttendanceDB {

    //Define names
    public static final String DB_NAME = "DB";
    public static final String TABLE_NAME = "attendance";

    //Define the column name
    public static final String COLUMN_1 = "lectureID";
    public static final String COLUMN_2 = "studentID";
    public static final String COLUMN_3 = "date";

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

        public HashMap<String, String> getAttendanceByStudentID(String studentID){
            HashMap<String, String> map = new HashMap<>();

            //get database reference
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();

            //get data
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT lectureID, studentID, date WHERE studentID=?", new String[]{studentID});
            while (cursor.moveToNext()){
                map.put("lectureID", cursor.getString(cursor.getColumnIndex(COLUMN_1)));
                map.put("studentID", cursor.getString(cursor.getColumnIndex(COLUMN_2)));
                map.put("date", cursor.getString(cursor.getColumnIndex(COLUMN_3)));
            }

            return map;
        }
    }
}

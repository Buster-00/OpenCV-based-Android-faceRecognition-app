package com.fyp.databaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.fyp.invariable.InVar;

public class LecturerDB {

    //Define names
    public static final String DB_NAME = "DB";
    public static final String TABLE_NAME = "LecturerAccount";

    //Define column NAME
    public static final String COLUMN_1 = "id";
    public static final String COLUMN_2 = "password";
    public static final String COLUMN_3 = "name";

    //local SQLite database
    SQLiteLecturerDB sqLiteLecturerDB;
    Context context;

    public LecturerDB(Context context){
        if(!InVar.IS_CONNECT_TO_MARIA_DB){
            this.context = context;
            sqLiteLecturerDB = new SQLiteLecturerDB(context);
        }
        else{
            //TODO REMOTE SERVER
        }

    }

    public boolean insert(String id, String password, String name){
        if(!InVar.IS_CONNECT_TO_MARIA_DB){
            return sqLiteLecturerDB.insert(id, password, name);
        }
        else{
            return false;
            //TODO REMOTE SERVER
        }
    }

    public Lecturer ReadByID(String id){
        if(!InVar.IS_CONNECT_TO_MARIA_DB){
            return sqLiteLecturerDB.ReadByID(id);
        }
        else{
            return null;
            //TODO REMOTE SERVER
        }
    }

    class SQLiteLecturerDB extends SQLiteOpenHelper{

        public SQLiteLecturerDB(@Nullable Context context) {
            super(context, DB_NAME, null, InVar.SQLite_version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "(id TEXT UNIQUE, password TEXT, name TEXT); ");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }

        @Override
        public void onOpen(SQLiteDatabase db){
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(id TEXT UNIQUE, password TEXT, name TEXT); ");
        }

        public boolean insert(String id, String password, String name){

            //get writable database reference
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", id);
            contentValues.put("password", password);
            contentValues.put("name", name);

            long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

            if(result == -1){
                return false;
            }
            else {
                return true;
            }

        }

        public Lecturer ReadByID(String id){
            //get readable database reference
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id=? ", new String[]{id});

            if(cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                Lecturer lecturer = new Lecturer(id, name, password);
                return lecturer;
            }
            else {
                return null;
            }
        }
    }

    public class Lecturer{
        String id;
        String name;
        String password;

        public Lecturer(String id, String name, String password) {
            this.id = id;
            this.name = name;
            this.password = password;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }


}

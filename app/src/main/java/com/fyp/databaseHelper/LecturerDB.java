package com.fyp.databaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.fyp.invariable.InVar;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

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
    MariaLecturer mariaLecturer;
    Context context;

    public LecturerDB(Context context){
        if(!InVar.IS_CONNECT_TO_MARIA_DB){
            this.context = context;
            sqLiteLecturerDB = new SQLiteLecturerDB(context);
        }
        else{
            mariaLecturer = new MariaLecturer();
        }

    }

    public boolean insert(String id, String password, String name){
        if(!InVar.IS_CONNECT_TO_MARIA_DB){
            return sqLiteLecturerDB.insert(id, password, name);
        }
        else{
            return mariaLecturer.insert(id, password, name);
        }
    }

    public Lecturer ReadByID(String id){
        if(!InVar.IS_CONNECT_TO_MARIA_DB){
            return sqLiteLecturerDB.ReadByID(id);
        }
        else{
            return mariaLecturer.ReadByID(id);
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

    private class MariaLecturer{

        public boolean insert(String id, String password, String name){

            final boolean[] isSuccess = {false};

            //get Connection
            Thread thread = new Thread(){
                public void run(){
                    try {
                        Connection mariaCon = new MariaDBconnector().getConnection(new Properties());
                        Statement statement = mariaCon.createStatement();

                        //create insert command
                        String query = String.format("INSERT INTO %s(%s, %s, %s) VALUES('%s', '%s', '%s')",
                                TABLE_NAME,COLUMN_1, COLUMN_2, COLUMN_3, id, password, name);
                        int result = statement.executeUpdate(query);

                        //close connection
                        mariaCon.close();

                        if(result > 0)
                            isSuccess[0] = true;
                        else
                            isSuccess[0] = false;


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

        public Lecturer ReadByID(String id){

            final Lecturer[] lecturer = new Lecturer[1];

            Thread thread = new Thread(){
                public void run(){
                    try {
                        Connection mariaCon = new MariaDBconnector().getConnection(new Properties());
                        Statement statement = mariaCon.createStatement();

                        String query = String.format("SELECT * FROM %s WHERE %s='%s'",
                                TABLE_NAME, COLUMN_1, id);

                        ResultSet resultSet = statement.executeQuery(query);
                        mariaCon.close();

                        if (resultSet.next()){
                             lecturer[0] = new Lecturer(resultSet.getString(COLUMN_1), resultSet.getString(COLUMN_2), resultSet.getString(COLUMN_3));
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

            return lecturer[0];
        }
    }

    public class Lecturer{
        String id;
        String password;
        String name;


        public Lecturer(String id, String password, String name) {
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

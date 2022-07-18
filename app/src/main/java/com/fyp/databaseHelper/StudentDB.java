package com.fyp.databaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.fyp.invariable.InVar;

import java.util.HashMap;

public class StudentDB {

    //Define names
    public static final String DB_NAME = "DB";
    public static final String TABLE_NAME = "studentAccount";

    //Define column NAME
    public static final String COLUMN_1 = "id";
    public static final String COLUMN_2 = "password";
    public static final String COLUMN_3 = "name";
    public static final String COLUMN_4 = "label";

    private static boolean IS_CONNECT_TO_MARIA_DB = InVar.IS_CONNECT_TO_MARIA_DB;

    //Context used for local SQLite database
    private Context context;
    SQLiteStudent sqLiteStudent;

    //Online Maria database
    MariaStudent mariaStudent;

    public static boolean getISConnectToNetwork(){
        return IS_CONNECT_TO_MARIA_DB;
    }

    public StudentDB(Context context){
        this.context = context;

        if(!IS_CONNECT_TO_MARIA_DB){
            //create local database helper
            sqLiteStudent = new SQLiteStudent(context);
        }
        else{
            mariaStudent = new MariaStudent();
        }
    }

    public StudentDB(){
        //For non parameter constructor
    }

    public void setConnectToServer(boolean bool){
        IS_CONNECT_TO_MARIA_DB = bool;
    }

    public boolean IsConnectToServer(){
        return IS_CONNECT_TO_MARIA_DB;
    }

    public boolean insert(String col_1, String col_2, String col_3){
        boolean isSuccess = false;

        if(IS_CONNECT_TO_MARIA_DB){
            //using online Maria database
            isSuccess = mariaStudent.insert(col_1, col_2, col_3);
        }
        else{
            //using local SQLite database
            isSuccess = sqLiteStudent.insert(col_1, col_2, col_3);
        }

        return isSuccess;
    }

    public boolean readById(String id, HashMap<String, String> hm){
        boolean isSuccess = false;

        if (IS_CONNECT_TO_MARIA_DB){
            //using online Maria database
            isSuccess = mariaStudent.readById(id, hm);
        }
        else{
            //using local SQLite database
            isSuccess = sqLiteStudent.readById(id, hm);
        }

        return isSuccess;
    }

    public String readAll(){
        String str = new String();

        if(IS_CONNECT_TO_MARIA_DB){
            //using online Maria database
        }
        else{
            //using local SQLite database
            str = sqLiteStudent.ReadAll();
        }

        return str;
    }

    public int getLabelByID(String studentID){
        int label = 0;

        if(IS_CONNECT_TO_MARIA_DB){
            //using online Maria database
            label = mariaStudent.getLabelByID(studentID);
        }
        else{
            //using local SQLite database
            label = sqLiteStudent.getLabelByID(studentID);
        }

        return label;
    }

    public String getNameByLabel(int label){
        String name = new String();

        if(IS_CONNECT_TO_MARIA_DB){
            //using online Maria database
        }
        else{
            //using local SQLite database
            sqLiteStudent.getNameByLabel(label);
        }

        return name;
    }

    public boolean deleteByID(String ID){
        boolean bool = false;

        if(IS_CONNECT_TO_MARIA_DB){
            //using online Maria database
        }
        else{
            //using local SQLite database
            bool = sqLiteStudent.deleteByID(ID);
        }

        return bool;
    }
}

package com.fyp.databaseHelper;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.LogRecord;

public class MariaStudent {

    //Maria database connection
    MariaDBconnector connector;

    //Define names
    String DB_NAME = Student.DB_NAME;
    String TABLE_NAME = Student.TABLE_NAME;

    //Define column NAME
    String COLUMN_1 = Student.COLUMN_1;
    String COLUMN_2 = Student.COLUMN_2;
    String COLUMN_3 = Student.COLUMN_3;
    String COLUMN_4 = Student.COLUMN_4;

    public MariaStudent(){

    }

    public boolean insert(String col_1, String col_2, String col_3){
        final boolean[] bool = {false};

        Thread thread = new Thread(){
            public void run(){
                try {
                    Connection MariaCon = new MariaDBconnector().getConnection(new Properties());
                    Statement statement = MariaCon.createStatement();

                    //Insert new student into table
                    String query = String.format("INSERT INTO %s(%s, %s, %s) VALUES('%s', '%s', '%s')",
                            TABLE_NAME, COLUMN_1, COLUMN_2, COLUMN_3, col_1, col_2, col_3);
                    Log.e("query", query);
                    int rs = statement.executeUpdate(query);
                    if(rs > 0){
                        Log.e("result", rs + "");
                        bool[0] = true;
                        Log.e("bool", String.valueOf(bool[0]));
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

        return bool[0];
    }

    public boolean readById(String id, HashMap<String, String> hm){
        boolean bool = false;

        return bool;
    }

    public String ReadAll(){
        String str = new String();

        return str;
    }

    public int getLabelByID(String studentID){
        int label = 0;

        return label;
    }

    public String getNameByLabel(int label){
        String name = new String();

        return name;
    }

    public boolean deleteByID(String ID){
        boolean bool = false;

        return bool;
    }
}

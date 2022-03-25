package com.fyp.databaseHelper;

import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;

public class MariaStudent {

    //Maria database connection
    MariaDBconnector connector;

    //Define names
    String DB_NAME = StudentDB.DB_NAME;
    String TABLE_NAME = StudentDB.TABLE_NAME;

    //Define column NAME
    String COLUMN_1 = StudentDB.COLUMN_1;
    String COLUMN_2 = StudentDB.COLUMN_2;
    String COLUMN_3 = StudentDB.COLUMN_3;
    String COLUMN_4 = StudentDB.COLUMN_4;

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
                    }else
                    {
                        bool[0] = false;
                    }

                    //close connection
                    MariaCon.close();
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
        final boolean[] bool = {false};

        Thread thread = new Thread(){
            public void run(){
                try {
                    Connection MariaCon = new MariaDBconnector().getConnection(new Properties());
                    Statement statement = MariaCon.createStatement();

                    //Retrieve data by id
                    String query = String.format("SELECT * FROM %s WHERE %s='%s'", TABLE_NAME, COLUMN_1, id);
                    ResultSet rs = statement.executeQuery(query);
                    if(rs.next()){
                        hm.put("id", rs.getString("id"));
                        hm.put("password", rs.getString("password"));
                        hm.put("name", rs.getString("name"));
                        bool[0] = true;
                    }
                    else {
                        bool[0] = false;
                    }

                    //close connection
                    MariaCon.close();
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

    public String ReadAll(){
        String str = new String();

        return str;
    }

    public int getLabelByID(String studentID){
        final int[] label = {0};

        Thread thread = new Thread(){
          public void run(){
              try {
                  Connection MariaCon = new MariaDBconnector().getConnection(new Properties());
                  Statement statement = MariaCon.createStatement();

                  String query = String.format("SELECT %s FROM %s WHERE %s='%s'", COLUMN_4, TABLE_NAME, COLUMN_1, studentID);
                  ResultSet rs = statement.executeQuery(query);
                  if(rs.next()){
                      label[0] = rs.getInt(COLUMN_4);
                  }
                  else{
                      label[0] = -1;
                  }

                  //close connection
                  MariaCon.close();
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

        return label[0];
    }

    public String getNameByLabel(int label){
        final String[] name = {new String()};

        Thread thread = new Thread(){
            public void run(){
                try {
                    Connection MariaCon = new MariaDBconnector().getConnection(new Properties());
                    Statement statement = MariaCon.createStatement();

                    String query = String.format("SELECT %s FROM %s WHERE %s=%s", COLUMN_3, TABLE_NAME, COLUMN_4, label);
                    ResultSet rs = statement.executeQuery(query);
                    if(rs.next()){
                        name[0] = rs.getString("name");
                    }
                    else{
                        name[0] = "unknown";
                    }

                    //close connection
                    MariaCon.close();
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
        return name[0];
    }

    public boolean deleteByID(String ID){
        final boolean[] bool = {false};

        Thread thread = new Thread(){
            public void run(){
                try {
                    Connection MariaCon = new MariaDBconnector().getConnection(new Properties());
                    Statement statement = MariaCon.createStatement();

                    String query = String.format("DELETE FROM %s WHERE %s='%s'", TABLE_NAME, COLUMN_1, ID);
                    int result = statement.executeUpdate(query);

                    if(result > 0){
                        bool[0] = true;
                    }
                    else{
                        bool[0] = false;
                    }

                    //close connection
                    MariaCon.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        };

        return bool[0];
    }

    //Another implementation for thread function
    /*class InsertThread extends Thread{

        private boolean isSuccess;
        private String col_1 = new String();
        private String col_2 = new String();
        private String col_3 = new String();

        public InsertThread(String col_1, String col_2, String col_3){
            this.col_1 = col_1;
            this.col_2 = col_2;
            this.col_3 = col_3;
        }

        @Override
        public void run() {
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
                    isSuccess = true;
                }
                else {
                    isSuccess = false;
                }
                Log.e("join", "completed" + "isSuccess:" + isSuccess);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        public boolean getResult(){
            return isSuccess;
        }
    }*/
}

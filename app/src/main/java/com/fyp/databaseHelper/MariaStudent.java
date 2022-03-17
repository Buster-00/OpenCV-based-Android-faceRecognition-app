package com.fyp.databaseHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;

public class MariaStudent {

    //Maria database connection
    Connection MariaCon;

    //Define names
    String DB_NAME = Student.DB_NAME;
    String TABLE_NAME = Student.TABLE_NAME;

    //Define column NAME
    String COLUMN_1 = Student.COLUMN_1;
    String COLUMN_2 = Student.COLUMN_2;
    String COLUMN_3 = Student.COLUMN_3;
    String COLUMN_4 = Student.COLUMN_4;

    public MariaStudent(){
        MariaDBconnector connector = new MariaDBconnector();

        //get Maria connection
        try {
            MariaCon = connector.getConnection(new Properties());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean insert(String col_1, String col_2, String col_3){
        boolean bool = false;

        if(MariaCon != null){
            try {
                Statement statement = MariaCon.createStatement();

                //Insert new student into table
                String query = String.format("INSERT INTO %s (%s, %s, %s) VALUES(%s, %s, %s)",
                        TABLE_NAME, COLUMN_1, COLUMN_2, COLUMN_3, col_1, col_2, col_3);
                ResultSet rs = statement.executeQuery(query);
                if(rs.next()){
                    bool = true;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return bool;
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

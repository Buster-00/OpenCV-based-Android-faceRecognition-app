package com.fyp.databaseHelper;

import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class MariaDBconnector {

    String host ="118.31.20.251";
    String port = "3306";
    String database = "test";
    String username = "ActionNet";
    String password = "2944Qq59608";
    String table_name = "MYTABLE";

    private Connection getConnection(Properties info) throws SQLException{
        String url = "jdbc:mysql://" + host + ":" + port +"/" + database;
        info.setProperty("user", username);
        info.setProperty("password", password);
        return DriverManager.getConnection(url, info);
    }

    private String testConnect(Properties info, boolean sslExpected) throws SQLException{
        Connection con = null;
        con = getConnection(info);
        Statement statement = con.createStatement();

        //Test a query to make sure we're connected:
        ResultSet rs = statement.executeQuery("SELECT * FROM " + table_name);
        rs.next();
        String result = rs.getString("name");
        con.close();
        return result;
    }

    public String connectNOSSL() throws SQLException{
        Properties info = new Properties();
        return testConnect(info, false);
    }

    public void connectSSLWithoutValidation() throws SQLException{
        Properties info = new Properties();
        info.setProperty("useSSL", "true");
        info.setProperty("trustServerCertificate", "true");
        testConnect(info, true);
    }
}

package com.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.fyp.databaseHelper.StudentAccountDB;

import java.util.HashMap;

public class Success extends AppCompatActivity {

    //Widget
    TextView tv_success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        tv_success = findViewById(R.id.tv_success);

        StudentAccountDB DB = new StudentAccountDB(Success.this);
        DB.insert("SWE1809366", "123456", "Qin Gen");

        HashMap<String, String> hm = new HashMap<>();
        if(DB.readById("SWE1809366", hm)){
            tv_success.setText(hm.get("id") + hm.get("password") + hm.get("name"));
        }
        else{
            tv_success.setText("cannot read from database");
        }
    }
}
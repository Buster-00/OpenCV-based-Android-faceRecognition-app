package com.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fyp.databaseHelper.StudentAccountDB;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    //widget
    Button btn_register;
    Button btn_recognition;
    Button btn_train;
    TextView tv_username;

    //handler
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initiate widgets
        btn_register = findViewById(R.id.btn_register);
        btn_recognition = findViewById(R.id.btn_recognition);
        btn_train = findViewById(R.id.btn_train);
        tv_username = findViewById(R.id.tv_username);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtn_register();
            }
        });

        btn_recognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtn_recognition();
            }
        });

        btn_train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtn_train();
            }
        });

        StudentAccountDB DB = new StudentAccountDB(this);
        HashMap<String, String> hm = new HashMap<>();
        tv_username.setText(DB.ReadAll());


        //Initiate JavaCV
        Loader.load(opencv_java.class);
    }

    protected void setBtn_register(){
        startActivity(new Intent(MainActivity.this, NameRegisterActivity.class));
    }

    protected void setBtn_recognition(){
        startActivity(new Intent(MainActivity.this, FaceRecognitionActivity.class));
    }

    protected void setBtn_train(){
        startActivity(new Intent(MainActivity.this, TrainActivity.class));
    }
}
package com.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fyp.helper.FaceDetectorHelper;

import org.opencv.objdetect.FaceDetectorYN;

public class MainActivity extends AppCompatActivity {

    //widget
    Button btn_register;

    //handler
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_register = findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtn_register();
            }
        });


    }

    protected  void setBtn_register(){
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }
}
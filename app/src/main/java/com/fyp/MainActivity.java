package com.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
    Button btn_test;

    //handler
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize widget
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_test_onClick();
            }
        });


    }

    protected void btn_test_onClick(){

    }
}
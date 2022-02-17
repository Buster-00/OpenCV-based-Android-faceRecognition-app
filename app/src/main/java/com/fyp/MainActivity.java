package com.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //widget
    Button btn_register;
    Button btn_recognition;
    Button btn_train;

    //handler
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_register = findViewById(R.id.btn_register);
        btn_recognition = findViewById(R.id.btn_recognition);
        btn_train = findViewById(R.id.btn_train);

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
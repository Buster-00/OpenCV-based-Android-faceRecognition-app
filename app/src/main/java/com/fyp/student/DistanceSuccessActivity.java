package com.fyp.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fyp.FaceRecognitionActivity;
import com.fyp.R;
import com.google.zxing.integration.android.IntentIntegrator;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;

public class DistanceSuccessActivity extends AppCompatActivity {

    //widget
    Button btn_next;
    TextView tv_distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_success);

        //Initialize widget
        btn_next = findViewById(R.id.btn_next);
        tv_distance = findViewById(R.id.tv_distance);

        //Set onclick function
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DistanceSuccessActivity.this, FaceRecognitionActivity.class);
                intent.putExtras(getIntent());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        //set TextView
//        double distance = getIntent().getDoubleExtra("DISTANCE", 0);
//        tv_distance.setText("your distance is: " + (int)distance + " meters");

        //Initiate JavaCV
        Loader.load(opencv_java.class);
    }
}
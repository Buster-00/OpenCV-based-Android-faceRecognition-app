package com.fyp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.fyp.R;

public class SignAsActivity extends AppCompatActivity {

    //Widget
    ImageButton btn_logInAsStudent;
    ImageButton btn_logInAsLecturer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_as);

        //Initialize widget
        btn_logInAsLecturer = findViewById(R.id.btn_loginAsLecturer);
        btn_logInAsStudent = findViewById(R.id.btn_loginAsStudent);

        btn_logInAsStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignAsActivity.this, MTLogin.class);
                startActivity(intent);
            }
        });

        btn_logInAsLecturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignAsActivity.this, MTLoginLecturer.class);
                startActivity(intent);
            }
        });
    }
}
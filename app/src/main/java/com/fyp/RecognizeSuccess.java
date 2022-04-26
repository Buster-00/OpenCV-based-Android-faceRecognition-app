package com.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fyp.databaseHelper.AttendanceDB;
import com.fyp.databaseHelper.UserManager;
import com.fyp.lecturer.AttendanceSheetActivity;
import com.github.chengang.library.TickView;

public class RecognizeSuccess extends AppCompatActivity {

    TextView tv_recognize_success;
    TickView tickView;
    Button btn_finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognize_success);

        tv_recognize_success = findViewById(R.id.tv_recognize_success);
        btn_finish = findViewById(R.id.btn_finish);
        tickView = findViewById(R.id.tick_view);

        String LectureID = getIntent().getStringExtra("LectureID");
        String LectureName = getIntent().getStringExtra("LectureName");
        String Date = getIntent().getStringExtra("Date");
        tv_recognize_success.setText("Attend " + LectureID + " " + LectureName + "Success!");

        tickView.toggle();

        //add record data to database
        AttendanceDB attendanceDB = new AttendanceDB(this);
        attendanceDB.insert(LectureID, UserManager.getCurrentUser().getID(), Date);

        //initialize button
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizeSuccess.this, AttendanceSheetActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("NAME", "Bob");
                startActivity(intent);
            }
        });

    }
}
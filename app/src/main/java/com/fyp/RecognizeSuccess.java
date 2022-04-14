package com.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.fyp.databaseHelper.AttendanceDB;
import com.fyp.databaseHelper.UserManager;
import com.github.chengang.library.TickView;

public class RecognizeSuccess extends AppCompatActivity {

    TextView tv_recognize_success;
    TickView tickView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognize_success);

        tv_recognize_success = findViewById(R.id.tv_recognize_success);
        tickView = findViewById(R.id.tick_view);

        String LectureID = getIntent().getStringExtra("LectureID");
        String LectureName = getIntent().getStringExtra("LectureName");
        String Date = getIntent().getStringExtra("Date");
        tv_recognize_success.setText("Attend " + LectureID + " " + LectureName + "Success!");

        tickView.toggle();

        //add record data to database
        AttendanceDB attendanceDB = new AttendanceDB(this);
        attendanceDB.insert(LectureID, UserManager.getCurrentUser().getID(), Date);

    }
}
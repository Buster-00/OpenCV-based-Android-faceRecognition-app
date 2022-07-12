package com.fyp.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fyp.R;
import com.fyp.databaseHelper.AttendanceDB;
import com.fyp.databaseHelper.UserManager;
import com.fyp.utilities.QRCodeHelper;
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

        //get data from json
        String json = getIntent().getStringExtra("JSON");
        ObjectMapper mapper = new JsonMapper();
        QRCodeHelper.QRInformation info = null;
        try {
            info = mapper.readValue(json, QRCodeHelper.QRInformation.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String LectureID = info.getLectureID();
        String LectureName = info.getLectureName();
        String Date = info.getDate();
        String lecturerName = info.getLecturer();
        String venue = info.getVenue();
        String lecturerID = info.getLecturerID();
        String studentName = UserManager.getCurrentUser().getName();

        tv_recognize_success.setText("Attend " + LectureID + " " + LectureName + " Success!");



        //add record data to database
        AttendanceDB attendanceDB = new AttendanceDB(this);
        boolean isSuccess = attendanceDB.insert(LectureID, UserManager.getCurrentUser().getID(), Date, lecturerName, venue, lecturerID, LectureName,studentName);
        Log.e("Database", "Insert attendance" + LectureID + UserManager.getCurrentUser().getID() + Date + lecturerName + venue + " " +isSuccess);

        tickView.toggle();

        //initialize button
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(RecognizeSuccess.this, AttendanceSheetActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                intent.putExtra("NAME", "Bob");
//                startActivity(intent);

                Intent intent = new Intent(RecognizeSuccess.this, Student_MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("NAME", "Bob");
                startActivity(intent);
            }
        });

    }
}
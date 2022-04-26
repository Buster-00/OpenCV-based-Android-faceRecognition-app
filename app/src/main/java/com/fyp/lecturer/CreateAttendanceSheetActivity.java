package com.fyp.lecturer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.fyp.R;

import java.util.Date;

public class CreateAttendanceSheetActivity extends AppCompatActivity {

    //widget
    EditText et_LectureID;
    EditText et_LectureName;
    EditText et_Venue;
    CalendarView calendarView;
    Button btn_create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_attendance_sheet);

        //Initialize widget
        et_LectureID = findViewById(R.id.et_lectureID);
        et_LectureName = findViewById(R.id.et_lectureName);
        et_Venue = findViewById(R.id.et_Venue);
        calendarView = findViewById(R.id.calendar_view);
        btn_create = findViewById(R.id.btn_create);

        //set widgets

        final String[] date = {new String()};
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                date[0] = year + "-" + month + "-" + day;

            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Retrieve information
                String lectureID = et_LectureID.getText().toString();
                String lectureName = et_LectureName.getText().toString();
                String venue = et_Venue.getText().toString();
                Toast.makeText(CreateAttendanceSheetActivity.this,
                        lectureID + "-" + lectureName + "-" + venue + "\n" + date[0],
                        Toast.LENGTH_LONG).show();

                //Turn to next activity
                Intent intent = new Intent(CreateAttendanceSheetActivity.this, AttendanceSheetActivity.class);
                startActivity(intent);
            }
        });

    }
}
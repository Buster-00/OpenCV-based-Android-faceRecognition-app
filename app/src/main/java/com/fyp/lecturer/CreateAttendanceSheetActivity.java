package com.fyp.lecturer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.fyp.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.Date;
import java.util.List;

public class CreateAttendanceSheetActivity extends AppCompatActivity implements Validator.ValidationListener{

    //widget
    @NotEmpty
    EditText et_LectureID;

    @NotEmpty
    EditText et_LectureName;

    @NotEmpty
    EditText et_Venue;

    CalendarView calendarView;
    Button btn_create;
    Toolbar toolbar;

    //Validator
    Validator validator;

    //date
    final String[] date = {new String()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_attendance_sheet);

        //Initialize Validator
        initValidator();

        //Initialize widget
        et_LectureID = findViewById(R.id.et_lectureID);
        et_LectureName = findViewById(R.id.et_lectureName);
        et_Venue = findViewById(R.id.et_Venue);
        calendarView = findViewById(R.id.calendar_view);
        btn_create = findViewById(R.id.btn_create);
        toolbar = findViewById(R.id.toolbar);

        //set widgets


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                date[0] = year + "-" + month + "-" + day;

            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initValidator(){
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onValidationSucceeded() {
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

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);


            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }

        }
    }


}
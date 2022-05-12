package com.fyp.lecturer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.fyp.FaceRecognitionActivity;
import com.fyp.R;
import com.fyp.databaseHelper.AttendanceDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.Vector;

public class AttendanceSheetActivity extends AppCompatActivity {

    //widget
    FloatingActionButton btn_add_student;
    FloatingActionButton btn_cancel;
    FloatingActionButton btn_done;
    RecyclerView recycle_student_list;
    Toolbar toolbar;

    //Recycler view data
    Vector<AttendanceDB.AttendanceRecord> data = new Vector<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_sheet);

        //initialize widget
        btn_add_student = findViewById(R.id.btn_add_student);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_done = findViewById(R.id.btn_done);
        recycle_student_list = findViewById(R.id.recycle_student_list);
        toolbar = findViewById(R.id.toolbar);

        btn_add_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttendanceSheetActivity.this, FaceRecognitionActivity.class);
                startActivity(intent);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttendanceSheetActivity.this, CreateAttendanceSheetActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                Toast.makeText(AttendanceSheetActivity.this, "Cancel the attendance sheet", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });

        Log.e("AttendanceSheet", "onCreate");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onNewIntent(Intent newIntent){
        super.onNewIntent(newIntent);
        setIntent(newIntent);
    }

    @Override
    protected  void onResume() {
        super.onResume();
        Log.e("AttendanceSheet", "onResume");
        Intent intent = getIntent();
        String studentName = intent.getStringExtra("NAME");

        //initialize recycler_view student list
        if(studentName != null){
            AttendanceDB.AttendanceRecord attendanceRecord = new AttendanceDB.AttendanceRecord(studentName, "swe1809223", "2022-1-1", "John", "D1-404", "D123", "software engineering");
            data.add(attendanceRecord);
            Log.e("AttendanceSheet", ""+data.size());
            Log.e("AttendanceSheet", "studentName" + studentName);
        }


        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recycle_student_list.setLayoutManager(mLayoutManager);
        recycle_student_list.setAdapter(new CommonAdapter<AttendanceDB.AttendanceRecord>(this, R.layout.listview_attedance_record, data) {
            @Override
            protected void convert(ViewHolder holder, AttendanceDB.AttendanceRecord o, int position) {
                holder.setText(R.id.tv_lectureID, o.getLectureID());
                holder.setText(R.id.tv_date,o.getDate());
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            //do something.
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

}
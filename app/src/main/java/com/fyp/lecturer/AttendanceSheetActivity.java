package com.fyp.lecturer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
    RecyclerView recycle_student_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_sheet);

        //initialize widget
        btn_add_student = findViewById(R.id.btn_add_student);
        recycle_student_list = findViewById(R.id.recycle_student_list);

        btn_add_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttendanceSheetActivity.this, FaceRecognitionActivity.class);
                startActivity(intent);
            }
        });

        //initialize recycler_view student list
        Vector<AttendanceDB.AttendanceRecord> data = new Vector<>();
        AttendanceDB.AttendanceRecord attendanceRecord = new AttendanceDB.AttendanceRecord("swe101", "swe1809223", "2022-1-1");
        data.add(attendanceRecord);

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
}
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
    RecyclerView recycle_student_list;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_sheet);

        //initialize widget
        recycle_student_list = findViewById(R.id.recycle_student_list);
        toolbar = findViewById(R.id.toolbar);


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
        String lectureID = intent.getStringExtra("LectureID");
        Log.e("lectureID", lectureID);

        //initialize recycler_view student list
        AttendanceDB DB = new AttendanceDB(this);
        Vector<AttendanceDB.AttendanceRecord> data = DB.getAttendanceByLecturerID(lectureID);
        Log.e("lectureID", "size" + data.size());





        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recycle_student_list.setLayoutManager(mLayoutManager);
        recycle_student_list.setAdapter(new CommonAdapter<AttendanceDB.AttendanceRecord>(this, R.layout.recycleview_student_item, data) {
            @Override
            protected void convert(ViewHolder holder, AttendanceDB.AttendanceRecord o, int position) {
                holder.setText(R.id.tv_studentID, o.getStudentID());
                holder.setText(R.id.tv_studentName,o.getStudentName());
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
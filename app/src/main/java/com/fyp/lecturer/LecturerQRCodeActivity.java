package com.fyp.lecturer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.R;
import com.fyp.databaseHelper.AttendanceDB;
import com.fyp.utilities.QRCodeHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.Vector;

public class LecturerQRCodeActivity extends AppCompatActivity {

    //Widget
    ImageView img_QRCode;

    //
    QRCodeHelper.QRInformation info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_qrcode);

        //Initialize widget
        img_QRCode = findViewById(R.id.img_QRCode);
        Button btn_settings = findViewById(R.id.btn_settings);
        Button btn_refresh = findViewById(R.id.btn_refresh);
        RecyclerView recyclerView = findViewById(R.id.recycle_view);

        //Initialize QR Code
        //GET JSON
        String json = getIntent().getStringExtra("INFO");
        img_QRCode.setImageBitmap(QRCodeHelper.createQRCodeBitmap(json, 320, 320));
        ObjectMapper mapper = new ObjectMapper();
        try {
            info = mapper.readValue(json, QRCodeHelper.QRInformation.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //Initialize student list
        AttendanceDB DB = new AttendanceDB(LecturerQRCodeActivity.this);
        Vector<AttendanceDB.AttendanceRecord> data = DB.getAttendanceByLecturerID(info.getLectureID());

        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new CommonAdapter<AttendanceDB.AttendanceRecord>(LecturerQRCodeActivity.this, R.layout.recycleview_student_item, data) {
            @Override
            protected void convert(ViewHolder holder, AttendanceDB.AttendanceRecord record, int position) {
                holder.setText(R.id.tv_studentName, record.getStudentName());
                holder.setText(R.id.tv_studentID, record.getStudentID());
            }
        });


        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LecturerQRCodeActivity.this, CreateAttendanceSheetActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        //set refresh
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttendanceDB DB = new AttendanceDB(LecturerQRCodeActivity.this);
                Vector<AttendanceDB.AttendanceRecord> data = DB.getAttendanceByLecturerID(info.getLectureID());

                RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);

                recyclerView.setAdapter(new CommonAdapter<AttendanceDB.AttendanceRecord>(LecturerQRCodeActivity.this, R.layout.recycleview_student_item, data) {
                    @Override
                    protected void convert(ViewHolder holder, AttendanceDB.AttendanceRecord record, int position) {
                        holder.setText(R.id.tv_studentName, record.getStudentName());
                        holder.setText(R.id.tv_studentID, record.getStudentID());
                    }
                });
            }
        });

    }
}
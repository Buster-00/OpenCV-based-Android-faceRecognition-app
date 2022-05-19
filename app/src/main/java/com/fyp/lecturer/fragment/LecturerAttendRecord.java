package com.fyp.lecturer.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fyp.R;
import com.fyp.databaseHelper.AttendanceDB;
import com.fyp.databaseHelper.Lecture;
import com.fyp.databaseHelper.LectureDB;
import com.fyp.databaseHelper.UserManager;
import com.fyp.helper.QRCodeHelper;
import com.fyp.lecturer.AttendanceSheetActivity;
import com.fyp.lecturer.LecturerQRCodeActivity;
import com.ramotion.foldingcell.FoldingCell;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.Vector;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LecturerAttendRecord#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LecturerAttendRecord extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LecturerAttendRecord() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LecturerAttendRecord.
     */
    // TODO: Rename and change types and number of parameters
    public static LecturerAttendRecord newInstance(String param1, String param2) {
        LecturerAttendRecord fragment = new LecturerAttendRecord();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lecturer_attend_record, container, false);

        //Widget
        RecyclerView recyclerView = view.findViewById(R.id.recycle_view);

        //Initialize recyclerView
        LectureDB DB = new LectureDB(getActivity());
        Vector<Lecture> data = DB.getLectureByID(UserManager.getCurrentUser().getID());

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new CommonAdapter<Lecture>(getActivity(), R.layout.listview_carditem, data) {
            @Override
            protected void convert(ViewHolder holder, Lecture record, int position) {
                FoldingCell foldingCell = holder.getView(R.id.folding_cell);
                holder.setText(R.id.tv_lectureName, record.getLectureName());
                holder.setText(R.id.tv_lectureID,record.getLectureID());
                holder.setText(R.id.tv_date, record.getDate());
                holder.setText(R.id.tv_venue, record.getVenue());
                Button btn_viewDetail = holder.getView(R.id.btn_click);
                Button btn_addStudent = holder.getView(R.id.btn_addStudent);

                btn_viewDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), AttendanceSheetActivity.class);
                        intent.putExtra("LectureID", record.getLectureID());
                        startActivity(intent);
                    }
                });

                btn_addStudent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), LecturerQRCodeActivity.class);

                        //set info
                        QRCodeHelper.QRInformation info = new QRCodeHelper.QRInformation();
                        info.setDate(record.getDate());
                        info.setLectureID(record.getLectureID());
                        info.setLecturer(UserManager.getCurrentUser().getName());
                        info.setLecturerID(record.getLectureID());
                        info.setLectureName(record.getLectureName());
                        info.setVenue(record.getVenue());
                        info.setLatitude(UserManager.location.getLatitude());
                        info.setLongitude(UserManager.location.getLongitude());
                        ObjectMapper mapper = new JsonMapper();
                        String json = new String();
                        try {
                            json = mapper.writeValueAsString(info);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }

                        intent.putExtra("INFO", json);
                        startActivity(intent);
                    }
                });

                foldingCell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        foldingCell.toggle(false);
                    }
                });
            }
        });

        return view;
    }
}
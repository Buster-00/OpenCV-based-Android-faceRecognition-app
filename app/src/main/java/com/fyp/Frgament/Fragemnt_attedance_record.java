package com.fyp.Frgament;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.dinuscxj.refresh.RefreshView;
import com.fyp.R;
import com.fyp.databaseHelper.AttendanceDB;
import com.fyp.databaseHelper.UserManager;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.HashMap;
import java.util.Vector;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragemnt_attedance_record#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragemnt_attedance_record extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragemnt_attedance_record() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragemnt_attedance_record.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragemnt_attedance_record newInstance(String param1, String param2) {
        Fragemnt_attedance_record fragment = new Fragemnt_attedance_record();
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
        View view = inflater.inflate(R.layout.fragment_attendace_record, container, false);

        //Widget
        RecyclerView recyclerView = view.findViewById(R.id.recycle_view);
        RecyclerRefreshLayout recyclerRefreshLayout = view.findViewById(R.id.refresh_layout);

        //Bind onRefresh method to refreshView
        recyclerRefreshLayout.setOnRefreshListener(new RecyclerRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AttendanceDB attendanceDB = new AttendanceDB(getActivity());
                Vector<AttendanceDB.AttendanceRecord> mDatas = new Vector<>();

                Vector<AttendanceDB.AttendanceRecord> data = attendanceDB.getAttendanceByStudentID(UserManager.getCurrentUser().getID());

                //Set Adapter to recycle view
                recyclerView.setAdapter(new CommonAdapter<AttendanceDB.AttendanceRecord>(getActivity(), R.layout.fragment_attendace_record, mDatas) {
                    @Override
                    protected void convert(ViewHolder holder, AttendanceDB.AttendanceRecord attendanceData, int position) {
                        holder.setText(R.id.tv_lectureID, attendanceData.getLectureID());
                        holder.setText(R.id.tv_date,attendanceData.getDate());
                    }

                });

            }
        });

        //Retrieve attendance record from database
        AttendanceDB attendanceDB = new AttendanceDB(getActivity());
        Vector<AttendanceDB.AttendanceRecord> mDatas = new Vector<>();

        Vector<AttendanceDB.AttendanceRecord> data = attendanceDB.getAttendanceByStudentID(UserManager.getCurrentUser().getID());

        //Set Adapter to recycle view
        recyclerView.setAdapter(new CommonAdapter<AttendanceDB.AttendanceRecord>(getActivity(), R.layout.fragment_attendace_record, mDatas) {
            @Override
            protected void convert(ViewHolder holder, AttendanceDB.AttendanceRecord attendanceData, int position) {
                holder.setText(R.id.tv_lectureID, attendanceData.getLectureID());
                holder.setText(R.id.tv_date,attendanceData.getDate());
            }

        });

        return view;
    }


}
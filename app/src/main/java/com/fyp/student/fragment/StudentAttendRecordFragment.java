package com.fyp.student.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fyp.R;
import com.fyp.databaseHelper.AttendanceDB;
import com.fyp.databaseHelper.UserManager;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.Vector;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentAttendRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentAttendRecordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StudentAttendRecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentAttendRecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentAttendRecordFragment newInstance(String param1, String param2) {
        StudentAttendRecordFragment fragment = new StudentAttendRecordFragment();
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
        View view = inflater.inflate(R.layout.fragment_student_attend_record, container, false);

        //Widget
        RecyclerView recyclerView = view.findViewById(R.id.rec_view);

        //set recyclerView
        AttendanceDB DB = new AttendanceDB(getActivity());
        Vector<AttendanceDB.AttendanceRecord> data =  DB.getAttendanceByStudentID(UserManager.getCurrentUser().getID());

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        recyclerView.setAdapter(new CommonAdapter<AttendanceDB.AttendanceRecord>(getActivity(), R.layout.listview_attedance_record, data) {
            @Override
            protected void convert(ViewHolder holder, AttendanceDB.AttendanceRecord o, int position) {
                holder.setText(R.id.tv_lectureID, o.getLectureID());
                holder.setText(R.id.tv_date, o.getDate());
            }
        });

        return view;
    }
}
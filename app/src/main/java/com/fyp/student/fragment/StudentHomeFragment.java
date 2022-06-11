package com.fyp.student.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fyp.FaceRegisterActivity;
import com.fyp.R;
import com.fyp.databaseHelper.AttendanceDB;
import com.fyp.databaseHelper.UserManager;
import com.fyp.student.QRScanActivity;

import java.util.Vector;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StudentHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentHomeFragment newInstance(String param1, String param2) {
        StudentHomeFragment fragment = new StudentHomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_student_home, container, false);

        //widget
        Button btn_face_register;
        Button btn_location;
        Button btn_record;
        TextView tv_username = view.findViewById(R.id.tv_userName);
        TextView tv_records = view.findViewById(R.id.tv_records);

        //initialize widgets
        btn_face_register = view.findViewById(R.id.btn_face_register);
        btn_location = view.findViewById(R.id.btn_location);
        btn_record = view.findViewById(R.id.btn_record);

        btn_face_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FaceRegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), QRScanActivity.class));
            }
        });

        tv_username.setText(UserManager.getCurrentUser().getName());
        AttendanceDB DB = new AttendanceDB(getActivity());
        Vector<AttendanceDB.AttendanceRecord> data = DB.getAttendanceByStudentID(UserManager.getCurrentUser().getID());
        tv_records.setText(String.valueOf(data.size()));

        return view;
    }
}
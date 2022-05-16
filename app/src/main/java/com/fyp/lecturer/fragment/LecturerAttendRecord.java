package com.fyp.lecturer.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fyp.R;
import com.fyp.databaseHelper.AttendanceDB;
import com.fyp.databaseHelper.UserManager;
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
        AttendanceDB DB = new AttendanceDB(getActivity());
        Vector<AttendanceDB.AttendanceRecord> data = DB.getAttendanceByLecturerID(UserManager.getCurrentUser().getID());

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new CommonAdapter<AttendanceDB.AttendanceRecord>(getActivity(), R.layout.listview_carditem, data) {
            @Override
            protected void convert(ViewHolder holder, AttendanceDB.AttendanceRecord record, int position) {
                FoldingCell foldingCell = holder.getView(R.id.folding_cell);
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
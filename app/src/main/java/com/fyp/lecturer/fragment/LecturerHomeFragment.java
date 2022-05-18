package com.fyp.lecturer.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fyp.R;
import com.fyp.databaseHelper.UserManager;
import com.fyp.lecturer.AttendanceSheetActivity;
import com.fyp.lecturer.CreateAttendanceSheetActivity;

import org.w3c.dom.Text;

import java.util.Calendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LecturerHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LecturerHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LecturerHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LecturerHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LecturerHomeFragment newInstance(String param1, String param2) {
        LecturerHomeFragment fragment = new LecturerHomeFragment();
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

        View view = inflater.inflate(R.layout.fragment_lecturer_home, container, false);

        //Widget
        Button btn_createNewAttendanceSheet = view.findViewById(R.id.btn_newAttendanceSheet);
        Button btn_viewAttendRecord = view.findViewById(R.id.btn_viewAttendRecord);
        TextView tv_username = view.findViewById(R.id.tv_userName);

        //Initialization
        //setCalendarView(view);

        btn_createNewAttendanceSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateAttendanceSheetActivity.class);
                startActivity(intent);
            }
        });

        btn_viewAttendRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateAttendanceSheetActivity.class);
                startActivity(intent);
            }
        });

        tv_username.setText(UserManager.getCurrentUser().getName());

        // Inflate the layout for this fragment
        return view;
    }

    private void setCalendarView(View view){
        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendar_view)
                .range(startDate, endDate)
                .datesNumberOnScreen(1)
                .build();


        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView,
                                         int dx, int dy) {

            }

            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                return true;
            }
        });
    }
}
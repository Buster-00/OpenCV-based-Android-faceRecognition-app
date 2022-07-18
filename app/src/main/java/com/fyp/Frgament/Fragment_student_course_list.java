package com.fyp.Frgament;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.fyp.FaceRecognitionActivity;
import com.fyp.R;
import com.fyp.databaseHelper.Lecture;
import com.fyp.databaseHelper.LectureDB;
import com.fyp.databaseHelper.StudentLectureDB;
import com.fyp.databaseHelper.UserManager;
import com.github.chengang.library.TickView;
import com.ramotion.foldingcell.FoldingCell;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_student_course_list#newInstance} factory method to
 * create an instance of this fragment.
 */

public class Fragment_student_course_list extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    StudentLectureDB studentLectureDB;
    LectureDB lectureDB;

    //widget
    ListView listView;

    public Fragment_student_course_list() {
        // Required empty public constructor
    }

    public static Fragment_student_course_list newInstance(String param1, String param2) {
        Fragment_student_course_list fragment = new Fragment_student_course_list();
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

        studentLectureDB = new StudentLectureDB(getActivity());
        lectureDB = new LectureDB(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_course_list, container, false);

        //widget
        listView = view.findViewById(R.id.list_view);
        RecyclerRefreshLayout recyclerRefreshLayout = view.findViewById(R.id.refresh_layout);

        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        //initialize widget

        //initalize calendar
        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

            }
        });


        CardItemAdapter IA = new CardItemAdapter(getActivity(), R.layout.listview_carditem);

        //generate cardItem
        Vector<String> IDs = studentLectureDB.getLecturesIDsByStudentID(UserManager.getCurrentUser().getID());
        Vector<Lecture> lectureVector = new Vector<>();

        for(String ID : IDs){
            //Lecture lecture = lectureDB.getLectureByID(ID);
            Lecture lecture = null;
            lectureVector.add(lecture);
        }

        for(Lecture lecture : lectureVector){
            CardItem cardItem = new CardItem(lecture, 1);
            IA.add(cardItem);
        }

        listView.setAdapter(IA);

        //initialize refresh recycle view
        recyclerRefreshLayout.setOnRefreshListener(new RecyclerRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerRefreshLayout.setRefreshing(true);
                CardItemAdapter IA = new CardItemAdapter(getActivity(), R.layout.listview_carditem);
                StudentLectureDB studentLectureDB = new StudentLectureDB(getActivity());
                LectureDB lectureDB = new LectureDB(getActivity());
                Vector<String> IDs = studentLectureDB.getLecturesIDsByStudentID(UserManager.getCurrentUser().getID());
                Vector<Lecture> lectureVector = new Vector<>();

                for(String ID : IDs){
                    //Lecture lecture = lectureDB.getLectureByID(ID);
                    Lecture lecture = null;
                    lectureVector.add(lecture);
                }

                for(Lecture lecture : lectureVector){
                    CardItem cardItem = new CardItem(lecture, 1);
                    IA.add(cardItem);
                }

                listView.setAdapter(IA);
                recyclerRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }


    private class CardItem{
        Lecture lecture;
        private String name;
        private int icon;

        public CardItem(Lecture lecture, int icon){
            this.lecture = lecture;
            this.icon = icon;
        }

        public String getName(){
            return lecture.getLectureName();
        }

        public String getID(){return lecture.getLectureID();}

        public int getIcon(){
            return icon;
        }

    }

    private class CardItemAdapter extends ArrayAdapter<CardItem>{

        static final int HANDLE_MSG_TOGGLE = 1;
        static final int TOGGLE_DELAY_TIME = 3000;

        private int resourceID;

        public CardItemAdapter(@NonNull Context context, int resource) {
            super(context, resource);
            resourceID = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            //get item from array Adapter
            CardItem cardItem = getItem(position);
            View view;
            ViewHolder viewHolder;
            final String[] date = {new String()};

            //initialize layout
            if(convertView == null){
                view = LayoutInflater.from(getContext()).inflate(resourceID, null);
                viewHolder = new ViewHolder();
                viewHolder.calendarView = view.findViewById(R.id.calendar_view);
                viewHolder.fc = view.findViewById(R.id.folding_cell);
                viewHolder.tk = view.findViewById(R.id.tick_view);
                viewHolder.btn_click = view.findViewById(R.id.btn_click);
                viewHolder.tv_lectureID = view.findViewById(R.id.tv_lectureID);
                view.setTag(viewHolder);
            }
            else{
                view = convertView;
                viewHolder = (ViewHolder)view.getTag();
            }

            //set widget
            viewHolder.fc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.fc.toggle(false);
                }
            });

            viewHolder.tvCardItem.setText(cardItem.getName());

            viewHolder.tv_lectureID.setText(cardItem.getID());

            viewHolder.tk.setClickable(false);

            Handler mHandler = new Handler(){
                public void handleMessage(Message msg){
                    switch (msg.what){
                        case HANDLE_MSG_TOGGLE:
                            viewHolder.tk.toggle();
                    }
                }
            };

            viewHolder.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                    Toast.makeText(getActivity(), "year:" + i + " month:" + i1 + " day:" + i2, Toast.LENGTH_LONG).show();
                    date[0] = "year:" + i + " month:" + i1 + " day:" + i2;
                }
            });

            viewHolder.btn_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = HANDLE_MSG_TOGGLE;
                            mHandler.sendMessage(msg);
                        }
                    };
                    timer.schedule(task, TOGGLE_DELAY_TIME);

                    Intent intent = new Intent(getActivity(), FaceRecognitionActivity.class);
                    intent.putExtra("LectureID", cardItem.getID());
                    intent.putExtra("LectureName", cardItem.getName());
                    intent.putExtra("Date", date[0]);
                    Log.e("LectureID", cardItem.getID());
                    boolean recognitionResult = false;
                    startActivity(intent);

                }
            });

            return view;
        }

         class ViewHolder{
            public CalendarView calendarView;
            public Button btn_click;
            public TickView tk;
            public FoldingCell fc;
            public TextView tvCardItem;
            public TextView tv_lectureID;
        }
    }
}
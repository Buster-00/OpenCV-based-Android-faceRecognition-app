package com.fyp.Frgament;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.fyp.R;
import com.fyp.databaseHelper.Lecture;
import com.fyp.databaseHelper.LectureDB;
import com.fyp.databaseHelper.StudentLectureDB;
import com.fyp.databaseHelper.UserManager;
import com.ramotion.foldingcell.FoldingCell;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.Random;
import java.util.Vector;

public class Fragment_course_list extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    //widget
    RecyclerRefreshLayout recyclerRefreshLayout;
    RecyclerView rec_course;
    RecyclerView.LayoutManager mLayoutManager;
    Vector<CourseData> mDatas = new Vector<>();

    public Fragment_course_list() {
        // Required empty public constructor
    }

    public static Fragment_course_list newInstance(String param1, String param2) {
        Fragment_course_list fragment = new Fragment_course_list();
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
        View view = inflater.inflate(R.layout.fragment_course_list, container, false);

        //Initiate View
        recyclerRefreshLayout = view.findViewById(R.id.refresh_layout);
        rec_course = view.findViewById(R.id.rec_course);
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        LectureDB lectureDB = new LectureDB(getActivity());
        Vector<Lecture> lectureData = lectureDB.getAllLecture();
        for(Lecture lecture : lectureData){
            CourseData data = new CourseData();
            data.courseID = lecture.getLectureID();
            data.courseName = lecture.getLectureName();
            data.lecturer = lecture.getLectureName();
            data.time = lecture.getDate();
            mDatas.add(data);
        }

        Random random = new Random();

        rec_course.setLayoutManager(mLayoutManager);
        rec_course.setAdapter(new CommonAdapter<CourseData>(getActivity(), R.layout.recycleview_carditem, mDatas){
            @Override
            protected void convert(ViewHolder holder, CourseData data, int position) {
                holder.setText(R.id.tv_courseName, data.courseID + " " + data.courseName);
                holder.setText(R.id.tv_lecturer, data.lecturer);
                holder.setText(R.id.tv_time, data.time);
                holder.setOnClickListener(R.id.btn_enter_lecture, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
                        Button button = holder.getView(R.id.btn_enter_lecture);

                        //Enter class
                        StudentLectureDB DB = new StudentLectureDB(getActivity());
                        if(DB.insert(UserManager.getCurrentUser().getID(), data.courseID)){
                            Toast.makeText(getActivity(), "Enter class successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getActivity(), "Enter class failure, you have entered this class before", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



                if(random.nextBoolean()){
                    if(random.nextBoolean()){
                        holder.setImageDrawable(R.id.img_course, getActivity().getDrawable(R.drawable.triangle_blue));
                    }else
                    {
                        holder.setImageDrawable(R.id.img_course, getActivity().getDrawable(R.drawable.triangle_purple));
                    }
                }


            }
        });

        recyclerRefreshLayout.setOnRefreshListener(new RecyclerRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerRefreshLayout.setRefreshing(true);
                boolean hasChanged = false;
                Vector<Lecture> lectureData = lectureDB.getAllLecture();
                for(Lecture lecture : lectureData){
                    CourseData data = new CourseData();
                    data.courseID = lecture.getLectureID();
                    data.courseName = lecture.getLectureName();
                    data.lecturer = lecture.getLectureName();
                    data.time = lecture.getDate();

                    if(!mDatas.contains(data)){
                        mDatas.add(data);
                        hasChanged = true;
                    }

                    if (hasChanged){
                        rec_course.getAdapter().notifyDataSetChanged();
                    }

                    recyclerRefreshLayout.setRefreshing(false);
                }
            }
        });

        return view;
    }

    class CourseData{
        public String time;
        public String courseID;
        public String courseName;
        public String lecturer;

        @Override
        public boolean equals(@Nullable Object object) {

            CourseData obj = (CourseData) object;
            if(obj.time.equals(time) && obj.courseID.equals(courseID) && obj.courseName.equals(courseName) && obj.lecturer.equals(lecturer)){
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
package com.fyp.Frgament;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fyp.FaceRecognitionActivity;
import com.fyp.R;
import com.fyp.databaseHelper.Lecture;
import com.fyp.databaseHelper.LectureDB;
import com.github.chengang.library.TickView;
import com.ramotion.foldingcell.FoldingCell;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_course_list, container, false);

        listView = view.findViewById(R.id.list_view);

        CardItemAdapter IA = new CardItemAdapter(getActivity(), R.layout.listview_carditem);

        //generate cardItem
        LectureDB lectureDB = new LectureDB(getActivity());
        Vector<Lecture> lectureVector= lectureDB.getAllLecture();

        for(Lecture lecture : lectureVector){
            CardItem cardItem = new CardItem(lecture.getLectureName(), 1);
            IA.add(cardItem);
        }

        listView.setAdapter(IA);

        return view;
    }

    private class CardItem{
        private String name;
        private int icon;

        public CardItem(String name, int icon){
            this.name = name;
            this.icon = icon;
        }

        public String getName(){
            return name;
        }

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
            CardItem i = getItem(position);
            View view;
            ViewHolder viewHolder;

            //initialize layout
            if(convertView == null){
                view = LayoutInflater.from(getContext()).inflate(resourceID, null);
                viewHolder = new ViewHolder();
                viewHolder.fc = view.findViewById(R.id.folding_cell);
                viewHolder.tvCardItem = view.findViewById(R.id.tv_cardText);
                viewHolder.tk = view.findViewById(R.id.tick_view);
                viewHolder.btn_click = view.findViewById(R.id.btn_click);
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

            viewHolder.tvCardItem.setText(i.getName());

            viewHolder.tk.setClickable(false);

            Handler mHandler = new Handler(){
                public void handleMessage(Message msg){
                    switch (msg.what){
                        case HANDLE_MSG_TOGGLE:
                            viewHolder.tk.toggle();
                    }
                }
            };

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
                    boolean recognitionResult = false;
                    startActivityForResult(intent, 1);
                }
            });

            return view;
        }

         class ViewHolder{
            public Button btn_click;
            public TickView tk;
            public FoldingCell fc;
            public TextView tvCardItem;
        }
    }
}
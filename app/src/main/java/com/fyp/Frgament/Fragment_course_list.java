package com.fyp.Frgament;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fyp.R;
import com.ramotion.foldingcell.FoldingCell;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.Vector;

public class Fragment_course_list extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    //widget
    RecyclerView rec_course;
    RecyclerView.LayoutManager mLayoutManager;
    Vector<CardItem> mDatas = new Vector<>();

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
        rec_course = view.findViewById(R.id.rec_course);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        for(int i = 0 ; i < 10; i++){
            CardItem cardItem = new CardItem();
            mDatas.add(cardItem);
        }

        rec_course.setLayoutManager(mLayoutManager);
        rec_course.setAdapter(new CommonAdapter<CardItem>(getActivity(), R.layout.recycleview_carditem, mDatas){

            @Override
            protected void convert(ViewHolder holder, CardItem cardItem, int position) {
                holder.setText(R.id.tv_courseName,"course: " + position);
            }
        });


        return view;
    }

    class CardItem{

    }
}
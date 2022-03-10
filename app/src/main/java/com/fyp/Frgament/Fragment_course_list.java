package com.fyp.Frgament;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fyp.R;
import com.ramotion.foldingcell.FoldingCell;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_course_list#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_course_list extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    //widget
    ListView listView;

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

        listView = view.findViewById(R.id.list_view);

        CardItem i1 = new CardItem("tom", 1);
        CardItem i2 = new CardItem("jack", 2);
        CardItem i3 = new CardItem("john", 3);
        CardItemAdapter IA = new CardItemAdapter(getActivity(), R.layout.listview_carditem);
        IA.add(i1);
        IA.add(i2);
        IA.add(i3);
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
                view.setTag(viewHolder);
            }
            else{
                view = convertView;
                viewHolder = (ViewHolder)view.getTag();
            }

            //widget

            viewHolder.fc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.fc.toggle(false);
                }
            });

            viewHolder.tvCardItem.setText(i.getName());
            return view;
        }

         class ViewHolder{
            public FoldingCell fc;
            public TextView tvCardItem;
        }
    }
}
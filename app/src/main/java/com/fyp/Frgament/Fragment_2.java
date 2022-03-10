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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //widget
    ListView listView;

    public Fragment_2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_2.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_2 newInstance(String param1, String param2) {
        Fragment_2 fragment = new Fragment_2();
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
        View view = inflater.inflate(R.layout.fragment_3, container, false);

        listView = view.findViewById(R.id.list_view);

        item i1 = new item("tom", 1);
        item i2 = new item("jack", 2);
        item i3 = new item("john", 3);
        itemAdapter IA = new itemAdapter(getActivity(), R.layout.listview_item);
        IA.add(i1);
        IA.add(i2);
        IA.add(i3);
        listView.setAdapter(IA);

        return view;
    }

    private class item{
        private String name;
        private int icon;

        public item(String name, int icon){
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

    private class itemAdapter extends ArrayAdapter<item>{

        private int resourceID;

        public itemAdapter(@NonNull Context context, int resource) {
            super(context, resource);
            resourceID = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            item i = getItem(position);

            View view = LayoutInflater.from(getContext()).inflate(resourceID, null);
            TextView tv_item = view.findViewById(R.id.tv_item);
            tv_item.setText(i.getName());
            return view;
        }
    }
}
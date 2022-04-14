package com.fyp.Frgament;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.Random;
import java.util.Vector;

public class myAdapter extends FragmentPagerAdapter {


    public myAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    Vector<Fragment> mFragments = new Vector<>();

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.e("position", ""+position);
        switch (position) {
            case 0:
                Fragment_home fragment_home = new Fragment_home();
                return fragment_home;
            case 1:
                Log.e("fragment", "create new fragment");
                return new Fragment_student_course_list();
            case 2:
                return new Fragment_course_list();
            case 3:
                return new Fragemnt_attedance_record();
        }
        return new Fragment_home();
    }


    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
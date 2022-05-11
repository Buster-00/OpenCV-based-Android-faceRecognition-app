package com.fyp.student.fragment;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.fyp.Frgament.Fragment_profile;

public class studentViewPagerAdapter extends FragmentPagerAdapter {

    public studentViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new StudentHomeFragment();
            case 1:
                return new StudentAttendRecordFragment();
            case 2:
                return new Fragment_profile();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}

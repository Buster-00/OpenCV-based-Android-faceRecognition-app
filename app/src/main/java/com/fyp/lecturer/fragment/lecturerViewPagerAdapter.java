package com.fyp.lecturer.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fyp.Frgament.Fragment_profile;

public class lecturerViewPagerAdapter extends FragmentPagerAdapter {
    public lecturerViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new LecturerHomeFragment();
            case 1:
                return new LecturerAttendRecord();
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

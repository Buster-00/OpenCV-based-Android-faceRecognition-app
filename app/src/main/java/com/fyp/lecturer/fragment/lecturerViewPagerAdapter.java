package com.fyp.lecturer.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class lecturerViewPagerAdapter extends FragmentPagerAdapter {
    public lecturerViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new LecturerHomeFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }
}

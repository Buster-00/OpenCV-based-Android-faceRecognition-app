package com.fyp.student.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class studentViewPagerAdapter extends FragmentPagerAdapter {

    public studentViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new StudentHomeFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }
}

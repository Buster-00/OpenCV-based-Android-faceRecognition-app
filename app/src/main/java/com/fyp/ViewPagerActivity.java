package com.fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.fyp.Frgament.Fragment_1;
import com.fyp.Frgament.Fragment_2;
import com.fyp.Frgament.Fragment_3;
import com.fyp.Frgament.myAdapter;

public class ViewPagerActivity extends FragmentActivity {

    FragmentStateAdapter pagerAdapter;
    ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        viewPager = findViewById(R.id.ViewPager);
        viewPager.setAdapter(pagerAdapter);

    }

}


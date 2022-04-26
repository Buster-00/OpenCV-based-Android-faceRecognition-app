package com.fyp.lecturer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.fyp.R;
import com.fyp.lecturer.fragment.lecturerViewPagerAdapter;
import com.fyp.student.fragment.studentViewPagerAdapter;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;

public class Lecturer_MainActivity extends AppCompatActivity {

    //Widget
    ViewPager viewPager;
    PageNavigationView pageNavigationView;

    //Navigation bar
    NavigationController mNavigationController;
    int[] testColors = {0xFF1E90FF, 0xFF1E90FF, 0xFF1E90FF, 0xFF1E90FF, 0xFF1E90FF};
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_main);

        //widget
        viewPager = findViewById(R.id.ViewPager);
        pageNavigationView = findViewById(R.id.bottom_navigation);

        //Initialize bottom navigation bar
        mNavigationController = pageNavigationView.material()
                .addItem(R.drawable.ic_audiotrack_black_24dp, "Home1")
                .addItem(R.drawable.ic_audiotrack_black_24dp, "Home2")
                .addItem(R.drawable.ic_audiotrack_black_24dp, "Home3")
                .build();

        mNavigationController.setMessageNumber(0, 8);
        mNavigationController.setHasMessage(2, true);

        mNavigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
                //Toast.makeText(MainActivity.this, "selected: " + index + " old: " + old, Toast.LENGTH_SHORT).show();
                mNavigationController.setHasMessage(index, false);
                mNavigationController.setMessageNumber(index, 0);
                /*Alerter.create(MainActivity.this)
                        .setTitle("Alert Title")
                        .setText("Alert text...")
                        .show();*/
            }

            @Override
            public void onRepeat(int index) {
                //Toast.makeText(MainActivity.this, "repeated: " + index, Toast.LENGTH_SHORT).show();
                mNavigationController.setHasMessage(index, false);
                mNavigationController.setMessageNumber(index, 0);
            }
        });

        //Set ViewPager
        viewPager.setAdapter(new lecturerViewPagerAdapter(getSupportFragmentManager()));
        mNavigationController.setupWithViewPager(viewPager);

        //Initiate JavaCV
        Loader.load(opencv_java.class);
    }
}
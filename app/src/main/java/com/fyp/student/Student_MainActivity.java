package com.fyp.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.fyp.MainActivity;
import com.fyp.R;
import com.fyp.login.MTLogin;
import com.fyp.login.SignAsActivity;
import com.fyp.student.fragment.studentViewPagerAdapter;
import com.heinrichreimersoftware.materialdrawer.DrawerActivity;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;

public class Student_MainActivity extends DrawerActivity {

    //Widget
    ViewPager viewPager;
    PageNavigationView pageNavigationView;

    //Navigation bar
    NavigationController mNavigationController;
    int[] testColors = {0xFF1E90FF, 0xFF1E90FF, 0xFF1E90FF, 0xFF1E90FF, 0xFF1E90FF};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        //widget
        viewPager = findViewById(R.id.ViewPager);
        pageNavigationView = findViewById(R.id.bottom_navigation);

        //Initialize bottom navigation bar
        mNavigationController = pageNavigationView.material()
                .addItem(R.drawable.ic_home, "Home1")
                .addItem(R.drawable.ic_home, "Home2")
                .addItem(R.drawable.ic_baseline_person_24, "Home3")
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
        viewPager.setAdapter(new studentViewPagerAdapter(getSupportFragmentManager()));
        mNavigationController.setupWithViewPager(viewPager);

        //Drawer
        initMaterialDrawer();
    }

    private void initMaterialDrawer() {
        addProfile(
                new DrawerProfile()
                        .setRoundedAvatar((BitmapDrawable)this.getDrawable(R.drawable.cat_1))
                        .setBackground(this.getDrawable(R.drawable.bg))
                        .setName(getString(R.string.profile_name))
                        .setDescription(getString(R.string.profile_description))
                        .setOnProfileClickListener(new DrawerProfile.OnProfileClickListener() {
                            @Override
                            public void onClick(DrawerProfile drawerProfile, long id) {
                                //Toast.makeText(MainActivity.this, "Clicked profile #" + id, Toast.LENGTH_SHORT).show();
                            }
                        })
        );

        addItem(
                new DrawerItem()
                        .setImage(this.getDrawable(R.drawable.ic_first_item))
                        .setTextPrimary(getString(R.string.title_first_item))
                        .setTextSecondary(getString(R.string.description_first_item))
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long id, int position) {
                                //Toast.makeText(MainActivity.this, "Clicked first item #" + id, Toast.LENGTH_SHORT).show();
                            }
                        })
        );

        addDivider();

        addItem(
                new DrawerItem()
                        .setImage(this.getDrawable(R.drawable.ic_log_out))
                        .setTextPrimary(getString(R.string.log_out))
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long id, int position) {
                                //Toast.makeText(MainActivity.this, "Clicked second item #" + id, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Student_MainActivity.this, SignAsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
        );

    }

}
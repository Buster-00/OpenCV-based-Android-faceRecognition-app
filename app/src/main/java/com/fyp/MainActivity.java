package com.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.databaseHelper.StudentAccountDB;
import com.fyp.login.MTLogin;
import com.heinrichreimersoftware.materialdrawer.DrawerActivity;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;

import java.util.HashMap;

public class MainActivity extends DrawerActivity {

    //widget
    Button btn_register;
    Button btn_recognition;
    TextView tv_username;

    //handler
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initiate widgets
        btn_register = findViewById(R.id.btn_register);
        btn_recognition = findViewById(R.id.btn_recognition);
        tv_username = findViewById(R.id.tv_username);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtn_register();
            }
        });

        btn_recognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtn_recognition();
            }
        });

        StudentAccountDB DB = new StudentAccountDB(this);
        HashMap<String, String> hm = new HashMap<>();
        tv_username.setText(DB.ReadAll());

        //Drawer
        initMaterialDrawer();

        //Initiate JavaCV
        Loader.load(opencv_java.class);
    }



    protected void setBtn_register(){
        startActivity(new Intent(MainActivity.this, FaceRegisterActivity.class));
    }

    protected void setBtn_recognition(){
        startActivity(new Intent(MainActivity.this, FaceRecognitionActivity.class));
    }

    protected void setBtn_train(){
        startActivity(new Intent(MainActivity.this, TrainActivity.class));
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
                                Toast.makeText(MainActivity.this, "Clicked profile #" + id, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MainActivity.this, "Clicked first item #" + id, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MainActivity.this, "Clicked second item #" + id, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, MTLogin.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
        );

    }
}
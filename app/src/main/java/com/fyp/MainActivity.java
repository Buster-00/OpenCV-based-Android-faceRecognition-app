package com.fyp;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fyp.Frgament.myAdapter;
import com.fyp.databaseHelper.SQLiteStudent;
import com.fyp.databaseHelper.StudentDB;
import com.fyp.invariable.InVar;
import com.fyp.login.MTLogin;
import com.heinrichreimersoftware.materialdrawer.DrawerActivity;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends DrawerActivity {

    private static boolean IS_SAVE_ACCOUNT = false;

    //widget
    ViewPager viewPager;



    //handler
    private Handler mHandler;

    //Navigation bar
    NavigationController mNavigationController;
    int[] testColors = {0xFF455A64, 0xFF00796B, 0xFF795548, 0xFF5B4947, 0xFFF57C00};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initiate widgets
        viewPager = findViewById(R.id.ViewPager_main);
        PageNavigationView pageBottomTabLayout = findViewById(R.id.tab);


        //initiate side navigation bar
        mNavigationController = pageBottomTabLayout.material()
                .addItem(R.drawable.ic_home, "Movies & TV", testColors[0])
                .addItem(R.drawable.ic_audiotrack_black_24dp, "Music", testColors[1])
                .addItem(R.drawable.ic_book_black_24dp, "Books", testColors[2])
                .addItem(R.drawable.ic_news_black_24dp, "Newsstand", testColors[3])
                .enableVerticalLayout()//使用垂直布局
                .build();

        mNavigationController.setMessageNumber(0, 8);
        mNavigationController.setHasMessage(3, true);

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
        viewPager.setAdapter(new myAdapter(getSupportFragmentManager()));
        mNavigationController.setupWithViewPager(viewPager);


        SQLiteStudent DB = new SQLiteStudent(this);
        HashMap<String, String> hm = new HashMap<>();

        //Drawer
        initMaterialDrawer();

        //Initiate JavaCV
        Loader.load(opencv_java.class);

        //Update train.xml file, if set connect to server
       if(StudentDB.getISConnectToNetwork()){
           String mPath = getExternalCacheDir() + "/facerecOPCV/" + "train.xml";
           updateTrainFile(mPath);
       }

    }

    private void updateTrainFile(String path) {

        final MaterialDialog[] dialog = {new MaterialDialog.Builder(this)
                .title("updating data")
                .content("updating face recognition data from server")
                .progress(true, 0)
                .positiveText("cancel")
                .show()};

        OkHttpClient httpClient = new OkHttpClient();

        Request request = new Request.Builder().url(InVar.NETWORK_UPDATE_URL).build();
        Call call = httpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("http", "http failure" + e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog[0].dismiss();
                        dialog[0] = new MaterialDialog.Builder(MainActivity.this)
                                .title("Message")
                                .positiveText("Confirm")
                                .content("update failure, No internet connection")
                                .show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String data = response.body().string();
                Log.e("http", "Response: update face recognition file");

                File file = new File(path);

                //if the train.xml file is not exist
                if(!file.exists()){
                    if(!file.getParentFile().exists()){
                        file.getParentFile().mkdirs();
                    }

                    file.createNewFile();
                    FileWriter fw = new FileWriter(file);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(data);
                    bw.close();
                }
                //if the train.xml file is already exist
                else{
                    FileWriter fw = new FileWriter(file);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(data);
                    bw.close();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog[0].dismiss();
                        dialog[0] = new MaterialDialog.Builder(MainActivity.this)
                                .title("Message")
                                .positiveText("Confirm")
                                .content("update success")
                                .show();
                    }
                });
            }
        });

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
                                Intent intent = new Intent(MainActivity.this, MTLogin.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
        );

    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
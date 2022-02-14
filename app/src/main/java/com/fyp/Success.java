package com.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.fyp.databaseHelper.StudentAccountDB;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import static org.opencv.imgcodecs.Imgcodecs.*;

public class Success extends AppCompatActivity {

    //Widget
    TextView tv_success;
    ImageView img_capturedFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        tv_success = findViewById(R.id.tv_success);
        img_capturedFace = findViewById(R.id.img_capturedFace);

        //Initialize widget
        tv_success.setText("The face is captured successfully");
        Bitmap bitmap = null;
        String Filename = getIntent().getStringExtra("name");
        Log.e("Filename", Filename);
        try {
            FileInputStream fis = new FileInputStream(Filename);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        img_capturedFace.setImageBitmap(bitmap);

    }
}
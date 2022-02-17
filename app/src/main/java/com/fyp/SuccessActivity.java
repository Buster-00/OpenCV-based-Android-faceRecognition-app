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
import com.fyp.face.Labels;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.HashMap;

import static org.opencv.imgcodecs.Imgcodecs.*;

public class SuccessActivity extends AppCompatActivity {

    //Widget
    TextView tv_success;
    TextView tv_labels;
    ImageView img_capturedFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        tv_success = findViewById(R.id.tv_success);
        tv_labels = findViewById(R.id.tv_labels);
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

        //Display the bitmap image
        img_capturedFace.setImageBitmap(bitmap);

        //Save all image files
        Labels labels = new Labels(getIntent().getStringExtra("mPath"));
        int counter = 0;
        File root = new File(getIntent().getStringExtra("mPath"));
        FilenameFilter pngFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jpg");
            }
        };

        File[] imgFiles = root.listFiles(pngFilter);
        String labelList = new String();
        for(File img : imgFiles){
            String filePath = img.getAbsolutePath();
            int index_1 = filePath.lastIndexOf('/');
            int index_2 = filePath.lastIndexOf('.');
            String label = filePath.substring(index_1 + 1, index_2);
            labelList += label + ", ";
            labels.add(label, counter);
            counter++;
        }
        labels.Save();

        //display
        tv_labels.setText(labels.display());

    }
}
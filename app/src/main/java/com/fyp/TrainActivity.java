package com.fyp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Layout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fyp.face.PersonRecognizer;

import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class TrainActivity extends AppCompatActivity {

    //layout
    LinearLayoutCompat linearLayout;

    //faceRecognizer
    FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create();

    //path
    String mPath = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        mPath = getExternalCacheDir() + "/facerecOPCV/";

        linearLayout = findViewById(R.id.linearLayout);

        //initiate OpenCV
        OpenCVLoader.initDebug();

        //Retrieve all the image file
        File root = new File(mPath);
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                return name.toLowerCase().endsWith(".jpg");
            }
        };

        File[] imgFiles = root.listFiles(filenameFilter);
        MatVector matVector = new MatVector();
        HashMap<String, Integer> hashMap = new HashMap<>();
        Mat labelsMat = new Mat();
        int counter = 0;
        String temp = new String();



        for(File file : imgFiles){
            String name = file.getName();
            int index_1 = name.lastIndexOf('.');
            name = name.substring(0, index_1);
            temp += name + "\n";

            //load image label
            hashMap.put("name", counter);
            counter++;

            //read image file
            Mat m = Imgcodecs.imread(file.getAbsolutePath());
            matVector.push_back(m);

            if(!matVector.isNull()){
                temp += "add mat successfully!";
                temp += matVector.size();
            }
        }

        int[] labels = new int[(int)matVector.size()];

        for(int i = 0; i < labels.length; i++){
            labels[i] = i;
        }

        Vector<Bitmap> bitmaps = new Vector<>();

        faceRecognizer.train(matVector, new org.bytedeco.opencv.opencv_core.Mat(labels));

    }
}
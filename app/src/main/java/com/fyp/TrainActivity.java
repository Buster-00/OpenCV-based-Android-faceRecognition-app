package com.fyp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fyp.face.PersonRecognizer;


import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import org.bytedeco.opencv.presets.opencv_core;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import static org.opencv.core.CvType.CV_32SC1;

public class TrainActivity extends AppCompatActivity {

    //Button
    Button btn_test;

    //layout
    LinearLayoutCompat linearLayout;

    //faceRecognizer
    FaceRecognizer faceRecognizer ;

    //path
    String mPath = new String();

    //data
    Mat matOfLabels;
    Vector<Mat> matVector;

    //BaseLoadCallBack
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status){
            switch (status){
                case BaseLoaderCallback.SUCCESS:{
                    Log.i("OpenCV", "OpenCV loaded successfully");
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        mPath = getExternalCacheDir() + "/facerecOPCV/";

        btn_test = findViewById(R.id.btn_test1);

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                train();
            }
        });

        //initialize OpenCV
        Loader.load(opencv_java.class);


        //Retrieve all the image file
        File root = new File(mPath);
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                return name.toLowerCase().endsWith(".jpg");
            }
        };

        File[] imgFiles = root.listFiles(filenameFilter);
        matVector = new Vector<>();
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
            Mat des = new Mat();
            Imgproc.cvtColor(m, des, Imgproc.COLOR_BGR2GRAY);
            matVector.add(des);

            if(!matVector.isEmpty()){
                temp += "add mat successfully!";
                temp += matVector.size();
            }
        }

        int[] labels = new int[(int)matVector.size()];



        for(int i = 0; i < labels.length; i++){
            labels[i] = i;
        }

        matOfLabels = new MatOfInt(labels);


    }

    void train(){
        faceRecognizer = LBPHFaceRecognizer.create();
        faceRecognizer.train(matVector, matOfLabels);

        File path = this.getFilesDir();
        try {
            File trainFile = new File(path.getAbsolutePath() + "/train.xml");
            trainFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(path);
            faceRecognizer.save(path.getAbsolutePath() + "/train.xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
package com.fyp.face;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.bytedeco.opencv.opencv_core.IplImage;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.objdetect.FaceRecognizerSF;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.security.spec.ECField;

import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvLoadImage;

public class PersonRecognizer {

    private LBPHFaceRecognizer LBPHfr;
    String mPath;
    int count = 0;
    Lables lablesFile;

    static final int WIDTH = 128;
    static final int HEIGHT = 128;
    private int mProb = 999;

    public PersonRecognizer(String path){

        //Initiate LBPH face recognizer
        LBPHfr = LBPHFaceRecognizer.create(2,8,8,8,200);

        //The save path
        mPath = path;

        //create lable files
        lablesFile = new Lables(mPath);
    }

    void add(Mat m, String description){

        //create bitmap
        Bitmap bmp = Bitmap.createBitmap(m.width(), m.height(), Bitmap.Config.ARGB_8888);

        //Convert mat to bitmap
        Utils.matToBitmap(m, bmp);

        //Create Scaled bitmap
        bmp = Bitmap.createScaledBitmap(bmp, WIDTH, HEIGHT, false);

        FileOutputStream FOS;
        try{

            //store the bitmap as JPEG file
            FOS = new FileOutputStream(mPath+description + "-" + count + ".jpg", true);
            count++;
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, FOS);
            FOS.close();

        }catch (Exception e){
            Log.e("error", e.getCause() + " " + e.getMessage());
        }
    }

    public boolean train(){

        File root = new File(mPath);

        FilenameFilter pngFilter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                return name.toLowerCase().endsWith(".jpg");
            }
        };

        File[] imagesFiles = root.listFiles(pngFilter);

        MatVector images = new MatVector(imagesFiles.length);

        int[] lables = new int[imagesFiles.length];

        int counter = 0;
        int label;

        IplImage img;
        IplImage grayImg;

        int i1 = mPath.length();

        for(File image : imagesFiles){
            String p = image.getAbsolutePath();
            img = cvLoadImage(p);

            if(img==null){
                Log.e("Error", "Eroor cvLoadImage");
            }

            int i2 = p.lastIndexOf("-");
            int i3 = p.lastIndexOf(".");
            int icount = Integer.parseInt(p.substring(i2+1, i3));
            if(count < icount){
                count++;
            }

            //get description included in file name
            String description = p.substring(i1,i2);


        }

        return true;
    }
}

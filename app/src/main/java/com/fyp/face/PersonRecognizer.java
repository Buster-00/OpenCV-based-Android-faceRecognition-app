package com.fyp.face;

import android.graphics.Bitmap;
import android.util.Log;

import org.bytedeco.opencv.opencv_core.IplImage;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.Vector;

import static org.bytedeco.opencv.global.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2GRAY;
import static org.bytedeco.opencv.global.opencv_imgproc.cvCvtColor;
import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvLoadImage;

public class PersonRecognizer {

    private FaceRecognizer faceRecognizer;
    String mPath;
    int count = 0;
    Labels labelsFile;

    static final int WIDTH = 128;
    static final int HEIGHT = 128;
    private int mProb = 999;

    public PersonRecognizer(String path){

        //Initiate LBPH face recognizer
        faceRecognizer = LBPHFaceRecognizer.create(2,8,8,8,200);

        //The save path
        mPath = path;

        //create lable files
        labelsFile = new Labels(mPath);
    }

    //store the photo
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

        MatVector images = new MatVector();

        int[] labels = new int[imagesFiles.length];

        int counter = 0;
        int label;

        IplImage img;
        IplImage grayImg;

        int i1 = mPath.length();
        for(File image : imagesFiles){
            String p = image.getAbsolutePath();
            Mat mat = Imgcodecs.imread(p);
            img = cvLoadImage(p);

            if(img==null){
                Log.e("Error", "Error cvLoadImage");
            }

            int i2 = p.lastIndexOf("-");
            int i3 = p.lastIndexOf(".");
            int icount = Integer.parseInt(p.substring(i2+1, i3));
            if(count < icount){
                count++;
            }

            //get description included in file name
            String description = p.substring(i1,i2);

            if(labelsFile.get(description) < 0){
                labelsFile.add(description, labelsFile.max() + 1);
            }

            label = labelsFile.get(description);

            grayImg = IplImage.create(img.width(), img.height(), IPL_DEPTH_8U, 1);

            cvCvtColor(img,grayImg, Imgproc.COLOR_BGR2GRAY);

            images.put(grayImg);

            labels[counter] = label;
            
            counter++;
        }
        
        if(counter > 0){
            if(labelsFile.max() > 1){
                //faceRecognizer.train();
                
            }
            labelsFile.Save();
        }

        return true;
    }

    //The train function which trains the matrix and output as xml file
    public boolean train2(Vector<Mat> mats, Mat label){

        faceRecognizer.train(mats, label);
        return  true;
    }

    public boolean canPredict(){
        if(labelsFile.max() > 1)
            return true;
        else
            return false;
    }

    public String predict(Mat m){
        //cannot predict
        if(!canPredict())
            return null;

        //predict
        int n[] = new int[1];
        double p[] = new double[1];
        IplImage ipl = MatToIplImage(m, WIDTH, HEIGHT);

        //faceRecognizer.predict(ipl, n, p);

        if(n[0] != -1)
            mProb = (int)p[0];
        else
            mProb = -1;

        if(n[0] != -1)
            return labelsFile.get(n[0]);
        else
            return "Unknown";
    }

    IplImage MatToIplImage(Mat m, int width, int height){
        Bitmap bmp = Bitmap.createBitmap(m.width(), m.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(m, bmp);
        return BitmapToIplImage(bmp, width, height);
    }

    IplImage BitmapToIplImage(Bitmap bmp, int width, int height){
        if((width != -1) || (height != -1)){
            Bitmap bmp2 = Bitmap.createScaledBitmap(bmp, width, height, false);
            bmp = bmp2;
        }

        IplImage image = IplImage.create(bmp.getWidth(), bmp.getHeight(),IPL_DEPTH_8U, 4);

        bmp.copyPixelsToBuffer(image.getByteBuffer());

        IplImage grayImg = IplImage.create(image.width(), image.height(), IPL_DEPTH_8U, 1);

        cvCvtColor(image, grayImg, COLOR_BGR2GRAY);

        return grayImg;
    }

    protected void SaveBmp(Bitmap bmp, String path){
        FileOutputStream file;
        try{
            file = new FileOutputStream(path, true);

            bmp.compress(Bitmap.CompressFormat.JPEG, 100, file);
            file.close();
        } catch (Exception e) {
            Log.e("error", e.getMessage() + e.getCause());
            e.printStackTrace();
        }
    }

    public void load(){
        train();
    }

    public int getProb(){
        return mProb;
    }
}

package com.fyp.utilities;

import android.content.Context;
import android.util.Log;

import com.fyp.R;

import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.FaceDetectorYN;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class FaceDetectorHelper {

    public static final Scalar FACE_RECT_COLOR = new Scalar(0, 255, 255, 0);
    public static final Scalar FONT_COLOR = new Scalar(0, 255, 0, 0);
    private static final String TAG_CASCADE= "CascadeClassifierHelper";
    private static final String TAG_YUNET= "Yunet";

    //Load CascadeClassifier to detect face
    public static CascadeClassifier loadClassifier(Context context){

        CascadeClassifier faceDetector = null;

        //Load Cascade file from application resources
        try{
            InputStream is = context.getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
            File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while ((bytesRead = is.read(buffer)) != -1){
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();

            faceDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
            if(faceDetector.empty()){
                Log.e(TAG_CASCADE, "failed to load cascade classifier");
            }else{
                Log.i(TAG_CASCADE, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());
            }

            cascadeDir.delete();

        } catch (IOException e){
            e.printStackTrace();
            Log.e(TAG_CASCADE, "Failed to load cascade, exception throw: " + e);
        }

        if (faceDetector == null){
            Log.e(TAG_CASCADE, "cannot load");
        }
        return faceDetector;
    }

    //Load FaceDetectorYN to detect face
    public static FaceDetectorYN LoadFaceDetectorYN(Context context, Size img_size){

        FaceDetectorYN faceDetectorYN = null;

        try{
            InputStream is = context.getResources().openRawResource(R.raw.face_detection_yunet_2021dec);
            File yunetDir = context.getDir("yunet", Context.MODE_PRIVATE);
            File mYunet = new File(yunetDir, "face_detection_yunet_2021dec.onnx");
            FileOutputStream os = new FileOutputStream(mYunet);

            byte[] buffer = new byte[4096];
            int byteRead = 0;
            while((byteRead = is.read(buffer)) != -1){
                os.write(buffer, 0, byteRead);
            }

            is.close();
            os.close();

            //Create faceDetectorYN
            faceDetectorYN = FaceDetectorYN.create(mYunet.getAbsolutePath(), "", img_size);
        }catch (IOException e){
            //TODO
            Log.e(TAG_YUNET, "Error, cannot create FaceDetecorYN");
        }

        //Create FaceDetectorYN

        return faceDetectorYN;
    }
}

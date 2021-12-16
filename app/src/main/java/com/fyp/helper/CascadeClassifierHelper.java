package com.fyp.helper;

import android.content.Context;
import android.util.Log;

import com.fyp.R;

import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class CascadeClassifierHelper {

    private static final String    TAG                 = "CascadeClassifierHelper";


    static public CascadeClassifier loadClassifier(Context context){

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
                Log.e(TAG, "failed to load cascade classifier");
            }else{
                Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());
            }

            cascadeDir.delete();

        } catch (IOException e){
            e.printStackTrace();
            Log.e(TAG, "Failed to load cascade, exception throw: " + e);
        }

        return faceDetector;
    }
}

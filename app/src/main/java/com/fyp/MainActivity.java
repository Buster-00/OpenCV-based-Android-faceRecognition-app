package com.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCamera2View;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.util.ArrayList;
import java.util.List;

import static com.fyp.helper.CascadeClassifierHelper.loadClassifier;
import static org.opencv.core.Core.ROTATE_180;
import static org.opencv.core.Core.flip;
import static org.opencv.core.Core.rotate;
import static org.opencv.imgproc.Imgproc.line;
import static org.opencv.imgproc.Imgproc.rectangle;

public class MainActivity extends CameraActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private JavaCamera2View javaCameraView;
    private CascadeClassifier faceDetector;
    private static final Scalar    FACE_RECT_COLOR     = new Scalar(0, 255, 255, 0);

    //Load native library
    static {
        System.loadLibrary("native-lib");
    }

    public native String stringFromJNI();

    private BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    javaCameraView.enableView();
                }
                break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        List<CameraBridgeViewBase> list = new ArrayList<>();
        list.add(javaCameraView);
        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        javaCameraView = (JavaCamera2View)findViewById(R.id.javaCameraView);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCameraIndex(1);
        javaCameraView.setCameraPermissionGranted();
        javaCameraView.setCvCameraViewListener(this);

        //initialize openCV
        initOpenCV();

        //Initialize face detector
        faceDetector = loadClassifier(this);

        //Native Method detect
        Log.e("Native method", stringFromJNI());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (javaCameraView != null) {
            javaCameraView.disableView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, baseLoaderCallback);
        } else {
            baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        if(faceDetector.empty()){
            Log.e("error", "Classifier cannot be loaded");
        }

        MatOfRect face_rec = new MatOfRect();
        faceDetector.detectMultiScale(inputFrame.gray(), face_rec);

        Mat rotatedFrame = new Mat();
        Mat flippedFrame = new Mat();

        Rect[] faceArray = face_rec.toArray();

        //Rotate the frame by 180
        rotate(inputFrame.rgba(), rotatedFrame, ROTATE_180);

        //Flip the frame
        flip(rotatedFrame, flippedFrame, 0);

        //Render rectangle
        for(int i = 0; i < faceArray.length; i++){
            //rectangle(temp, faceArray[i].tl(),  faceArray[i].br(), new Scalar(255, 0, 0), 1);
            rectangle(flippedFrame, new Point(flippedFrame.width()-faceArray[i].x, faceArray[i].y),
                    new Point(flippedFrame.width()-(faceArray[i].x+faceArray[i].width), faceArray[i].y+faceArray[i].height) , new Scalar(255, 0, 0), 1);
            //line(mRgba, new Point(0.0, mRgba.height()), new Point(mRgba.width(), 0.0), new Scalar(255, 0, 0), 2);
            Log.e("Rendering",faceArray[i].tl().toString() + faceArray[i].br().toString()+ flippedFrame.rows() + flippedFrame.cols() );
        }

        //Final frame;
        return flippedFrame;
        //return inputFrame.rgba();
    }

    private void initOpenCV(){
        boolean state = OpenCVLoader.initDebug();

        if(state){
            Log.i("OpenCV", "Load Successfully");
        }else{
            Log.i("OpenCV", "Load failed");
        }
    }

    //Temp method to test native library
    private  void tempNativeTest(){

    }

}


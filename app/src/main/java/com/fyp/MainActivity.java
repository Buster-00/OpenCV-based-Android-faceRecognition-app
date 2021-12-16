package com.fyp;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.objdetect.CascadeClassifier;

import java.util.ArrayList;
import java.util.List;

import static com.fyp.helper.CascadeClassifierHelper.loadClassifier;
import static org.opencv.core.Core.ROTATE_180;
import static org.opencv.core.Core.flip;
import static org.opencv.core.Core.rotate;

public class MainActivity extends CameraActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private JavaCameraView javaCameraView;
    private CascadeClassifier faceDetector;

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
        javaCameraView = findViewById(R.id.javaCameraView);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCameraIndex(1);
        javaCameraView.setCvCameraViewListener(this);

        initOpenCV();

        //Initialize face detector
        faceDetector = loadClassifier(this);
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

//        MatOfRect face_rec = new MatOfRect();
//        face_detect.detectMultiScale(inputFrame.gray(), face_rec);

        Mat rotatedFrame = new Mat();
        Mat flippedFrame = new Mat();

        //Rotate the frame by 180
        rotate(inputFrame.rgba(), rotatedFrame, ROTATE_180);

        //Flip the frame
        flip(rotatedFrame, flippedFrame, 0);

        //Final frame;
        return flippedFrame;
        //return inputFrame.rgba();
    }

    private void initOpenCV(){
        boolean state = OpenCVLoader.initDebug();

        if(state){

        }
    }

}


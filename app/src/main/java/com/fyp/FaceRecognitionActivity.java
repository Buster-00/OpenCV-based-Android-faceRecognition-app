package com.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;

import com.fyp.helper.FaceDetectorHelper;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCamera2View;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.objdetect.CascadeClassifier;

import java.util.ArrayList;
import java.util.List;

import static com.fyp.helper.FaceDetectorHelper.loadClassifier;
import static org.opencv.core.Core.ROTATE_180;
import static org.opencv.core.Core.flip;
import static org.opencv.core.Core.rotate;
import static org.opencv.imgproc.Imgproc.FONT_HERSHEY_COMPLEX;
import static org.opencv.imgproc.Imgproc.putText;
import static org.opencv.imgproc.Imgproc.rectangle;

public class FaceRecognitionActivity extends CameraActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    //widgets
    private JavaCamera2View javaCameraView;

    //Face Detector
    private CascadeClassifier faceDetector;

    //Face Recognizer
    private FaceRecognizer faceRecognizer;

    //BaseLoaderCallback
    BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);

        //initialize camera view
        javaCameraView = (JavaCamera2View)findViewById(R.id.javaCameraView_recognition);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCameraIndex(1);
        javaCameraView.setCameraPermissionGranted();
        javaCameraView.setCvCameraViewListener(this);
        javaCameraView.enableView();

        //Initialize OpenCV library
        initOpenCV();

        //Load cascade classifier
        faceDetector = loadClassifier(this);

    }

    @Override
    public void onResume(){
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, baseLoaderCallback);
        } else {
            baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        List<CameraBridgeViewBase> list = new ArrayList<>();
        list.add(javaCameraView);
        return list;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        return detectFace(inputFrame);
    }

    private void initOpenCV(){
        boolean state = OpenCVLoader.initDebug();

        if(state){
            Log.i("OpenCV", "Load Successfully");
        }else{
            Log.i("OpenCV", "Load failed");
        }
    }

    private Mat detectFace(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        if (faceDetector.empty()) {
            Log.e("error", "Classifier cannot be loaded");
        }

        //detect the face
        MatOfRect face_rec = new MatOfRect();
        faceDetector.detectMultiScale(inputFrame.gray(), face_rec);

        Mat rotatedFrame = new Mat();
        Mat flippedFrame = new Mat();

        Rect[] faceArray = face_rec.toArray();

        //Rotate the frame by 180
        rotate(inputFrame.rgba(), rotatedFrame, ROTATE_180);

        //Flip the frame
        flip(rotatedFrame, flippedFrame, 0);

        // Rect to clip the face
        Rect clipRect = null;

        //Render rectangle
        for (int i = 0; i < faceArray.length; i++) {
            //rectangle(temp, faceArray[i].tl(),  faceArray[i].br(), new Scalar(255, 0, 0), 1);
            clipRect = new Rect(new Point(flippedFrame.width() - faceArray[i].x, faceArray[i].y), new Point(flippedFrame.width() - (faceArray[i].x + faceArray[i].width), faceArray[i].y + faceArray[i].height));
            rectangle(flippedFrame, clipRect, FaceDetectorHelper.FACE_RECT_COLOR, 1);
            putText(flippedFrame, "karazawa", new Point(flippedFrame.width() - faceArray[i].x, faceArray[i].y), FONT_HERSHEY_COMPLEX, 2,  FaceDetectorHelper.FACE_RECT_COLOR);
            //line(mRgba, new Point(0.0, mRgba.height()), new Point(mRgba.width(), 0.0), new Scalar(255, 0, 0), 2);
            Log.e("Rendering", faceArray[i].tl().toString() + faceArray[i].br().toString() + flippedFrame.rows() + flippedFrame.cols());
        }

        //Final frame;
        return flippedFrame;
    }
}
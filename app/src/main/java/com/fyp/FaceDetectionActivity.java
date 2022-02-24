package com.fyp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import com.fyp.helper.FaceDetectorHelper;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCamera2View;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.FaceDetectorYN;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.fyp.helper.FaceDetectorHelper.loadClassifier;
import static org.opencv.core.Core.ROTATE_180;
import static org.opencv.core.Core.flip;
import static org.opencv.core.Core.rotate;
import static org.opencv.imgproc.Imgproc.COLOR_RGBA2RGB;
import static org.opencv.imgproc.Imgproc.line;
import static org.opencv.imgproc.Imgproc.rectangle;

public class FaceDetectionActivity extends CameraActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    enum FACE_DETECT{CASCADE_DETECT, YN_DETECT}
    private FACE_DETECT DETECT_METHOD;
    private static final String TAG = "FaceDetectionActivity";

    //Counter for detection times
    int detect_counter = 0;

    //widgets
    private JavaCamera2View javaCameraView;

    //CascadeClassifier
    private CascadeClassifier faceDetector;

    //DNN face detector
    private FaceDetectorYN faceDetectorYN;

    //Color
    private static final Scalar FACE_RECT_COLOR = new Scalar(0, 255, 255, 0);

    //Load native library
    static {
        System.loadLibrary("native-lib");
    }

    //path
    String mPath;

    //face description
    String faceDescription;

    private BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    //Load OpenCV successfully
                    Log.i(TAG, "OpenCV loaded successfully");
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
        setContentView(R.layout.activity_face_detection);

        //Initialize javaCameraView
        javaCameraView = (JavaCamera2View)findViewById(R.id.javaCameraView_detection);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCameraIndex(1);
        javaCameraView.setCameraPermissionGranted();
        javaCameraView.setCvCameraViewListener(this);

        //initialize openCV
        initOpenCV();

        //Initialize face detector
        faceDetector = loadClassifier(this);

        //Load FaceDetectorYN
        faceDetectorYN = FaceDetectorHelper.LoadFaceDetectorYN(this, new Size(1600,720));

        //Set detect method, cascade or DNN
        DETECT_METHOD = FACE_DETECT.CASCADE_DETECT;

        //Initiate store path and make dir
        mPath = getExternalCacheDir() + "/facerecOPCV/";
        boolean success = (new File(mPath)).mkdirs();

        //if create dir success
        if(success)
            Log.e("dir", "mkdir success" + mPath);
        else
            Log.e("dir", "mkdir failed" + mPath);

        //Load face description from bundle
        faceDescription = getIntent().getStringExtra("name");
        Log.e("facedesctiption", faceDescription);
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
        //TODO
    }

    @Override
    public void onCameraViewStopped() {
        //TODO
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        //Select detect method
        if(DETECT_METHOD == FACE_DETECT.CASCADE_DETECT){
            return CascadeFaceDetect(inputFrame);
        }else if(DETECT_METHOD == FACE_DETECT.YN_DETECT){
            return YNFaceDetect(inputFrame);
        }else{
            return inputFrame.rgba();
        }

    }

    private void initOpenCV(){
        boolean state = OpenCVLoader.initDebug();

        if(state){
            Log.i("OpenCV", "Load Successfully");
        }else{
            Log.i("OpenCV", "Load failed");
        }
    }

    //Cascade detect method
    private Mat CascadeFaceDetect(CameraBridgeViewBase.CvCameraViewFrame inputFrame){
        if(faceDetector.empty()){
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

        //count the face detection times;
        if(faceArray.length > 0){
            detect_counter++;
        }

        // Rect to clip the face
        Rect clipRect = null;

        //Render rectangle
        for(int i = 0; i < faceArray.length; i++){
            //rectangle(temp, faceArray[i].tl(),  faceArray[i].br(), new Scalar(255, 0, 0), 1);
            clipRect = new Rect(new Point(flippedFrame.width()-faceArray[i].x, faceArray[i].y),new Point(flippedFrame.width()-(faceArray[i].x+faceArray[i].width), faceArray[i].y+faceArray[i].height));
            rectangle(flippedFrame, clipRect, new Scalar(255, 0, 0), 1);
            //line(mRgba, new Point(0.0, mRgba.height()), new Point(mRgba.width(), 0.0), new Scalar(255, 0, 0), 2);
            Log.e("Rendering",faceArray[i].tl().toString() + faceArray[i].br().toString()+ flippedFrame.rows() + flippedFrame.cols() );
        }

        //if detect face > 10 times, turn to another activity
       if(detect_counter > 10){
           SaveFace(flippedFrame.submat(clipRect), faceDescription);
           Intent intent = new Intent(FaceDetectionActivity.this, SuccessActivity.class);
           Bundle bundle = new Bundle();
           intent.putExtra("name", mPath + faceDescription + ".jpg");
           intent.putExtra("mPath", mPath);
           intent.putExtra("id", getIntent().getStringExtra("id"));
           startActivity(intent);
        }

        //Final frame;
        return flippedFrame;
        //return inputFrame.rgba();
    }

    //DNN detect method
    private Mat YNFaceDetect(CameraBridgeViewBase.CvCameraViewFrame inputFrame){

        //detect the face
        Mat faces = new Mat();
        Mat rgb = new Mat();
        Imgproc.cvtColor(inputFrame.rgba(), rgb, COLOR_RGBA2RGB);
        faceDetectorYN.detect(rgb, faces);


        Mat rotatedFrame = new Mat();
        Mat flippedFrame = new Mat();


        //Rotate the frame by 180
        rotate(inputFrame.rgba(), rotatedFrame, ROTATE_180);

        //Flip the frame
        flip(rotatedFrame, flippedFrame, 0);

        //count the face detection times;
        if(faces.cols() > 0){
            detect_counter++;
        }

//        //Render rectangle
//        for(int i = 0; i < faces.cols(); i++){
//
////            int x[] = new int[0];
////            int y[] = new int[0];
////            faces.get(1, 1, x);
////            faces.get(1, 2, y);
//
////            //rectangle(temp, faceArray[i].tl(),  faceArray[i].br(), new Scalar(255, 0, 0), 1);
////            rectangle(flippedFrame, new Point(flippedFrame.width()-faces.get, faceArray[i].y),
////                    new Point(flippedFrame.width()-(faceArray[i].x+faceArray[i].width), faceArray[i].y+faceArray[i].height) , new Scalar(255, 0, 0), 1);
////            //line(mRgba, new Point(0.0, mRgba.height()), new Point(mRgba.width(), 0.0), new Scalar(255, 0, 0), 2);
////            Log.e("Rendering",faceArray[i].tl().toString() + faceArray[i].br().toString()+ flippedFrame.rows() + flippedFrame.cols() );
//        }

//        if(faces.isContinuous()){
//
//            float[] x = new float[1];
//            float[] y = new float[1];
//            float[] w = new float[1];
//            float[] h = new float[1];
//
//            for(int row = 0; row < faces.rows(); row++){
//                faces.get(row, 1, x);
//                faces.get(row, 2, y);
//                faces.get(row, 3, w);
//                faces.get(row, 4, h);
//            }
//        }


        //if detect face > 10 times, turn to another activity
//        if(detect_counter > 10){
//            startActivity(new Intent(FaceDetectionActivity.this, Success.class));
//        }

        //Final frame;
        return flippedFrame;
        //return inputFrame.rgba();
    }

    private void SaveFace(Mat face, String description){

        //Create Bitmap
        Bitmap bitmap = Bitmap.createBitmap(face.width(), face.height(), Bitmap.Config.ARGB_8888);

        //Convert mat to bitmap
        Utils.matToBitmap(face, bitmap);

        FileOutputStream os;
        try{
            //store the bitmap as JPEG file
            os = new FileOutputStream(mPath + description + ".jpg", true);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


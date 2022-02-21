package com.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;

import com.fyp.face.PersonRecognizer;
import com.fyp.helper.FaceDetectorHelper;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCamera2View;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import static com.fyp.helper.FaceDetectorHelper.loadClassifier;
import static org.opencv.core.Core.ROTATE_180;
import static org.opencv.core.Core.flip;
import static org.opencv.core.Core.rotate;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;
import static org.opencv.imgproc.Imgproc.FONT_HERSHEY_COMPLEX;
import static org.opencv.imgproc.Imgproc.putText;
import static org.opencv.imgproc.Imgproc.rectangle;

public class FaceRecognitionActivity extends CameraActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    //Hash map of label and name
    HashMap<Integer, String> mapLabelName;

    //Vector of mat
    Vector<Mat> matVector;

    //mPath
    String mPath;

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

        //initialize mPath
        mPath = getExternalCacheDir()  + "/facerecOPCV/";

        //Initialize OpenCV library
        initOpenCV();
        Loader.load(opencv_java.class);

        //Load cascade classifier
        faceDetector = loadClassifier(this);

        //create PersonRecognizer
        faceRecognizer = LBPHFaceRecognizer.create(2,8,8,8,200);

        //train
        train();

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
        rotate(inputFrame.gray(), rotatedFrame, ROTATE_180);

        //Flip the frame
        flip(rotatedFrame, flippedFrame, 0);

        // Rect to clip the face
        Rect clipRect = null;

        //Render rectangle
        for (int i = 0; i < faceArray.length; i++) {
            //Clip face
            clipRect = new Rect(new Point(flippedFrame.width() - faceArray[i].x, faceArray[i].y), new Point(flippedFrame.width() - (faceArray[i].x + faceArray[i].width), faceArray[i].y + faceArray[i].height));
            rectangle(flippedFrame, clipRect, FaceDetectorHelper.FACE_RECT_COLOR, 1);

            //predict face
            int label[] = new int[1];
            double confidence[] = new double[1];
            faceRecognizer.predict(flippedFrame.submat(clipRect), label, confidence);

            //Render image
            if(label[0] != -1){
                putText(flippedFrame, mapLabelName.get(label[0]), new Point(flippedFrame.width() - faceArray[i].x, faceArray[i].y), FONT_HERSHEY_COMPLEX, 2,  FaceDetectorHelper.FACE_RECT_COLOR);
            }else{
                putText(flippedFrame, "karazawa", new Point(flippedFrame.width() - faceArray[i].x, faceArray[i].y), FONT_HERSHEY_COMPLEX, 2,  FaceDetectorHelper.FACE_RECT_COLOR);
            }
            Log.e("Rendering", faceArray[i].tl().toString() + faceArray[i].br().toString() + flippedFrame.rows() + flippedFrame.cols());
        }

        //Final frame;
        return flippedFrame;
    }

    private void train(){
        //Retrieve all face image file
        File root = new File(mPath);
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                return name.toLowerCase().endsWith(".jpg");
            }
        };

        File[] imgFiles = root.listFiles(filenameFilter);
        matVector = new Vector<>();
        mapLabelName = new HashMap<>();
        int counter = 0;

        for(File file : imgFiles){

            //map label and name
            String fileName = file.getName();
            int index = fileName.lastIndexOf('.');
            String name = fileName.substring(0, index);
            mapLabelName.put(counter, name);
            counter++;

            //Load image
            Mat m = Imgcodecs.imread(file.getAbsolutePath());
            Mat des = new Mat();
            Imgproc.cvtColor(m, des, COLOR_BGR2GRAY);
            matVector.add(des);

        }

        //Generate mat of labels
        int[] labels = new int[matVector.size()];
        for(int i = 0; i < matVector.size(); i++){
            labels[i] = i;
        }
        Mat matOfLabels = new MatOfInt(labels);

        //LBPH train
        faceRecognizer.train(matVector, matOfLabels);
    }
}
package com.fyp;

import static com.fyp.databaseHelper.UserManager.getCurrentUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fyp.databaseHelper.StudentDB;
import com.fyp.face.Labels;
import com.fyp.student.Student_MainActivity;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterSuccessActivity extends AppCompatActivity {

    //Widget
    Button btn_confirm;
    TextView tv_success;
    TextView tv_labels;
    ImageView img_capturedFace;

    //face image storage path
    String mPath;

    //Data
    private Vector<Mat> matVector;
    private HashMap<Integer, String> mapLabelName;

    //LBPH FaceRecognizer
    FaceRecognizer faceRecognizer;

    //dialog
    MaterialDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        //Initialize widgets
        btn_confirm = findViewById(R.id.btn_confirm);
        tv_success = findViewById(R.id.tv_success);
        tv_labels = findViewById(R.id.tv_labels);
        img_capturedFace = findViewById(R.id.img_capturedFace);

        //Initialize widget
        tv_success.setText("The face is captured successfully");

        //Initialize mPath
        mPath = getIntent().getStringExtra("mPath");

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtn_confirm();
            }
        });


        Bitmap bitmap = null;
        String Filename = getIntent().getStringExtra("name");
        Log.e("Filename", Filename);
        try {
            FileInputStream fis = new FileInputStream(Filename);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Display the bitmap image
        img_capturedFace.setImageBitmap(bitmap);

        //Save all image files
        Labels labels = new Labels(getIntent().getStringExtra("mPath"));
        int counter = 0;
        File root = new File(getIntent().getStringExtra("mPath"));
        FilenameFilter pngFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jpg");
            }
        };

        File[] imgFiles = root.listFiles(pngFilter);
        String labelList = new String();
        for(File img : imgFiles){
            String filePath = img.getAbsolutePath();
            int index_1 = filePath.lastIndexOf('/');
            int index_2 = filePath.lastIndexOf('.');
            String label = filePath.substring(index_1 + 1, index_2);
            labelList += label + ", ";
            labels.add(label, counter);
            counter++;
        }
        labels.Save();

        //display
        tv_labels.setText(labels.display());

        //
        Loader.load(opencv_java.class);

        //create lbph face recognizer
        faceRecognizer = faceRecognizer = LBPHFaceRecognizer.create(2,8,8,8,200);

    }

    protected void setBtn_confirm(){

        //LBPH train
        if(new File(mPath + "train.xml").exists()){
            train_single();
        }
        else{
            train();
        }


        //Upload train.xml to server
        if(StudentDB.getISConnectToNetwork()){
            uploadTrainFile(mPath + "train.xml");
        }

        Intent intent = new Intent(RegisterSuccessActivity.this, Student_MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }

    //upload train.xml file to server
    private void uploadTrainFile(String path) {

        dialog = new MaterialDialog.Builder(this)
                .title("Uploading data")
                .content("Uploading the data to server, please wait")
                .progress(true, 0)
                .positiveText("cancel")
                .show();

        OkHttpClient httpClient = new OkHttpClient();

        MediaType contentType = MediaType.parse("text/plain");
        File file = new File(path);
        RequestBody body = RequestBody.create(file, contentType);

        Request getRequest = new Request.Builder()
                .url("http://118.31.20.251:8080/newServlet/upload")
                .post(body)
                .build();



        Call call = httpClient.newCall(getRequest);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("http", "http failure" + e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        dialog = new MaterialDialog.Builder(RegisterSuccessActivity.this)
                                .title("Message")
                                .positiveText("Confirm")
                                .content("Upload failure")
                                .show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.e("http", "okHttpPost enqueue: \n onResponse:"+ response.toString() +"\n body:" +response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        dialog = new MaterialDialog.Builder(RegisterSuccessActivity.this)
                                .title("Message")
                                .positiveText("Confirm")
                                .content("Upload success")
                                .show();
                    }
                });
            }
        });

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
        int counter = 1;

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
        int[] labels = new int[1];
        labels[0] = 1;
        Mat matOfLabels = new MatOfInt(labels);

        //LBPH train

        faceRecognizer.train(matVector, matOfLabels);
        faceRecognizer.write(mPath + "train.xml");
    }

    private void train_single(){

        //Retrieve all face image file
        File imgFile = new File(getIntent().getExtras().getString("name"));


        matVector = new Vector<>();
        int counter = 1;

            //map label and name
            String fileName = imgFile.getName();
            int index = fileName.lastIndexOf('.');
            String name = fileName.substring(0, index);

            //Load image
            Mat m = Imgcodecs.imread(imgFile.getAbsolutePath());
            Mat des = new Mat();
            Imgproc.cvtColor(m, des, COLOR_BGR2GRAY);
            matVector.add(des);


        //Generate mat of labels
        int[] label = new int[1];
        label[0] = getCurrentUser().getLabel();
        Mat matOfLabels = new MatOfInt(label);

        //LBPH train
        faceRecognizer.read(mPath + "train.xml");
        faceRecognizer.update(matVector, matOfLabels);
        faceRecognizer.write(mPath + "train.xml");
    }

}
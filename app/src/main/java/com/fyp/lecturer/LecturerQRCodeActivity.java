package com.fyp.lecturer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.fyp.R;
import com.fyp.helper.QRCodeHelper;

public class LecturerQRCodeActivity extends AppCompatActivity {

    //Widget
    ImageView img_QRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_qrcode);

        //Initialize widget
        img_QRCode = findViewById(R.id.img_QRCode);

        //Initialize QR Code
        //GET JSON
        String json = getIntent().getStringExtra("INFO");
        img_QRCode.setImageBitmap(QRCodeHelper.createQRCodeBitmap(json, 320, 320));
    }
}
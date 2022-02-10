package com.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    Button btn_next;
    EditText tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initialize widget
        btn_next = findViewById(R.id.btn_next);
        tv_name = findViewById(R.id.tv_name);

        //implement onclick method
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_next_click();
            }
        });


    }

    private void btn_next_click(){

        //create bundle to transform data
        Bundle bundle = new Bundle();
        bundle.putString("name", tv_name.getText().toString());
        Intent intent = new Intent(RegisterActivity.this, FaceDetectionActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
package com.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fyp.databaseHelper.StudentAccountDB;

import java.util.HashMap;

public class NameRegisterActivity extends AppCompatActivity {

    Button btn_next;
    EditText tv_name;
    EditText tv_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initialize widget
        btn_next = findViewById(R.id.btn_next);
        tv_name = findViewById(R.id.tv_name);
        tv_ID = findViewById(R.id.tv_ID);

        //implement onclick method
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_next_click();
            }
        });


    }

    private void btn_next_click(){

        //Insert data into database
        StudentAccountDB db = new StudentAccountDB(this);
        db.insert(tv_ID.getText().toString(), "123456", tv_name.toString().toString());
        HashMap<String, String> hm = new HashMap<>();
        db.readById(tv_ID.getText().toString(), hm);
        Toast.makeText(this, "Insert student data success" + hm, Toast.LENGTH_SHORT).show();
        //create bundle to transform data
        Bundle bundle = new Bundle();
        bundle.putString("id", tv_ID.getText().toString());
        bundle.putString("name", tv_name.getText().toString());
        Intent intent = new Intent(NameRegisterActivity.this, FaceRegisterActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
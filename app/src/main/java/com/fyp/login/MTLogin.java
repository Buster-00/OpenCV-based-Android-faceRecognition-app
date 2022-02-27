package com.fyp.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.MainActivity;
import com.fyp.R;
import com.fyp.databaseHelper.StudentAccountDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.HashMap;
import java.util.List;

import cn.fanrunqi.materiallogin.a.AActivityOne;
import cn.fanrunqi.materiallogin.a.AActivityThree;
import cn.fanrunqi.materiallogin.a.AActivityTwo;

public class MTLogin extends AppCompatActivity implements Validator.ValidationListener {

    //Widget
    @NotEmpty
    protected EditText etStudentID;

    @NotEmpty
    protected EditText etPassword;

    protected Button btGo;
    protected CardView cv;
    protected FloatingActionButton fab;

    //Validator
    Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_login);
        initView();
        initValidator();
        setListener();

    }

    private void initValidator() {
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    private void initView() {
        etStudentID = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btGo = findViewById(R.id.bt_go);
        cv = findViewById(R.id.cv);
        fab = findViewById(R.id.fab);
    }

    private void setListener() {
        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //validate input
                validator.validate();

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MTLogin.this, fab, fab.getTransitionName());
                startActivity(new Intent(MTLogin.this, MTRegister.class), options.toBundle());
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onRestart() {
        super.onRestart();
        fab.setVisibility(View.GONE);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onResume() {
        super.onResume();
        fab.setVisibility(View.VISIBLE);
    }


    @Override
    public void onValidationSucceeded() {
        //Toast.makeText(this, "Yay! we got it right!", Toast.LENGTH_SHORT).show();
        //validate password and account
        Login();

        /*Explode explode = new Explode();
        explode.setDuration(500);

        getWindow().setExitTransition(explode);
        getWindow().setEnterTransition(explode);
        ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(MTLogin.this);
        Intent i2 = new Intent(MTLogin.this, MainActivity.class);
        startActivity(i2, oc2.toBundle());*/
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);


            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }

        }
    }

    private void Login(){
        String studentID = etStudentID.getText().toString();
        String password = etStudentID.getText().toString();

        StudentAccountDB DB = new StudentAccountDB(this);
        HashMap<String, String> hashMap = new HashMap<>();

        if(DB.readById(studentID, hashMap)){
            String DB_password = hashMap.get("password");

            if(DB_password.equals(password)){
                Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "The password is incorrect, please enter again", Toast.LENGTH_SHORT).show();
                etPassword.setText("", TextView.BufferType.EDITABLE);
            }
        }
        else{
            Toast.makeText(this, "cannot find account", Toast.LENGTH_SHORT).show();
        }

    }
}
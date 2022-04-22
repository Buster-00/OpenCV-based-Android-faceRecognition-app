package com.fyp.login;

import static com.fyp.databaseHelper.UserManager.initUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.MainActivity;
import com.fyp.R;
import com.fyp.ResetPasswordActivity;
import com.fyp.databaseHelper.StudentDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;


public class MTLogin extends AppCompatActivity implements Validator.ValidationListener {

    protected static boolean  IS_SAVE_ACCOUNT = false;

    //Widget

    protected CheckBox cb_saveAccount;

    @NotEmpty
    protected EditText etStudentID;

    @NotEmpty
    protected EditText etPassword;
    protected TextView tv_forgetPassword;
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
        cb_saveAccount = findViewById(R.id.cb_saveAccount);
        etStudentID = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btGo = findViewById(R.id.bt_go);
        cv = findViewById(R.id.cv);
        fab = findViewById(R.id.fab);
        tv_forgetPassword = findViewById(R.id.tv_forgetPassword);

        cb_saveAccount.setChecked(IS_SAVE_ACCOUNT);

        if(IS_SAVE_ACCOUNT){
            try{
                FileInputStream fis = new FileInputStream(this.getExternalCacheDir() + "/savedAccount.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                String strLine;

                if((strLine = br.readLine()) != null){
                    etStudentID.setText(strLine);
                }
                else{
                    Log.e("error", "the strLine is null" + this.getExternalCacheDir());
                }
            }catch (Exception e){

            }

        }
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
        tv_forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MTLogin.this, ResetPasswordActivity.class);
                startActivity(intent);
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

        //save account
        if(cb_saveAccount.isChecked()){
                IS_SAVE_ACCOUNT = true;

                File file = new File(this.getExternalCacheDir() + "/savedAccount.txt");

                try {
                    file.createNewFile();
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                    bufferedWriter.write(etStudentID.getText().toString());
                    bufferedWriter.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        else{
            IS_SAVE_ACCOUNT = false;
        }

        //validate password and account
        if(Login()){

            Explode explode = new Explode();
            explode.setDuration(500);

            Intent i2 = new Intent(MTLogin.this, MainActivity.class);
            i2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i2);
        }

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

    private boolean Login(){
        String studentID = etStudentID.getText().toString();
        String password = etPassword.getText().toString();

        StudentDB DB = new StudentDB(this);
        HashMap<String, String> hashMap = new HashMap<>();

        if(DB.readById(studentID, hashMap)){
            String DB_password = hashMap.get("password");

            //password current
            if(DB_password.equals(password)){
                Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show();
                initUser(studentID, DB);
                return true;
            }
            //password inCurrent
            else{
                Toast.makeText(this, "The password is incorrect, please enter again", Toast.LENGTH_SHORT).show();
                etPassword.setText("", TextView.BufferType.EDITABLE);
                return false;
            }
        }
        else{
            Toast.makeText(this, "cannot find account", Toast.LENGTH_SHORT).show();
            return false;
        }

    }
}
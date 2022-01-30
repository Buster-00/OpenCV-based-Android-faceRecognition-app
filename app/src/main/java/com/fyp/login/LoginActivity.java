package com.fyp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.fyp.R;
import com.google.android.material.textfield.TextInputLayout;

import shem.com.materiallogin.DefaultLoginView;
import shem.com.materiallogin.DefaultRegisterView;
import shem.com.materiallogin.MaterialLoginView;

public class LoginActivity extends AppCompatActivity {

    //widget
    private MaterialLoginView materialLoginView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize widget
        materialLoginView = findViewById(R.id.material_log_in);
        InitLogin();

    }

    private void InitLogin(){

        //Log in event
        ((DefaultLoginView)materialLoginView.getLoginView()).setListener(new DefaultLoginView.DefaultLoginViewListener() {
            @Override
            public void onLogin(TextInputLayout textInputLayout, TextInputLayout textInputLayout1) {
                //TODO Login
            }
        });

        //Register event
        ((DefaultRegisterView)materialLoginView.getRegisterView()).setListener(new DefaultRegisterView.DefaultRegisterViewListener() {
            @Override
            public void onRegister(TextInputLayout textInputLayout, TextInputLayout textInputLayout1, TextInputLayout textInputLayout2) {
                //TODO Register

            }
        });
    }
}
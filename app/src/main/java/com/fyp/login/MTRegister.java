package com.fyp.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.Toast;

import com.fyp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import cn.fanrunqi.materiallogin.a.AActivityTwo;

public class MTRegister extends AppCompatActivity implements Validator.ValidationListener {

        //Widget
        @NotEmpty
        private EditText et_reg_username;

        @Password(min = 6, scheme = Password.Scheme.ALPHA_MIXED_CASE)
        private EditText et_reg_password;

        @ConfirmPassword
        private EditText et_reg_repeatPassword;

        private FloatingActionButton fab;
        private CardView cvAdd;

        //Validator
        Validator validator = new Validator(this);

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_material_register);
            ShowEnterAnimation();
            initView();
            initValidator();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validator.validate();
                    animateRevealClose();
                }
            });
        }

    private void initValidator() {
            validator.setValidationListener(this);

    }

    private void initView() {
            et_reg_username = findViewById(R.id.et_username);
            et_reg_password = findViewById(R.id.et_password);
            et_reg_repeatPassword = findViewById(R.id.et_repeatpassword);
            fab = findViewById(R.id.fab);
            cvAdd = findViewById(R.id.cv_add);
        }

        private void ShowEnterAnimation() {
            Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
            getWindow().setSharedElementEnterTransition(transition);

            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                    cvAdd.setVisibility(View.GONE);
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    transition.removeListener(this);
                    animateRevealShow();
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }


            });
        }

        public void animateRevealShow() {
            Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth()/2,0, fab.getWidth() / 2, cvAdd.getHeight());
            mAnimator.setDuration(500);
            mAnimator.setInterpolator(new AccelerateInterpolator());
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    cvAdd.setVisibility(View.VISIBLE);
                    super.onAnimationStart(animation);
                }
            });
            mAnimator.start();
        }

        public void animateRevealClose() {
            Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd,cvAdd.getWidth()/2,0, cvAdd.getHeight(), fab.getWidth() / 2);
            mAnimator.setDuration(500);
            mAnimator.setInterpolator(new AccelerateInterpolator());
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    cvAdd.setVisibility(View.INVISIBLE);
                    super.onAnimationEnd(animation);
                    fab.setImageResource(R.drawable.plus);
                    MTRegister.super.onBackPressed();
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                }
            });
            mAnimator.start();
        }
        @Override
        public void onBackPressed() {
            animateRevealClose();
        }

    @Override
    public void onValidationSucceeded() {
        Toast.makeText(this, "Register successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for(ValidationError error : errors){
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if(view instanceof EditText){
                ((EditText) view).setError(message);
            }
            else{
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
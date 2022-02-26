package cn.fanrunqi.materiallogin.a;


import android.os.Bundle;
import android.transition.Explode;

import androidx.appcompat.app.AppCompatActivity;

import cn.fanrunqi.materiallogin.R;

public class AActivityThree extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_three);

        Explode explode = new Explode();
        explode.setDuration(500);
        getWindow().setExitTransition(explode);
        getWindow().setEnterTransition(explode);
    }
}

package com.elham.sound_direction_detection_app.ui.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;

import com.elham.sound_direction_detection_app.databinding.ActivitySplashBinding;
import com.elham.sound_direction_detection_app.ui.base.BaseActivity;
import com.elham.sound_direction_detection_app.ui.help.HelpActivity;

import java.io.File;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends BaseActivity {

    private final String TAG = this.getClass().getName();
    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        new CountDownTimer(2000, 1000) {

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(SplashActivity.this, HelpActivity.class));
            }
        }.start();

    }
}
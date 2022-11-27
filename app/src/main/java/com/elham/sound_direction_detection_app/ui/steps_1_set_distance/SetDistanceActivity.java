package com.elham.sound_direction_detection_app.ui.steps_1_set_distance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.elham.sound_direction_detection_app.app.AppGlobalVariables;
import com.elham.sound_direction_detection_app.databinding.ActivitySelectDistanceBinding;
import com.elham.sound_direction_detection_app.ui.base.BaseActivity;
import com.elham.sound_direction_detection_app.ui.steps_2_get_point_sound.GetPointSoundActivity;

public class SetDistanceActivity extends BaseActivity {

    private ActivitySelectDistanceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectDistanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnNext.setOnClickListener(view -> {
            try {
                int distance = Integer.parseInt(binding.etDistance.getText().toString().trim());
                if (distance > 0) {
                    AppGlobalVariables.init();
                    AppGlobalVariables.distance = distance;
                    startActivity(new Intent(SetDistanceActivity.this, GetPointSoundActivity.class));
                } else {
                    Toast.makeText(this, "فاصله نمیتواند صفر یا منفی باشد.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "لطفا عدد صحیح وارد کنید.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package com.elham.sound_direction_detection_app.ui.help;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import com.elham.sound_direction_detection_app.databinding.ActivityHelpBinding;
import com.elham.sound_direction_detection_app.ui.base.BaseActivity;
import com.elham.sound_direction_detection_app.ui.steps_1_set_distance.SetDistanceActivity;

public class HelpActivity extends BaseActivity {

    private final String TAG = this.getClass().getName();
    private ActivityHelpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 10);
        }

        binding.btnNext.setOnClickListener(view -> {
            startActivity(new Intent(HelpActivity.this, SetDistanceActivity.class));
        });
    }
}
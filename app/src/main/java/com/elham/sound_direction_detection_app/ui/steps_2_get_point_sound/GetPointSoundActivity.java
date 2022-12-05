package com.elham.sound_direction_detection_app.ui.steps_2_get_point_sound;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.elham.sound_direction_detection_app.app.AppGlobalVariables;
import com.elham.sound_direction_detection_app.databinding.ActivityFirstPointBinding;
import com.elham.sound_direction_detection_app.ui.base.BaseActivity;
import com.elham.sound_direction_detection_app.ui.steps_3_show_result.ShowResultActivity;
import com.elham.sound_direction_detection_app.utils.OrientationUtils;

import java.io.File;
import java.util.List;

import linc.com.amplituda.Amplituda;
import linc.com.amplituda.exceptions.io.AmplitudaIOException;
import linc.com.library.AudioTool;

public class GetPointSoundActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getName();
    private ActivityFirstPointBinding binding;
    private static double mEMA = 0.0;
    private MediaRecorder mRecorder;
    private static final double EMA_FILTER = 0.6;
    private int step = 1;
    private char currentDirection = 'N';
    private int currentDirectionIndex = 0;
    private float currentAzimuth = 0;
    private OrientationUtils orientationUtils;
    private int[] wavesArray;
    private int wavesArrayCounter;
    private File tempSoundFile;
    private float[] degreeDifferenceArray;
    private float degreeDifference = 0;
    private int degreeDifferenceIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFirstPointBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnNext.setOnClickListener(this);
        binding.tvTitle.setText(String.format("دریافت صدا در نقطه %c", (char) AppGlobalVariables.currentPointAsciiCode));
        //Prepare
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        tempSoundFile = new File(folder, "temp_sound.wav");
        setupOrientation();
        //Do it
        doSteps();
    }

    @Override
    protected void onStart() {
        super.onStart();
        orientationUtils.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        orientationUtils.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        orientationUtils.stop();
    }

    private void setupOrientation() {
        orientationUtils = new OrientationUtils(this);
        OrientationUtils.OrientationListener cl = getCompassListener();
        orientationUtils.setListener(cl);
    }

    private void adjustArrow(float azimuth) {
        Animation an = new RotateAnimation(currentAzimuth, azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        currentAzimuth = azimuth;
        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);
        binding.coordinatorAngle.startAnimation(an);
        binding.tvAngel.setText(String.format("%s", Math.round(azimuth)));

        //North
        if (step == 2) {
            float temp = azimuth > 180 ? (360 - azimuth) : azimuth;
            degreeDifference = temp;
            binding.tvAngelDiff.setText(String.format("\u2103 %s", Math.round(temp)));
            if (azimuth < 10 || azimuth > 350) {
                binding.btnNext.setVisibility(View.VISIBLE);
            } else {
                binding.btnNext.setVisibility(View.GONE);
            }
        }
        if (step == 3) {
            float temp = azimuth > 180 ? (360 - azimuth) : azimuth;
            degreeDifference = temp;
            binding.tvAngelDiff.setText(String.format("\u2103 %s", Math.round(temp)));
        }
        //East
        if (step == 4) {
            float temp = 90 - azimuth;
            degreeDifference = temp;
            binding.tvAngelDiff.setText(String.format("\u2103 %s", Math.round(Math.abs(temp))));
            if (azimuth > 80 && azimuth < 100) {
                binding.btnNext.setVisibility(View.VISIBLE);
            } else {
                binding.btnNext.setVisibility(View.GONE);
            }
        }
        if (step == 5) {
            float temp = 90 - azimuth;
            degreeDifference = temp;
            binding.tvAngelDiff.setText(String.format("\u2103 %s", Math.round(Math.abs(temp))));
        }
        //South
        if (step == 6) {
            float temp = 180 - azimuth;
            degreeDifference = temp;
            binding.tvAngelDiff.setText(String.format("\u2103 %s", Math.round(Math.abs(temp))));
            if (azimuth > 170 && azimuth < 190) {
                binding.btnNext.setVisibility(View.VISIBLE);
            } else {
                binding.btnNext.setVisibility(View.GONE);
            }
        }
        if (step == 7) {
            float temp = 180 - azimuth;
            degreeDifference = temp;
            binding.tvAngelDiff.setText(String.format("\u2103 %s", Math.round(Math.abs(temp))));
        }
        //West
        if (step == 8) {
            float temp = 270 - azimuth;
            degreeDifference = temp;
            binding.tvAngelDiff.setText(String.format("\u2103 %s", Math.round(Math.abs(temp))));
            if (azimuth > 260 && azimuth < 280) {
                binding.btnNext.setVisibility(View.VISIBLE);
            } else {
                binding.btnNext.setVisibility(View.GONE);
            }
        }
        if (step == 9) {
            float temp = 270 - azimuth;
            degreeDifference = temp;
            binding.tvAngelDiff.setText(String.format("\u2103 %s", Math.round(Math.abs(temp))));
        }
        if (step == 10) {
            binding.btnNext.setVisibility(View.VISIBLE);
        }
    }

    private OrientationUtils.OrientationListener getCompassListener() {
        return azimuth -> {
            // UI updates only in UI thread
            runOnUiThread(() -> adjustArrow(azimuth));
        };
    }

    public void processSound() {
        showLoading();
        new Handler(Looper.getMainLooper()).post(() -> {
            Log.d(TAG, "processSoundThreadInit: Start processing.............");
            try {
                AudioTool.getInstance(GetPointSoundActivity.this)
                        .withAudio(tempSoundFile)
                        .generateWaveform(2048, 2048, "#f84a91", "/storage/emulated/0/Download/wave_for_point_" + (char) AppGlobalVariables.currentPointAsciiCode + "_direction_" + currentDirectionIndex + "_before.png", null)
//                        .removeAudioNoise(output -> {
//                        })
//                        .removeVocal(output -> {
//                        })
                        .normalizeAudioVolume(output -> {
                            Amplituda amplituda = new Amplituda(GetPointSoundActivity.this);
                            amplituda.processAudio(output)
                                    .get(result -> {
                                        List<Integer> amplitudesData = result.amplitudesAsList();
                                        float average = 0;
                                        float average2 = 0;
                                        binding.tvSoundAmp.setText(amplitudesData.size()+"-");
                                        for (Integer i : amplitudesData) {
                                            average += (i * 1000);
                                            average2 += (1 - (degreeDifferenceArray[i] / 180)) * (i * 1000);
//                                            Log.d(TAG, "processSound:  " + i);
                                        }
                                        average /= amplitudesData.size();
                                        average2 /= amplitudesData.size();
                                        AppGlobalVariables.data[AppGlobalVariables.currentPointIndex][currentDirectionIndex] = (int) average;
                                        GetPointSoundActivity.this.dismissLoading();
                                        binding.tvAverage.setText(String.format("%s\nمتوسط %s (%s میکروفن پایین) = %s :@>:%s", binding.tvAverage.getText().toString(), AppGlobalVariables.getDirectionPersianName(String.valueOf(currentDirection)), AppGlobalVariables.getDirectionPersianNameForBottomSensors(String.valueOf(currentDirection)), (int) average , (int)average2));
                                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                                        } else {
                                            v.vibrate(500);
                                        }
                                        if (currentDirection == 'N') {
                                            currentDirection = 'E';
                                            currentDirectionIndex = 1;
                                        } else if (currentDirection == 'E') {
                                            currentDirection = 'S';
                                            currentDirectionIndex = 2;
                                        } else if (currentDirection == 'S') {
                                            currentDirection = 'W';
                                            currentDirectionIndex = 3;
                                        }
                                        step++;
                                        doSteps();
                                    }, exception -> {
                                        dismissLoading();
                                        if (exception instanceof AmplitudaIOException) {
                                            System.out.println("IO Exception!");
                                        }
                                    });
                        }).generateWaveform(2048, 2048, "#f84a91", "/storage/emulated/0/Download/wave_for_point_" + (char) AppGlobalVariables.currentPointAsciiCode + "_direction_" + currentDirectionIndex + "_after.png", null)
                        .release();
            } catch (Exception e) {
                dismissLoading();
                Log.d(TAG, "run: why???");
                e.printStackTrace();
            }
        });
    }

    private void doSteps() {
        switch (step) {
            case 1: {
                binding.tvMessage.setText(String.format("در نقطه %c قرار بگیرید.", (char) AppGlobalVariables.currentPointAsciiCode));
                break;
            }
            case 2: {
                binding.tvMessage.setText("رو به شمال ایستاده، گوشی را ثابت نگه داشته و دکمه شروع را بزنید. زمانی که گوشی لرزید به مرحله بعد بروید.");
                binding.btnNext.setText("شروع");
                binding.btnNext.setVisibility(View.GONE);
                binding.coordinatorDirection.setVisibility(View.VISIBLE);
                binding.tvAngel.setVisibility(View.VISIBLE);
                binding.tvAngelDiff.setVisibility(View.VISIBLE);
                binding.tvSoundAmp.setVisibility(View.VISIBLE);
                break;
            }
            case 3: {
                startTimer();
                startRecorder();
                binding.tvMessage.setText("تا زمان لرزش منتظر بمانید.");
                binding.btnNext.setVisibility(View.GONE);
                break;
            }
            case 4: {
                binding.tvMessage.setText("رو به شرق ایستاده، و مراحل قبل را تکرار کنید.");
                binding.btnNext.setVisibility(View.GONE);
                binding.coordinatorDirection.setVisibility(View.VISIBLE);
                binding.tvAngel.setVisibility(View.VISIBLE);
                binding.tvAngelDiff.setVisibility(View.VISIBLE);
                binding.tvSoundAmp.setVisibility(View.VISIBLE);
                break;
            }
            case 5: {
                startTimer();
                startRecorder();
                binding.tvMessage.setText("تا زمان لرزش منتظر بمانید.");
                binding.btnNext.setVisibility(View.GONE);
                break;
            }
            case 6: {
                binding.tvMessage.setText("رو به جنوب ایستاده، و مراحل قبل را تکرار کنید.");
                binding.btnNext.setVisibility(View.GONE);
                binding.coordinatorDirection.setVisibility(View.VISIBLE);
                binding.tvAngel.setVisibility(View.VISIBLE);
                binding.tvAngelDiff.setVisibility(View.VISIBLE);
                binding.tvSoundAmp.setVisibility(View.VISIBLE);
                break;
            }
            case 7: {
                startTimer();
                startRecorder();
                binding.tvMessage.setText("تا زمان لرزش منتظر بمانید.");
                binding.btnNext.setVisibility(View.GONE);
                break;
            }
            case 8: {
                binding.tvMessage.setText("رو به غرب ایستاده، و مراحل قبل را تکرار کنید.");
                binding.btnNext.setVisibility(View.GONE);
                binding.coordinatorDirection.setVisibility(View.VISIBLE);
                binding.tvAngel.setVisibility(View.VISIBLE);
                binding.tvAngelDiff.setVisibility(View.VISIBLE);
                binding.tvSoundAmp.setVisibility(View.VISIBLE);
                break;
            }
            case 9: {
                startTimer();
                startRecorder();
                binding.tvMessage.setText("تا زمان لرزش منتظر بمانید.");
                binding.btnNext.setVisibility(View.GONE);
                break;
            }
            case 10: {
                binding.coordinatorDirection.setVisibility(View.GONE);
                binding.tvAngel.setVisibility(View.GONE);
                binding.tvAngelDiff.setVisibility(View.GONE);
                binding.tvSoundAmp.setVisibility(View.GONE);
                char firstPoint = (char) AppGlobalVariables.currentPointAsciiCode;
                char secondPoint = (char) (AppGlobalVariables.currentPointAsciiCode + 1);
                if (AppGlobalVariables.isSecondPoint) {
                    binding.tvMessage.setText(String.format("برای مشاهده نتیجه کلیک کنید."));
                    binding.btnNext.setText("مشاهده نتیجه");
                    binding.btnNext.setVisibility(View.VISIBLE);
                } else {
                    binding.tvMessage.setText(String.format("پایان بررسی نقطه %c، به نقطه %c رفته و دکمه ادامه را کلیک کنید.", firstPoint, secondPoint));
                    binding.btnNext.setText("ادامه");
                    binding.btnNext.setVisibility(View.VISIBLE);
                }
                break;
            }

        }
    }

    private void startTimer() {
        degreeDifference = 0;
        degreeDifferenceArray = new float[1000];
        degreeDifferenceIndex = 0;
        wavesArrayCounter = 0;
        wavesArray = new int[1000];
        binding.tvTimer.setVisibility(View.VISIBLE);
        new CountDownTimer(10000, 10) {
            @Override
            public void onTick(long l) {
                degreeDifferenceArray[degreeDifferenceIndex++] = degreeDifference;
                binding.tvTimer.setText(String.valueOf(l / 1000));
                if (l % 5 == 0) {
                    binding.tvSoundAmp.setText(String.format("%s dB", Math.round(getAmplitudeEMA())));
                }
                wavesArray[wavesArrayCounter++] = (int) getAmplitudeEMA();
                binding.waveformSeekBar.setSampleFrom(wavesArray);
                binding.waveformSeekBar.setProgress(wavesArrayCounter);
            }

            @Override
            public void onFinish() {
                stopRecorder();
                binding.tvTimer.setVisibility(View.GONE);
                processSound();
            }
        }.start();
    }

    public void startRecorder() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile(tempSoundFile.getAbsolutePath());
            try {
                mRecorder.prepare();
            } catch (java.io.IOException ioe) {
                android.util.Log.e("[Monkey]", "IOException: " + android.util.Log.getStackTraceString(ioe));

            } catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " + android.util.Log.getStackTraceString(e));
            }
            try {
                mRecorder.start();
            } catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " + android.util.Log.getStackTraceString(e));
            }
        }
    }

    public void stopRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRecorder();
        orientationUtils.stop();
    }

    public double getAmplitude() {
        if (mRecorder != null)
            return (mRecorder.getMaxAmplitude());
        else
            return 0;
    }

    public double getAmplitudeEMA() {
        double amp = getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == binding.btnNext.getId()) {
            switch (step) {
                case 1: {
                    step = 2;
                    doSteps();
                    break;
                }
                case 2: {
                    step = 3;
                    doSteps();
                    break;
                }
                case 4: {
                    step = 5;
                    doSteps();
                    break;
                }
                case 6: {
                    step = 7;
                    doSteps();
                    break;
                }
                case 8: {
                    step = 9;
                    doSteps();
                    break;
                }
                case 10: {
                    if (AppGlobalVariables.isSecondPoint) {
                        AppGlobalVariables.isSecondPoint = false;
                        startActivity(new Intent(GetPointSoundActivity.this, ShowResultActivity.class));
                        finish();
                    } else {
                        AppGlobalVariables.isSecondPoint = true;
                        AppGlobalVariables.currentPointAsciiCode++;
                        AppGlobalVariables.currentPointIndex++;
                        startActivity(new Intent(GetPointSoundActivity.this, GetPointSoundActivity.class));
                        finish();
                    }
                    break;
                }
            }
        }
    }
}
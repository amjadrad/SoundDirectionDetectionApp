package com.elham.sound_direction_detection_app.ui.steps_3_show_result;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.elham.sound_direction_detection_app.app.AppGlobalVariables;
import com.elham.sound_direction_detection_app.databinding.ActivityShowResultBinding;
import com.elham.sound_direction_detection_app.ui.base.BaseActivity;
import com.elham.sound_direction_detection_app.ui.steps_2_get_point_sound.GetPointSoundActivity;

public class ShowResultActivity extends BaseActivity {
    private final String TAG = this.getClass().getName();
    private ActivityShowResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnNext.setOnClickListener(view -> {
            AppGlobalVariables.currentPointAsciiCode++;
            AppGlobalVariables.isSecondPoint = false;
            AppGlobalVariables.currentPointIndex++;
            startActivity(new Intent(ShowResultActivity.this, GetPointSoundActivity.class));
            finish();
        });

        //Point A
        int averageAN = AppGlobalVariables.data[AppGlobalVariables.currentPointIndex - 1][0];
        int averageAE = AppGlobalVariables.data[AppGlobalVariables.currentPointIndex - 1][1];
        int averageAS = AppGlobalVariables.data[AppGlobalVariables.currentPointIndex - 1][2];
        int averageAW = AppGlobalVariables.data[AppGlobalVariables.currentPointIndex - 1][3];
        //Point B
        int averageBN = AppGlobalVariables.data[AppGlobalVariables.currentPointIndex][0];
        int averageBE = AppGlobalVariables.data[AppGlobalVariables.currentPointIndex][1];
        int averageBS = AppGlobalVariables.data[AppGlobalVariables.currentPointIndex][2];
        int averageBW = AppGlobalVariables.data[AppGlobalVariables.currentPointIndex][3];


        //Just for show averages in logcat
        Log.d(TAG, "doSteps:AVN " + averageAN);
        Log.d(TAG, "doSteps:AVE " + averageAE);
        Log.d(TAG, "doSteps:AVS " + averageAS);
        Log.d(TAG, "doSteps:AVW " + averageAW);

        Log.d(TAG, "doSteps:BVN " + averageBN);
        Log.d(TAG, "doSteps:BVE " + averageBE);
        Log.d(TAG, "doSteps:BVS " + averageBS);
        Log.d(TAG, "doSteps:BVW " + averageBW);

        //Attach results for pint a like: NE | NW | SE | SW
        char firstDirectionPointA = 'N';
        if (averageAS > averageAN) {
            firstDirectionPointA = 'S';
        }
        char secondDirectionPointA = 'E';
        if (averageAW > averageAE) {
            secondDirectionPointA = 'W';
        }

        char firstDirectionPointB = 'N';
        if (averageBS > averageBN) {
            firstDirectionPointB = 'S';
        }
        char secondDirectionPointB = 'E';
        if (averageBW > averageBE) {
            secondDirectionPointB = 'W';
        }

        String a = String.format("%c%c", firstDirectionPointA, secondDirectionPointA);
        String b = String.format("%c%c", firstDirectionPointB, secondDirectionPointB);

        Log.d(TAG, "onCreate: a: " + a);
        Log.d(TAG, "onCreate: b: " + b);

        binding.tvFormula.setText(String.format("%s:%s", a, b));
        String resultB = AppGlobalVariables.getVectorsResultForPointB(String.format("%s:%s", a, b));
        Log.d(TAG, "onCreate: Result is: " + resultB);
        if (resultB.equals("ERROR")) {
            binding.tvResultStartFromB.setText("خطا! این خطا ممکن است به علت خطاهای اندازه گیری و یا نویز بالا اتفاق افتاده باشد. لطفا مجدد امتحان کنید.");
        } else {
            String messageB = "";
            String[] arrResultB = resultB.split(",");
            if (arrResultB.length > 0) {
                String[] arr = arrResultB[0].split(":");
                messageB += String.format("%s متر به سمت %s،", (Integer.parseInt(arr[0]) * AppGlobalVariables.distance), AppGlobalVariables.getDirectionPersianNameForBottomSensors(arr[1]));
            }
            if (arrResultB.length > 1) {
                String[] arr = arrResultB[1].split(":");
                messageB += String.format("%s متر به سمت %s", (Integer.parseInt(arr[0]) * AppGlobalVariables.distance), AppGlobalVariables.getDirectionPersianNameForBottomSensors(arr[1]));
            }
            messageB += " حرکت کنید.";
            binding.tvResultStartFromB.setText(messageB);
        }
    }
}
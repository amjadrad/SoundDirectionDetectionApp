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


//        //Point A
//        int averageAN = 0;
//        int averageAE = 0;
//        int averageAS = 0;
//        int averageAW = 0;
//        //Point B
//        int averageBN = 0;
//        int averageBE = 0;
//        int averageBS = 0;
//        int averageBW = 0;

//        for (int i = 0; i < 1000; i++) {
//            averageAN += AppGlobalVariables.data[AppGlobalVariables.currentPointIndex - 1][0][i];
//            averageAE += AppGlobalVariables.data[AppGlobalVariables.currentPointIndex - 1][1][i];
//            averageAS += AppGlobalVariables.data[AppGlobalVariables.currentPointIndex - 1][2][i];
//            averageAW += AppGlobalVariables.data[AppGlobalVariables.currentPointIndex - 1][3][i];
//
//            averageBN += AppGlobalVariables.data[AppGlobalVariables.currentPointIndex][0][i];
//            averageBE += AppGlobalVariables.data[AppGlobalVariables.currentPointIndex][1][i];
//            averageBS += AppGlobalVariables.data[AppGlobalVariables.currentPointIndex][2][i];
//            averageBW += AppGlobalVariables.data[AppGlobalVariables.currentPointIndex][3][i];
//        }
//        averageAN /= 1000;
//        averageAE /= 1000;
//        averageAS /= 1000;
//        averageAW /= 1000;
//
//        averageBN /= 1000;
//        averageBE /= 1000;
//        averageBS /= 1000;
//        averageBW /= 1000;


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


        Log.d(TAG, "doSteps:AVN " + averageAN);
        Log.d(TAG, "doSteps:AVE " + averageAE);
        Log.d(TAG, "doSteps:AVS " + averageAS);
        Log.d(TAG, "doSteps:AVW " + averageAW);

        Log.d(TAG, "doSteps:BVN " + averageBN);
        Log.d(TAG, "doSteps:BVE " + averageBE);
        Log.d(TAG, "doSteps:BVS " + averageBS);
        Log.d(TAG, "doSteps:BVW " + averageBW);

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

//        String result = AppGlobalVariables.getVectorsResultForPointA(String.format("%s:%s", a, b));
//        Log.d(TAG, "onCreate: Result is: " + result);
//
//        if (result.equals("ERROR")) {
//            binding.tvResultStartFromA.setText("خطا! این خطا ممکن است به علت خطاهای اندازه گیری و یا نویز بالا اتفاق افتاده باشد. لطفا مجدد امتحان کنید.");
//        } else {
//
//            //Because microphone sensor is in front of phone, North is South and West is East
//            String message = "";
//            String[] arrResult = result.split(",");
//            if (arrResult.length > 0) {
//                String[] arr = arrResult[0].split(":");
//                message += String.format("%s متر به سمت %s،", (Integer.parseInt(arr[0]) * AppGlobalVariables.distance), AppGlobalVariables.getDirectionPersianNameForBottomSensors(arr[1]));
//            }
//            if (arrResult.length > 1) {
//                String[] arr = arrResult[1].split(":");
//                message += String.format("%s متر به سمت %s", (Integer.parseInt(arr[0]) * AppGlobalVariables.distance), AppGlobalVariables.getDirectionPersianNameForBottomSensors(arr[1]));
//            }
//            message += " حرکت کنید.";
//            binding.tvResultStartFromA.setText(message);
//        }

        String resultB = AppGlobalVariables.getVectorsResultForPointB(String.format("%s:%s", a, b));
        Log.d(TAG, "onCreate: Result is: " + resultB);

        if (resultB.equals("ERROR")) {
            binding.tvResultStartFromB.setText("خطا! این خطا ممکن است به علت خطاهای اندازه گیری و یا نویز بالا اتفاق افتاده باشد. لطفا مجدد امتحان کنید.");
        } else {
            //if start from point B
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
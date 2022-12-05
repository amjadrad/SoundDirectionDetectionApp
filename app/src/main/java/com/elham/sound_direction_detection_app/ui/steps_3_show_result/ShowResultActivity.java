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

        //Attach results for pint a like: NE | NW | SE | SW

        float averageDiffA1 = Math.abs(averageAS - averageAN) / (float) Math.max(averageAS, averageAN);
        char firstDirectionPointA = 'N';
        if (averageAS > averageAN) {
            firstDirectionPointA = 'S';
        }

        float averageDiffA2 = Math.abs(averageAW - averageAE) / (float) Math.max(averageAW, averageAE);
        char secondDirectionPointA = 'E';
        if (averageAW > averageAE) {
            secondDirectionPointA = 'W';
        }

        float averageDiffB1 = Math.abs(averageBS - averageBN) / (float) Math.max(averageBS, averageBN);
        char firstDirectionPointB = 'N';
        if (averageBS > averageBN) {
            firstDirectionPointB = 'S';
        }

        float averageDiffB2 = Math.abs(averageBW - averageBE) / (float) Math.max(averageBW, averageBE);
        char secondDirectionPointB = 'E';
        if (averageBW > averageBE) {
            secondDirectionPointB = 'W';
        }


        // A,B => NS
        char directionNORS = 'S';
        float finalNORS;
        if (firstDirectionPointA == firstDirectionPointB && firstDirectionPointB == 'N') {
            directionNORS = 'N';
            finalNORS = averageDiffA1 + averageDiffB1;
        } else if (firstDirectionPointA == firstDirectionPointB) {
            finalNORS = averageDiffA1 + averageDiffB1;
        } else {
            finalNORS = Math.abs(averageDiffA1 - averageDiffB1);
        }

        //A,B =>EW
        char directionWORE;
        float finalWORE;

        if ((secondDirectionPointA == secondDirectionPointB && secondDirectionPointA == 'E')) {
            finalWORE = averageDiffA2 + averageDiffB2;
            directionWORE = 'E';
        } else if (secondDirectionPointA == secondDirectionPointB) {
            finalWORE = averageDiffA2 + averageDiffB2;
            directionWORE = 'W';
        } else if (averageDiffA2 > averageDiffB2) {
            finalWORE = Math.abs(averageDiffA2 - averageDiffB2);
            directionWORE = secondDirectionPointA;
        } else {
            directionWORE = secondDirectionPointB;
            finalWORE = Math.abs(averageDiffA2 - averageDiffB2);
        }


        String message = String.format("%s متر به سمت %s", (finalNORS * AppGlobalVariables.distance), AppGlobalVariables.getDirectionPersianNameForBottomSensors(directionNORS + ""));
        message += String.format("\n %s متر به سمت %s", (finalWORE * AppGlobalVariables.distance), AppGlobalVariables.getDirectionPersianNameForBottomSensors(directionWORE + ""));
        binding.tvResultStartFromB.setText(message);

//        String a = String.format("%c%c", firstDirectionPointA, secondDirectionPointA);
//        String b = String.format("%c%c", firstDirectionPointB, secondDirectionPointB);
//        binding.tvFormula.setText(String.format("%s:%s", a, b));
//        String resultB = AppGlobalVariables.getVectorsResultForPointB(String.format("%s:%s", a, b));
//        if (resultB.equals("ERROR")) {
//            binding.tvResultStartFromB.setText("خطا! این خطا ممکن است به علت خطاهای اندازه گیری و یا نویز بالا اتفاق افتاده باشد. لطفا مجدد امتحان کنید.");
//        } else {
//            String messageB = "";
//            String[] arrResultB = resultB.split(",");
//            if (arrResultB.length > 0) {
//                String[] arr = arrResultB[0].split(":");
//                messageB += String.format("%s متر به سمت %s،", (Integer.parseInt(arr[0]) * AppGlobalVariables.distance), AppGlobalVariables.getDirectionPersianNameForBottomSensors(arr[1]));
//            }
//            if (arrResultB.length > 1) {
//                String[] arr = arrResultB[1].split(":");
//                messageB += String.format("%s متر به سمت %s", (Integer.parseInt(arr[0]) * AppGlobalVariables.distance), AppGlobalVariables.getDirectionPersianNameForBottomSensors(arr[1]));
//            }
//            messageB += " حرکت کنید.";
//            binding.tvResultStartFromB.setText(messageB);
//        }
    }
}
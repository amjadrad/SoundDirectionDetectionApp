package com.elham.sound_direction_detection_app.app;

import java.util.HashMap;

public class AppGlobalVariables {

    public static int distance = 2;//Default is 2
    //    public static int[][][] data = new int[100][4][1000];
    public static int[][] data = new int[100][4];//100 different point, 4 direction, average Amplitude

    public static int currentPointIndex = 0;
    public static int currentPointAsciiCode = 65;
    public static boolean isSecondPoint = false;

    public static void init() {
        distance = 2;
//        data = new int[100][4][1000];
        data = new int[100][4];
        currentPointIndex = 0;
        currentPointAsciiCode = 65;
        isSecondPoint = false;
    }

//    public static String getVectorsResultForPointA(String input) {
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("NE:NE", "2:N,2:E");
//        hashMap.put("NE:NW", "2:N");
//        hashMap.put("NE:SE", "1:N,2:E");
//        hashMap.put("NE:SW", "1:N");
//        hashMap.put("NW:NE", "2:N");
//        hashMap.put("NW:NW", "2:N,2:W");
//        hashMap.put("NW:SE", "1:N");
//        hashMap.put("NW:SW", "1:N,2:W");
//        hashMap.put("SE:NE", "2:E");
//        hashMap.put("SE:NW", "ERROR");
//        hashMap.put("SE:SE", "1:S,2:E");
//        hashMap.put("SE:SW", "1:S");
//        hashMap.put("SW:NE", "ERROR");
//        hashMap.put("SW:NW", "2:W");
//        hashMap.put("SW:SE", "1:S");
//        hashMap.put("SW:SW", "1:S,2:W");
//        return hashMap.get(input);
//    }


    public static String getVectorsResultForPointB(String input) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("NE:NE", "1:N,2:E");
        hashMap.put("NE:NW", "1:N");
        hashMap.put("NE:SE", "1:S,2:E");
        hashMap.put("NE:SW", "1:S");
        hashMap.put("NW:NE", "2:N");
        hashMap.put("NW:NW", "1:N,2:W");
        hashMap.put("NW:SE", "1:S");
        hashMap.put("NW:SW", "1:S,2:W");
        hashMap.put("SE:NE", "2:E");
        hashMap.put("SE:NW", "ERROR");
        hashMap.put("SE:SE", "2:S,2:E");
        hashMap.put("SE:SW", "2:S");
        hashMap.put("SW:NE", "ERROR");
        hashMap.put("SW:NW", "2:W");
        hashMap.put("SW:SE", "2:S");
        hashMap.put("SW:SW", "2:S,2:W");
        return hashMap.get(input);
    }


    public static String getDirectionPersianName(String input) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("N", "شمال");
        hashMap.put("E", "شرق");
        hashMap.put("S", "جنوب");
        hashMap.put("W", "غرب");
        return hashMap.get(input);
    }

    public static String getDirectionPersianNameForBottomSensors(String input) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("N", "جنوب");
        hashMap.put("E", "غرب");
        hashMap.put("S", "شمال");
        hashMap.put("W", "شرق");
        return hashMap.get(input);
    }

}

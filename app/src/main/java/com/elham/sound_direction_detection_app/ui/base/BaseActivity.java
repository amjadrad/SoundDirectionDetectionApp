package com.elham.sound_direction_detection_app.ui.base;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.elham.sound_direction_detection_app.R;

import java.util.Stack;

public class BaseActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();
    private Stack<View> loadingViewsStack;
    private ViewGroup loadingViewGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingViewsStack = new Stack<>();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    protected void showLoading() {
        loadingViewGroup = getWindow().getDecorView().findViewById(android.R.id.content);
        View loadingView = getLayoutInflater().inflate(R.layout.dialog_base_loading, null);
        CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        loadingView.setLayoutParams(layoutParams);
        loadingViewsStack.push(loadingView);
        loadingViewGroup.addView(loadingView);
    }

    protected void dismissLoading() {
        if (!loadingViewsStack.empty()) {
            for (View view : loadingViewsStack) {
                loadingViewGroup.removeView(view);
            }
            loadingViewsStack.clear();
        }
    }
}

package com.abc.qwert.thescience;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class HideFab extends FloatingActionButton.Behavior {

    Context context;

    public HideFab(Context context, AttributeSet attrs) {
        super();
        this.context = context;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        //InputMethodManager imm = (InputMethodManager) context.getSystemService(PhysSelectActivity.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(target.getWindowToken(), 0);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(sharedPreferences.getBoolean("hideFab", true)){
            if (dyConsumed > 0) {
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
                int fab_bottomMargin = layoutParams.bottomMargin;
                child.animate().translationY(2 * (child.getHeight() + fab_bottomMargin)).setInterpolator(new LinearInterpolator()).start();
            } else if (dyConsumed < 0) {
                child.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
            }
        } else {
            child.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
        }

    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int nestedScrollAxes) {

        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }
}

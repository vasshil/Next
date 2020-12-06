package com.abc.qwert.thescience;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

public class CustomLinearLayout extends LinearLayout {

    public CustomLinearLayout(Context context) {
        super(context);

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(target, velocityX, (Math.min(Math.abs(velocityY), 0.7f) ) * Math.signum(velocityY));
    }
}

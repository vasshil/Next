package com.abc.qwert.thescience;

import android.content.Context;
import android.util.AttributeSet;

import io.github.kexanie.library.MathView;

import static io.github.kexanie.library.MathView.Engine.KATEX;

public class AdvancedMathView extends MathView {
    public AdvancedMathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup() {
        getSettings().setSupportZoom(false);
        getSettings().setBuiltInZoomControls(false);

        //hide control view
        getSettings().setDisplayZoomControls(false);
        setEngine(KATEX);
    }
}

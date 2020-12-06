package com.abc.qwert.thescience;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

import io.github.kexanie.library.MathView;

public class FunctionCalculatorActivity extends AppCompatActivity {

    private static ImageView[] keyboardNumViews = new ImageView[24];
    private static ImageView[] keyboardFuncViews = new ImageView[24];



    private static int[] keyboardNumId = {
            R.id.key_x, R.id.key_left, R.id.key_right, R.id.key_backspace,
            R.id.key_frac, R.id.key_sqr, R.id.key_sqrt, R.id.key_clear,
            R.id.key_7, R.id.key_8, R.id.key_9, R.id.key_divide,
            R.id.key_4, R.id.key_5, R.id.key_6, R.id.key_times,
            R.id.key_1, R.id.key_2, R.id.key_3, R.id.key_minus,
            R.id.key_change_keyboard, R.id.key_0, R.id.key_point, R.id.key_plus
    };

    private static int[] keyboardFuncId = {
            R.id.key_pi, R.id.key_alpha, R.id.key_beta, R.id.key_backspace2,
            R.id.key_e, R.id.key_pow, R.id.key_nroot, R.id.key_clear2,
            R.id.key_ln, R.id.key_sin, R.id.key_asin, R.id.key_divide2,
            R.id.key_lg, R.id.key_cos, R.id.key_acos, R.id.key_times2,
            R.id.key_log, R.id.key_tg, R.id.key_atg, R.id.key_minus2,
            R.id.key_change_keyboard2, R.id.key_ctg, R.id.key_actg, R.id.key_plus2
    };


    private Derivative obj;

    private CardView mainCard;
    private CardView keyboardNumCard;
    private AppBarLayout appBar;
    private MathView mainFormula;
    private MathView finalFormula;
    private TextView error;
    private CoordinatorLayout keyboardBackground;
    private NestedScrollView scrollView;

    private Button graphButton;
    private Button hideGraphButton;

    private ImageButton positionLeft;
    private Button differentiationButton;
    private ImageButton positionRight;

    private BottomSheetBehavior bottomSheetBehavior;

    private GraphView graph;


    private String texMainFormula = "";
    private String texFinalFormula = "";

    private String textFinalFormula = "";


    private View transitionContents;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_calculator);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


        setupViews();

        new Handler().post(() -> {
            setupGraph();
            setupNumKeyboard();
            setupFuncKeyboard();

        });

        setupKeyboardSwipe();


        obj = new Derivative();

        graphButton.setOnClickListener((l) -> {
            if(graph.getVisibility() == View.INVISIBLE || graph.getVisibility() == View.GONE){
                graph.setAlpha(0f);
                graph.setVisibility(View.VISIBLE);
                graph.animate().alpha(1f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        graph.setAlpha(1f);
                    }
                }).start();
            }

        });

        hideGraphButton.setOnClickListener((l) -> {
            if(graph.getVisibility() == View.VISIBLE){
                graph.animate().alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        graph.setVisibility(View.INVISIBLE);
                        graph.setAlpha(0f);
                    }
                });
            }

        });

        differentiationButton.setOnClickListener((l) -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            if(!texMainFormula.isEmpty()){
                obj.setTextMainFormula();
                textFinalFormula = obj.getTextFinalFormula();
                texFinalFormula = obj.toTeX(textFinalFormula);
                setFinalFormula();
            }

        });

        positionLeft.setOnClickListener((l) -> {
            texMainFormula = obj.positionLeft();
            setMainFormula();
        });

        positionRight.setOnClickListener((l) -> {
            texMainFormula = obj.positionRight();
            setMainFormula();
        });

        mainCard.setOnClickListener((l) -> {
            new Handler().post(() -> {
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }

            });
            //Log.d("tag", "mainFormulaTouch");
        });



        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("B6130A2B6EDEB71E7270A790AD097F01")
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);



        transitionContents = this.<NestedScrollView>findViewById(R.id.calc_nsv);

        if (savedInstanceState == null) {
            transitionContents.setAlpha(0f);

            ObjectAnimator animator = ObjectAnimator.ofFloat(transitionContents, "alpha", 0f, 1f);

            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    new Handler().postDelayed(() -> {
                        if(PreferenceManager.getDefaultSharedPreferences(FunctionCalculatorActivity.this).getBoolean("calc_help", true)){
                            PreferenceManager.getDefaultSharedPreferences(FunctionCalculatorActivity.this).edit().putBoolean("calc_help", false).apply();
                            target1();
                        }
                    }, 500);

                }
            });

            animator.setDuration(1000);
            animator.start();
        }


    }

    private void setupViews(){
        appBar = findViewById(R.id.app_bar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_more_vert_white_24dp));

        mainCard = findViewById(R.id.mainCard);

        keyboardNumCard = findViewById(R.id.keyboard_num);
        keyboardBackground = findViewById(R.id.keyboardBackground);

        mainFormula = findViewById(R.id.inFormula);
        finalFormula = findViewById(R.id.finalFormula);

        error = findViewById(R.id.error);

        mainFormula.setHorizontalScrollBarEnabled(false);
        mainFormula.setVerticalScrollBarEnabled(false);

        finalFormula.setHorizontalScrollBarEnabled(false);
        finalFormula.setVerticalScrollBarEnabled(false);

        graphButton = findViewById(R.id.graphButton);
        hideGraphButton = findViewById(R.id.hideGraphButton);

        positionLeft = findViewById(R.id.positionLeftButton);
        differentiationButton = findViewById(R.id.differentiateButton);
        positionRight = findViewById(R.id.positionRightButton);

        scrollView = findViewById(R.id.calc_nsv);

        graph = findViewById(R.id.graph);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void setupGraph(){
        graph.getViewport().setScalable(false);
        graph.getViewport().setScalableY(false);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxX(5);
        graph.getViewport().setMinX(-5);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(5);
        graph.getViewport().setMinY(-5);

        //graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().resetStyles();
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
        graph.getLegendRenderer().setBackgroundColor(android.R.color.transparent);
        graph.getLegendRenderer().setTextColor(R.color.black);

        graph.getViewport().setScrollable(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(PreferenceManager.getDefaultSharedPreferences(FunctionCalculatorActivity.this).getBoolean("calc_help", false)){
            PreferenceManager.getDefaultSharedPreferences(FunctionCalculatorActivity.this).edit().putBoolean("calc_help", false).apply();
            target1();
        }
    }

    @Override
    public void onBackPressed() {
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            ObjectAnimator animator = ObjectAnimator.ofFloat(transitionContents, "alpha", 1f, 0f);
            animator.setDuration(1000);
            animator.start();

            supportFinishAfterTransition();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_bar, menu);
        menu.getItem(0).setOnMenuItemClickListener(menuItem -> {
            Intent intent = new Intent(FunctionCalculatorActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        });
        return true;
    }



    private void setupKeyboardSwipe(){
        bottomSheetBehavior = BottomSheetBehavior.from(keyboardBackground);

        bottomSheetBehavior.setHideable(false);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_HIDDEN){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if(newState == BottomSheetBehavior.STATE_EXPANDED){
                    appBar.setExpanded(false, true);
                    scrollView.setNestedScrollingEnabled(false);
                    showRevealLine();
                } else if(newState == BottomSheetBehavior.STATE_COLLAPSED){
                    hideRevealLine();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) { }
        });


    }

    private void setupNumKeyboard(){
        for(int i=0;i<24;i++) {
            keyboardNumViews[i] = findViewById(keyboardNumId[i]);
            //Log.d("tag", String.valueOf(keyboardNumViews[i].getBottom() - keyboardNumViews[i].getTop()) + " " + getResources().getDisplayMetrics().densityDpi);

            final int j = i;
            keyboardNumViews[i].setOnClickListener((l) -> {
                if(j == 3){
                    if(!texMainFormula.isEmpty()){
                        texMainFormula = obj.deleteToken();
                        setMainFormula();
                    }
                } else if(j == 7) {
                    if(!texMainFormula.isEmpty()){
                        texMainFormula = "\\square";
                        obj.clear();
                        setMainFormula();
                    }
                } else if(j == 20){
                    hideNumKeyboard();
                } else {
                    texMainFormula = obj.addToken("num", j);
                    setMainFormula();
                }

            });


        }

    }

    private void setupFuncKeyboard(){
        for(int i=0;i<24;i++) {
            keyboardFuncViews[i] = findViewById(keyboardFuncId[i]);

            final int j = i;
            keyboardFuncViews[i].setOnClickListener((l) -> {
                if(j == 3){
                    if(!texMainFormula.isEmpty()){
                        texMainFormula = obj.deleteToken();
                        setMainFormula();
                    }
                } else if(j == 7) {
                    if(!texMainFormula.isEmpty()){
                        texMainFormula = "\\square";
                        obj.clear();
                        setMainFormula();
                    }
                } else if(j == 20){
                    new Handler().postDelayed(this::showNumKeyboard, 100);
                } else {
                    texMainFormula = obj.addToken("func", j);
                    setMainFormula();
                    showNumKeyboard();
                }

            });

        }

    }


    private void showRevealLine(){
        ImageView revealLine = findViewById(R.id.reveal_line);

        Animator animator = ViewAnimationUtils.createCircularReveal(
                revealLine,
                getResources().getDisplayMetrics().widthPixels / 4,
                (revealLine.getTop() + revealLine.getBottom()) / 2,
                0,
                getResources().getDisplayMetrics().widthPixels);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

                revealLine.setVisibility(View.VISIBLE);

            }
        });

        animator.setDuration(500);
        animator.start();

    }

    private void hideRevealLine(){
        ImageView revealLine = findViewById(R.id.reveal_line);

        Animator animator = ViewAnimationUtils.createCircularReveal(
                revealLine,
                getResources().getDisplayMetrics().widthPixels / 4,
                (revealLine.getTop() + revealLine.getBottom()) / 2,
                getResources().getDisplayMetrics().widthPixels,
                0);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                revealLine.setVisibility(View.INVISIBLE);

            }
        });

        animator.setDuration(500);
        animator.start();

    }


    private void showNumKeyboard(){
        keyboardNumCard.animate().translationY(0).setDuration(200).start();

    }

    private void hideNumKeyboard(){
        keyboardNumCard.animate().translationY(374 * getResources().getDisplayMetrics().density).setDuration(200).start();

    }



    private void setMainFormula(){
        mainFormula.setText("\\(f(x) = {" + texMainFormula + "}\\)");
        //mainFormula.setText("f(x) = " + textMainFormula);
    }

    private void setFinalFormula(){
        if(texFinalFormula.equals("error")){
            if(error.getVisibility() == View.INVISIBLE){
                error.setVisibility(View.VISIBLE);
            }
        } else if(!texFinalFormula.isEmpty()){
            if(error.getVisibility() == View.VISIBLE){
                error.setVisibility(View.INVISIBLE);
            }
            finalFormula.setText("\\(f'(x) = {" + texFinalFormula + "}\\)");
            new Handler().post(this::buildGraph);

        }

    }


    private void buildGraph(){
        graph.removeAllSeries();
        String f = obj.getTextMainFormula();
        String F = textFinalFormula;
        ExprEvaluator graphUtil = new ExprEvaluator();
        IExpr graphResult;

        //f(x)
        int addPosition = 0;
        DataPoint[] dataPointsf = new DataPoint[101];
        for(int i=-50;i<=50;i++){
            double x = i / 10d;

            graphUtil.eval("x=" + x);
            graphResult = graphUtil.eval(f);

            double y;
            try {
                BigDecimal decimal = new BigDecimal(graphResult.toString());
                y = Double.parseDouble(decimal.setScale(3, BigDecimal.ROUND_HALF_UP).toString());
            } catch (Exception e){
                continue;
            }

            dataPointsf[addPosition] = new DataPoint(x, y);
            addPosition++;
        }

        dataPointsf = Arrays.copyOf(dataPointsf, addPosition);
        LineGraphSeries<DataPoint> seriesf = new LineGraphSeries<>(dataPointsf);
        seriesf.setTitle("f(x)");
        seriesf.setThickness(7);
        seriesf.setColor(getResources().getColor(R.color.colorf));
        seriesf.setAnimated(true);



        //f'(x)
        addPosition = 0;
        DataPoint[] dataPointsF = new DataPoint[101];
        for(int i=-50;i<=50;i++){
            float x = i / 10f;

            graphUtil.eval("x=" + x);
            graphResult = graphUtil.eval(F);

            double y;
            try {
                BigDecimal decimal = new BigDecimal(graphResult.toString());
                y = Double.parseDouble(decimal.setScale(3, BigDecimal.ROUND_HALF_UP).toString());
            } catch (Exception e){
                continue;
            }

            dataPointsF[addPosition] = new DataPoint(x, y);
            addPosition++;
        }

        dataPointsF = Arrays.copyOf(dataPointsF, addPosition);
        LineGraphSeries<DataPoint> seriesF = new LineGraphSeries<>(dataPointsF);
        seriesF.setTitle("f'(x)");
        seriesF.setThickness(7);
        seriesF.setColor(getResources().getColor(R.color.colorF));
        seriesF.setAnimated(true);

        graph.addSeries(seriesf);
        graph.addSeries(seriesF);

    }


/*

    private void target(View view) {
        final TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(view, "Words words words")
                                .outerCircleColor(R.color.calcAccent)      // Specify a color for the outer circle
                                .targetCircleColor(R.color.white)   // Specify a color for the target circle
                                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.white)      // Specify the color of the title text
                                .descriptionTextSize(18)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.white)  // Specify the color of the description text
                                .textColor(R.color.white)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(true)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(false)                   // Whether to tint the target view's color
                                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(50)
                                .id(1)
                )
                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                        Log.d("TapTargetView", "onSequenceFinish");
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                    }
                });
        sequence.considerOuterCircleCanceled(true);
        sequence.continueOnCancel(true);
        sequence.start();
    }
*/

    private void target1() {
        TapTargetView.showFor(this,
                TapTarget.forView(findViewById(R.id.differentiateButton),
                        getResources().getString(R.string.calc_target2_title),
                        getResources().getString(R.string.calc_target2_description))
                        .outerCircleColor(R.color.calcAccent)
                        .outerCircleAlpha(0.9f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(25)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(18)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.white)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(false)
                        .icon(getResources().getDrawable(R.drawable.nothing))
                        .targetRadius(60),
                new TapTargetView.Listener(){
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                        target2();


                    }
                }
        );
    }

    private void target2() {
        TapTargetView.showFor(this,
                TapTarget.forView(findViewById(R.id.graphButton),
                        getResources().getString(R.string.calc_target3_title),
                        getResources().getString(R.string.calc_target3_description))
                        .outerCircleColor(R.color.calcAccent)
                        .outerCircleAlpha(0.9f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(25)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(18)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.white)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(false)
                        .icon(getResources().getDrawable(R.drawable.nothing))
                        .targetRadius(60),
                new TapTargetView.Listener(){
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                        target3();

                    }
                }
        );
    }

    private void target3(){
        TapTargetView.showFor(this,
                TapTarget.forView(findViewById(R.id.mainCard),
                        getResources().getString(R.string.calc_target1_title),
                        getResources().getString(R.string.calc_target1_description))
                        .outerCircleColor(R.color.calcAccent)
                        .outerCircleAlpha(0.9f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(25)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(18)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.white)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(false)
                        .icon(getResources().getDrawable(R.drawable.nothing))
                        .targetRadius(60),
                new TapTargetView.Listener() {
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        target4();
                    }
                });
    }

    private void target4(){
        TapTargetView.showFor(this,
                TapTarget.forView(findViewById(R.id.key_change_keyboard),
                        getResources().getString(R.string.calc_target4_title),
                        getResources().getString(R.string.calc_target4_description))
                        .outerCircleColor(R.color.calcAccent)
                        .outerCircleAlpha(0.9f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(25)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(18)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.white)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(false)
                        .icon(getResources().getDrawable(R.drawable.nothing))
                        .targetRadius(60),
                new TapTargetView.Listener(){
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);

                    }
                }
        );
    }


}

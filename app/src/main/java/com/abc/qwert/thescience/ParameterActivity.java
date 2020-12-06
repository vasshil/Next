package com.abc.qwert.thescience;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

public class ParameterActivity extends AppCompatActivity {

    String a = "0";
    String formula = "";

    private GraphView graph;
    private EditText enterFunction;
    private EditText enterParameter;
    private TextView errorText;

    private View transitionContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);

        graph = findViewById(R.id.graph);

        enterFunction = findViewById(R.id.enter_formula);
        enterParameter = findViewById(R.id.enter_parameter);

        Button buildGraph = findViewById(R.id.build_graph);
        Button clear = findViewById(R.id.clear);

        errorText = findViewById(R.id.error);

        SeekBar seekBar = findViewById(R.id.seek_bar);


        setupGraph();


        enterParameter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                a = enterParameter.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        enterFunction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                formula = enterFunction.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        enterParameter.setOnEditorActionListener((textView, i, keyEvent) -> buildGraph.callOnClick());
        enterFunction.setOnEditorActionListener((textView, i, keyEvent) -> buildGraph.callOnClick());

        buildGraph.setOnClickListener((l) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(ParameterActivity.INPUT_METHOD_SERVICE);
            try{
                imm.hideSoftInputFromWindow(Objects.requireNonNull(this.getCurrentFocus()).getWindowToken(), 0);
            } catch (NullPointerException e){ }

            buildGraph();
        });
        clear.setOnClickListener((l) -> clear());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                a = i + "";
                enterParameter.setText(a);
                buildGraph();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_more_vert_white_24dp));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        transitionContents = findViewById(R.id.param_main);

        if (savedInstanceState == null) {
            transitionContents.setAlpha(0f);

            ObjectAnimator animator = ObjectAnimator.ofFloat(transitionContents, "alpha", 0f, 1f);
            animator.setDuration(1000);
            animator.start();
        }
    }

    private void buildGraph(){
        if(a.isEmpty()){
            errorText.setVisibility(View.VISIBLE);
        } else {
            try{
                errorText.setVisibility(View.INVISIBLE);

                graph.removeAllSeries();
                ExprEvaluator graphUtil = new ExprEvaluator();
                graphUtil.eval("a = " + a);

                formula = enterFunction.getText().toString();
                formula = formula.replaceAll("=", "==");

                int n = 10;
                ArrayList<DataPoint>[] dataPoints = (ArrayList<DataPoint>[]) new ArrayList[n];

                IExpr[] graphResults;
                if(formula.contains("y")){
                    formula = graphUtil.eval("Solve({" + formula + "}, {y})").toString();
                    formula = formula.substring(2, formula.length() - 2);
                    formula = formula.replaceAll("y->", "");
                    String[] formulas = formula.split("\\},\\{");
                    n = formulas.length;
                    graphResults = new IExpr[n];
                    for(int i=0;i<n;i++) {
                        graphResults[i] = graphUtil.eval(formulas[i]);
                        dataPoints[i] = new ArrayList<>();
                    }
                } else {
                    n = 1;
                    graphResults = new IExpr[n];
                    dataPoints[0] = new ArrayList<>();
                    graphResults[0] = graphUtil.eval(formula);
                }

                for(int i=-100;i<=100;i++){
                    double x = i / 10d;

                    graphUtil.eval("x=" + x);
                    for(int j = 0;j<n;j++){
                        IExpr graphResult = graphUtil.eval(graphResults[j]);
                        double y;
                        try {
                            BigDecimal decimal = new BigDecimal(graphResult.toString());
                            y = Double.parseDouble(decimal.setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                            dataPoints[j].add(new DataPoint(x, y));
                        } catch (Exception e){}
                    }
                }

                for (int i=0;i<n;i++) {
                    if(dataPoints[0].isEmpty()){
                        errorText.setVisibility(View.VISIBLE);
                    } else {
                        errorText.setVisibility(View.INVISIBLE);
                        LineGraphSeries<DataPoint> seriesf = new LineGraphSeries(dataPoints[i].toArray(new DataPoint[dataPoints[i].size()]));
                        seriesf.setThickness(7);
                        seriesf.setColor(getResources().getColor(R.color.parameterAccent));
                        seriesf.setAnimated(false);

                        graph.addSeries(seriesf);
                    }
                }
            } catch (Exception exception){
                errorText.setVisibility(View.VISIBLE);
            }

        }


    }

    private void clear(){
        formula = "";
        enterFunction.setText("");
        graph.removeAllSeries();
        graph.clearSecondScale();
        errorText.setVisibility(View.INVISIBLE);
    }

    private void setupGraph(){
        CardView graphCard = findViewById(R.id.graph_card);
        android.view.ViewGroup.LayoutParams layoutParams = graphCard.getLayoutParams();
        layoutParams.height = getWindowManager().getDefaultDisplay().getWidth() - 2 * (getResources().getDimensionPixelOffset(R.dimen.margin));
        graphCard.setLayoutParams(layoutParams);

        graph.getViewport().setScalable(false);
        graph.getViewport().setScalableY(false);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxX(10);
        graph.getViewport().setMinX(-10);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(10);
        graph.getViewport().setMinY(-10);

        //graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);

        graph.getLegendRenderer().setVisible(false);
        graph.getGridLabelRenderer().setGridColor(R.color.black);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(R.color.black);
        graph.getGridLabelRenderer().setVerticalLabelsColor(R.color.black);
//        graph.getLegendRenderer().resetStyles();
//        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
//        graph.getLegendRenderer().setBackgroundColor(android.R.color.transparent);
//        graph.getLegendRenderer().setTextColor(R.color.black);

        //graph.getViewport().setScrollable(true);

    }


    @Override
    public void onBackPressed() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(transitionContents, "alpha", 1f, 0f);
        animator.setDuration(1000);
        animator.start();

        supportFinishAfterTransition();
    }


    @SuppressLint("InflateParams")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_param, menu);

        menu.findItem(R.id.action_settings_param).setOnMenuItemClickListener(menuItem -> {
            Intent intent = new Intent(ParameterActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        });

        return true;
    }


}

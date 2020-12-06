package com.abc.qwert.thescience;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import com.google.android.material.appbar.AppBarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.abc.qwert.thescience.databinding.ActivityPhysTabBinding;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.util.List;
import java.util.Random;

import static android.graphics.Color.TRANSPARENT;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class PhysTabActivity extends AppCompatActivity {

    private PhysValues values;

    private ExampleFormulaAdapter formulaAdapter;

    static int position;
    private int rotate;
    private boolean isRevealing;
    private PhysTab1 tab1;
    private PhysTab2 tab2;
    private FormulaHandler formulaHandler;
    static List<PhysFormulaData> visualData;
    private String[] input;

    private DisplayMetrics mDisplayMetrics;
    private ActivityPhysTabBinding mBinding;
    private float mStartX;
    private float mStartY;
    private int mBottomY;

    private boolean isAnimating;
    private float mBottomListStartY;
    private boolean resetBottomList;

    String[] models;
    Random r;
    private Integer[] colors = null;
    ArgbEvaluator evaluator = new ArgbEvaluator();
    Integer[] colors_pallet = new Integer[]{Color.parseColor("#FFFFCDD2"), Color.parseColor("#FFE1BEE7"), Color.parseColor("#FFC5CAE9"), Color.parseColor("#FFB3E5FC"), Color.parseColor("#FFB3E5FC"), Color.parseColor("#FFDCEDC8"), Color.parseColor("#FFF0F4C3"), Color.parseColor("#FFF8BBD0"), Color.parseColor("#FFB2DFDB"), Color.parseColor("#FFFFCCBC")};

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phys_tab);

        if(getResources().getConfiguration().locale.getLanguage().equals("ru")){
            values = new PhysValuesRU();
        } else {
            values = new PhysValuesEN();
        }

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_phys_tab);

        mDisplayMetrics = getResources().getDisplayMetrics();

        final Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mBinding.physTabToolbar.setTitle(mBundle.getString("title"));
            position = mBundle.getInt("position");

        }

        new Handler().post(() -> {

            tab1 = new PhysTab1();
            tab2 = new PhysTab2();

            SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), tab1, tab2);

            mBinding.container.setAdapter(mSectionsPagerAdapter);

            mBinding.tabs.setElevation(mBinding.physTabToolbar.getElevation());
            mBinding.tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if(tab.getPosition() == 0){
                        animAppBar0();
                    } else if(tab.getPosition() == 1){
                        animAppBar1();
                    }
                }
                @Override
                public void onTabUnselected(TabLayout.Tab tab) { }
                @Override
                public void onTabReselected(TabLayout.Tab tab) { }
            });
            mBinding.container.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mBinding.tabs));
            mBinding.tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mBinding.container));

            LinearLayout tabStrip = ((LinearLayout)mBinding.tabs.getChildAt(0));
            tabStrip.setEnabled(false);
            for(int i = 0; i < tabStrip.getChildCount(); i++) { tabStrip.getChildAt(i).setClickable(false); }

        });


        setSupportActionBar(mBinding.physTabToolbar);
        mBinding.physTabToolbar.setNavigationOnClickListener(v -> onBackPressed());
        mBinding.physTabToolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_more_vert_black_24dp));
        ((ViewGroup.MarginLayoutParams) mBinding.physTabToolbar.getLayoutParams()).setMargins(0, getStatusBarHeight(), 0, 0);

        mBinding.background.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.tb2) + getStatusBarHeight()));
        mBinding.reveal.setLayoutParams(mBinding.background.getLayoutParams());

        AppBarLayout appBarLayout = mBinding.appbar;
        appBarLayout.setElevation(mBinding.physTabToolbar.getElevation());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));

        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


        formulaHandler = new FormulaHandler(position, getResources().getConfiguration().locale.getLanguage());

        input = new String[values.getFieldsLength(position)];

        rotate = 0;isRevealing = false;isAnimating = false;

        mBinding.fab.setOnClickListener(view -> {
            if(mBinding.tabs.getSelectedTabPosition() == 0){

                for(int i=0;i<input.length;i++) { input[i] = tab1.mDataFieldList.get(i).getText();}

                visualData = formulaHandler.NEXT(input);

                if(visualData.size() == 0){
                    Snackbar snackbar = Snackbar.make(view, getResources().getString(R.string.phys_tab_not_enough_data), Snackbar.LENGTH_LONG).setAction("Action", null).setActionTextColor(Color.WHITE);
                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.itAccent));
                    snackbar.show();

                } else {
                    InputMethodManager imm = (InputMethodManager) mBinding.container.getContext().getSystemService(PhysSelectActivity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mBinding.container.getWindowToken(), 0);

                    if(visualData.size() == 1){
                        tab2.displayFormulas(visualData, 0);
                        changeTab();

                    } else {
                        registerForContextMenu(mBinding.fab);
                        openContextMenu(mBinding.fab);
                    }
                }

            } else { changeTab(); }

        });

        mBinding.fab.setOnLongClickListener(view -> {
            if(mBinding.tabs.getTabAt(0).isSelected()){
                for (DataFieldData i:tab1.mDataFieldList){
                    i.setText("");
                }

                tab1.setAdapter();

                Snackbar snackbar = Snackbar.make(view, getResources().getString(R.string.phys_tab_cleared), Snackbar.LENGTH_LONG).setAction("Action", null).setActionTextColor(Color.WHITE);
                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.itPrimary));
                snackbar.show();
                return true;
            } else {
                return true;
            }

        });

        mBinding.questionFab.setOnClickListener(this::animate);

        mBinding.bottomListBackground.setVisibility(INVISIBLE);
        mBinding.bottomListBackground.setOnClickListener(view -> {
            if (mBinding.bottomListBackground.getVisibility() == VISIBLE) acceptFilters(view);
        });

        new Handler().post(() -> {
            if(models == null) examplesSetup();
        });

        //Log.d("tag", "end OnCreate " + System.currentTimeMillis());//--------------------------------------------------------------

        if (savedInstanceState == null) {
            mBinding.container.setAlpha(0f);

            ObjectAnimator animator = ObjectAnimator.ofFloat(mBinding.container, "alpha", 0f, 1f);
            animator.setDuration(200);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) { }
                @Override
                public void onAnimationEnd(Animator animator) {
                    tab1.setupResyclerView();
                    new Handler().postDelayed(() -> {
                        if(PreferenceManager.getDefaultSharedPreferences(PhysTabActivity.this).getBoolean("phys_help", true)){
                            PreferenceManager.getDefaultSharedPreferences(PhysTabActivity.this).edit().putBoolean("phys_help", false).apply();
                            target1();
                        }
                    }, 500);


                }
                @Override
                public void onAnimationCancel(Animator animator) { }
                @Override
                public void onAnimationRepeat(Animator animator) { }
            });

            animator.start();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(PreferenceManager.getDefaultSharedPreferences(PhysTabActivity.this)
                .getBoolean("phys_help", false)){
            PreferenceManager.getDefaultSharedPreferences(PhysTabActivity.this).edit().putBoolean("phys_help", false).apply();
            //PreferenceManager.setDefaultValues(PhysTabActivity.this, "phys_help", );
            target1();
        }
    }

    private void target1(){
        TapTargetView.showFor(this,
                TapTarget.forView(mBinding.questionFab,
                        getResources().getString(R.string.phys_tab_target1_title),
                        getResources().getString(R.string.phys_tab_target1_description))
                        .outerCircleColor(R.color.physTabAccent)
                        .outerCircleAlpha(0.7f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(25)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(18)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.white)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(false)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .icon(getResources().getDrawable(R.drawable.question))
                        .targetRadius(60),
                new TapTargetView.Listener(){
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        view.dismiss(true);

                        target2();
                    }
                }
        );
    }

    private void target2(){
        TapTargetView.showFor(this,
                TapTarget.forView(mBinding.container,
                        getResources().getString(R.string.phys_tab_target2_title),
                        getResources().getString(R.string.phys_tab_target2_description))
                        .outerCircleColor(R.color.physTabAccent)
                        .outerCircleAlpha(0.7f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(25)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(18)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.white)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(false)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .icon(getResources().getDrawable(R.drawable.nothing))
                        .targetRadius(60),
                new TapTargetView.Listener(){
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        view.dismiss(true);

                        target3();
                    }
                }
        );
    }

    private void target3(){
        TapTargetView.showFor(this,
                TapTarget.forView(mBinding.fab,
                        getResources().getString(R.string.phys_tab_target3_title),
                        getResources().getString(R.string.phys_tab_target3_description))
                        .outerCircleColor(R.color.physTabAccent)
                        .outerCircleAlpha(0.7f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(25)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(18)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.white)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(false)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .icon(getResources().getDrawable(R.drawable.ic_arrow_forward_white_24dp))
                        .targetRadius(60),
                new TapTargetView.Listener(){
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        view.dismiss(true);

                        //target3();
                    }
                }
        );
    }

    private void examplesSetup() {

        ((GradientDrawable) mBinding.bottomListBackground.getBackground()).setCornerRadius(0f);


        setModels();

        formulaAdapter = new ExampleFormulaAdapter(models, this);

        mBinding.exampleViewPager.setAdapter(formulaAdapter);
        mBinding.exampleViewPager.setPadding(130, 0, 130, 0);

        r = new Random();
        colors = new Integer[models.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = colors_pallet[r.nextInt(colors_pallet.length)];
        }

        mBinding.exampleViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if (i < (formulaAdapter.getCount() - 1) && i < (colors.length - 1)) {
                    mBinding.exampleViewPager.setBackgroundColor((Integer) evaluator.evaluate(v, colors[i], colors[i + 1]));
                } else {
                    mBinding.exampleViewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        mBinding.indicator.setViewPager(mBinding.exampleViewPager);


    }

    private void changeTab(){
        if(!isRevealing){
            mBinding.tabs.getTabAt((mBinding.tabs.getTabAt(0).isSelected()?1:0)).select();
            ViewPropertyAnimator viewPropertyAnimator = mBinding.fab.animate();
            viewPropertyAnimator.rotation((mBinding.tabs.getTabAt(1).isSelected()?rotate + 180:rotate + 180)).setDuration(300);
            rotate += 180;
            viewPropertyAnimator.setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    isRevealing = true;
                    if(mBinding.tabs.getSelectedTabPosition() == 1) {
                        //tab2.displayFormulas(visualData, 0);
                        mBinding.questionFab.hide();
                    }
                }
                @Override
                public void onAnimationEnd(Animator animator) {
                    isRevealing = false;
                    if(mBinding.tabs.getSelectedTabPosition() == 0){
                        mBinding.questionFab.show();
                    }
                }
                @Override
                public void onAnimationCancel(Animator animator) {}
                @Override
                public void onAnimationRepeat(Animator animator) {}
            });

        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Выберите нужную формулу:");
        for(int i=0;i<visualData.size();i++){
            menu.add(Menu.NONE, i, Menu.NONE, visualData.get(i).getDescription());
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        tab2.displayFormulas(visualData, item.getItemId());
        changeTab();
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_phys_tab, menu);

        menu.findItem(R.id.action_settings_phys_tab).setOnMenuItemClickListener(menuItem -> {
            Intent intent = new Intent(PhysTabActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(mBinding.question.getVisibility() == VISIBLE){
            acceptFilters(mBinding.bottomListBackground);
        } else if(mBinding.tabs.getSelectedTabPosition() == 1) {
            mBinding.fab.performClick();
        } else {
            getWindow().setStatusBarColor(getResources().getColor(R.color.light_background));
            AppBarLayout appBarLayout = mBinding.appbar;
            appBarLayout.setElevation(0);

            mBinding.questionReveal.setVisibility(INVISIBLE);
            mBinding.list.setVisibility(INVISIBLE);
            mBinding.bottomListBackground.setVisibility(INVISIBLE);
            mBinding.bottomList.setVisibility(INVISIBLE);

            ObjectAnimator animator = ObjectAnimator.ofFloat(mBinding.container, "alpha", 1f, 0f);
            animator.setDuration(1000);
            animator.start();

            supportFinishAfterTransition();

        }

    }

    private void animAppBar0(){

        int width = Math.abs((mBinding.tabs.getRight() + mBinding.tabs.getLeft()) / 4);
        int height = mBinding.tabs.getBottom();
        Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(mBinding.reveal, width, height, 0, (float) Math.hypot(width * 3, height));
        createCircularReveal.setDuration(300);
        createCircularReveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

                mBinding.background.setVisibility(View.VISIBLE);
                mBinding.background.setBackgroundColor(getResources().getColor(R.color.physTab2DarkPrimary));
                mBinding.reveal.setBackgroundColor(getResources().getColor(R.color.physTab1Primary));

                mBinding.physTabToolbar.setTitleTextColor(getResources().getColor(R.color.primaryText));
                mBinding.tabs.setTabTextColors(getResources().getColor(R.color.primaryText), getResources().getColor(R.color.primaryText));

                final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
                getSupportActionBar().setHomeAsUpIndicator(upArrow);

                mBinding.physTabToolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_more_vert_black_24dp));

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                mBinding.background.setVisibility(View.INVISIBLE);



            }
        });

        createCircularReveal.start();
    }

    private void animAppBar1(){
        mBinding.fab.setElevation(mBinding.questionFab.getElevation());

        int width = Math.abs((mBinding.tabs.getRight() + mBinding.tabs.getLeft()) / 4 * 3);
        int height = mBinding.tabs.getBottom();
        Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(mBinding.reveal, width, height, 0, (float) Math.hypot(width, height));
        createCircularReveal.setDuration(300);
        createCircularReveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

                mBinding.reveal.setVisibility(View.VISIBLE);
                mBinding.reveal.setBackgroundColor(getResources().getColor(R.color.physTab2DarkPrimary));
                mBinding.background.setBackgroundColor(getResources().getColor(R.color.physTab1Primary));

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                mBinding.physTabToolbar.setTitleTextColor(getResources().getColor(R.color.white));
                mBinding.tabs.setTabTextColors(getResources().getColor(R.color.white), getResources().getColor(R.color.physTabAccent));

                final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
                upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                getSupportActionBar().setHomeAsUpIndicator(upArrow);

                mBinding.physTabToolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_more_vert_white_24dp));


            }
        });

        createCircularReveal.start();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        PhysTab1 tab1;
        PhysTab2 tab2;

        SectionsPagerAdapter(FragmentManager fm, PhysTab1 tab1, PhysTab2 tab2) {
            super(fm);
            this.tab1 = tab1;
            this.tab2 = tab2;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return tab1;
                case 1:
                    return tab2;
            }
            return null;

        }

        @Override
        public int getCount() {
            return 2;
        }

    }


    private void setModels(){
        List<PhysFormulaData> data = formulaHandler.NEXT();
        models = new String[data.size()];
        for(int i=0;i<models.length;i++){
            models[i] = data.get(models.length - 1 - i).getMainFormula();
        }
    }

    @SuppressLint("RestrictedApi")
    public void animate(View view) {
        mBinding.fab.setVisibility(INVISIBLE);
        mBinding.bottomListBackground.setVisibility(VISIBLE);

        if(!isAnimating){
            isAnimating = true;
            if (mStartX == 0.0f) {
                mStartX = view.getX();
                mStartY = view.getY();

                mBottomY = (int) (mDisplayMetrics.heightPixels - mDisplayMetrics.density * 60);//(int) (question.getY() + (mDisplayMetrics.heightPixels - getStatusBarHeight() - mDisplayMetrics.density * 64) - mDisplayMetrics.density * 4);

                mBottomListStartY = mBinding.bottomListBackground.getY();
            }

            final int x = getFinalXPosition();
            final int y = getFinalYPosition();

            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);

            animator.addUpdateListener(animation -> {
                float v = (float) animation.getAnimatedValue();
                mBinding.questionFab.setX(x + (mStartX - x - ((mStartX - x) * v)));
                mBinding.questionFab.setY(y + (mStartY - y - ((mStartY - y) * (v * v))));
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    mBinding.questionFab.setElevation(0f);
                    mBinding.questionFab.setBackgroundTintList(ColorStateList.valueOf(TRANSPARENT));

                    new Handler().postDelayed(() -> mBinding.questionFab.animate().y(mBottomY).setDuration(200).start(), 50);

                    if (resetBottomList) { resetBottomListBackground(); }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.bottomListBackground.animate().alpha(1f).setDuration(300).setListener(new AnimatorListenerAdapter() {
                                @SuppressLint("RestrictedApi")
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    mBinding.question.setVisibility(VISIBLE);
                                    mBinding.questionFab.setVisibility(View.INVISIBLE);
                                    mBinding.questionFab.setElevation(0f);

                                    isAnimating = false;
                                }
                            }).start();

                        }
                    }, 400);
                    new Handler().postDelayed(() -> mBinding.exampleViewPager.animate().alpha(1f).setDuration(300).start(), 400);

                    revealFilterSheet(y);
                }

                @Override
                public void onAnimationStart(Animator animation, boolean isReverse) {
                    mBinding.questionFab.setElevation(0f);
                }
            });


            animator.start();

        }

    }

    private int getStatusBarHeight() {
        return getResources().getDimensionPixelSize(getResources().getIdentifier("status_bar_height", "dimen", "android"));
    }

    private void resetBottomListBackground() {
        resetBottomList = false;
        mBinding.bottomListBackground.setVisibility(VISIBLE);
        Drawable d = mBinding.bottomListBackground.getBackground();
        final GradientDrawable gd = (GradientDrawable) d;
        mBinding.bottomListBackground.setAlpha(0f);
        gd.setCornerRadius(0f);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mBinding.bottomListBackground.getLayoutParams();
        params.width = -1;
        params.height = (int) (mDisplayMetrics.density * 64);
        mBinding.bottomListBackground.setY(mBottomListStartY + mDisplayMetrics.density * 8);
        mBinding.bottomListBackground.requestLayout();

    }

    private void revealFilterSheet(int y) {
        mBinding.questionReveal.setVisibility(VISIBLE);

        Animator a = ViewAnimationUtils.createCircularReveal(
                mBinding.questionReveal,
                mDisplayMetrics.widthPixels / 2,
                (int) (y - mBinding.questionReveal.getY()) + getFabSize() / 2,
                getFabSize() / 2f,
                mDisplayMetrics.widthPixels * 1.5f);//mBinding.questionReveal.getHeight() * .7f
        a.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBinding.list.setVisibility(VISIBLE);
            }
        });
        a.start();
    }

    public int getFinalXPosition() {
        return mDisplayMetrics.widthPixels / 2 - getFabSize() / 2;
    }

    public int getFinalYPosition() {
        return mDisplayMetrics.heightPixels - getFinalYPositionFromBottom() + getFabSize() / 2;
    }

    public int getFinalYPositionFromBottom() {
        return (int) (mDisplayMetrics.density * 230);
    }

    public int getFabSize() {
        return (int) (mDisplayMetrics.density * 56);
    }

    @SuppressLint("RestrictedApi")
    public void acceptFilters(View view) {
        if(!isAnimating){
            isAnimating = true;

            mBinding.questionFab.setVisibility(VISIBLE);
            //mBinding.questionFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(PhysTabActivity.this, R.color.example_accent)));
            mBinding.list.setVisibility(INVISIBLE);

            final int x = getFinalXPosition();
            final int y = getFinalYPosition();

            mBinding.question.setVisibility(INVISIBLE);

            final int startX = (int) mBinding.questionFab.getX();
            final int startY = (int) mBinding.questionFab.getY();

            Animator reveal = ViewAnimationUtils.createCircularReveal(mBinding.questionReveal,
                    mDisplayMetrics.widthPixels / 2,
                    (int) (y - mBinding.questionReveal.getY()) + getFabSize() / 2,//(int) ((int) (y - this.questionReveal.getY()) + getFabSize() / 2 - mDisplayMetrics.density * 22),
                    this.mBinding.questionReveal.getHeight() * .5f,
                    getFabSize() / 2f);

            animateBottomSheet();

            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
            animator.addUpdateListener(animation -> {
                float v = (float) animation.getAnimatedValue();
                mBinding.bottomListBackground.setAlpha(1 - ((float) animation.getAnimatedValue()));
                mBinding.questionFab.setX(x - (x - startX - ((x - startX) * v)));
                mBinding.questionFab.setY(y + (startY - y - ((startY - y) * (v * v))));

            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    new Handler().postDelayed(() -> {
                        returnFabToInitialPosition();
                        //mBinding.bottomListBackground.setVisibility(INVISIBLE);
                        //exampleViewPager.setVisibility(INVISIBLE);
                    }, 500);


                }
            });
            animator.start();

            reveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mBinding.questionReveal.setVisibility(INVISIBLE);
                    mBinding.questionFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(PhysTabActivity.this, R.color.colorAccent)));
                    mBinding.questionFab.setElevation(mDisplayMetrics.density * 1);

                }
            });

            reveal.start();

        }

    }

    private void animateBottomSheet() {
        Drawable d = mBinding.bottomListBackground.getBackground();
        final GradientDrawable gd = (GradientDrawable) d;

        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mBinding.bottomListBackground.getLayoutParams();

        final int startWidth = mBinding.bottomListBackground.getWidth();
        final int startHeight = mBinding.bottomListBackground.getHeight();
        final int startY = (int) mBinding.bottomListBackground.getY();

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.addUpdateListener(animation -> {
            float v = (float) animation.getAnimatedValue();
            gd.setCornerRadius(mDisplayMetrics.density * 50 * v);

            params.width = (int) (startWidth - (startWidth - getFabSize()) * v);
            params.height = (int) (startHeight - (startHeight - getFabSize()) * v);
            mBinding.bottomListBackground.setY(getFinalYPosition() + (startY - getFinalYPosition()) - ((startY - getFinalYPosition()) * v));

            mBinding.bottomListBackground.requestLayout();
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) { mBinding.exampleViewPager.setAlpha(0f);}
            @Override
            public void onAnimationEnd(Animator animator) { mBinding.bottomListBackground.setAlpha(0f); }

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        animator.start();
    }

    private void returnFabToInitialPosition() {
        final int x = getFinalXPosition();
        final int y = getFinalYPosition();
        resetBottomList = true;

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.addUpdateListener(animation -> {
            float v = (float) animation.getAnimatedValue();
            mBinding.questionFab.setX(x + ((mStartX - x) * v));
            mBinding.questionFab.setY((float) (y + (mStartY - y) * (Math.pow(v, .5f))));
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @SuppressLint("RestrictedApi")
            @Override
            public void onAnimationEnd(Animator animator) {
                mBinding.fab.setVisibility(VISIBLE);

                mBinding.bottomListBackground.setVisibility(INVISIBLE);
                isAnimating = false;
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        animator.start();
    }



}

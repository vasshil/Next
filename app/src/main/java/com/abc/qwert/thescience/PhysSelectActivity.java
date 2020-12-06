package com.abc.qwert.thescience;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PhysSelectActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private PhysValues values;

    private View transitionContents;
    private MenuItem search;
    private Toolbar toolbar;
    RecyclerView recyclerView;
    List<FormulaCardData> formulaCardDataList;
    RVAdapter rvAdapter;
    private AppBarLayout appBarLayout;
    private boolean fullLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phys_select_layout);

        if(getResources().getConfiguration().locale.getLanguage().equals("ru")){
            values = new PhysValuesRU();
        } else {
            values = new PhysValuesEN();
        }

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        appBarLayout = findViewById(R.id.phys_app_bar);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_more_vert_white_24dp));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);


        formulaCardDataList = getIntent().getParcelableArrayListExtra("formulaCardDataList");
        if(formulaCardDataList == null){
            formulaCardDataList = new ArrayList<>(values.getLength());
            for(int i=0;i<values.getLength();i++) {
                formulaCardDataList.add(new FormulaCardData(values.getName(i), values.getGroup(i), values.getImage(i), i));
            }
        }
        setupRecyclerView();
        /*new Thread(() -> {


            setupRecyclerView();

        }).start();*/
        /*for(int i=0;i<PhysValues.getLength();i++) {
            formulaCardDataList.add(new FormulaCardData(PhysValues.getName(i), PhysValues.getGroup(i), PhysValues.getImage(i), i));//, i
        }*/

        recyclerView.setPadding(0, (int) getResources().getDimension(R.dimen.dp7), 0, (int) getResources().getDimension(R.dimen.dp7) + (int) getResources().getDimension(R.dimen.adHeight));
        recyclerView.setItemViewCacheSize(formulaCardDataList.size());
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        transitionContents = findViewById(R.id.phys_ll);


        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("B6130A2B6EDEB71E7270A790AD097F01")
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);


        if (savedInstanceState == null) {
            transitionContents.setAlpha(0f);

            ObjectAnimator animator = ObjectAnimator.ofFloat(transitionContents, "alpha", 0f, 1f);
            animator.setDuration(1000);
            animator.start();
        }

    }

    @Override
    protected void onResume() {
        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("fullLength", false) != fullLength) setupRecyclerView();
        super.onResume();
    }

    private void setupRecyclerView(){
        setPreferences();
        if(fullLength){
            StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(getRotateOrientation(), StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(PhysSelectActivity.this, getRotateOrientation());
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        rvAdapter = new RVAdapter(PhysSelectActivity.this, formulaCardDataList, getString(R.string.phys_tab_key), findViewById(R.id.searchNoResults), fullLength) {
            @NonNull
            @Override
            public FormulaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return super.onCreateViewHolder(parent, viewType);
            }
        };
        recyclerView.setAdapter(rvAdapter);
    }

    private void setPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        fullLength = sharedPreferences.getBoolean("fullLength", false);


    }

    @Override
    public void onBackPressed(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(transitionContents, "alpha", 1f, 0f);
        animator.setDuration(1000);
        animator.start();

        supportFinishAfterTransition();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_phys_select, menu);

        MenuItem settings = menu.findItem(R.id.action_settings_phys);
        settings.setOnMenuItemClickListener(menuItem -> {
            Intent intent = new Intent(PhysSelectActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        });

        search = menu.findItem(R.id.action_search);
        search.setOnMenuItemClickListener(menuItem -> {
            appBarLayout.setExpanded(false, true);
            settings.setVisible(false);
            return true;
        });

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(search, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                animateSearchToolbar(1, true, true);
                settings.setVisible(true);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                if (search.isActionViewExpanded()) {
                    animateSearchToolbar(2, false, false);

                }
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query){
        rvAdapter.filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        rvAdapter.filter(newText);
        return true;
    }

    private int getRotateOrientation() {
        int rotate = getWindowManager().getDefaultDisplay().getRotation();

        switch (rotate) {
            case Surface.ROTATION_0:
                return 2;
            case Surface.ROTATION_90:
                return 3;
            case Surface.ROTATION_180:
                return 2;
            case Surface.ROTATION_270:
                return 3;
            default:
                return 2;
        }
    }

    public void animateSearchToolbar(int numberOfMenuIcon, boolean containsOverflow, boolean show) {

        toolbar.setElevation(0);
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));//ContextCompat.getColor(PhysSelectActivity.this, android.R.color.white)

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);

        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.quantum_grey_600));

        if (show) {
            int width = toolbar.getWidth() - (containsOverflow ? getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) : 0) - ((getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2);
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(toolbar, isRtl(getResources()) ? toolbar.getWidth() - width : width, toolbar.getHeight() / 2, 0.0f, (float) width);
            createCircularReveal.setDuration(250);
            createCircularReveal.start();

        } else {
            toolbar.setElevation(0);
            int width = toolbar.getWidth() - (containsOverflow ? getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) : 0) - ((getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2);
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(toolbar, isRtl(getResources()) ? toolbar.getWidth() - width : width, toolbar.getHeight() / 2, (float) width, 0.0f);
            createCircularReveal.setDuration(250);
            createCircularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    toolbar.setBackgroundColor(getThemeColor(PhysSelectActivity.this, R.color.physPrimary));//getResources().getColor(R.color.physPrimary)//getThemeColor(PhysSelectActivity.this, R.attr.colorPrimary)
                    collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.physDarkPrimary));                        //mDrawerLayout.setStatusBarBackgroundColor(getThemeColor(MainActivity.this, R.attr.colorPrimaryDark));
                }
            });
            createCircularReveal.start();

            collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.physDarkPrimary));            //mDrawerLayout.setStatusBarBackgroundColor(getThemeColor(MainActivity.this, R.attr.colorPrimaryDark));
        }

    }

    private boolean isRtl(Resources resources) {
        return resources.getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    private static int getThemeColor(Context context, int id) {
        Resources.Theme theme = context.getTheme();
        TypedArray a = theme.obtainStyledAttributes(new int[]{id});
        int result = a.getColor(0, 0);
        a.recycle();
        return result;
    }

}

package com.abc.qwert.thescience;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class ITActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private View transitionContents;
    private ImageView done;
    private EditText[] fields;
    private int source = 0;
    private CoordinatorLayout gl;
    private Button clrButt;
    private boolean anim;

    private Menu menu;

    TextWatcher binTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable) {
            source = 0;
        }
    };
    TextWatcher octTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable) {
            source = 1;
        }
    };
    TextWatcher decTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable) {
            source = 2;
        }
    };
    TextWatcher hexTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable) {
            source = 3;
        }
    };
    TextWatcher strTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable) {
            source = 4;
        }
    };
    TextWatcher[] watcherFields = {binTextWatcher, octTextWatcher, decTextWatcher, hexTextWatcher, strTextWatcher};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.it_son_layout);

        setPreferences();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        gl = findViewById(R.id.it_layout);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_more_vert_white_24dp));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        final int[] fieldsInt = {R.id.bin, R.id.oct, R.id.dec, R.id.hex, R.id.str};
        fields = new EditText[5];
        for(int i=0;i<5;i++){
            fields[i] = findViewById(fieldsInt[i]);
            fields[i].addTextChangedListener(watcherFields[i]);
            fields[i].setOnEditorActionListener((textView, i1, keyEvent) -> {
                done.callOnClick();
                return true;
            });
        }

        final int[] imgInt = {R.id.binView, R.id.octView, R.id.decView, R.id.hexView, R.id.strView};
        final int[] img = {R.drawable.bin_son, R.drawable.oct_son, R.drawable.dec_son, R.drawable.hex_son, R.drawable.str_son};
        final ImageView[] imgViews = new ImageView[5];
        for(int i=0;i<5;i++){
            imgViews[i] = findViewById(imgInt[i]);
            final int j = i;

            final Drawable image = getResources().getDrawable(img[i]);
            image.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            imgViews[i].setImageDrawable(image);
            imgViews[i].setOnClickListener(view -> {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", fields[j].getText().toString());
                clipboard.setPrimaryClip(clip);
                showToast();
            });

        }

        clrButt = findViewById(R.id.clr_btn);
        clrButt.setOnClickListener(view -> animClr());
        //clrButt.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.sharp_clear_white_48dp), null, null, null);

        NestedScrollView nsv = findViewById(R.id.it_nsv);
        nsv.setPadding(0,0, 0, getResources().getDimensionPixelSize(R.dimen.clr_btn_dp));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("B6130A2B6EDEB71E7270A790AD097F01")
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);



        transitionContents = nsv;

        if (savedInstanceState == null) {
            transitionContents.setAlpha(0f);

            ObjectAnimator animator = ObjectAnimator.ofFloat(transitionContents, "alpha", 0f, 1f);

            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) { }
                @Override
                public void onAnimationEnd(Animator animator) {
                    new Handler().postDelayed(() -> {
                        if(PreferenceManager.getDefaultSharedPreferences(ITActivity.this).getBoolean("it_help", true)){
                            PreferenceManager.getDefaultSharedPreferences(ITActivity.this).edit().putBoolean("it_help", false).apply();
                            target1();
                        }
                    }, 50);


                }
                @Override
                public void onAnimationCancel(Animator animator) { }
                @Override
                public void onAnimationRepeat(Animator animator) { }
            });

            animator.setDuration(1000);
            animator.start();
        }

    }

    @Override
    public void onBackPressed() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(transitionContents, "alpha", 1f, 0f);
        animator.setDuration(1000);
        animator.start();

        supportFinishAfterTransition();
    }

    private void showToast() {
        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.it_copy_to_clipboard), Toast.LENGTH_SHORT);
        toast.show();

    }

    private void apply(){
        String[] a = new String[5];
        Bin BIN; Oct OCT; Dec DEC; Hex HEX; Str STR;

        if(fields[source].getText().toString().equals("")) {
            return;
        } else {
            if(source == 0) {
                BIN = new Bin(fields[source].getText().toString());
                a = BIN.a;
            } else if(source == 1){
                OCT = new Oct(fields[source].getText().toString());
                a = OCT.a;
            } else if(source == 2) {
                DEC = new Dec(fields[source].getText().toString());
                a = DEC.a;
            } else if(source == 3) {
                HEX = new Hex(fields[source].getText().toString());
                a = HEX.a;
            } else if(source == 4) {
                STR = new Str(fields[source].getText().toString());
                a = STR.a;
            }

        }

        for(int i=0;i<5;i++) {
            fields[i].setText("");
            fields[i].setText(a[i]);
        }

    }

    private void clear(){
        for(int i=0;i<5;i++) {
            fields[i].setText("");

        }

    }

    private void animClr(){
        if(!anim){
            int width = Math.abs(clrButt.getLeft() + clrButt.getRight()) / 2;
            int height = Math.abs(clrButt.getBottom() + clrButt.getTop()) / 2;
            Animator createCircularReveal;
            final View blue = findViewById(R.id.blue);
            createCircularReveal = ViewAnimationUtils.createCircularReveal(blue, width, height, 0, (float)Math.hypot((float)gl.getWidth(), (float)gl.getHeight()));
            createCircularReveal.setDuration(400);
            createCircularReveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    gl.setElevation(0);
                    blue.setVisibility(View.VISIBLE);
                }
                @Override
                public void onAnimationEnd(Animator animator) {
                    gl.setElevation(0);
                    blue.setVisibility(View.INVISIBLE);
                }
                @Override
                public void onAnimationCancel(Animator animator) {}
                @Override
                public void onAnimationRepeat(Animator animator) {}
            });
            createCircularReveal.start();

        }

        clear();
    }

    private void setPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        anim = sharedPreferences.getBoolean("anim_it", false);


    }

    @SuppressLint("InflateParams")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_it, menu);

        this.menu = menu;

        menu.findItem(R.id.action_settings_it).setOnMenuItemClickListener(menuItem -> {
            Intent intent = new Intent(ITActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        });

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        done = (ImageView)inflater.inflate(R.layout.done_image_icon, null);

        done.setOnClickListener(v -> {
            if(!anim){
                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.android_rotate_animation);
                rotation.setDuration(400);

                final OvershootInterpolator interpolator = new OvershootInterpolator();
                rotation.setInterpolator(interpolator);

                done.startAnimation(rotation);
            }

            InputMethodManager imm = (InputMethodManager) findViewById(R.id.gridLayout).getContext().getSystemService(ITActivity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(findViewById(R.id.gridLayout).getWindowToken(), 0);

            apply();
        });

        final MenuItem doneItem = menu.findItem(R.id.action_apply);
        doneItem.setActionView(done);

        return true;
    }

    @Override
    protected void onResume() {
        if(PreferenceManager.getDefaultSharedPreferences(ITActivity.this).getBoolean("it_help", false)){
            PreferenceManager.getDefaultSharedPreferences(ITActivity.this).edit().putBoolean("it_help", false).apply();
            target1();
        }
        setPreferences();
        super.onResume();
    }



    private void target1(){
        TapTargetView.showFor(this,
                TapTarget.forView(findViewById(R.id.bin),
                        getResources().getString(R.string.it_target1_title),
                        getResources().getString(R.string.it_target1_description))
                        .outerCircleColor(R.color.itAccent)
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
                        target2();
                    }
                });
    }

    private void target2() {
        TapTargetView.showFor(this,
                TapTarget.forView(findViewById(R.id.binView),
                        getResources().getString(R.string.it_target2_title),
                        getResources().getString(R.string.it_target2_description))
                        .outerCircleColor(R.color.itAccent)
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
                        //target3();


                    }
                }
        );
    }
/*

    private void target3() {
        toolbar.inflateMenu(R.menu.menu_it);
        TapTargetView.showFor(this,
                TapTarget.forToolbarMenuItem(toolbar,
                        R.id.action_apply,
                        "Применить",
                        "Нажмите, чтобы перевести ваше последнее введенное число в остальные системы счисления")
                        .outerCircleColor(R.color.itAccent)
                        .outerCircleAlpha(0.9f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(20)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(10)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.white)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(true)
                        .transparentTarget(false)
                        .icon(getResources().getDrawable(R.drawable.ic_check_white_24dp))
                        .targetRadius(60),
                new TapTargetView.Listener(){
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);

                    }
                }
        );
    }
*/


}

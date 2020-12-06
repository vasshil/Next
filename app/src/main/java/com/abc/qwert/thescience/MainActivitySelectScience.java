package com.abc.qwert.thescience;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.ActivityOptionsCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivitySelectScience extends AppCompatActivity {

    private ArrayList<FormulaCardData> formulaCardDataList;

    private CardView physCard;
    private CardView calcCard;
    private CardView paramCard;
    private CardView chemCard;
    private CardView itCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_select_science);


        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_more_vert_white_24dp));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        physCard = findViewById(R.id.phys_card);
        physCard.setOnClickListener((l) -> onClickPhys());

        calcCard = findViewById(R.id.calc_card);
        calcCard.setOnClickListener((l) -> onClickCalc());

        paramCard = findViewById(R.id.parameter_card);
        paramCard.setOnClickListener((l) -> onClickParam());

        chemCard = findViewById(R.id.chem_card);
        chemCard.setOnClickListener((l) -> onClickChem());

        itCard = findViewById(R.id.it_card);
        itCard.setOnClickListener((l) -> onClickIT());


        PhysValues values;
        if(getResources().getConfiguration().locale.getLanguage().equals("ru")){
            values = new PhysValuesRU();
        } else {
            values = new PhysValuesEN();
        }

        formulaCardDataList = new ArrayList<>();
        for(int i=0;i<values.getLength();i++) {
            formulaCardDataList.add(new FormulaCardData(values.getName(i), values.getGroup(i), values.getImage(i), i));
        }


        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("B6130A2B6EDEB71E7270A790AD097F01")
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);


    }

    public void onClickPhys(){
        Intent intent = new Intent(MainActivitySelectScience.this, PhysSelectActivity.class);
        intent.putParcelableArrayListExtra("formulaCardDataList", formulaCardDataList);
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivitySelectScience.this, physCard, "phys_key").toBundle();
        startActivity(intent, bundle);

    }

    public void onClickCalc(){
        Intent intent = new Intent(MainActivitySelectScience.this, FunctionCalculatorActivity.class);
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivitySelectScience.this, calcCard, "calc_key").toBundle();//physCard, getString(R.string.phys_key)
        startActivity(intent, bundle);

    }

    public void onClickParam(){
        Intent intent = new Intent(MainActivitySelectScience.this, ParameterActivity.class);
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivitySelectScience.this, paramCard, "parameter_key").toBundle();//physCard, getString(R.string.phys_key)
        startActivity(intent, bundle);

    }

    public void onClickChem(){
        Intent intent = new Intent(MainActivitySelectScience.this, ChemSelectActivity.class);
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivitySelectScience.this, chemCard, "chem_key").toBundle();
        startActivity(intent, bundle);

    }

    public void onClickIT(){
        Intent intent = new Intent(MainActivitySelectScience.this, ITActivity.class);
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivitySelectScience.this, itCard, "it_key").toBundle();
        startActivity(intent, bundle);

    }


    @Override
    protected void onResume() {
        super.onResume();
        //progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_bar, menu);
        menu.getItem(0).setOnMenuItemClickListener(menuItem -> {
            Intent intent = new Intent(MainActivitySelectScience.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        });
        return true;
    }


}

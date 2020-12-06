package com.abc.qwert.thescience;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import io.github.kexanie.library.MathView;

public class PhysTab2 extends Fragment {

    private PhysValues values;

    private LinearLayout cardsContainer;

    private List<PhysFormulaData> visualData;
    private String mainFormulaText;
    private String secondFormulaText;
    private String answerText;
    private String descriptionText;
    private int i;

    private MathView mainFormula;
    private MathView secondFormula;
    private MathView answer;

    private TextView description;
    private TextView answerMeasure;

    private List<PhysFormulaData> testList;
    private int I;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.phys_tab_2, container, false);
        cardsContainer = view.findViewById(R.id.cardsContainer);

        if(getResources().getConfiguration().locale.getLanguage().equals("ru")){
            values = new PhysValuesRU();
        } else {
            values = new PhysValuesEN();
        }

        if (mainFormula == null) {
            mainFormula = view.findViewById(R.id.mainFormula);
            secondFormula = view.findViewById(R.id.secondFormula);
            answer = view.findViewById(R.id.answer);

            mainFormula.setOnTouchListener((l, motionEvent) -> false);
            secondFormula.setOnTouchListener((l, motionEvent) -> false);
            answer.setOnTouchListener((l, motionEvent) -> false);

            description = view.findViewById(R.id.textDescription);
            answerMeasure = view.findViewById(R.id.textViewAnswer);

            CardView answerCard = view.findViewById(R.id.answerCard);
            answerCard.setOnClickListener(view1 -> {
                String copy = "";
                for(int j=0;j<answerText.length();j++){
                    if(answerText.charAt(j) == '\\'){
                        break;
                    } else {
                        copy += answerText.charAt(j);
                    }
                }
                if(answerText.charAt(answerText.length() - 1) == '%'){
                    copy += "%";
                }
                ClipboardManager clipboard = (ClipboardManager) Objects.requireNonNull(this.getContext()).getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", copy);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this.getContext(), getResources().getString(R.string.it_copy_to_clipboard), Toast.LENGTH_SHORT).show();
            });
        }


        return view;
    }

    void displayFormulas(List<PhysFormulaData> visualData, int i) {
        this.visualData = visualData;
        this.i = i;

        mainFormulaText = visualData.get(i).getMainFormula();
        secondFormulaText = visualData.get(i).getSecondFormula();
        answerText = visualData.get(i).getAnswer();
        descriptionText = visualData.get(i).getDescription();



        if (testList == null) {
            set();
        } else {
            if (!visualData.get(i).getSecondFormula().equals(testList.get(I).getSecondFormula())) {
                set();
            }
        }


    }

    private void set()  {
        testList = visualData;
        I = i;

        cardsContainer.setAlpha(0f);

        mainFormula.setText("$$\n$$" + "$$" + mainFormulaText + "$$" + "$$\n$$");
        secondFormula.setText("$$\n$$" + "$$" + secondFormulaText + "$$" + "$$\n$$");
        answer.setText("$$\n$$" + "$$" + answerText + "$$" + "$$\n$$");

        description.setText((descriptionText + " " + getResources().getString(R.string.main_card_description)));

        if(answerMeasure.getText().toString().equals(getResources().getString(R.string.answer_textview))){

            answerMeasure.setText(
                    answerMeasure.getText().toString().
                            concat("    (").
                            concat(values.getAnswerMeasure(PhysTabActivity.position)).
                            concat(")"));

        }

        ObjectAnimator anim = ObjectAnimator.ofFloat(cardsContainer, "alpha", 0f, 1f);
        anim.setDuration(1000);
        new Handler().postDelayed(anim::start, 300);
    }

}

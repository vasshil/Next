package com.abc.qwert.thescience;

import android.annotation.SuppressLint;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class FormulaHandler {

    private PhysValues values;

    private String[] input;
    private static Method method;
    private List<PhysFormulaData> visualData;
    //private int value;
    private String methodName;

    private static String nl = "=$$\n$$=";

    private String[] exampleInput = {"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"};

    FormulaHandler(int value, String language){
        //this.value = value;
        if(language.equals("ru")){
            values = new PhysValuesRU();
        } else {
            values = new PhysValuesEN();
        }
        methodName = values.getMethodName(value);

    }

    List<PhysFormulaData> NEXT(String[] input) {
        try {
            method = this.getClass().getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        visualData = new ArrayList<>();
        this.input = input;

        try {
            method.invoke(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return visualData;

    }

    List<PhysFormulaData> NEXT(){
        return NEXT(exampleInput);
    }

    @SuppressLint("DefaultLocale")
    private String answer(double in){
        if(in == Double.POSITIVE_INFINITY || in == Double.NEGATIVE_INFINITY || Double.isNaN(in)) return "1";

        if(in % 1 == 0) {
            return String.format("%.0f", in);
        } else {
            DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
            df.setMaximumFractionDigits(340); //340 = DecimalFormat.DOUBLE_FRACTION_DIGITS
            in = Double.parseDouble((df.format(in)).trim());


            if (Math.abs(in) < 2) {
                if(String.valueOf(in).length() > 7) {
                    return String.format("%.5f", in);
                } else {
                    return String.valueOf(in);
                }
            } else {
                if(String.valueOf(in).length() > 7){
                    return String.format("%.5f", in) + "\\approx" + Math.round(in);
                } else {
                    return in + "\\approx" + Math.round(in);
                }

            }
        }
    }

    @SuppressLint("DefaultLocale")
    private String answer(double in, int pow){
        if(in == Double.POSITIVE_INFINITY || in == Double.NEGATIVE_INFINITY || Double.isNaN(in)) return "1";

        if(in % 1 == 0) {
            return String.format("%.0f", in) + "*10^{" + pow + "}";
        } else {
            DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
            df.setMaximumFractionDigits(340); //340 = DecimalFormat.DOUBLE_FRACTION_DIGITS
            in = Double.parseDouble((df.format(in)).trim());

            if (Math.abs(in) < 2) {
                if(String.valueOf(in).length() > 7) {
                    return String.format("%.5f", in) + "*10^{" + pow + "}";
                } else {
                    return in + "*10^{" + pow + "}";
                }
            } else {
                if(String.valueOf(in).length() > 7){
                    return String.format("%.5f", in) + "*10^{" + pow + "}" + "\\approx" + Math.round(in) + "*10^{" + pow + "}";
                } else {
                    return in + "*10^{" + pow + "}" + "\\approx" + Math.round(in) + "*10^{" + pow + "}";
                }

            }
        }
    }

    private boolean notNull(int[] positions){
        for(int i:positions){
            if(input[i].equals("")) return false;
        }
        return true;
    }

    private double dbl(String s){
        return Double.parseDouble(s);
    }

    private double log(double n, int p){
        return Math.log(n) / Math.log(p);
    }


    private void calcA() {
        if(notNull(new int[]{11, 26})){
            double answer = dbl(input[11]) * dbl(input[26]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(0, 0),
                    "P=\\frac{A}{t}",
                    "A=Pt=" + input[26] + "*" + input[11],
                    answer(answer) ));
        }
        if(notNull(new int[]{11, 24, 25})){
            double answer = dbl(input[24]) * dbl(input[24]) * dbl(input[11]) / dbl(input[25]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(0, 1),
                    "A=\\frac{U^2}{R}t",
                    "A=\\frac{" + input[24] + "^2}{" + input[25] + "}*" + input[11],
                    answer(answer) ));
        }
        if(notNull(new int[]{11, 23, 25})){
            double answer = dbl(input[23]) * dbl(input[23]) * dbl(input[25]) * dbl(input[11]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(0, 2),
                    "A=I^2Rt",
                    "A=" + input[23] + "^2*" + input[25] + "*" + input[11],
                    answer(answer) ));
        }
        if(notNull(new int[]{11, 23, 24})){
            double answer = dbl(input[11]) * dbl(input[23]) * dbl(input[24]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(0, 3),
                    "A=IUt",
                    "A=" + input[23] + "*" + input[24] + "*" + input[11],
                    answer(answer) ));
        }
        if(notNull(new int[]{16, 20})){
            double answer = dbl(input[16]) * dbl(input[20]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(0, 4),
                    "\\eta=\\frac{A'}{Q}",
                    "A'=Q\\eta =" + input[20] + "*" + input[16],
                    answer(answer) ));
        }
        if(notNull(new int[]{20, 21, 22})){
            double answer = dbl(input[20]) + dbl(input[21]) - dbl(input[22]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(0, 5),
                    "Q=\\Delta U+A'",//=(U_2-U_1)+A'
                    "A'=Q-\\Delta U=Q-(U_2-U_1)" + nl + input[20] + "-(" + input[22] + "-" + input[21] + ")",
                    answer(answer) ));
        }
        if(notNull(new int[]{17, 18, 19})){
            double answer = dbl(input[17]) * (dbl(input[19]) - dbl(input[18]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(0, 6),
                    "A'=p\\Delta V",
                    "A=p(V_2-V_1)" + nl + input[17] + "*(" + input[19] + "-" + input[18] + ")",
                    answer(answer) ));
        }
        if(notNull(new int[]{4, 12, 13})){
            double answer = (dbl(input[4]) * dbl(input[13]) * dbl(input[13]) / 2) - (dbl(input[4]) * dbl(input[12]) * dbl(input[12]) / 2);
            visualData.add(new PhysFormulaData(values.getCalcDescription(0, 7),
                    "A=\\frac{mv^2}{2}-\\frac{mv_0^2}{2}",
                    "A=\\frac{" + input[4] + "*" + input[13] + "^2}{2}-\\frac{" + input[4] + "*" + input[12] + "^2}{2}",
                    answer(answer) ));
        }
        if(notNull(new int[]{7, 8, 9})){
            double answer = dbl(input[7]) / 2 * ((dbl(input[8]) * dbl(input[8])) - (dbl(input[9]) * dbl(input[9])));
            visualData.add(new PhysFormulaData(values.getCalcDescription(0, 8),
                    "A=\\frac{k}{2}(x_1^2 -x_2^2)",
                    "A=\\frac{" + input[7] + "}{2}(" + input[8] + "^2-" + input[9] + "^2)",
                    answer(answer) ));
        }
        if(notNull(new int[]{4, 5, 6})){
            double answer = dbl(input[4]) * Consts.g * (dbl(input[5]) - dbl(input[6]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(0, 9),
                    "A=mg(h_1-h_2)",
                    "A=" + input[4] + "*" + Consts.g + "*(" + input[5] + "-" + input[6] + ")",
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 3})){
            double answer = dbl(input[3]) * dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(0, 10),
                    "A=Fs",
                    "A=" + input[3] + "*" + input[1],
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1, 2})){
            double answer = dbl(input[0]) * dbl(input[1]) * Math.cos(dbl(input[2]) * Math.PI / 180);
            visualData.add(new PhysFormulaData(values.getCalcDescription(0, 11),
                    "A=Fs\\cos{\\alpha}",
                    "A=" + input[0] + "*" + input[1] + "*\\cos{" + input[2] + "^{\\circ}}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[0]) * dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(0, 12),
                    "A=Fs",
                    "A=" + input[0] + "*" + input[1],
                    answer(answer) ));
        }


    }

    private void calca() {
        if(notNull(new int[]{9, 10})){
            double answer = dbl(input[9]) / dbl(input[10]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(1, 0),
                    "\\vec{a} = \\frac{\\vec{F_R}}{m}",
                    "a=\\frac{" + input[9] + "}{" + input[10] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{7, 8})){
            double answer = dbl(input[8]) * dbl(input[8]) * dbl(input[7]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(1, 1),
                    "a=\\omega^2 r",
                    "a=" + input[8] + "^2*" + input[7],
                    answer(answer) ));
        }
        if(notNull(new int[]{6, 7})){
            double answer = dbl(input[6]) * dbl(input[6]) / dbl(input[7]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(1, 2),
                    "a=\\frac{v^2}{r}",
                    "a=\\frac{" + input[6] + "^2}{" + input[7] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 2, 4, 5})){
            double answer = 2 * (dbl(input[5]) - dbl(input[4]) - (dbl(input[1]) * dbl(input[2]))) / (dbl(input[2]) * dbl(input[2]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(1, 3),
                    "x=x_0 +v_0 t+\\frac{at^2}{2}",
                    "a=\\frac{2(x-x_0 -v_0 t)}{t^2}" + nl + "\\frac{2*(" + input[5] + "-" + input[4] + "-" + input[1] + "*" + input[2] + ")}{" + input[2] + "^2}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1, 3})){
            double answer = ((dbl(input[0]) * dbl(input[0])) - (dbl(input[1]) * dbl(input[1]))) / (2 * dbl(input[3]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(1, 4),
                    "S_x=\\frac{v_x^2 -v_{0x}^2}{2a_x}",
                    "a=\\frac{v^2-v_0^2}{2S}" + nl + "\\frac{" + input[0] + "^2-" + input[1] + "^2}{2*" + input[3] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 2, 3})){
            double answer = 2 * (dbl(input[3]) - (dbl(input[1]) * dbl(input[2]))) / (dbl(input[2]) * dbl(input[2]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(1, 5),
                    "s=v_0 t+\\frac{at^2}{2}",
                    "a=\\frac{2(s-v_0 t)}{t^2}" + nl + "\\frac{2*(" + input[3] + "-" + input[1] + "*" + input[2] + ")}{" + input[2] + "^2}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1, 2})){
            double answer = (dbl(input[0]) - dbl(input[1])) / dbl(input[2]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(1, 6),
                    "\\vec{a}=\\frac{\\vec{v}-\\vec{v_0}}{t}",
                    "a=\\frac{" + input[0] + "-" + input[1] + "}{" + input[2] + "}",
                    answer(answer) ));
        }

    }

    private void calcB(){
        if(notNull(new int[]{0, 1, 2})){
            double answer = dbl(input[0]) / dbl(input[1]) / dbl(input[2]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(43, 0),
                    "B=\\frac{F}{I*l}",
                    "B=\\frac{" + input[0]  + "}{" + input[1] + "*" + input[2] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{3, 4, 5})){
            double answer = dbl(input[3]) / dbl(input[4]) / Math.cos(dbl(input[5]) / 180 * Math.PI);
            visualData.add(new PhysFormulaData(values.getCalcDescription(43, 0),
                    "\\phi =B*S*\\cos(\\alpha)",
                    "B=\\frac{\\phi}{S*\\cos(\\alpha)}" + nl + "\\frac{" + input[3] + "}{" + input[4] + "*\\cos(" + input[5] + " ^{\\circ})}",
                    answer(answer) ));
        }

    }

    private void calcc() {
        if(notNull(new int[]{1, 4})){
            double answer = dbl(input[4]) / dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(2, 0),
                    "C=cm",
                    "c=\\frac{C}{m}=\\frac{" + input[4] + "}{" + input[1] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1, 2, 3})){
            double answer = dbl(input[0]) / (dbl(input[1]) * (dbl(input[3]) - dbl(input[2])));
            visualData.add(new PhysFormulaData(values.getCalcDescription(2, 1),
                    "Q=cm(t_2-t_1)",
                    "c=\\frac{Q}{m(t_2-t_1)}" + nl + "\\frac{" + input[0] + "}{" + input[1] + "*(" + input[3] + "-" + input[2] + ")}",
                    answer(answer) ));
        }

    }

    private void calcC() {
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[0]) * dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(3, 0),
                    "C=cm",
                    "C=" + input[0] + "*" + input[1],
                    answer(answer) ));
        }
    }

    private void calcCC(){
        if(notNull(new int[]{2, 6})){
            double answer = 4 * Math.PI * dbl(input[2]) * Consts.epsilon0 * dbl(input[6]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(4, 0),
                    "C=4\\pi\\varepsilon\\varepsilon_0 r",
                    "C=4*" + Consts.pi + "*" + input[2] + "*" + Consts.epsilon0 + "*" + input[6],
                    answer(answer, Consts.epsilon0pow) ));
        }
        if(notNull(new int[]{0, 5})){
            double answer = dbl(input[0]) / dbl(input[5]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(4, 1),
                    "C=\\frac{q}{\\varphi}",
                    "C=\\frac{" + input[0] + "}{" + input[5] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{2, 3, 4})){
            double answer = dbl(input[2]) * Consts.epsilon0 * dbl(input[3]) / dbl(input[4]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(4, 2),
                    "C=\\frac{\\varepsilon\\varepsilon_0 S}{d}",
                    "C=\\frac{" + input[2] + "*" + Consts.epsilon0 + "*" + input[3] + "}{" + input[4] + "}",
                    answer(answer, Consts.epsilon0pow) ));
        }
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[0]) / dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(4, 3),
                    "C=\\frac{q}{U}",
                    "C=\\frac{" + input[0] + "}{" + input[1] + "}",
                    answer(answer) ));
        }
    }

    private void calcD() {
        if(notNull(new int[]{0})){
            double answer = 1 / dbl(input[0]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(5, 0),
                    "D=\\frac{1}{F}",
                    "D=\\frac{1}{" + input[0] + "}",
                    answer(answer) ));
        }

    }

    private void calcd() {
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[0]) / dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(6, 0),
                    "M=F*d",
                    "d=\\frac{M}{F}=\\frac{" + input[0] + "}{" + input[1] + "}",
                    answer(answer) ));
        }
    }

    private void calcE() {
        if(notNull(new int[]{8})){
            double answer = Consts.h * dbl(input[8]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(7, 0),
                    "E=h\\nu",
                    "E=" + Consts.h + "*10^{" + Consts.hpow + "}*" + input[8],
                    answer(answer, Consts.hpow) ));
        }
        if(notNull(new int[]{0})){
            double answer = dbl(input[0]) * Consts.c * Consts.c;
            visualData.add(new PhysFormulaData(values.getCalcDescription(7, 1),
                    "E=mc^2",
                    "E=" + input[0] + "*" + Consts.c + "^2 *10^{" + (2 * Consts.cpow) + "}",
                    answer(answer, (2 * Consts.cpow)) ));
        }
        if(notNull(new int[]{6, 7})){
            double answer = dbl(input[6]) + dbl(input[7]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(7, 2),
                    "E=E_k+E_p",
                    "E=" + input[6] + "+" + input[7],
                    answer(answer) ));
        }
        if(notNull(new int[]{5, 6})){
            double answer = dbl(input[5]) - dbl(input[6]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(7, 3),
                    "E=E_k+E_p",
                    "E_p=E-E_k",
                    answer(answer) ));
        }
        if(notNull(new int[]{5, 7})){
            double answer = dbl(input[5]) - dbl(input[7]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(7, 4),
                    "E=E_k+E_p",
                    "E_k=E-E_p",
                    answer(answer) ));
        }
        if(notNull(new int[]{3, 4})){
            double answer = dbl(input[3]) * dbl(input[4]) * dbl(input[4]) / 2;
            visualData.add(new PhysFormulaData(values.getCalcDescription(7, 5),
                    "E_p=\\frac{kx^2}{2}",
                    "E_p=\\frac{" + input[3] + "*" + input[4] + "^2}{2}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 2})){
            double answer = dbl(input[0]) * Consts.g * dbl(input[2]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(7, 6),
                    "E_p=mgh",
                    "E_p=" + input[0] + "*" + Consts.g + "*" + input[2],
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[0]) * dbl(input[1]) * dbl(input[1]) / 2;
            visualData.add(new PhysFormulaData(values.getCalcDescription(7, 7),
                    "E_k=\\frac{mv^2}{2}",
                    "E_k=\\frac{" + input[0] + "*" + input[1] + "^2}{2}",
                    answer(answer) ));
        }

    }

    private void calcF() {
        if(notNull(new int[]{0})){
            double answer = 1 / dbl(input[0]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(8, 0),
                    "D=\\frac{1}{F}",
                    "F=\\frac{1}{D}=\\frac{1}{" + input[0] + "}",
                    answer(answer) ));
        }
    }

    private void calcFF() {
        if(notNull(new int[]{20, 21})){
            double answer = dbl(input[20]) / dbl(input[21]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(9, 0),
                    "A=Fs",
                    "F=\\frac{A}{s}=\\frac{" + input[20] + "}{" + input[21] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{20, 21, 22})){
            double answer = dbl(input[20]) / (dbl(input[21]) * Math.cos(dbl(input[22]) * Math.PI / 180));
            visualData.add(new PhysFormulaData(values.getCalcDescription(9, 1),
                    "A=Fs\\cos{\\alpha}",
                    "F=\\frac{A}{s\\cos{\\alpha}}=\\frac{" + input[20] + "}{" + input[21] + "*\\cos{" + input[22] + "^{\\circ}}}",
                    answer(answer) ));
        }
        if(notNull(new int[]{15, 19})){
            double answer = dbl(input[15]) * Consts.g * dbl(input[19]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(9, 2),
                    "F_A=\\rho gV",
                    "F_A=" + input[15] + "*" + Consts.g + "*" + input[19],
                    answer(answer) ));
        }
        if(notNull(new int[]{15, 16, 18})){
            double answer = 0.5 * dbl(input[15]) * Consts.g * dbl(input[16]) * dbl(input[18]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(9, 3),
                    "F=\\frac{1}{2}\\rho gHS",
                    "F=\\frac{1}{2}*" + input[15] + "*" + Consts.g + "*" + input[16] + "*" + input[18],
                    answer(answer) ));
        }
        if(notNull(new int[]{15, 16, 17})){
            double answer = 0.5 * dbl(input[15]) * Consts.g * dbl(input[16]) * dbl(input[17]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(9, 4),
                    "F=\\frac{1}{2}\\rho gHS",
                    "F=\\frac{1}{2}*" + input[15] + "*" + Consts.g + "*" + input[16] + "*" + input[17],
                    answer(answer) ));
        }
        if(notNull(new int[]{11, 12})){
            double answer = dbl(input[11]) / dbl(input[12]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(9, 5),
                    "M=Fd",
                    "F=\\frac{M}{d}=\\frac{" + input[11] + "}{" + input[12] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 9, 10, 24})){
            double answer = ((dbl(input[1]) * dbl(input[9])) - (dbl(input[1]) * dbl(input[10]))) / dbl(input[24]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(9, 6),
                    "\\vec{F}t=m\\vec{v}-m\\vec{v_0}",
                    "F=\\frac{m(v-v_0)}{t}" + nl + "\\frac{" + input[1] + "*(" + input[9] + "-" + input[10] + ")}{" + input[24] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{6, 7, 8})){
            double answer = Consts.G * dbl(input[6]) * dbl(input[7]) / (dbl(input[8]) * dbl(input[8]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(9, 7),
                    "F=G\\frac{m_1 m_2}{r^2}",
                    "F=" + Consts.G + "*10^{" + Consts.Gpow + "}\\frac{" + input[6] + "*" + input[7] + "}{" + input[8] + "^2}",
                    answer(answer, Consts.Gpow) ));
        }
        if(notNull(new int[]{1})){
            double answer = dbl(input[1]) * Consts.g;
            visualData.add(new PhysFormulaData(values.getCalcDescription(9, 8),
                    "\\vec{F}=m\\vec{g}",
                    "F=" + input[1] + "*" + Consts.g,
                    answer(answer) ));
        }
        if(notNull(new int[]{4, 5})){
            double answer = -dbl(input[4]) * dbl(input[5]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(9, 9),
                    "F=-kx",
                    "F=-(" + input[4] + ")*" + input[5],
                    answer(answer) ));
        }
        if(notNull(new int[]{2, 3})){
            double answer = dbl(input[2]) * dbl(input[3]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(9, 10),
                    "F=\\mu N",
                    "F=" + input[2] + "*" + input[3],
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[1]) * dbl(input[0]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(9, 11),
                    "\\vec{a}=\\frac{\\vec{F_R}}{m}",
                    "F_R=" + input[1] + "*" + input[0],
                    answer(answer) ));
        }

    }

    private void calch() {
        if(notNull(new int[]{6, 7})){
            double answer = dbl(input[6]) / (dbl(input[7]) * Consts.g);
            visualData.add(new PhysFormulaData(values.getCalcDescription(10, 0),
                    "E_p=mgh",
                    "h=\\frac{E_p}{mg}=\\frac{" + input[6] + "}{" + input[7] + "*" + Consts.g + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 7, 9})){
            double answer = dbl(input[0]) / (dbl(input[7]) * Consts.g) + dbl(input[9]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(10, 1),
                    "A=mg(h_1-h_2)",
                    "h_1=\\frac{A}{mg} + h_2" + nl + "\\frac{" + input[0] + "}{" + input[7] + "*" + Consts.g + "}+" + input[9],
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 7, 8})){
            double answer = dbl(input[0]) / (dbl(input[7]) * Consts.g) + dbl(input[8]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(10, 2),
                    "A=mg(h_1-h_2)",
                    "h_2=\\frac{A}{mg} + h_1" + nl + "\\frac{" + input[0] + "}{" + input[7] + "*" + Consts.g + "}+" + input[8],
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 4, 5})){
            double answer = 2 * dbl(input[4]) / (dbl(input[1]) * Consts.g * dbl(input[5]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(10, 3),
                    "F=\\frac{1}{2}\\rho gHS",
                    "H=\\frac{2F}{\\rho gS}" + nl + "\\frac{2*" + input[4] + "}{" + input[1] + "*" + Consts.g + "*" + input[5] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 2, 3})){
            double answer = 2 * dbl(input[2]) / (dbl(input[1]) * Consts.g * dbl(input[3]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(10, 4),
                    "F=\\frac{1}{2}\\rho gHS",
                    "H=\\frac{2F}{\\rho gS}" + nl + "\\frac{2*" + input[2] + "}{" + input[1] + "*" + Consts.g + "*" + input[3] + "}",
                    answer(answer) ));
        }

    }

    private void calcI() {
        if(notNull(new int[]{11})){
            double answer = dbl(input[11]) / Math.sqrt(2);
            visualData.add(new PhysFormulaData(values.getCalcDescription(11, 0),
                    "I_d=\\frac{I_m}{\\sqrt{2}}",
                    "I_d=\\frac{" + input[11] + "}{\\sqrt{2}}",
                    answer(answer) ));
        }
        if(notNull(new int[]{12})){
            double answer = dbl(input[12]) * Math.sqrt(2);
            visualData.add(new PhysFormulaData(values.getCalcDescription(11, 1),
                    "I_m=I_d\\sqrt{2}",
                    "I_m=" + input[12] + "*\\sqrt{2}",
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 2, 3, 4})){
            double answer = dbl(input[3]) * dbl(input[2]) * dbl(input[4]) * dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(11, 2),
                    "I=q_0 nvS",
                    "I=" + input[3] + "*" + input[2] + "*" + input[4] + "*" + input[1],
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 7, 10})){
            double answer = Math.sqrt(dbl(input[10]) / (dbl(input[7]) * dbl(input[0])));
            visualData.add(new PhysFormulaData(values.getCalcDescription(11, 3),
                    "Q=I^2Rt",
                    "I=\\sqrt{\\frac{Q}{Rt}}" + nl + "\\sqrt{\\frac{" + input[10] + "}{" + input[7] + "*" + input[0] + "}}",
                    answer(answer) ));
        }
        if(notNull(new int[]{6, 9})){
            double answer = dbl(input[9]) / dbl(input[6]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(11, 4),
                    "P=IU",
                    "I=\\frac{P}{U}=\\frac{" + input[9] + "}{" + input[6] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{7, 9})){
            double answer = Math.sqrt(dbl(input[9]) / dbl(input[7]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(11, 5),
                    "P=I^2 R",
                    "I=\\sqrt{\\frac{P}{R}}=\\sqrt{\\frac{" + input[9] + "}{" + input[7] + "}}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 6, 8})){
            double answer = dbl(input[8]) / (dbl(input[6]) * dbl(input[0]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(11, 6),
                    "A=IUt",
                    "I=\\frac{A}{Ut}=\\frac{" + input[8] + "}{" + input[6] + "*" + input[0] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 7, 8})){
            double answer = Math.sqrt(dbl(input[8]) / (dbl(input[7]) * dbl(input[0])));
            visualData.add(new PhysFormulaData(values.getCalcDescription(11, 7),
                    "A=I^2 Rt",
                    "I=\\sqrt{\\frac{A}{Rt}}=\\sqrt{\\frac{" + input[8] + "}{" + input[7] + "*" + input[0] + "}}",
                    answer(answer) ));
        }
        if(notNull(new int[]{6, 7})){
            double answer = dbl(input[6]) / dbl(input[7]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(11, 8),
                    "I=\\frac{U}{R}",
                    "I=\\frac{" + input[6] + "}{" + input[7] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 5})){
            double answer = dbl(input[1]) * dbl(input[5]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(11, 9),
                    "j=\\frac{I}{S}",
                    "I=Sj=" + input[1] + "*" + input[5],
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 3})){
            double answer = dbl(input[3]) / dbl(input[0]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(11, 10),
                    "I=\\frac{q}{t}",
                    "I=\\frac{" + input[3] + "}{" + input[0] + "}",
                    answer(answer) ));
        }

    }

    private void calcj() {
        if(notNull(new int[]{0,1})){
            double answer = dbl(input[0]) / dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(12, 0),
                    "j=\\frac{I}{S}",
                    "j=\\frac{" + input[0] + "}{" + input[1] + "}",
                    answer(answer) ));
        }

    }

    private void calcl() {
        if(notNull(new int[]{1, 2, 3})){
            double answer = dbl(input[1]) * dbl(input[2]) / dbl(input[3]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(13, 0),
                    "R=\\rho\\frac{l}{S}",
                    "l=\\frac{RS}{\\rho}=\\frac{" + input[1] + "*" + input[2] + "}{" + input[3] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0})){
            double answer = dbl(input[0]) * dbl(input[0]) * Consts.g / (4 * Math.PI * Math.PI);
            visualData.add(new PhysFormulaData(values.getCalcDescription(13, 1),
                    "T=2\\pi\\sqrt{\\frac{l}{g}}",
                    "l=\\frac{T^2g}{4\\pi^2}" + nl + "\\frac{" + input[0] + "^2*" + Consts.g + "}{4*" + Consts.pi + "^2}",
                    answer(answer) ));
        }

    }

    private void calcm() {
        if(notNull(new int[]{33})){
            double answer = dbl(input[33]) / (Consts.c * Consts.c);
            visualData.add(new PhysFormulaData(values.getCalcDescription(14, 0),
                    "E=mc^2",
                    "m=\\frac{E}{c^2}=\\frac{" + input[33] + "}{" + Consts.c + "^2*10^{" + 2 * Consts.cpow + "}}",
                    answer(answer, -2 * Consts.cpow) ));
        }
        if(notNull(new int[]{16, 32})){
            double answer = dbl(input[32]) / Math.sqrt(1-((dbl(input[16]) * dbl(input[16])) / (Consts.c * Consts.c * 10000 * 10000 * 10000 * 10000)));
            visualData.add(new PhysFormulaData(values.getCalcDescription(14, 1),
                    "m=\\frac{m_0}{\\sqrt{1-\\frac{v^2}{c^2}}}",
                    "m=\\frac{" + input[32] + "}{\\sqrt{1-\\frac{" + input[16] + "^2}{" + Consts.c + "^2*10^{" + 2 * Consts.cpow + "}}}}",
                    answer(answer) ));
        }
        if(notNull(new int[]{28, 31})){
            double answer = dbl(input[31]) / dbl(input[28]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(14, 2),
                    "C=cm",
                    "m=\\frac{C}{c}=\\frac{" + input[31] + "}{" + input[28] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{27, 28, 29, 30})){
            double answer = dbl(input[27]) / (dbl(input[28]) * (dbl(input[30]) - dbl(input[29])));
            visualData.add(new PhysFormulaData(values.getCalcDescription(14, 3),
                    "Q=cm(t_2-t_1)",
                    "m=\\frac{Q}{c(t_2-t_1)}" + nl + "\\frac{" + input[27] + "}{" + input[28] + "*(" + input[30] + "-" + input[29] + ")}",
                    answer(answer) ));
        }
        if(notNull(new int[]{22, 25, 26})){
            double answer = 2 * dbl(input[26]) * dbl(input[22]) / (3 * Consts.R * dbl(input[25]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(14, 4),
                    "U=\\frac{3}{2}\\frac{m}{M}RT",
                    "m=\\frac{2UM}{3RT}" + nl + "\\frac{2*" + input[26] + "*" + input[22] + "}{3*" + Consts.R + "*" + input[25] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{22, 23, 24, 25})){
            double answer = dbl(input[23]) * dbl(input[24]) * dbl(input[22]) / (Consts.R * dbl(input[25]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(14, 5),
                    "pV=\\frac{m}{M}RT",
                    "m=\\frac{pVM}{RT}" + nl + "\\frac{" + input[23] + "*" + input[24] + "*" + input[22] + "}{" + Consts.R + "*" + input[25] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{21, 22})){
            double answer = dbl(input[21]) * dbl(input[22]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(14, 6),
                    "\\nu=\\frac{m}{M}",
                    "m=\\nu M=" + input[21] + "*" + input[22],
                    answer(answer) ));
        }
        if(notNull(new int[]{19, 20})){
            double answer = dbl(input[20]) * Math.pow((dbl(input[19]) / (2 * Math.PI)), 2);
            visualData.add(new PhysFormulaData(values.getCalcDescription(14, 7),
                    "T=2\\pi\\sqrt{\\frac{m}{k}}",
                    "m=k\\left(\\frac{T}{2\\pi}\\right)^2" + nl + input[20] + "*\\left(\\frac{" + input[19] + "}{2*" + Consts.pi + "}\\right)^2",
                    answer(answer) ));
        }
        if(notNull(new int[]{8, 9, 12})){
            double answer = 2 * dbl(input[12]) / ((dbl(input[8]) * dbl(input[8])) - (dbl(input[9]) * dbl(input[9])));
            visualData.add(new PhysFormulaData(values.getCalcDescription(14, 8),
                    "A=\\frac{mv^2}{2}-\\frac{mv_0^2}{2}",
                    "m=\\frac{2A}{v^2-v_0^2}" + nl + "\\frac{2*" + input[12] + "}{" + input[8] + "^2-" + input[9] + "^2}",
                    answer(answer) ));
        }
        if(notNull(new int[]{17, 18})){
            double answer = dbl(input[17]) / (Consts.g * dbl(input[18]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(14, 9),
                    "E_p=mgh",
                    "m=\\frac{E_p}{gh}=\\frac{" + input[17] + "}{" + Consts.g + "*" + input[18] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{15, 16})){
            double answer = 2 * dbl(input[15]) / (dbl(input[16]) * dbl(input[16]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(14, 10),
                    "E_k=\\frac{mv^2}{2}",
                    "m=\\frac{E_k}{v^2}=\\frac{2*" + input[15] + "}{" + input[16] + "^2}",
                    answer(answer) ));
        }
        if(notNull(new int[]{12, 13, 14})){
            double answer = dbl(input[12]) / (Consts.g * (dbl(input[13]) - dbl(input[14])));
            visualData.add(new PhysFormulaData(values.getCalcDescription(14, 11),
                    "A=mg(h_1-h_2)",
                    "m=\\frac{A}{g(h_1-h_2)}" + nl + "\\frac{" + input[12] + "}{" + Consts.g + "*(" + input[13] + "-" + input[14] + ")}",
                    answer(answer) ));
        }
        if(notNull(new int[]{10, 11})){
            double answer = dbl(input[10]) * dbl(input[11]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(14, 12),
                    "\\rho=\\frac{m}{V}",
                    "m=\\rho V=" + input[10] + "*" + input[11],
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 7, 8, 9})){
            double answer = dbl(input[1]) * dbl(input[7]) / (dbl(input[8]) - dbl(input[9]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(14, 13),
                    "\\vec{F}t=m\\vec{v}-m\\vec{v_0}",
                    "m=\\frac{Ft}{v-v_0}" + nl + "\\frac{" + input[1] + "*" + input[7] + "}{" + input[8] + "-" + input[9] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{4, 5, 6})){
            double answer = dbl(input[5]) * dbl(input[6]) * dbl(input[6]) / (Consts.G * dbl(input[4]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(14, 14),
                    "F=G\\frac{m_1 m_2}{r^2}",
                    "m_2=\\frac{Fr^2}{Gm_1}" + nl + "\\frac{" + input[5] + "*" + input[6] + "^2}{" + Consts.G + "*10^{" + Consts.Gpow + "}*" + input[4] + "}",
                    answer(answer, -Consts.Gpow) ));
        }
        if(notNull(new int[]{2})){
            double answer = dbl(input[2]) / Consts.g;
            visualData.add(new PhysFormulaData(values.getCalcDescription(14, 15),
                    "\\vec{F}=m\\vec{g}",
                    "m=\\frac{F}{g}=\\frac{" + input[2] + "}{" + Consts.g + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[1]) / dbl(input[0]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(14, 16),
                    "\\vec{a}=\\frac{\\vec{F_R}}{m}",
                    "m=\\frac{F_R}{a}",
                    answer(answer) ));
        }

    }

    private void calcM() {
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[0]) * dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(15, 0),
                    "M=Fd",
                    "M=" + input[0] + "*" + input[1],
                    answer(answer) ));
        }

    }

    private void calcnu() {
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[0]) / dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(16, 0),
                    "\\nu=\\frac{m}{M}",
                    "\\nu=\\frac{" + input[0] + "}{" + input[1] + "}",
                    answer(answer) ));
        }

    }

    private void calcnunu(){
        if(notNull(new int[]{5})){
            double answer = dbl(input[5]) / (2 * Math.PI);
            visualData.add(new PhysFormulaData(values.getCalcDescription(17, 0),
                    "\\omega=2\\pi\\nu",
                    "\\nu=\\frac{\\omega}{2\\pi}" + nl + "\\frac{" + input[5] + "}{2*" + Consts.pi + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{3, 4})){
            double answer = dbl(input[3]) / (2 * Math.PI * dbl(input[4]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(17, 1),
                    "v=2\\pi r\\nu",
                    "\\nu=\\frac{v}{2\\pi r}" + nl + "\\frac{" + input[3] + "}{2*" + Consts.pi + "*" + input[4] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{2})){
            double answer = 1 / dbl(input[2]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(17, 2),
                    "T=\\frac{1}{\\nu}",
                    "\\nu=\\frac{1}{T}=\\frac{1}{" + input[2] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[0]) / dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(17, 3),
                    "\\nu=\\frac{N}{t}",
                    "\\nu=\\frac{" + input[0] + "}{" + input[1] + "}",
                    answer(answer) ));
        }
    }

    private void calcN() {
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[0]) / dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(18, 0),
                    "F=\\mu N",
                    "N=\\frac{F}{\\mu}=\\frac{" + input[0] + "}{" + input[1] + "}",
                    answer(answer) ));
        }

    }

    private void calcNN(){
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[0]) * dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(19, 0),
                    "N=Fv",
                    "N=" + input[0] + "*" + input[1],
                    answer(answer) ));
        }
        if(notNull(new int[]{2, 3})){
            double answer = dbl(input[2]) / dbl(input[3]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(19, 1),
                    "N=\\frac{A}{t}",
                    "N=\\frac{" + input[2] + "}{" + input[3] + "}",
                    answer(answer) ));
        }
    }

    private void calcp() {
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[0]) * dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(20, 0),
                    "\\vec{p}=m\\vec{v}",
                    "p=" + input[0] + "*" + input[1],
                    answer(answer) ));
        }

    }

    private void calcpp(){
        if(notNull(new int[]{0, 1, 2, 3})){
            double answer = dbl(input[1]) * Consts.R * dbl(input[3]) / (dbl(input[2]) * dbl(input[0]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(21, 0),
                    "pV=\\frac{m}{M}RT",
                    "p=\\frac{mRT}{MV}" + nl + "\\frac{" + input[1] + "*" + Consts.R + "*" + input[3] + "}{" + input[2] + "*" + input[0] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{3, 4})){
            double answer = dbl(input[4]) *  Consts.k * dbl(input[3]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(21, 1),
                    "p=nkT",
                    "p=" + input[4] + "*" + Consts.k + "*10^{" + Consts.kpow + "}*" + input[3],
                    answer(answer, Consts.kpow) ));
        }
        if(notNull(new int[]{5, 6})){
            double answer = dbl(input[5]) * Consts.g * dbl(input[6]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(21, 2),
                    "p=\\rho gh",
                    "p=" + input[5] + "*" + Consts.g + "*" + input[6],
                    answer(answer) ));
        }
        if(notNull(new int[]{7, 8})){
            double answer = dbl(input[7]) / dbl(input[8]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(21, 3),
                    "p=\\frac{F}{S}",
                    "p=\\frac{" + input[7] + "}{" + input[8] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{9, 10, 11})){
            double answer = dbl(input[9]) / (dbl(input[11]) - dbl(input[10]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(21, 4),
                    "A'=p\\Delta V",
                    "p=\\frac{A'}{V_2-V_1}" + nl + "\\frac{" + input[9] + "}{" + input[11] + "-" + input[10] + "}",
                    answer(answer) ));
        }


    }

    private void calcP(){
        if(notNull(new int[]{0})){
            double answer = dbl(input[0]) * Consts.g;
            visualData.add(new PhysFormulaData(values.getCalcDescription(22, 0),
                    "P=mg",
                    "P=" + input[0] + "*" + Consts.g,
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[0]) * (Consts.g + dbl(input[1]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(22, 1),
                    "P=m(g+a)",
                    "P=" + input[0] + "*(" + Consts.g + "+" + input[1] + ")",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[0]) * (Consts.g - dbl(input[1]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(22, 2),
                    "P=m(g-a)",
                    "P=" + input[0] + "*(" + Consts.g + "-" + input[1] + ")",
                    answer(answer) ));
        }
    }

    private void calcPP() {
        if(notNull(new int[]{3, 4})){
            double answer = dbl(input[3]) * dbl(input[3]) / dbl(input[4]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(23, 0),
                    "P=\\frac{U^2}{R}",
                    "P=\\frac{" + input[3] + "^2}{" + input[4] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{2, 4})){
            double answer = dbl(input[2]) * dbl(input[2]) * dbl(input[4]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(23, 1),
                    "P=I^2 R",
                    "P=" + input[2] + "^2 *" + input[4],
                    answer(answer) ));
        }
        if(notNull(new int[]{2, 3})){
            double answer = dbl(input[2]) * dbl(input[3]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(23, 2),
                    "P=IU",
                    "P=" + input[2] +  "*" + input[3],
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[0]) / dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(23, 3),
                    "P=\\frac{A}{t}",
                    "P=\\frac{" + input[0] + "}{" + input[1] + "}",
                    answer(answer) ));
        }

    }

    private void calcq() {
        if(notNull(new int[]{3, 4})){
            double answer = dbl(input[3]) * dbl(input[4]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(24, 0),
                    "I=\\frac{q}{t}",
                    "q=It=" + input[3] + "*" + input[4],
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1, 2})){
            double answer = (dbl(input[0]) * dbl(input[2])) / (dbl(input[1]) * Consts.k);
            visualData.add(new PhysFormulaData(values.getCalcDescription(24, 1),
                    "W=k\\frac{q_1q_2}{r}",
                    "q_2=\\frac{W r}{q_1 k}" + nl + "\\frac{" + input[0] + "*" + input[2] + "}{" + input[1] + "*" + Consts.k + "*10^{" + Consts.kpow + "}}",
                    answer(answer, -Consts.kpow) ));
        }

    }

    private void calcQ() {
        if(notNull(new int[]{10, 11, 12})){
            double answer = dbl(input[10]) * dbl(input[10]) * dbl(input[11]) * dbl(input[12]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(25, 0),
                    "Q=I^2Rt",
                    "Q=" + input[10] + "^2*" + input[11] + "*" + input[12],
                    answer(answer) ));
        }
        if(notNull(new int[]{4, 9})){
            double answer = dbl(input[4]) / dbl(input[9]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(25, 1),
                    "\\eta=\\frac{A'}{Q}",
                    "Q=\\frac{A'}{\\eta}=\\frac{" + input[4] + "}{" + input[9] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{8, 9})){
            double answer = dbl(input[8]) / (1-dbl(input[9]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(25, 2),
                    "Q_1=\\frac{Q_2}{1-\\eta}",
                    "Q_1=\\frac{" + input[8] + "}{1-" + input[9] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{7, 9})){
            double answer = dbl(input[7]) * (1-dbl(input[9]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(25, 3),
                    "Q_2=Q_1(1-\\eta)",
                    "Q_2=" + input[7] + "*(1-" + input[9] + ")",
                    answer(answer) ));
        }
        if(notNull(new int[]{4, 5, 6})){
            double answer = (dbl(input[6]) - dbl(input[5])) + dbl(input[4]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(25, 4),
                    "Q=(U_2-U_1)+A'",
                    "Q=(" + input[6] + "-" + input[5] + ")+" + input[4],
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1, 2, 3})){
            double answer = dbl(input[0]) * dbl(input[1]) * (dbl(input[3]) - dbl(input[2]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(25, 5),
                    "Q=cm(t_2-t_1)",
                    "Q=" + input[0] + "*" + input[1] + "*(" + input[3] + "-" + input[2] + ")",
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 13})){
            double answer = dbl(input[13]) * dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(25, 6),
                    "Q=qm",
                    "Q=" + input[13] + "*" + input[1],
                    answer(answer) ));
        }

    }

    private void calcr() {
        if(notNull(new int[]{3, 4})){
            double answer = dbl(input[4]) / (dbl(input[3]) * dbl(input[3]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(26, 0),
                    "a=\\omega^2r",
                    "r=\\frac{a}{\\omega^2}=\\frac{" + input[4] + "}{" + input[3] + "^2}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 4})){
            double answer = dbl(input[0]) * dbl(input[0]) / dbl(input[4]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(26, 1),
                    "a=\\frac{v^2}{r}",
                    "r=\\frac{" + input[0] + "^2}{" + input[4] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 3})){
            double answer = dbl(input[0]) / dbl(input[3]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(26, 2),
                    "v=\\omega r",
                    "r=\\frac{v}{\\omega}=\\frac{" + input[0] + "}{" + input[3] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 2})){
            double answer = dbl(input[0]) / (2 * Math.PI * dbl(input[2]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(26, 3),
                    "v=2\\pi r\\nu",
                    "r=\\frac{v}{2\\pi r\\nu}=\\frac{" + input[0] + "}{2*" + Consts.pi + "*" + input[2] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[1]) * dbl(input[0]) / (2 * Math.PI);
            visualData.add(new PhysFormulaData(values.getCalcDescription(26, 4),
                    "v=\\frac{2\\pi r}{T}",
                    "r=\\frac{vT}{2\\pi}=\\frac{" + input[1] + "*" + input[0] + "}{2*" + Consts.pi + "}",
                    answer(answer) ));
        }

    }

    private void calcR() {
        if(notNull(new int[]{0, 6, 8})){
            double answer = dbl(input[8]) / (dbl(input[0]) * dbl(input[0]) * dbl(input[6]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(27, 0),
                    "Q=I^2Rt",
                    "R=\\frac{Q}{I^2t}=\\frac{" + input[8] + "}{" + input[0] + "^2*" + input[6] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 7})){
            double answer = dbl(input[7]) / (dbl(input[0]) * dbl(input[0]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(27, 1),
                    "P=I^2R",
                    "R=\\frac{P}{I^2}=\\frac{" + input[7] + "}{" + input[0] + "^2}",
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 7})){
            double answer = dbl(input[1]) * dbl(input[1]) / dbl(input[7]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(27, 2),
                    "P=\\frac{U^2}{R}",
                    "R=\\frac{U^2}{P}=\\frac{" + input[1] + "^2}{" + input[7] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 5, 6})){
            double answer = dbl(input[5]) / (dbl(input[0]) * dbl(input[0]) * dbl(input[6]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(27, 3),
                    "A=I^2Rt",
                    "R=\\frac{A}{I^2t}=\\frac{" + input[5] + "}{" + input[0] + "^2*" + input[6] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 5, 6})){
            double answer = dbl(input[1]) * dbl(input[1]) * dbl(input[6]) / dbl(input[5]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(27, 4),
                    "A=\\frac{U^2}{R}t",
                    "R=\\frac{U^2}{A}t=\\frac{" + input[1] + "^2}{" + input[5] + "}*" + input[6],
                    answer(answer) ));
        }
        if(notNull(new int[]{2, 3, 4})){
            double answer = dbl(input[2]) * dbl(input[3]) / dbl(input[4]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(27, 5),
                    "R=\\rho\\frac{l}{S}",
                    "R=" + input[2] + "*\\frac{" + input[3] + "}{" + input[4] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[1]) / dbl(input[0]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(27, 6),
                    "I=\\frac{U}{R}",
                    "R=\\frac{U}{I}=\\frac{" + input[1] + "}{" + input[0] + "}",
                    answer(answer) ));
        }

    }

    private void calcS() {
        if(notNull(new int[]{2, 6, 7})){
            double answer = dbl(input[2]) * dbl(input[7]) / dbl(input[6]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(28, 0),
                    "R=\\rho\\frac{l}{S}",
                    "S=\\rho\\frac{l}{R}" + nl + input[2] + "*\\frac{" + input[7] + "}{" + input[6] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{4, 5})){
            double answer = dbl(input[5]) / dbl(input[4]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(28, 1),
                    "j=\\frac{I}{S}",
                    "S=\\frac{I}{j}=\\frac{" + input[5] + "}{" + input[4] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 2, 3})){
            double answer = 2 * dbl(input[3]) / (dbl(input[2]) * Consts.g * dbl(input[1]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(28, 2),
                    "F=\\frac{1}{2}\\rho gHS",
                    "S=\\frac{2F}{\\rho gH}" + nl + "\\frac{2*" + input[3] + "}{" + input[2] + "*" + Consts.g + "*" + input[1] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1, 2})){
            double answer = 2 * dbl(input[0]) / (dbl(input[2]) * Consts.g * dbl(input[1]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(28, 3),
                    "F=\\frac{1}{2}\\rho gHS",
                    "S=\\frac{2F}{\\rho gH}" + nl + "\\frac{2*" + input[0] + "}{" + input[2] + "*" + Consts.g + "*" + input[1] + "}",
                    answer(answer) ));
        }

    }

    private void calcs() {
        if(notNull(new int[]{4, 7})){
            double answer = dbl(input[4]) / dbl(input[7]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(29, 0),
                    "A=Fs",
                    "s=\\frac{A}{F}=\\frac{" + input[4] + "}{" + input[7] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{4, 5, 6})){
            double answer = dbl(input[4]) / (dbl(input[5]) * Math.cos(dbl(input[6]) * Math.PI / 180));
            visualData.add(new PhysFormulaData(values.getCalcDescription(29, 1),
                    "A=Fs\\cos{\\alpha}",
                    "s=\\frac{A}{F\\cos{\\alpha}}=\\frac{" + input[4] + "}{" + input[5] + "*\\cos{" + input[6] + "^{\\circ}}}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 2, 3})){
            double answer = ((dbl(input[0]) * dbl(input[0])) - (dbl(input[2]) * dbl(input[2]))) / (2 * dbl(input[3]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(29, 2),
                    "s_x=\\frac{v_x^2-v_{0x}^2}{2a_x}",
                    "s_x=\\frac{" + input[0] + "^2-" + input[2] + "^2}{2*" + input[3] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 2, 3})){
            double answer = (dbl(input[2]) * dbl(input[1])) + ((dbl(input[3]) * dbl(input[1]) * dbl(input[1])) / 2);
            visualData.add(new PhysFormulaData(values.getCalcDescription(29, 3),
                    "\\vec{s}=\\vec{v_0}t+\\frac{\\vec{a}t^2}{2}",
                    "s=" + input[2] + "*" + input[1] + "+\\frac{" + input[3] + "*" + input[1] + "^2}{2}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1, 2})){
            double answer = (dbl(input[0]) + dbl(input[2])) * dbl(input[1]) / 2;
            visualData.add(new PhysFormulaData(values.getCalcDescription(29, 4),
                    "\\vec{s}=\\frac{\\vec{v} + \\vec{v_0}}{2}t",
                    "s=\\frac{" + input[0] + "+" + input[2] + "}{2}*" + input[1],
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[0]) * dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(29, 5),
                    "\\vec{s}=\\vec{v}t",
                    "s=" + input[0] + "*" + input[1],
                    answer(answer) ));
        }

    }

    private void calcT() {
        if(notNull(new int[]{8})){
            double answer = 2 * Math.PI * Math.sqrt(dbl(input[8]) / Consts.g);
            visualData.add(new PhysFormulaData(values.getCalcDescription(30, 0),
                    "T=2\\pi\\sqrt{\\frac{l}{g}}",
                    "T=2*" + Consts.pi + "*\\sqrt{\\frac{" + input[8] +  "}{" + Consts.g + "}}",
                    answer(answer) ));
        }
        if(notNull(new int[]{6, 7})){
            double answer = 2 * Math.PI * Math.sqrt(dbl(input[6]) / dbl(input[7]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(30, 1),
                    "T=2\\pi\\sqrt{\\frac{m}{k}}",
                    "T=2*" + Consts.pi + "*\\sqrt{\\frac{" + input[6] + "}{" + input[7] + "}}",
                    answer(answer) ));
        }
        if(notNull(new int[]{5})){
            double answer = 2 * Math.PI / dbl(input[5]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(30, 2),
                    "\\omega=\\frac{2\\pi}{T}",
                    "T=\\frac{2\\pi}{\\omega}=\\frac{2*" + Consts.pi + "}{" + input[5] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{3, 4})){
            double answer = 2 * Math.PI * dbl(input[4]) / dbl(input[3]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(30, 3),
                    "v=\\frac{2\\pi r}{T}",
                    "T=\\frac{2\\pi r}{v}=\\frac{2*" + Consts.pi + "*" + input[4] + "}{" + input[3] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{2})){
            double answer = 1 / dbl(input[2]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(30, 4),
                    "T=\\frac{1}{\\nu}",
                    "T=\\frac{1}{" + input[2] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[0]) / dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(30, 5),
                    "T=\\frac{t}{N}",
                    "T=\\frac{" + input[0] + "}{" + input[1] + "}",
                    answer(answer) ));
        }

    }

    private void calct() {
        if(notNull(new int[]{10, 11})){
            double answer = dbl(input[10]) / (1 - dbl(input[11]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(31, 0),
                    "\\eta=\\frac{T_1-T_2}{T_1}",
                    "T_1=\\frac{T_2}{1-\\eta}=\\frac{" + input[10] + "}{1-" + input[11] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{9, 11})){
            double answer = dbl(input[9]) * (1-dbl(input[11]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(31, 1),
                    "\\eta=\\frac{T_1-T_2}{T_1}",
                    "T_2=(1-\\eta)" + nl + input[9] + "(1-" + input[11] + ")",
                    answer(answer) ));
        }
        if(notNull(new int[]{2, 5, 6, 8})){
            double answer = dbl(input[8]) - (dbl(input[5]) / (dbl(input[6]) * dbl(input[2])));
            visualData.add(new PhysFormulaData(values.getCalcDescription(31, 2),
                    "Q=cm(t_2-t_1)",
                    "t_1=t_2-\\frac{Q}{cm}" + nl + input[8] + "-\\frac{" + input[5] + "}{" + input[6] + "*" + input[2] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{2, 5, 6, 7})){
            double answer = dbl(input[7]) + (dbl(input[5]) / (dbl(input[6]) * dbl(input[2])));
            visualData.add(new PhysFormulaData(values.getCalcDescription(31, 3),
                    "Q=cm(t_2-t_1)",
                    "t_2=t_1+\\frac{Q}{cm}" + nl + input[7] + "+\\frac{" + input[5] + "}{" + input[6] + "*" + input[2] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{2, 3, 4})){
            double answer = 2 * dbl(input[3]) * dbl(input[4]) / (3 * dbl(input[2]) * Consts.R);
            visualData.add(new PhysFormulaData(values.getCalcDescription(31, 4),
                    "U=\\frac{3}{2}\\frac{m}{M}RT",
                    "T=\\frac{2MU}{3mR}" + nl + "\\frac{2*" + input[3] + "*" + input[4] + "}{3*" + input[2] + "*" + Consts.R + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1, 2, 3})){
            double answer = dbl(input[0]) * dbl(input[1]) * dbl(input[3]) / (dbl(input[2]) * Consts.R);
            visualData.add(new PhysFormulaData(values.getCalcDescription(31, 5),
                    "pV=\\frac{m}{M}RT",
                    "T=\\frac{pVM}{mR}" + nl + "\\frac{" + input[0] + "*" + input[1] + "*" + input[3] + "}{" + input[2] + "*" + Consts.R + "}",
                    answer(answer) + "\\space (K) = " + "$$\n$$" + answer(answer - Consts.T0) + "\\space( ^{\\circ}C)"));
        }

    }

    private void calcTT(){
        if(notNull(new int[]{0})){
            double answer = dbl(input[0]) + Consts.T0;
            visualData.add(new PhysFormulaData(values.getCalcDescription(32, 0),
                    "T = t+T_0",
                    "T=" + input[0] + "+" + Consts.T0,
                    answer(answer) + "\\ K"));
        }
        if(notNull(new int[]{1})){
            double answer = dbl(input[1]) - Consts.T0;
            visualData.add(new PhysFormulaData(values.getCalcDescription(32, 1),
                    "t = T-T_0",
                    "t=" + input[1] + "-" + Consts.T0,
                    answer(answer) + "\\ ^{\\circ}C"));
        }

    }

    private void calctt() {
        if(notNull(new int[]{20, 21, 22})){
            double answer = -dbl(input[22]) * log(dbl(input[20]) / dbl(input[21]), 2);
            visualData.add(new PhysFormulaData(values.getCalcDescription(33, 0),
                    "N=N_0*2^{-\\frac{t}{T}}",
                    "t=-T*\\log_2{\\frac{N}{N_0}}" + nl + "-(" + input[22] + "*\\log_2{\\frac{" + input[20] + "}{" + input[21] + "}})",
                    answer(answer) ));
        }
        if(notNull(new int[]{13, 17, 19})){
            double answer = dbl(input[19]) / (dbl(input[13]) * dbl(input[13]) * dbl(input[17]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(33, 1),
                    "Q=I^2Rt",
                    "t=\\frac{Q}{I^2R}=\\frac{" + input[19] + "}{" + input[13] + "^2*" + input[17] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{15, 18})){
            double answer = dbl(input[15]) / dbl(input[18]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(33, 2),
                    "P=\\frac{A}{t}",
                    "t=\\frac{A}{P}=\\frac{" + input[15] + "}{" + input[18] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{13, 15, 16})){
            double answer = dbl(input[15]) / (dbl(input[13]) * dbl(input[16]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(33, 3),
                    "A=IUt",
                    "t=\\frac{A}{IU}=\\frac{" + input[15] + "}{" + input[13] + "*" + input[16] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{13, 15, 17})){
            double answer = dbl(input[15]) / (dbl(input[13]) * dbl(input[13]) * dbl(input[17]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(33, 4),
                    "A=I^2Rt",
                    "t=\\frac{A}{I^2R}=\\frac{" + input[15] + "}{" + input[13] + "^2*" + input[17] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{15, 16, 17})){
            double answer = dbl(input[15]) * dbl(input[17]) / (dbl(input[16]) * dbl(input[16]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(33, 5),
                    "A=\\frac{U^2}{R}t",
                    "t=\\frac{AR}{U^2}=\\frac{" + input[15] + "*" + input[17] + "}{" + input[16] + "^2}",
                    answer(answer) ));
        }
        if(notNull(new int[]{13, 14})){
            double answer = dbl(input[14]) / dbl(input[13]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(33, 6),
                    "I=\\frac{q}{t}",
                    "t=\\frac{q}{I}=\\frac{" + input[14] + "}{" + input[13] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 5, 9, 12})){
            double answer = dbl(input[9]) * (dbl(input[0]) - dbl(input[5])) / dbl(input[12]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(33, 7),
                    "\\vec{F}t=m\\vec{v}-m\\vec{v_0}",
                    "t=\\frac{m(v-v_0)}{F}" + nl + "\\frac{" + input[9] + "*(" + input[0] + "-" + input[5] + ")}{" + input[12] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{10, 11})){
            double answer = dbl(input[11]) * Math.PI / 180 / dbl(input[10]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(33, 8),
                    "\\omega=\\frac{\\varphi}{t}",
                    "t=\\frac{\\varphi}{\\omega}=\\frac{" + input[11] + "^{\\circ}}{" + input[10] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{7, 8})){
            double answer = dbl(input[8]) * dbl(input[7]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(33, 9),
                    "T=\\frac{t}{N}",
                    "t=TN=" + input[8] + "*" + input[7],
                    answer(answer) ));
        }
        if(notNull(new int[]{2, 3, 4, 5})){
            double answer = (Math.sqrt(dbl(input[5]) * dbl(input[5]) + (2 * dbl(input[4]) * (dbl(input[2]) - dbl(input[3])))) - dbl(input[5])) / dbl(input[4]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(33, 10),
                    "x=x_0+v_0t+\\frac{at^2}{2}",
                    "t=\\frac{\\sqrt{v_0^2+2a(x-x_0)}-v_0}{a}" + nl + "\\frac{\\sqrt{" + input[3] + "^2+2*" + input[4] + "(" + input[2] + "-" + input[3] + ")}-" + input[5] + "}{" + input[4] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 4, 5})){
            double answer = (Math.sqrt(dbl(input[5]) * dbl(input[5]) + (2 * dbl(input[4]) * dbl(input[1]))) - dbl(input[5])) / dbl(input[4]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(33, 11),
                    "\\vec{s}=\\vec{v_0}t+\\frac{at^2}{2}",
                    "t=\\frac{\\sqrt{v_0^2+2aS}-v_0}{a}" + nl + "\\frac{\\sqrt{" + input[5] + "^2+2*" + input[4] + "*" + input[1] + "}-" + input[5] + "}{" + input[4] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1, 5})){
            double answer = 2 * dbl(input[1]) / (dbl(input[0]) + dbl(input[5]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(33, 12),
                    "\\vec{s}=\\frac{\\vec{v}+\\vec{v_0}}{2}t",
                    "t=\\frac{2s}{v+v_0}" + nl + "\\frac{2*" + input[1] + "}{" + input[0] + "+" + input[5] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 4, 5})){
            double answer = (dbl(input[0]) - dbl(input[5])) / dbl(input[4]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(33, 13),
                    "\\vec{v}=\\vec{v_0}+\\vec{a}t",
                    "t=\\frac{v-v_0}{a}=\\frac{" + input[0] + "-" + input[5] + "}{" + input[4] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 2, 3})){
            double answer = (dbl(input[2]) - dbl(input[3])) / dbl(input[0]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(33, 14),
                    "x=x_0+v_xt",
                    "t=\\frac{x-x_0}{v_x}=\\frac{" + input[2] + "-" + input[3] + "}{" + input[0] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[1]) / dbl(input[0]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(33, 15),
                    "\\vec{v}=\\frac{\\vec{s}}{t}",
                    "t=\\frac{s}{v}=\\frac{" + input[1] + "}{" + input[0] + "}",
                    answer(answer) ));
        }

    }

    private void calcU() {
        if(notNull(new int[]{5})){
            double answer = dbl(input[5]) / Math.sqrt(2);
            visualData.add(new PhysFormulaData(values.getCalcDescription(34, 0),
                    "U_d=\\frac{U_m}{\\sqrt{2}}",
                    "U_d=\\frac{" + input[5] + "}{\\sqrt{2}}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 4})){
            double answer = dbl(input[4]) / dbl(input[0]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(34, 1),
                    "P=IU",
                    "U=\\frac{P}{I}=\\frac{" + input[4] + "}{" + input[0] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 4})){
            double answer = Math.sqrt(dbl(input[4]) * dbl(input[1]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(34, 2),
                    "P=\\frac{U^2}{R}",
                    "U=\\sqrt{PR}" + nl + "\\sqrt{" + input[4] + "*" + input[1] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 2, 3})){
            double answer = dbl(input[2]) / (dbl(input[0]) * dbl(input[3]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(34, 3),
                    "A=IUt",
                    "U=\\frac{A}{It}=\\frac{" + input[2] + "}{" + input[0] + "*" + input[3] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 2, 3})){
            double answer = Math.sqrt(dbl(input[2]) * dbl(input[1]) / dbl(input[3]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(34, 4),
                    "A=\\frac{U^2}{R}t",
                    "U=\\sqrt{\\frac{AR}{t}}" + nl + "\\sqrt{\\frac{" + input[2] + "*" + input[1] + "}{" + input[3] + "}}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[0]) * dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(34, 5),
                    "I=\\frac{U}{R}",
                    "U=IR=" + input[0] + "*" + input[1],
                    answer(answer) ));
        }

    }

    private void calcUU() {
        if(notNull(new int[]{0, 1, 2})){
            double answer = (3f / 2f) * (dbl(input[0]) / dbl(input[1]) * Consts.R * dbl(input[2]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(35, 0),
                    "U=\\frac{3}{2}\\frac{m}{M}RT",
                    "U=\\frac{3}{2}*\\frac{" + input[0] + "}{" + input[1] + "}*" + Consts.R + "*" + input[2],
                    answer(answer) ));
        }

    }

    private void calcV() {
        if(notNull(new int[]{3, 6, 8})){
            double answer = dbl(input[6]) / dbl(input[3]) + dbl(input[8]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(36, 0),
                    "A'=p(V_2-V_1)",
                    "V_1=\\frac{A}{p}+V_2=\\frac{" + input[6] + "}{" + input[3] + "}+" + input[8],
                    answer(answer) ));
        }
        if(notNull(new int[]{3, 6, 7})){
            double answer = dbl(input[7]) - dbl(input[6]) / dbl(input[3]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(36, 1),
                    "A'=p(V_2-V_1)",
                    "V_2=V_1-\\frac{A}{p}=" + input[7] + "-\\frac{" + input[6] + "}{" + input[3] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 3, 4, 5})){
            double answer = dbl(input[1]) * Consts.R * dbl(input[5]) / (dbl(input[3]) * dbl(input[4]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(36, 2),
                    "pV=\\frac{m}{M}RT",
                    "V=\\frac{mRT}{pM}" + nl + "\\frac{" + input[1] + "*" + Consts.R + "*" + input[5] + "}{" + input[3] + "*" + input[4] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{2, 0})){
            double answer = dbl(input[2]) / (dbl(input[0]) * Consts.g);
            visualData.add(new PhysFormulaData(values.getCalcDescription(36, 3),
                    "F_A=\\rho gV",
                    "V=\\frac{F_A}{\\rho g}=\\frac{" + input[2] + "}{" + input[0] + "*" + Consts.g + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[1]) / dbl(input[0]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(36, 4),
                    "\\rho=\\frac{m}{V}",
                    "V=\\frac{m}{\\rho}=\\frac{" + input[1] + "}{" + input[0] + "}",
                    answer(answer) ));
        }

    }

    private void calcv() {
        if(notNull(new int[]{6, 12, 15})){
            double answer = Math.sqrt((2 * dbl(input[15]) / dbl(input[12])) + (dbl(input[6]) * dbl(input[6])));
            visualData.add(new PhysFormulaData(values.getCalcDescription(37, 0),
                    "A=\\frac{mv^2}{2}-\\frac{mv_0^2}{2}",
                    "v=\\sqrt{\\frac{2A}{m}+v_0^2}" + nl + "\\sqrt{\\frac{2*" + input[15] + "}{" + input[12] + "}+" + input[6] + "^2}",
                    answer(answer) ));
        }
        if(notNull(new int[]{5, 12, 15})){
            double answer = Math.sqrt((dbl(input[5]) * dbl(input[5])) - (2 * dbl(input[15]) / dbl(input[12])));
            visualData.add(new PhysFormulaData(values.getCalcDescription(37, 1),
                    "A=\\frac{mv^2}{2}-\\frac{mv_0^2}{2}",
                    "v_0=\\sqrt{v^2-\\frac{2A}{m}}" + nl + "\\sqrt{" + input[5] + "^2-\\frac{2*" + input[15] + "}{" + input[12] + "}}",
                    answer(answer) ));
        }
        if(notNull(new int[]{12, 14})){
            double answer = Math.sqrt(2 * dbl(input[14]) / dbl(input[12]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(37, 2),
                    "E_k=\\frac{mv^2}{2}",
                    "v=\\sqrt{\\frac{2E_k}{m}}" + nl + "\\sqrt{\\frac{2*" + input[14] + "}{" + input[12] + "}}",
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 6, 11, 12})){
            double answer = dbl(input[11]) * dbl(input[1]) / dbl(input[12]) + dbl(input[6]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(37, 3),
                    "\\vec{F}t=m\\vec{v}-m\\vec{v_0}",
                    "v=\\frac{Ft}{m} + v_0" + nl + "\\frac{" + input[11] + "*" + input[1] + "}{" + input[12] + "}+" + input[6],
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 5, 11, 12})){
            double answer = dbl(input[5]) - (dbl(input[11]) * dbl(input[1]) / dbl(input[12]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(37, 4),
                    "\\vec{F}t=m\\vec{v}-m\\vec{v_0}",
                    "v_0=v-\\frac{Ft}{m}" + nl + input[5] + "-\\frac{" + input[11] + "*" + input[1] + "}{" + input[12] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{4, 7})){
            double answer = Math.sqrt(dbl(input[4]) * dbl(input[7]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(37, 5),
                    "a=\\frac{v^2}{r}",
                    "v=\\sqrt{ar}" + nl + "\\sqrt{" + input[4] + "*" + input[7] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{7, 10})){
            double answer = dbl(input[10]) * dbl(input[7]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(37, 6),
                    "v=\\omega r",
                    "v=" + input[10] + "*" + input[7],
                    answer(answer) ));
        }
        if(notNull(new int[]{7, 9})){
            double answer = 2 * Math.PI * dbl(input[7]) * dbl(input[9]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(37, 7),
                    "v=2\\pi r\\nu",
                    "v=2*" + Consts.pi + "*" + input[7] + "*" + input[9],
                    answer(answer) ));
        }
        if(notNull(new int[]{7, 8})){
            double answer = 2 * Math.PI * dbl(input[7]) / dbl(input[8]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(37, 8),
                    "v=\\frac{2\\pi r}{T}",
                    "v=\\frac{2*" + Consts.pi + "*" + input[7] + "}{" + input[8] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 2, 3, 4})){
            double answer = ((2 * (dbl(input[2]) - dbl(input[3]))) - (dbl(input[4]) * dbl(input[1]) * dbl(input[1]))) / dbl(input[1]) / 2;
            visualData.add(new PhysFormulaData(values.getCalcDescription(37, 9),
                    "x=x_0+v_0t+\\frac{at^2}{2}",
                    "v_0=\\frac{2(x-x_0)-at^2}{2t}" + nl + "\\frac{2(" + input[2] + "-" + input[3] + ")-" + input[4] + "*" + input[1] + "^2}{2*" + input[1] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 4, 6})){
            double answer = Math.sqrt(2 * dbl(input[4]) * dbl(input[0]) + (dbl(input[6]) * dbl(input[6])));
            visualData.add(new PhysFormulaData(values.getCalcDescription(37, 10),
                    "s_x=\\frac{v_x^2-v_{0x}^2}{2a_x}",
                    "v_x=\\sqrt{2a_x s_x+v_{0x}^2}" + nl + "\\sqrt{2*" + input[4] + "*" + input[0] + "+" + input[6] + "^2}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 4, 5})){
            double answer = Math.sqrt((dbl(input[5]) * dbl(input[5])) - 2 * dbl(input[4]) * dbl(input[0]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(37, 11),
                    "s_x=\\frac{v_x^2-v_{0x}^2}{2a_x}",
                    "v_{0x}=\\sqrt{v_x^2-2a_x s_x}" + nl + "\\sqrt{" + input[5] + "^2-2*" + input[4] + "*" + input[0] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1, 4})){
            double answer = (2 * dbl(input[0]) - dbl(input[4]) * dbl(input[1]) * dbl(input[1])) / (2 * dbl(input[1]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(37, 12),
                    "\\vec{s}=\\vec{v_0}t+\\frac{at^2}{2}",
                    "v_0=\\frac{2S-at^2}{2t^2}" + nl + "\\frac{2*" + input[0] + "-" + input[4] + "*" + input[1] + "}{2*" + input[1] + "^2}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1, 6})){
            double answer = (2 * dbl(input[0]) / dbl(input[1])) - dbl(input[6]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(37, 13),
                    "\\vec{s}=\\frac{\\vec{v}+\\vec{v_0}}{2}t",
                    "v=\\frac{2S}{t}-v_0" + nl + "\\frac{2*" + input[0] + "}{" + input[1] + "}-" + input[6],
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1, 5})){
            double answer = (2 * dbl(input[0]) / dbl(input[1])) - dbl(input[5]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(37, 14),
                    "\\vec{s}=\\frac{\\vec{v}+\\vec{v_0}}{2}t",
                    "v_0=\\frac{2S}{t}-v" + nl + "\\frac{2*" + input[0] + "}{" + input[1] + "}-" + input[5],
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 4, 6})){
            double answer = dbl(input[6]) + dbl(input[4]) * dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(37, 15),
                    "\\vec{v}=\\vec{v_0} + \\vec{a}t",
                    "v=" + input[6] + "+" + input[4] + "*" + input[1],
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 4, 5})){
            double answer = dbl(input[5]) - dbl(input[4]) * dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(37, 16),
                    "\\vec{v}=\\vec{v_0} + \\vec{a}t",
                    "v_0=v-at" + nl + input[5] + "-" + input[4] + "*" + input[1],
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 2, 3})){
            double answer = (dbl(input[2]) - dbl(input[3])) / dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(37, 17),
                    "x=x_0+v_x t",
                    "v_x=\\frac{x-x_0}{t}" + nl + "\\frac{" + input[2] + "-" + input[3] + "}{" + input[1] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[0]) / dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(37, 18),
                    "\\vec{v}=\\frac{\\vec{s}}{t}",
                    "v=\\frac{" + input[0] + "}{" + input[1] + "}",
                    answer(answer) ));
        }

    }

    private void calcx() {
        if(notNull(new int[]{5, 9})){
            double answer = Math.sqrt(2 * dbl(input[9]) / dbl(input[5]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(38, 0),
                    "E_p=\\frac{kx^2}{2}",
                    "x=\\sqrt{\\frac{2E_p}{k}}" + nl + "\\sqrt{\\frac{2*" + input[9] + "}{" + input[5] + "}}",
                    answer(answer) ));
        }
        if(notNull(new int[]{5, 6, 8})){
            double answer = Math.sqrt((2 * dbl(input[6]) / dbl(input[5])) + (dbl(input[8]) * dbl(input[8])));
            visualData.add(new PhysFormulaData(values.getCalcDescription(38, 1),
                    "A=\\frac{k}{2}(x_1^2-x_2^2)",
                    "x_1=\\sqrt{\\frac{2A}{k}+x_2^2}" + nl + "\\sqrt{\\frac{2*" + input[6] + "}{" + input[5] + "}+" + input[8] + "^2}" ,
                    answer(answer) ));
        }
        if(notNull(new int[]{5, 6, 7})){
            double answer = Math.sqrt((dbl(input[7]) * dbl(input[7])) - (2 * dbl(input[6]) / dbl(input[5])));
            visualData.add(new PhysFormulaData(values.getCalcDescription(38, 2),
                    "A=\\frac{k}{2}(x_1^2-x_2^2)",
                    "x_2=\\sqrt{x_1^2-\\frac{2A}{k}}" + nl + "\\sqrt{" + input[7] + "^2-\\frac{2*" + input[6] + "}{" + input[5] + "}}",
                    answer(answer) ));
        }
        if(notNull(new int[]{4, 5})){
            double answer = -dbl(input[4]) / dbl(input[5]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(38, 3),
                    "F_x=-kx",
                    "x=-\\frac{F_x}{k}=-\\frac{" + input[4] + "}{" + input[5] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 2, 3, 7})){
            double answer = dbl(input[7]) + dbl(input[2]) * dbl(input[1]) + (dbl(input[3]) * dbl(input[1]) * dbl(input[1]) / 2);
            visualData.add(new PhysFormulaData(values.getCalcDescription(38, 4),
                    "x=x_0+v_0 t+\\frac{at^2}{2}",
                    "x=" + input[7] + "+" + input[2] + "*" + input[1] + "*\\frac{" + input[3] + "*" + input[1] + "^2}{2}",
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 2, 3, 8})){
            double answer = dbl(input[8]) - dbl(input[2]) * dbl(input[1]) - (dbl(input[3]) * dbl(input[1]) * dbl(input[1]) / 2);
            visualData.add(new PhysFormulaData(values.getCalcDescription(38, 5),
                    "x=x_0+v_0 t+\\frac{at^2}{2}",
                    "x_0=x-v_0 t-\\frac{at^2}{2}" + nl + input[8] + "-" + input[2] + "*" + input[1] + "-\\frac{" + input[3] + "*" + input[1] + "^2}{2}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1, 7})){
            double answer = dbl(input[7]) + (dbl(input[0]) * dbl(input[1]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(38, 6),
                    "x=x_0+v_xt",
                    "x=" + input[7] + "+" + input[0] + "*" + input[1],
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1, 8})){
            double answer = dbl(input[8]) - (dbl(input[0]) * dbl(input[1]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(38, 7),
                    "x=x_0+v_xt",
                    "x_0=x-v_xt" + nl + input[8] + "-" + input[0] + "*" + input[1],
                    answer(answer) ));
        }

    }

    private void calcmu() {
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[0]) / dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(39, 0),
                    "F=\\mu N",
                    "\\mu=\\frac{F}{N}=\\frac{" + input[0] + "}{" + input[1] + "}",
                    answer(answer) ));
        }

    }

    private void calcrho(){
        if(notNull(new int[]{4, 6, 7})){
            double answer = dbl(input[6]) * dbl(input[4]) / dbl(input[7]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(40, 0),
                    "R=\\rho \\frac{l}{S}",
                    "\\rho=\\frac{RS}{l}" + nl + "\\frac{" + input[6] + "*" + input[4] + "}{" + input[7] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{1, 5})){
            double answer = dbl(input[5]) / Consts.g / dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(40, 1),
                    "F_A=\\rho gV",
                    "\\rho=\\frac{F_A}{gV}" + nl + "\\frac{" + input[5] + "}{" + Consts.g + "*" + input[1] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{3, 4, 5})){
            double answer = dbl(input[5]) / (Consts.g * dbl(input[3]) * dbl(input[4]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(40, 2),
                    "F=\\rho gHS",
                    "\\rho=\\frac{F}{gHS}" + nl + "\\frac{" + input[5] + "}{" + Consts.g + "*" + input[3] + "*" + input[4] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{2, 3})){
            double answer = dbl(input[2]) / (Consts.g * dbl(input[3]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(40, 3),
                    "p=\\rho gh",
                    "\\rho=\\frac{p}{gh}" + nl + "\\frac{" + input[2] + "}{" + Consts.g + "*" + input[3] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[0]) / dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(40, 4),
                    "\\rho=\\frac{m}{V}",
                    "\\rho=\\frac{" + input[0] + "}{" + input[1] + "}",
                    answer(answer) ));
        }

    }

    @SuppressLint("DefaultLocale")
    private void calcomega() {
        if(notNull(new int[]{5, 6})){
            double answer = Math.sqrt(dbl(input[6]) / dbl(input[5]));
            visualData.add(new PhysFormulaData(values.getCalcDescription(41, 0),
                    "a=\\omega^2r",
                    "\\omega=\\sqrt{\\frac{a}{r}}=\\sqrt{\\frac{" + input[6] + "}{" + input[5] + "}}",
                    answer(answer) ));
        }
        if(notNull(new int[]{4, 5})){
            double answer = dbl(input[4]) / dbl(input[5]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(41, 1),
                    "v=\\omega r",
                    "\\omega=\\frac{v}{r}=\\frac{" + input[4] + "}{" + input[5] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{3})){
            double answer = 2 * Math.PI * dbl(input[3]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(41, 2),
                    "\\omega=2\\pi\\nu",
                    "\\omega=2*" + Consts.pi + "*" + input[3],
                    answer(answer) ));
        }
        if(notNull(new int[]{2})){
            double answer = 2 * Math.PI / dbl(input[2]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(41, 3),
                    "\\omega=\\frac{2\\pi}{T}",
                    "\\omega=\\frac{2*" + Consts.pi + "}{" + input[2] + "}",
                    answer(answer) ));
        }
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[0]) * Math.PI / 180 / dbl(input[1]);
            visualData.add(new PhysFormulaData(values.getCalcDescription(41, 4),
                    "\\omega=\\frac{\\varphi}{t}",
                    "\\omega=\\frac{" + input[0] + "^{\\circ}}{" + input[1] + "}=\\frac{" + String.format("%.2f", dbl(input[0]) * Math.PI / 180) + "(rad)}{" + input[1] + "}",
                    answer(answer) ));
        }

    }

    private void calceta() {
        if(notNull(new int[]{4, 5})){
            double answer = (dbl(input[4]) - dbl(input[5])) / dbl(input[4]) * 100;
            visualData.add(new PhysFormulaData(values.getCalcDescription(42, 0),
                    "\\eta=\\frac{T_1-T_2}{T_1}*100\\%",
                    "\\eta=\\frac{" + input[4] + "-" + input[5] + "}{" + input[4] + "}*100\\%",
                    answer(answer) + "\\%" ));
        }
        if(notNull(new int[]{2, 3})){
            double answer = (dbl(input[2]) - dbl(input[3])) / dbl(input[2]) * 100;
            visualData.add(new PhysFormulaData(values.getCalcDescription(42, 1),
                    "\\eta=\\frac{Q_1-Q_2}{Q_1}*100\\%",
                    "\\eta=\\frac{" + input[2] + "-" + input[3] + "}{" + input[2] + "}*100\\%",
                    answer(answer) + "\\%" ));
        }
        if(notNull(new int[]{0, 1})){
            double answer = dbl(input[0]) / dbl(input[1]) * 100;
            visualData.add(new PhysFormulaData(values.getCalcDescription(42, 2),
                    "\\eta=\\frac{A'}{Q}*100\\%",
                    "\\eta=\\frac{" + input[0] + "}{" + input[1] + "}*100\\%",
                    answer(answer) + "\\%" ));
        }

    }

    private void calcPhi(){
        if(notNull(new int[]{0, 1, 2})){
            double answer = dbl(input[0]) * dbl(input[1]) * Math.cos(dbl(input[2]) / 180 * Math.PI);
            visualData.add(new PhysFormulaData(values.getCalcDescription(44, 0),
                    "\\phi = B*S*\\cos(\\alpha)",
                    "\\phi = " + input[0] + "*" + input[1] + "*\\cos(" + input[2] + "^{\\circ})",
                    answer(answer) ));
        }
    }


    /*
    ///////////////////////////////////////////////////        44

        if(notNull(new int[]{})){
            double answer = 0;
            visualData.add(new PhysFormulaData(values.getCalcDescription(, ),
                    "",
                    "",
                    answer(answer) ));
        }




    if(notNull(new int[]{})){
            double answer = 0;
            visualData.add(new PhysFormulaData("",
                    "",
                    "",
                    answer(answer) ));
    }

    ///////////////////////////////////////////////////
    */

}

class PhysFormulaData{
    private String description;
    private String mainFormula;
    private String secondFormula;
    private String answer;

    PhysFormulaData(String description, String mainFormula, String secondFormula, String answer){
        this.description = description;
        this.mainFormula = mainFormula;
        this.secondFormula = secondFormula;
        this.answer = answer;
    }

    String getDescription(){
        return description;
    }

    String getMainFormula(){
        return mainFormula;
    }

    String getSecondFormula(){
        return secondFormula;
    }

    String getAnswer(){
        return answer;
    }

}

package com.abc.qwert.thescience;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.eval.TeXUtilities;
import org.matheclipse.core.interfaces.IExpr;

import java.io.StringWriter;
import java.util.ArrayList;

class Derivative {


    private static String[] keyboardNumTEX = {
            "{x}", "(", ")", "",
            "{", " ^{", "\\sqrt{", "",
            "7", "8", "9", "\\div",
            "4", "5", "6", "\\cdot",
            "1", "2", "3", "-",
            "", "0", " .", "+"
    };

    private static String[] keyboardFuncTEX = {
            "\\pi", "\\alpha", "\\beta", "",
            "{e}", " ^{", "\\sqrt[", "",
            "\\ln(", "\\sin(", "\\arcsin(", "\\div",
            "\\lg(", "\\cos(", "\\arccos(", "\\cdot",
            "\\log_{", "\\tan(", "\\arctan(", "-",
            "", "\\cot(", "\\text{arccot}(", "+"
    };


    //private String prime = "' ";

    
    private int inputPosition = 0;
    private int lengthPosition = 0;


    private ArrayList<Integer> tokensLength;


    private String textMainFormula = "";
    private String textFinalFormula = "";

    private String texMainFormula = "";


    private ExprEvaluator util;

    private StringWriter writer;
    private TeXUtilities tex;



    Derivative(){
        util = new ExprEvaluator();
        writer = new StringWriter();
        EvalEngine engine = new EvalEngine();
        tex = new TeXUtilities(engine, false);

        tokensLength = new ArrayList<>();

    }

    void setTextMainFormula() {
        textMainFormula = toText(texMainFormula);
    }

    String getTextMainFormula(){
        return textMainFormula;
    }

    String getTextFinalFormula() {
        textFinalFormula = differentiate(textMainFormula);

        return textFinalFormula;
    }


    private String differentiate(String inputFormula){
        inputFormula = "D(" + inputFormula + ",x)";
        IExpr result;
        String s = "";
        try {
            result = util.eval(inputFormula);
            s = util.eval(result).toString().toLowerCase();
        } catch (Exception e){
            return "error";
        }


        return result.toString().toLowerCase();
    }

    String toTeX(String inputFormula){
        if(inputFormula.isEmpty() || inputFormula.equals("error")) {
            return inputFormula;
        }
        IExpr inputExpr = util.eval(inputFormula);
        writer = new StringWriter();
        tex.toTeX(inputExpr, writer);

        String out = writer.toString().toLowerCase();
        out = replace(out, "log", "ln");
        //out = replace(out, "\\csc", "1\\over\\sin");
        //out = replace(out, "\\sec", "1\\over\\cos");
        /*while (out.contains("log")){
            out = out.replace("log", "ln");
        }*/

        return out;
    }

    private String toText(String inputFormula){
        inputFormula = replace(inputFormula, "\\pi", "(pi)");
        inputFormula = replace(inputFormula, "\\alpha", "(alpha)");
        inputFormula = replace(inputFormula, "\\beta", "(beta)");
        inputFormula = replace(inputFormula, "\\", "");
        inputFormula = replace(inputFormula, "_", "");

        while (inputFormula.contains("sqrt[")){
            int l = 1, r = 0;
            int start, end;
            String sqrt;
            String found = "";
            String pow = "";
            int position = inputFormula.indexOf("sqrt[") + 5;
            start = position - 5;
            while (position < inputFormula.length() && l != r){
                char c = inputFormula.charAt(position);
                switch (c){
                    case '[':
                        l++;
                        break;
                    case ']':
                        r++;
                        break;
                    default:
                        pow = pow.concat(String.valueOf(c));

                }

                position++;
            }
            position = inputFormula.indexOf("]{") + 2;
            l=1;r=0;
            while (position < inputFormula.length() && l != r){
                char c = inputFormula.charAt(position);
                switch (c){
                    case '{':
                        l++;
                        break;
                    case '}':
                        r++;
                        break;
                    default:
                        found = found.concat(String.valueOf(c));

                }

                position++;
            }
            end = position;
            sqrt = found + "^(1/" + pow + ")";
            inputFormula = inputFormula.substring(0, start) + sqrt + inputFormula.substring(end);
        }

        inputFormula = replace(inputFormula, "} (", ",");
        inputFormula = replace(inputFormula, "text{arccot}(", "ArcCot(");
        inputFormula = replace(inputFormula, "{", "(");
        inputFormula = replace(inputFormula, "}", ")");
        inputFormula = replace(inputFormula, "over", ")/(");
        inputFormula = replace(inputFormula, "div", "/");
        inputFormula = replace(inputFormula, "cdot", "*");
        inputFormula = replace(inputFormula, "acr", "a");
        inputFormula = replace(inputFormula, "lg(", "log(10,");

        return inputFormula;
    }

    private String replace(String s, String a, String b){
        while (s.contains(a)){
            s = s.replace(a, b);
        }
        return s;
    }


    String addToken(String keyboardType, int position){
        String token = (keyboardType.equals("num")?keyboardNumTEX[position]:keyboardFuncTEX[position]);

        if(token.equals(" ^{") && inputPosition > 0){
            String previousToken = texMainFormula.substring(inputPosition - tokensLength.get(lengthPosition - 1), inputPosition);
            if(previousToken.equals("}")){
                String previousPreviousToken = texMainFormula.substring(inputPosition - tokensLength.get(lengthPosition - 1) - tokensLength.get(lengthPosition - 2), inputPosition - tokensLength.get(lengthPosition - 1));
                if(!previousPreviousToken.equals("\\over")){
                    positionLeft();
                    addToken(keyboardType, position);
                    return addSpace();
                }

            } else if(previousToken.equals("\\over")){
                positionRight();
                addToken(keyboardType, position);
                return addSpace();
            }

        }

        texMainFormula = insertToken(texMainFormula, token, inputPosition);
        if(keyboardType.equals("num")){
            inputPosition += keyboardNumTEX[position].length();
            tokensLength.add(lengthPosition, keyboardNumTEX[position].length());

            switch (position){
                case 4:
                    texMainFormula = insertToken(texMainFormula, "\\over", inputPosition);
                    tokensLength.add(lengthPosition + 1, 5);
                    texMainFormula = insertToken(texMainFormula, "}", inputPosition + 5);
                    tokensLength.add(lengthPosition + 2, 1);
                    break;
                case 5:
                    texMainFormula = insertToken(texMainFormula, "2", inputPosition++);
                    tokensLength.add(lengthPosition + 1, 1);
                    texMainFormula = insertToken(texMainFormula, "}", inputPosition++);
                    tokensLength.add(lengthPosition + 2, 1);
                    lengthPosition +=2;
                    break;
                case 6:
                    texMainFormula = insertToken(texMainFormula, "}", inputPosition);
                    tokensLength.add(lengthPosition + 1, 1);
                    break;
            }
        } else {
            inputPosition += keyboardFuncTEX[position].length();
            tokensLength.add(lengthPosition, keyboardFuncTEX[position].length());

            switch (position){
                case 5:
                    texMainFormula = insertToken(texMainFormula, "}", inputPosition);
                    tokensLength.add(lengthPosition + 1, 1);
                    break;
                case 6:
                    texMainFormula = insertToken(texMainFormula, "]{", inputPosition);
                    tokensLength.add(lengthPosition + 1, 2);
                    texMainFormula = insertToken(texMainFormula, "}", inputPosition + 2);
                    tokensLength.add(lengthPosition + 2, 1);
                    break;
                case 8:
                case 9:
                case 10:
                case 12:
                case 13:
                case 14:
                case 17:
                case 18:
                case 21:
                case 22:
                    texMainFormula = insertToken(texMainFormula, ")", inputPosition);
                    tokensLength.add(lengthPosition + 1, 1);
                    break;
                case 16:
                    texMainFormula = insertToken(texMainFormula, "} (", inputPosition);
                    tokensLength.add(lengthPosition + 1, 3);
                    texMainFormula = insertToken(texMainFormula, ")", inputPosition + 3);
                    tokensLength.add(lengthPosition + 2, 1);
                    break;
            }
        }

        lengthPosition++;

        return addSpace();
    }

    String deleteToken(){
        if(inputPosition != 0){
            lengthPosition--;
            String currentToken = texMainFormula.substring(inputPosition - tokensLength.get(lengthPosition), inputPosition);

            String nextToken;
            String previousToken;

            switch (currentToken){
                //a/b
                case "{":
                    nextToken = texMainFormula.substring(inputPosition, inputPosition + tokensLength.get(lengthPosition + 1));
                    if(nextToken.equals("\\over")){
                        String nextNextToken = texMainFormula.substring(inputPosition + tokensLength.get(lengthPosition + 1), inputPosition + tokensLength.get(lengthPosition + 1) + tokensLength.get(lengthPosition + 2));
                        inputPosition--;
                        if(nextNextToken.equals("}")) {
                            texMainFormula = texMainFormula.substring(0, inputPosition) +
                                    texMainFormula.substring(inputPosition + tokensLength.get(lengthPosition) + tokensLength.get(lengthPosition + 1) + tokensLength.get(lengthPosition + 2));

                            tokensLength.remove(lengthPosition);
                            tokensLength.remove(lengthPosition);
                            tokensLength.remove(lengthPosition);
                        }
                    } else {
                        inputPosition--;
                    }
                    break;
                case "\\over":
                    nextToken = texMainFormula.substring(inputPosition, inputPosition + tokensLength.get(lengthPosition + 1));
                    if(nextToken.equals("}")){
                        previousToken = texMainFormula.substring(inputPosition - tokensLength.get(lengthPosition) - tokensLength.get(lengthPosition - 1), inputPosition - tokensLength.get(lengthPosition));
                        if(previousToken.equals("{")){
                            texMainFormula = texMainFormula.substring(0, inputPosition - tokensLength.get(lengthPosition - 1) - tokensLength.get(lengthPosition)) + texMainFormula.substring(inputPosition + tokensLength.get(lengthPosition + 1));
                            lengthPosition--;
                            tokensLength.remove(lengthPosition);
                            tokensLength.remove(lengthPosition);
                            tokensLength.remove(lengthPosition);
                            inputPosition -= 6;
                        } else {
                            inputPosition -= 5;
                        }
                    } else {
                        inputPosition -= 5;
                    }
                    break;


                //sqrt(a)
                case "\\sqrt{":
                    nextToken = texMainFormula.substring(inputPosition, inputPosition + tokensLength.get(lengthPosition + 1));
                    inputPosition -= 6;
                    if(nextToken.equals("}")){
                        texMainFormula = texMainFormula.substring(0, inputPosition) + texMainFormula.substring(inputPosition + tokensLength.get(lengthPosition) + tokensLength.get(lengthPosition + 1));
                        tokensLength.remove(lengthPosition);
                        tokensLength.remove(lengthPosition);
                    }
                    break;


                //a ^{b}
                case " ^{":
                    nextToken = texMainFormula.substring(inputPosition, inputPosition + tokensLength.get(lengthPosition + 1));
                    inputPosition -= 3;
                    if(nextToken.equals("}")){
                        texMainFormula = texMainFormula.substring(0, inputPosition) + texMainFormula.substring(inputPosition + tokensLength.get(lengthPosition) + tokensLength.get(lengthPosition + 1));
                        tokensLength.remove(lengthPosition);
                        tokensLength.remove(lengthPosition);
                    }
                    break;


                //sqrt[a]{b}
                case "\\sqrt[":
                    nextToken = texMainFormula.substring(inputPosition, inputPosition + tokensLength.get(lengthPosition + 1));
                    if(nextToken.equals("]{")){
                        String nextNextToken = texMainFormula.substring(inputPosition + tokensLength.get(lengthPosition + 1), inputPosition + tokensLength.get(lengthPosition + 1) + tokensLength.get(lengthPosition + 2));
                        inputPosition -= 6;
                        if(nextNextToken.equals("}")) {
                            texMainFormula = texMainFormula.substring(0, inputPosition) +
                                    texMainFormula.substring(inputPosition + tokensLength.get(lengthPosition) + tokensLength.get(lengthPosition + 1) + tokensLength.get(lengthPosition + 2));

                            tokensLength.remove(lengthPosition);
                            tokensLength.remove(lengthPosition);
                            tokensLength.remove(lengthPosition);
                        }
                    } else {
                        inputPosition -= 6;
                    }
                    break;
                case "]{":
                    nextToken = texMainFormula.substring(inputPosition, inputPosition + tokensLength.get(lengthPosition + 1));
                    if(nextToken.equals("}")){
                        previousToken = texMainFormula.substring(inputPosition - tokensLength.get(lengthPosition) - tokensLength.get(lengthPosition - 1), inputPosition - tokensLength.get(lengthPosition));
                        if(previousToken.equals("\\sqrt[")){
                            texMainFormula = texMainFormula.substring(0, inputPosition - tokensLength.get(lengthPosition - 1) - tokensLength.get(lengthPosition)) + texMainFormula.substring(inputPosition + tokensLength.get(lengthPosition + 1));
                            lengthPosition--;
                            tokensLength.remove(lengthPosition);
                            tokensLength.remove(lengthPosition);
                            tokensLength.remove(lengthPosition);
                            inputPosition -= 8;
                        } else {
                            inputPosition -= 2;
                        }
                    } else {
                        inputPosition -= 2;
                    }
                    break;


                //log_{a}(b)
                case "\\log_{":
                    nextToken = texMainFormula.substring(inputPosition, inputPosition + tokensLength.get(lengthPosition + 1));
                    if(nextToken.equals("} (")){
                        if(inputPosition + 3 != texMainFormula.length()){
                            String nextNextToken = texMainFormula.substring(inputPosition + tokensLength.get(lengthPosition + 1), inputPosition + tokensLength.get(lengthPosition + 1) + tokensLength.get(lengthPosition + 2));
                            inputPosition -= 6;
                            if(nextNextToken.equals(")")) {
                                texMainFormula = texMainFormula.substring(0, inputPosition) +
                                        texMainFormula.substring(inputPosition + tokensLength.get(lengthPosition) + tokensLength.get(lengthPosition + 1) + tokensLength.get(lengthPosition + 2));

                                tokensLength.remove(lengthPosition);
                                tokensLength.remove(lengthPosition);
                                tokensLength.remove(lengthPosition);
                            }
                        } else {
                            inputPosition -= 6;
                            texMainFormula = texMainFormula.substring(0, inputPosition) +
                                    texMainFormula.substring(inputPosition + tokensLength.get(lengthPosition) + tokensLength.get(lengthPosition + 1));

                            tokensLength.remove(lengthPosition);
                            tokensLength.remove(lengthPosition);

                        }

                    } else {
                        inputPosition -= 6;
                    }
                    break;
                case "} (":
                    if(inputPosition != texMainFormula.length()){
                        nextToken = texMainFormula.substring(inputPosition, inputPosition + tokensLength.get(lengthPosition + 1));
                        if(nextToken.equals(")")){
                            previousToken = texMainFormula.substring(inputPosition - tokensLength.get(lengthPosition) - tokensLength.get(lengthPosition - 1), inputPosition - tokensLength.get(lengthPosition));
                            if(previousToken.equals("\\log_{")){
                                texMainFormula = texMainFormula.substring(0, inputPosition - tokensLength.get(lengthPosition - 1) - tokensLength.get(lengthPosition)) + texMainFormula.substring(inputPosition + tokensLength.get(lengthPosition + 1));
                                lengthPosition--;
                                tokensLength.remove(lengthPosition);
                                tokensLength.remove(lengthPosition);
                                tokensLength.remove(lengthPosition);
                                inputPosition -= 9;
                            } else {
                                inputPosition -= 3;
                            }
                        } else {
                            inputPosition -= 3;
                        }
                    } else {
                        inputPosition -= 3;
                    }

                    break;


                //()
                case "(":
                case "\\ln(":
                case "\\sin(":
                case "\\arcsin(":
                case "\\lg(":
                case "\\cos(":
                case "\\arccos(":
                case "\\tan(":
                case "\\arctan(":
                case "\\cot(":
                case "\\text{arccot}(":
                    if(inputPosition != texMainFormula.length()){
                        nextToken = texMainFormula.substring(inputPosition, inputPosition + tokensLength.get(lengthPosition + 1));
                        inputPosition -= tokensLength.get(lengthPosition);
                        if(nextToken.equals(")")){
                            texMainFormula = texMainFormula.substring(0, inputPosition) + texMainFormula.substring(inputPosition + tokensLength.get(lengthPosition) + tokensLength.get(lengthPosition + 1));
                            tokensLength.remove(lengthPosition);
                            tokensLength.remove(lengthPosition);

                        } else {
                            texMainFormula = texMainFormula.substring(0, inputPosition) + texMainFormula.substring(inputPosition + tokensLength.get(lengthPosition));
                            tokensLength.remove(lengthPosition);
                        }
                    } else {
                        inputPosition -= tokensLength.get(lengthPosition);
                        texMainFormula = texMainFormula.substring(0, inputPosition);
                        tokensLength.remove(lengthPosition);
                    }
                    break;


                //}
                case "}":
                    inputPosition--;
                    break;


                default:
                    inputPosition -= tokensLength.get(lengthPosition);
                    texMainFormula = texMainFormula.substring(0, inputPosition) + texMainFormula.substring(inputPosition + tokensLength.get(lengthPosition));
                    tokensLength.remove(lengthPosition);
                    break;

            }

        }

        return addSpace();
    }

    private String insertToken(String data, String token, int inputPosition){
        if(inputPosition == 0){
            data = token + data;
        } else if(inputPosition == data.length()){
            data = data + token;
        } else {
            data = data.substring(0, inputPosition) + token + data.substring(inputPosition);
        }

        return data;
    }

    void clear(){
        textMainFormula = "";
        texMainFormula = "";
        textFinalFormula = "";

        inputPosition = 0;
        lengthPosition = 0;

        tokensLength.clear();

    }


    String positionLeft(){
        if(inputPosition != 0){
            lengthPosition--;
            inputPosition -= tokensLength.get(lengthPosition);

        }

        return addSpace();
    }

    String positionRight(){
        if(inputPosition != texMainFormula.length()){
            inputPosition += tokensLength.get(lengthPosition);
            lengthPosition++;

        }

        return addSpace();
    }


    private String addSpace(){
        String space = " \\square ";
        return insertToken(texMainFormula, space, inputPosition);
    }

}

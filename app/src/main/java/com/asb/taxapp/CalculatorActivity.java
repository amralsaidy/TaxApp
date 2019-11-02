package com.asb.taxapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CalculatorActivity extends BaseActivity {

    TextView[] textViews = new TextView[10];


    TextView clearTextView, divideTextView, multiplyTextView, subtractTextView, sumTextView, pointTextView,
            equalTextView, sendTextView;
    EditText result1EditText, result2EditText;
    ImageView backspaceImageView, backImageView;

    private String expression = "";
    private boolean lastEqual = false;//Whether the last button is equal

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        setStatusBarGradiant(this, R.color.md_cyan_A700);
        init();


    }

    void init() {
        textViews[0] = findViewById(R.id.zero_tv_id);
        textViews[1] = findViewById(R.id.one_tv_id);
        textViews[2] = findViewById(R.id.two_tv_id);
        textViews[3] = findViewById(R.id.three_tv_id);
        textViews[4] = findViewById(R.id.four_tv_id);
        textViews[5] = findViewById(R.id.five_tv_id);
        textViews[6] = findViewById(R.id.six_tv_id);
        textViews[7] = findViewById(R.id.seven_tv_id);
        textViews[8] = findViewById(R.id.eight_tv_id);
        textViews[9] = findViewById(R.id.nine_tv_id);
        clearTextView = findViewById(R.id.clear_tv_id);
        backspaceImageView = findViewById(R.id.backspace_iv_id);
        divideTextView = findViewById(R.id.divide_tv_id);
        multiplyTextView = findViewById(R.id.multiply_tv_id);
        subtractTextView = findViewById(R.id.subtract_tv_id);
        sumTextView = findViewById(R.id.sum_tv_id);
        pointTextView = findViewById(R.id.point_tv_id);
        equalTextView = findViewById(R.id.equal_tv_id);
        result1EditText = findViewById(R.id.result1_et_id);
        result2EditText = findViewById(R.id.result2_et_id);
        sendTextView = findViewById(R.id.send_tv_id);
        backImageView = findViewById(R.id.back_iv_id);

        for (int i = 0; i < 10; i++) {
            final int m = i;
            textViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lastEqual) {
                        expression = ""; //The number pressed this time, if the last equal sign is pressed, the expression is cleared
                        lastEqual = false;
                    }
                    expression += textViews[m].getText();
                    result2EditText.setText(expression);
                    result2EditText.setSelection(expression.length());
                }
            });
        }

        clearTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expression = "";
                result2EditText.setText("");
                result1EditText.setText(null);
                lastEqual = false;
            }
        });

        backspaceImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expression.length() < 1) {
                    return;
                }
                expression = expression.substring(0, expression.length() - 1);
                result2EditText.setText(expression);
                result2EditText.setSelection(expression.length());
                lastEqual = false;

            }
        });

        divideTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOperIsRepeated(divideTextView);
                expression += divideTextView.getText();
                result2EditText.setText(expression);
                result2EditText.setSelection(expression.length());
                lastEqual = false;

            }
        });

        multiplyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOperIsRepeated(multiplyTextView);
                expression += multiplyTextView.getText();
                result2EditText.setText(expression);
                result2EditText.setSelection(expression.length());
                lastEqual = false;

            }
        });

        subtractTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOperIsRepeated(subtractTextView);
                expression += subtractTextView.getText();
                result2EditText.setText(expression);
                result2EditText.setSelection(expression.length());
                lastEqual = false;

            }
        });

        sumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOperIsRepeated(sumTextView);
                expression += sumTextView.getText();
                result2EditText.setText(expression);
                result2EditText.setSelection(expression.length());
                lastEqual = false;

            }
        });

        pointTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (result2EditText.getText().toString().equals("")) {
                    expression += pointTextView.getText();
                    result2EditText.setText(expression);
                    Log.e("expression", expression + "");
                }
                ArrayList<String> expressionList = Calculate.getStringListWithDot(expression);
                ArrayList<String> expressionList1 = Calculate.getStringList(expression);
                String lastInput = expressionList.get(Calculate.getStringListWithDot(expression).size() - 1);
                String lastInput1 = expressionList1.get(Calculate.getStringList(expression).size() - 1);

//                Log.e("expressionList", expressionList + "");
//                Log.e("expressionList1", expressionList1 + "");
//                Log.e("lastInput", lastInput + "");
//                Log.e("lastInput1", lastInput1 + "");
                if (lastInput.equals(".") || lastInput1.contains(".")) {
                    return;
                } else {
                    expression += pointTextView.getText();
                    Log.e("expression'=", expression + "");
                    result2EditText.setText(expression);
                    result2EditText.setSelection(expression.length());
                    lastEqual = false;
                }
            }
        });

        equalTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastEqual) return;//If you still press the equal sign last time, then do nothing
                //Small animation effect
                AnimationSet animSet = new AnimationSet(true);
                TranslateAnimation ta = new TranslateAnimation(0, 0, 0, -100);
                ta.setDuration(80);
                AlphaAnimation aa = new AlphaAnimation(1f, 0f);
                aa.setDuration(75);
                animSet.addAnimation(ta);
                animSet.addAnimation(aa);
                result2EditText.startAnimation(animSet);
                animSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //Calculated after the animation ends
                        result1EditText.setText(expression + "=");
                        result1EditText.setSelection(expression.length() + 1);//Display the calculation expression on the first line
                        try {
                            expression = Calculate.calculate(expression);
                            result2EditText.setText(expression);//Display calculation results in the second line
                        } catch (Exception ex) {
//                            result2EditText.setText();//Display calculation results in the second line
                            Toast.makeText(getApplicationContext(), "يوجد خطأ", Toast.LENGTH_LONG).show();
                            result2EditText.setText("");
                            expression = "";
                        }

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });


                // Prepare for the next time you press the calculator keyboard.
                // If the next time you press a number, empty the second line and re-enter the first number.
                // If it is non-numeric, then the result of this time is the first number entered, directly involved in the operation.
                lastEqual = true;

            }
        });


        sendTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                if(!isNumeric(result2EditText.getText().toString())){
                    Toast.makeText(getApplicationContext(), "لا يوجد نتيجة للترحيل", Toast.LENGTH_LONG).show();
                }else {
                    data.putExtra("RESPONSE", result2EditText.getText().toString());
                    setResult(RESULT_OK, data);
                    finish();
                    overridePendingTransition(R.anim.fragment_slide_in_left, 0);
               }
            }
        });

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.fragment_slide_in_left, 0);
            }
        });

    }



    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    void checkOperIsRepeated(TextView textView) {
        String lastInput = Calculate.getStringList(expression).get(Calculate.getStringList(expression).size() - 1);
        if (Calculate.isOper(textView.getText().toString()) && Calculate.isOper(lastInput)) {
            expression = expression.substring(0, expression.length() - 1);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fragment_slide_in_left, 0);
    }
}

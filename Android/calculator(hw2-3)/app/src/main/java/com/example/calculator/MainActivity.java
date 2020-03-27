package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    Button button0, button1, button2, button3, button4, button5, button6, button7,
            button8, button9, buttonAdd, buttonSub, buttonDivision, buttonMul,
            button10, buttonC, buttonEqual, buttonAC, buttonRoot, buttonSquare,
            buttonsin, buttonnthSquare, buttonFact, buttonnthRoot, buttonPercent,
            buttonln, buttonlog, buttoncos, buttontan;

    TextView crunchifyTextView;

    float mValueOne, mValueTwo;

    boolean crunchifyAddition = false, mSubtract = false, crunchifyMultiplication = false, crunchifyDivision = false,
            crunchifyTakeRoot = false, crunchifyExponentiation = false, crunchifySin = false, crunchifyCos = false,
            crunchifyTan = false, crunchifyLn = false, crunchifyLog = false, crunchifyPercent = false,
            crunchifyNthRoot = false, crunchifyNthSquare = false, crunchifyFactorial = false, isError = false, firstGet = false;

    String operation;

    private static final String STATE_MValueOne = "mValueOne", STATE_MValueTwo = "mValueTwo", STATE_MOperation = "operation";

    NumberFormat nf = new DecimalFormat("#.######");

    public static int calculateFactorial(float n){
        int result = 1;
        for (int i = 1; i <= n; i ++){
            result = result*i;
        }
        return result;
    };

    public static double nthRoot(float A, float N) {
        double xPre = Math.random() % 10;
        double eps = 0.001;
        double delX = 2147483647;
        double xK = 0.0;
        while (delX > eps) {
            xK = ((N - 1.0) * xPre + (double)A / Math.pow(xPre, N - 1)) / (double)N;
            delX = Math.abs(xK - xPre);
            xPre = xK;
        }
        return xK;
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putFloat("mValueOne", mValueOne);
        savedInstanceState.putFloat("mValueTwo", mValueTwo);
        savedInstanceState.putString("operation", operation);
        savedInstanceState.putString("textView", String.valueOf(crunchifyTextView.getText()));
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        button10 = (Button) findViewById(R.id.button10);
        buttonAdd = (Button) findViewById(R.id.buttonadd);
        buttonSub = (Button) findViewById(R.id.buttonsub);
        buttonMul = (Button) findViewById(R.id.buttonmul);
        buttonDivision = (Button) findViewById(R.id.buttondiv);
        buttonC = (Button) findViewById(R.id.buttonC);
        buttonEqual = (Button) findViewById(R.id.buttoneql);
        buttonAC = (Button) findViewById(R.id.buttonAC);
        buttonRoot = (Button) findViewById(R.id.buttonRoot);
        buttonSquare = (Button) findViewById(R.id.buttonSquare);
        buttonsin = (Button) findViewById(R.id.buttonsin);
        buttonnthSquare = (Button) findViewById(R.id.buttonnthSquare);
        buttonFact = (Button) findViewById(R.id.buttonFact);
        buttonnthRoot = (Button) findViewById(R.id.buttonnthRoot);
        buttonPercent = (Button) findViewById(R.id.buttonPercent);
        buttonln = (Button) findViewById(R.id.buttonln);
        buttonlog = (Button) findViewById(R.id.buttonlog);
        buttoncos = (Button) findViewById(R.id.buttoncos);
        buttontan = (Button) findViewById(R.id.buttontan);
        crunchifyTextView = (TextView) findViewById(R.id.textView);

        if (savedInstanceState != null) {
            mValueOne = savedInstanceState.getFloat("mValueOne");
            mValueTwo = savedInstanceState.getFloat("mValueTwo");
            operation = savedInstanceState.getString("operation");
            String DisplayText = savedInstanceState.getString("textView");
            crunchifyTextView.setText(DisplayText);
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = crunchifyTextView.getText().toString();
                if(s.equals("0") || isError || s.equals("error") || s.equals("∞")){
                    crunchifyTextView.setText("1");
                    isError=false;
                }
                else crunchifyTextView.setText(crunchifyTextView.getText() + "1");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = crunchifyTextView.getText().toString();
                if(s.equals("0") || isError || s.equals("error") || s.equals("∞")){
                    crunchifyTextView.setText("2");
                    isError=false;
                }
                else crunchifyTextView.setText(crunchifyTextView.getText() + "2");
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = crunchifyTextView.getText().toString();
                if(s.equals("0") || isError || s.equals("error") || s.equals("∞")){
                    crunchifyTextView.setText("3");
                    isError=false;
                }
                else crunchifyTextView.setText(crunchifyTextView.getText() + "3");
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = crunchifyTextView.getText().toString();
                if(s.equals("0") || isError || s.equals("error") || s.equals("∞")){
                    crunchifyTextView.setText("4");
                    isError=false;
                }
                else crunchifyTextView.setText(crunchifyTextView.getText() + "4");
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = crunchifyTextView.getText().toString();
                if(s.equals("0") || isError || s.equals("error") || s.equals("∞")){
                    crunchifyTextView.setText("5");
                    isError=false;
                }
                else crunchifyTextView.setText(crunchifyTextView.getText() + "5");
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = crunchifyTextView.getText().toString();
                if(s.equals("0") || isError || s.equals("error") || s.equals("∞")){
                    crunchifyTextView.setText("6");
                    isError=false;
                }
                else crunchifyTextView.setText(crunchifyTextView.getText() + "6");
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = crunchifyTextView.getText().toString();
                if(s.equals("0") || isError || s.equals("error") || s.equals("∞")){
                    crunchifyTextView.setText("7");
                    isError=false;
                }
                else crunchifyTextView.setText(crunchifyTextView.getText() + "7");
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = crunchifyTextView.getText().toString();
                if(s.equals("0") || isError || s.equals("error") || s.equals("∞")){
                    crunchifyTextView.setText("8");
                    isError=false;
                }
                else crunchifyTextView.setText(crunchifyTextView.getText() + "8");
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = crunchifyTextView.getText().toString();
                if(s.equals("0") || isError || s.equals("error") || s.equals("∞")){
                    crunchifyTextView.setText("9");
                    isError=false;
                }
                else crunchifyTextView.setText(crunchifyTextView.getText() + "9");
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = crunchifyTextView.getText().toString();
                if(s.equals("0") || isError || s.equals("error") || s.equals("∞")){
                    crunchifyTextView.setText(crunchifyTextView.getText() + "0");
                    isError=false;
                }
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!crunchifyTextView.getText().equals("") && crunchifyAddition == false && !crunchifyTextView.getText().equals("∞")) {
                    operation += "+";
                    mValueOne = Float.parseFloat(crunchifyTextView.getText() + "");
                    crunchifyAddition = true;
                    crunchifyTextView.setText(null);
                }
                else operation += "+";
            }
        });

        buttonSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!crunchifyTextView.getText().equals("") && mSubtract == false && !crunchifyTextView.getText().equals("∞")){
                    operation += "-";
                    mValueOne = Float.parseFloat(crunchifyTextView.getText() + "");
                    mSubtract = true;
                    crunchifyTextView.setText(null);
                }
                else operation += "-";
            }
        });

        buttonMul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!crunchifyTextView.getText().equals("") && crunchifyMultiplication == false && !crunchifyTextView.getText().equals("∞")){
                    operation += "*";
                    mValueOne = Float.parseFloat(crunchifyTextView.getText() + "");
                    crunchifyMultiplication = true;
                    crunchifyTextView.setText(null);
                }
                else operation += "*";
            }
        });

        buttonDivision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!crunchifyTextView.getText().equals("") && crunchifyDivision == false && !crunchifyTextView.getText().equals("∞")){
                    operation += "/";
                    mValueOne = Float.parseFloat(crunchifyTextView.getText() + "");
                    crunchifyDivision = true;
                    crunchifyTextView.setText(null);
                }
                else operation += "/";
            }
        });

        buttonRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!crunchifyTextView.getText().equals("") && crunchifyTakeRoot == false && !crunchifyTextView.getText().equals("∞")){
                    operation += "v";
                    mValueOne = Float.parseFloat(crunchifyTextView.getText() + "");
                    crunchifyTakeRoot = true;
                }
                else operation += "v";
            }
        });

        buttonSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!crunchifyTextView.getText().equals("") && crunchifySin == false && !crunchifyTextView.getText().equals("∞")){
                    operation += "s";
                    mValueOne = Float.parseFloat(crunchifyTextView.getText() + "");
                    crunchifyExponentiation = true;
                }
                else operation += "s";
            }
        });

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

            buttonsin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!crunchifyTextView.getText().equals("") && crunchifySin == false && !crunchifyTextView.getText().equals("∞")){
                        operation += "i";
                        mValueOne = Float.parseFloat(crunchifyTextView.getText() + "");
                        crunchifySin = true;
                    }
                    else operation += "i";
                }
            });

            buttoncos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!crunchifyTextView.getText().equals("") && crunchifyCos == false && !crunchifyTextView.getText().equals("∞")){
                        operation += "c";
                        mValueOne = Float.parseFloat(crunchifyTextView.getText() + "");
                        crunchifyCos = true;
                    }
                    else operation += "c";
                }
            });

            buttontan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!crunchifyTextView.getText().equals("") && crunchifyTan == false && !crunchifyTextView.getText().equals("∞")){
                        operation += "t";
                        mValueOne = Float.parseFloat(crunchifyTextView.getText() + "");
                        crunchifyTan = true;
                    }
                    else operation += "t";
                }
            });

            buttonln.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!crunchifyTextView.getText().equals("") && crunchifyLn == false && !crunchifyTextView.getText().equals("∞")){
                        operation += "d";
                        mValueOne = Float.parseFloat(crunchifyTextView.getText() + "");
                        crunchifyLn = true;
                    }
                    else operation += "d";
                }
            });

            buttonlog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!crunchifyTextView.getText().equals("") && crunchifyLog == false && !crunchifyTextView.getText().equals("∞")){
                        operation += "l";
                        mValueOne = Float.parseFloat(crunchifyTextView.getText() + "");
                        crunchifyLog = true;
                    }
                    else operation += "l";
                }
            });

            buttonPercent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!crunchifyTextView.getText().equals("") && crunchifyPercent == false && !crunchifyTextView.getText().equals("∞")){
                        operation += "%";
                        mValueOne = Float.parseFloat(crunchifyTextView.getText() + "");
                        crunchifyPercent = true;
                        crunchifyTextView.setText(null);
                    }
                    else operation += "%";
                }
            });

            buttonnthRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!crunchifyTextView.getText().equals("") && crunchifyNthRoot == false && !crunchifyTextView.getText().equals("∞")){
                        operation += "n";
                        mValueOne = Float.parseFloat(crunchifyTextView.getText() + "");
                        crunchifyNthRoot = true;
                        crunchifyTextView.setText(null);
                    }
                    else operation += "n";
                }
            });

            buttonnthSquare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!crunchifyTextView.getText().equals("") && crunchifyNthSquare == false && !crunchifyTextView.getText().equals("∞")){
                        operation += "a";
                        mValueOne = Float.parseFloat(crunchifyTextView.getText() + "");
                        crunchifyNthSquare = true;
                        crunchifyTextView.setText(null);
                    }
                    else operation += "a";
                }
            });

            buttonFact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!crunchifyTextView.getText().equals("") && crunchifyFactorial == false && !crunchifyTextView.getText().equals("∞")){
                        operation += "!";
                        mValueOne = Float.parseFloat(crunchifyTextView.getText() + "");
                        crunchifyFactorial = true;
                    }
                    else operation += "!";
                }
            });
        }

        buttonEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = crunchifyTextView.getText().toString();
                if (!a.isEmpty() && !a.equals("error") && !a.equals("∞")){
                    mValueTwo = Float.parseFloat(a);

                    if (operation.charAt(operation.length() - 1) == '+') {
                        crunchifyTextView.setText(nf.format(mValueOne + mValueTwo));
                        crunchifyAddition = false;
                    }

                    if (operation.charAt(operation.length() - 1) == '-') {
                        crunchifyTextView.setText(nf.format(mValueOne - mValueTwo));
                        mSubtract = false;
                    }

                    if (operation.charAt(operation.length() - 1) == '*') {
                        crunchifyTextView.setText(nf.format(mValueOne * mValueTwo));
                        crunchifyMultiplication = false;
                    }

                    if (operation.charAt(operation.length() - 1) == '/'){
                        if(mValueTwo == 0){
                            crunchifyTextView.setText("error");
                        }
                        else crunchifyTextView.setText(nf.format(mValueOne / mValueTwo));
                        crunchifyDivision = false;
                    }

                    if (operation.charAt(operation.length() - 1) == 'v') {
                        crunchifyTextView.setText(nf.format(Math.sqrt(mValueOne)));
                        crunchifyTakeRoot = false;
                    }

                    if (operation.charAt(operation.length() - 1) == 's') {
                        crunchifyTextView.setText(nf.format(mValueOne * mValueOne));
                        crunchifyExponentiation = false;
                    }

                    if (operation.charAt(operation.length() - 1) == 'i') {
                        crunchifyTextView.setText(nf.format( Math.sin(mValueOne)));
                        crunchifySin = false;
                    }

                    if (operation.charAt(operation.length() - 1) == 'c') {
                        crunchifyTextView.setText(nf.format( Math.cos(mValueOne)));
                        crunchifyCos = false;
                    }

                    if (operation.charAt(operation.length() - 1) == 't') {
                        crunchifyTextView.setText(nf.format( Math.tan(mValueOne)));
                        crunchifyTan = false;
                    }

                    if (operation.charAt(operation.length() - 1) == 'd') {
                        crunchifyTextView.setText(nf.format( Math.log(mValueOne)));
                        crunchifyLn = false;
                    }

                    if (operation.charAt(operation.length() - 1) == 'l') {
                        crunchifyTextView.setText(nf.format( Math.log10(mValueOne)));
                        crunchifyLog = false;
                    }

                    if (operation.charAt(operation.length() - 1) == '%') {
                        crunchifyTextView.setText(nf.format(mValueOne / 100 * mValueTwo));
                        crunchifyPercent = false;
                    }

                    if (operation.charAt(operation.length() - 1) == 'a') {
                        crunchifyTextView.setText(nf.format(Math.pow(mValueOne, mValueTwo)));
                        crunchifyNthSquare = false;
                    }

                    if (operation.charAt(operation.length() - 1) == 'n') {
                        double nthRootValue = nthRoot(mValueOne, mValueTwo);
                        crunchifyTextView.setText(nf.format(Math.round(nthRootValue*1000.0)/1000.0));
                        crunchifyNthRoot = false;
                    }

                    if (operation.charAt(operation.length() - 1) == '!') {
                        crunchifyTextView.setText(nf.format( calculateFactorial(mValueOne) ));
                        crunchifyFactorial = false;
                    }
                }
                return;
            }
        });

        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!crunchifyTextView.getText().equals("") && !crunchifyTextView.getText().equals("error")){
                    String s = crunchifyTextView.getText().toString();
                    s = s.substring(0, s.length()-1);
                    crunchifyTextView.setText(s);
                }
                else crunchifyTextView.setText("");
            }
        });

        buttonAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!crunchifyTextView.getText().equals("")){
                    crunchifyTextView.setText("");
                }
            }
        });

        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!crunchifyTextView.getText().toString().contains(".")) {
                    if (crunchifyTextView.getText().equals("")) {
                        crunchifyTextView.setText(crunchifyTextView.getText() + "0.");
                    } else crunchifyTextView.setText(crunchifyTextView.getText() + ".");
                }
            }
        });
    }
}

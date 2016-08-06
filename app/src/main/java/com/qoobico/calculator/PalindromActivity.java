package com.qoobico.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PalindromActivity extends Activity implements View.OnClickListener {

    EditText etNumForPalindrom;
    Button btnCalcPalindrom;
    TextView tvResPalindrom;
    String r = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palindrom);

        etNumForPalindrom = (EditText) findViewById(R.id.etNumForPalindrom);
        btnCalcPalindrom = (Button) findViewById(R.id.btnCalcPalindrom);
        tvResPalindrom = (TextView) findViewById(R.id.tvResPalindrom);
        btnCalcPalindrom.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnCalcPalindrom:
                if (TextUtils.isEmpty(etNumForPalindrom.getText().toString())) {
                    {
                        tvResPalindrom.setText("Enter the number");
                    }
                    return;
                }

                // читаем EditText и заполняем переменные числами
                if(isPalindrome(etNumForPalindrom.getText().toString())) {
                    tvResPalindrom.setText("It's Palindrom");
                }else{
                    tvResPalindrom.setText("It's not Palindrom");
                }
                break;
        }
    }


    public static String reverseString(String s) {
        String r = "";
        for (int i = s.length() - 1; i >= 0; --i)
            r += s.charAt(i);
        return r;


    }

    public boolean isPalindrome(String s) {
        if (s.equals(reverseString(s))) {
            return true;
        }
        return false;

    }
}
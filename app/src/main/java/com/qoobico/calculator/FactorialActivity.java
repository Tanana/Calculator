package com.qoobico.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigInteger;

public class FactorialActivity extends Activity implements View.OnClickListener {

    EditText etNumForFactorial;
    Button btnCalcFactorial;
    TextView tvResFactorial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factorial);

        etNumForFactorial = (EditText) findViewById(R.id.etNumForFactorial);
        btnCalcFactorial = (Button) findViewById(R.id.btnCalcFactorial);
        tvResFactorial = (TextView) findViewById(R.id.tvResFactorial);
        btnCalcFactorial.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        float calculate;
        switch (v.getId()) {
            case R.id.btnCalcFactorial:
                if (TextUtils.isEmpty(etNumForFactorial.getText().toString())) {
                    tvResFactorial.setText("Enter the number");
                }else {
                    tvResFactorial.setText(" = " +summOfFactorialDigits(etNumForFactorial.getText().toString()));
                }

        }
    }
    public String summOfFactorialDigits(String input) {
        if(!input.isEmpty() && input!=null) {
            int max = Integer.valueOf(input);
            BigInteger sum = BigInteger.valueOf(0);
            BigInteger result = BigInteger.valueOf(1);
            BigInteger currentNum = BigInteger.valueOf(0);

            for (long i = 1; i <= max; i++) {
                result = result.multiply(BigInteger.valueOf(i));
            }

            while (!result.equals(BigInteger.ZERO)) {
                sum = sum.add(result.mod(BigInteger.valueOf(10)));
                result = result.divide(BigInteger.valueOf(10));

            }

            return sum.toString();
        } else{
            return null;
        }
    }

}

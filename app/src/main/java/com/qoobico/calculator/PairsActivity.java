package com.qoobico.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qoobico.calculator.pairs.CalculatePairs;

public class PairsActivity extends Activity implements View.OnClickListener {

    EditText etNumForPairs;
    Button btnCalcPairs;
    TextView tvResPairs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairs);

        etNumForPairs = (EditText) findViewById(R.id.etNumForPairs);
        btnCalcPairs = (Button) findViewById(R.id.btnCalcPairs);
        tvResPairs = (TextView) findViewById(R.id.tvResPairs);
        btnCalcPairs.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnCalcPairs:
                tvResPairs.setText(CalculatePairs.getPairs(etNumForPairs.getText().toString()));
                break;


        }


    }
}
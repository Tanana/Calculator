package com.qoobico.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistrActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etLoginName, etPass, etEmail;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registr);

        etLoginName = (EditText) findViewById(R.id.etLoginName);
        etPass = (EditText) findViewById(R.id.etPass);
        etEmail = (EditText) findViewById(R.id.etEmail);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                break;
        }
    }
}


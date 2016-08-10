package com.qoobico.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogoutActivity extends AppCompatActivity implements View.OnClickListener{
    EditText etLoginName, etPass;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        etLoginName = (EditText) findViewById(R.id.etLoginName);
        etPass = (EditText) findViewById(R.id.etPass);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogout:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}

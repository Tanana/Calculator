package com.qoobico.calculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnLog;
    EditText etUsername, etPass;
    TextView tvRegisterLink;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        etUsername = (EditText)findViewById(R.id.etUsername);
        etPass = (EditText)findViewById(R.id.etPass);
        btnLog = (Button) findViewById(R.id.btnLog);
        tvRegisterLink = (TextView)findViewById(R.id.tvRegisterLink);

        btnLog.setOnClickListener(this);
        tvRegisterLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLog:
                break;

            case R.id.tvRegisterLink:
                startActivity(new Intent(this, RegistrActivity.class));

                break;

        }
    }
}


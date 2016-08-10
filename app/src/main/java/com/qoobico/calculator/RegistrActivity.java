package com.qoobico.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qoobico.calculator.db.DbHelper;
import com.qoobico.calculator.db.DbOperations;
import com.qoobico.calculator.db.UserModel;

public class RegistrActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etLoginName, etPass, etEmail;
    Button btnRegister;
    DbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registr);

        etLoginName = (EditText) findViewById(R.id.etLoginName);
        etPass = (EditText) findViewById(R.id.etPass);
        etEmail = (EditText) findViewById(R.id.etEmail);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(this);
        dbHelper = new DbHelper(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                UserModel user= new UserModel();
                user.setLogin(etLoginName.getText().toString());
                user.setPassword(etPass.getText().toString());
                user.setEmail(etEmail.getText().toString());
                if(DbOperations.addUser(user,dbHelper)!=0){
                    etLoginName.setText("");
                    etPass.setText("");
                    etEmail.setText("");
                   finish();
                    Toast.makeText(this,"Registration successfuly",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Enter correct information", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}


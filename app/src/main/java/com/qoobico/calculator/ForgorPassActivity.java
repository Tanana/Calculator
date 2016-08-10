package com.qoobico.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qoobico.calculator.db.DbHelper;
import com.qoobico.calculator.db.DbOperations;
import com.qoobico.calculator.db.UserModel;

public class ForgorPassActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail;
    Button btnSend;
    DbHelper dbHelper;
    TextView tvForgotPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgor_pass);
        btnSend=(Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        etEmail = (EditText) findViewById(R.id.etEmail);
        tvForgotPass=(TextView)findViewById(R.id.tvForgotPass);
        dbHelper=new DbHelper(this);
    }

    @Override
    public void onClick(View view) {
switch (view.getId()){
    case R.id.btnSend:
        UserModel user = new UserModel();
        user.setEmail(etEmail.getText().toString());
        String password=DbOperations.checkEmail(user,dbHelper);
        if(!password.equals("")){
        tvForgotPass.setText("Your password is: "+password);
        } else
        {
            tvForgotPass.setText("Invaild email");
        }
        break;


}


    }
}

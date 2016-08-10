package com.qoobico.calculator;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.twitter.sdk.android.Twitter;

public class CalculatorActivity extends TabActivity implements View.OnClickListener{
Button buttonLogOut;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        // получаем TabHost
        TabHost tabHost = getTabHost();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Palindrom");
        tabSpec.setContent(new Intent(this, PalindromActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Factorial");
        tabSpec.setContent(new Intent(this, FactorialActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator("Pairs");
        tabSpec.setContent(new Intent(this, PairsActivity.class));
        tabHost.addTab(tabSpec);

        buttonLogOut=(Button)findViewById(R.id.qwer);
        buttonLogOut.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.qwer:
                LoginManager.getInstance().logOut();
                CookieSyncManager.createInstance(this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeSessionCookie();
                Twitter.getSessionManager().clearActiveSession();
                Twitter.logOut();
                try {
                    Auth.GoogleSignInApi.signOut(MainActivity.mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {

                                }
                            });
                }catch(IllegalStateException e){

                }
                startActivity(new Intent(this, MainActivity.class));

                Toast.makeText(this,"Loged out successfuly!", Toast.LENGTH_SHORT);
                break;
        }
    }
}
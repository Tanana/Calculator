package com.qoobico.calculator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.qoobico.calculator.db.DbHelper;
import com.qoobico.calculator.db.DbOperations;
import com.qoobico.calculator.db.UserModel;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    //regist
    Button btnLog;
    EditText etUsername, etPass;
    TextView tvRegisterLink,tvForgPass;

    //integr twitt
    TwitterLoginButton loginButton;
    private static final String CONSUMER_KEY="MqagSjWP0fy88NTshO94YDWMV";
    private static final String CONSUMER_SECRET="lPfzkrm1fOR4cmRWZZxT7kUaphbzqtZtSaY4gWZqvb8razDmiu";
    private TwitterAuthClient client;

    //integr google+
    TextView tv_username;
   static GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    SignInButton sign_in_button;
    ImageButton custom_goog_btn;
    DbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        etUsername = (EditText) findViewById(R.id.etLoginName);
        etPass = (EditText) findViewById(R.id.etPass);
        btnLog = (Button) findViewById(R.id.btnLog);
        tvForgPass = (TextView) findViewById(R.id.tvForgPass);
        tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);
        btnLog.setOnClickListener(this);
        tvRegisterLink.setOnClickListener(this);
        tvForgPass.setOnClickListener(this);

        dbHelper = new DbHelper(this);
//twitter
        TwitterAuthConfig authConfig = new TwitterAuthConfig(this.CONSUMER_KEY, this.CONSUMER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        client = new TwitterAuthClient();

        ImageButton customLoginButton = (ImageButton) findViewById(R.id.twitter_custom_button);
        customLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                client.authorize(MainActivity.this, new Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> twitterSessionResult) {
                        Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, CalculatorActivity.class));

                    }

                    @Override
                    public void failure(TwitterException e) {
                        Toast.makeText(MainActivity.this, "Failure! Install Twitter on your phone. ", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });


//google+

        custom_goog_btn = (ImageButton) findViewById(R.id.custom_goog_btn);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(AppIndex.API).build();

        custom_goog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //tw
        client.onActivityResult(requestCode, resultCode, data);


        //g+
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnLog:
                UserModel user = new UserModel();
                user.setLogin(etUsername.getText().toString());
                user.setPassword(etPass.getText().toString());
                if(DbOperations.checkUserForLogin(user,dbHelper)){
                    startActivity(new Intent(this, CalculatorActivity.class));
                    Toast.makeText(this, "Welcome",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Incorrect login or password", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tvRegisterLink:
                startActivity(new Intent(this, RegistrActivity.class));

                break;


         case R.id.custom_goog_btn:
                    signIn();
               //     signOut();

                break;
            case R.id.tvForgPass:
                startActivity(new Intent(this, ForgorPassActivity.class));

                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
Toast.makeText(this,"Check your internet connection",Toast.LENGTH_SHORT).show();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        tv_username.setText("");
                    }
                });

    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            System.out.println(acct.getFamilyName());
            System.out.println(acct.getEmail());
            startActivity(new Intent(MainActivity.this, CalculatorActivity.class));

        } else {

        }
    }


    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Main Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.qoobico.calculator/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Main Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.qoobico.calculator/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
        mGoogleApiClient.disconnect();
    }
}



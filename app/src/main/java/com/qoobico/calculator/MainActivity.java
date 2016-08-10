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
    private boolean isAuthenticated = false;
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


                    }

                    @Override
                    public void failure(TwitterException e) {
                        Toast.makeText(MainActivity.this, "failure", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });


//google+
        //   sign_in_button = (SignInButton) findViewById(R.id.sign_in_button);
        //    sign_in_button.setOnClickListener(this);
        //   tv_username = (TextView) findViewById(R.id.tv_username);


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
                startActivity(new Intent(MainActivity.this, CalculatorActivity.class));
            }
        });
    }


   /*     GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(AppIndex.API).build();
        setGooglePlusButtonText(this.sign_in_button, "Sign in");
    }
 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //tw
        client.onActivityResult(requestCode, resultCode, data);
        startActivity(new Intent(this, CalculatorActivity.class));

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
                if (!isAuthenticated) {
                    signIn();
                    //this.swign_in_button.setVisibility(View.INVISIBLE);
             //       setGooglePlusButtonText(this.custom_goog_btn, "Sign out");
                startActivity(new Intent(this, CalculatorActivity.class));

                    isAuthenticated = true;
                } else {
                    signOut();
            //        setGooglePlusButtonText(this.custom_goog_btn, "Sign in");
                    isAuthenticated = false;
                }

                break;
            case R.id.tvForgPass:
                startActivity(new Intent(this, ForgorPassActivity.class));

                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Toast.makeText(this,"You loged in with google+",Toast.LENGTH_SHORT).show();
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        tv_username.setText("");
                    }
                });
        Toast.makeText(this,"You have loged out from google+",Toast.LENGTH_SHORT).show();
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
//            tv_username.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));

        } else {
            // Signed out, show unauthenticated UI.
            // updateUI(false);
        }
    }


    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
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
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.qoobico.calculator/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.qoobico.calculator/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
        mGoogleApiClient.disconnect();
    }
}



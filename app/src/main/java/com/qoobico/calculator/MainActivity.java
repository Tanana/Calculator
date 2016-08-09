package com.qoobico.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.twitter.sdk.android.Twitter;
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
    TextView tvRegisterLink;

    //integr twitt
    TwitterLoginButton loginButton;
    private static final String CONSUMER_KEY="MqagSjWP0fy88NTshO94YDWMV";
    private static final String CONSUMER_SECRET="lPfzkrm1fOR4cmRWZZxT7kUaphbzqtZtSaY4gWZqvb8razDmiu";
    TwitterAuthClient mTwitterAuthClient;

    //integr google+
    TextView tv_username;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private boolean isAuthenticated = false;
    SignInButton sign_in_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPass = (EditText) findViewById(R.id.etPass);
        btnLog = (Button) findViewById(R.id.btnLog);
        tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);
        btnLog.setOnClickListener(this);
        tvRegisterLink.setOnClickListener(this);




//twitter
        if (!Fabric.isInitialized()) {
            TwitterAuthConfig authConfig = new TwitterAuthConfig(this.CONSUMER_KEY, this.CONSUMER_SECRET);
            Fabric.with(this, new Twitter(authConfig));
        }

     /*   loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();


            }


            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

*/
        TwitterAuthClient mTwitterAuthClient= new TwitterAuthClient();

        ImageButton twitter_custom_button = (ImageButton) findViewById(R.id.twitter_custom_button);


        //google+
        sign_in_button = (SignInButton) findViewById(R.id.sign_in_button);
        sign_in_button.setOnClickListener(this);
        tv_username = (TextView) findViewById(R.id.tv_username);
        // Button click listeners


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        setGooglePlusButtonText(this.sign_in_button, "Sign in");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        loginButton.onActivityResult(requestCode, resultCode, data);



        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLog:
                break;

            case R.id.tvRegisterLink:
                startActivity(new Intent(this, RegistrActivity.class));

                break;


            case R.id.sign_in_button:
                if (!isAuthenticated) {
                    signIn();
                    //this.sign_in_button.setVisibility(View.INVISIBLE);
                    setGooglePlusButtonText(this.sign_in_button, "Sign out");
                    isAuthenticated = true;
                } else {
                    signOut();
                    setGooglePlusButtonText(this.sign_in_button, "Sign in");
                    isAuthenticated = false;
                }

                break;
            case R.id.twitter_custom_button:
                mTwitterAuthClient.authorize(this, new com.twitter.sdk.android.core.Callback<TwitterSession>() {

                    @Override
                    public void success(Result<TwitterSession> twitterSessionResult) {
                        // Success
                    }

                    @Override
                    public void failure(TwitterException e) {
                        e.printStackTrace();
                    }
                });
                break;

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
            tv_username.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));

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
}



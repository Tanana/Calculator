package com.qoobico.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class FacebookFragment extends Fragment {
 //   private TextView mTextDetails;
    private CallbackManager mcallbackManager;


    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
      //      displayWelcomeMassage(profile);

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };


    public FacebookFragment () {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mcallbackManager = CallbackManager.Factory.create();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_facebook, container, false);
    }

/* private void displayWelcomeMassage(Profile profile){
        if (profile != null){
            mTextDetails.setText("Welcome " + profile.getName());
        }

    } */

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

      //  ImageButton mFacebook = (ImageButton) view.findViewById(R.id.custom_fb_button);
  //   LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);

       ImageButton mFacebook = (ImageButton) view.findViewById(R.id.custom_fb_button);
        final LoginButton login_button = (LoginButton) view.findViewById(R.id.login_button);

      mFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
                login_button.performClick();
           }
      });




        login_button.setReadPermissions("user_friends");
        login_button.setFragment(this);
        login_button.registerCallback(mcallbackManager,mCallback);
      //  mTextDetails = (TextView)view.findViewById(R.id.text_details);

    }


    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
   //     displayWelcomeMassage(profile);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mcallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
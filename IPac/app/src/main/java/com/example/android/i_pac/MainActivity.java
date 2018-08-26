package com.example.android.i_pac;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.widget.ShareDialog;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    Button share, details;
    private ShareDialog shareDialog;
    private LoginButton login;
    ImageView profileImage;
    ProfilePictureView profile;
    Dialog details_dialog;
    TextView details_txt;
    private TwitterLoginButton twitterLoginButton;
    private String facebook_id, f_name, m_name, l_name, gender, profile_image, full_name, email_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Twitter.initialize(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        login = findViewById(R.id.login_button);
        shareDialog = new ShareDialog(this);
        login.setReadPermissions("public_profile email");

        if (AccessToken.getCurrentAccessToken() != null) {
            RequestData();

        }

            login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccessToken.getCurrentAccessToken();
            }
        });
        login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                facebook_id = f_name = m_name = l_name = gender = profile_image = full_name = email_id = "";

                if (AccessToken.getCurrentAccessToken() != null) {
                    RequestData();
                    Profile profile = Profile.getCurrentProfile();
                    if (profile != null) {
                        facebook_id = profile.getId();
                        Log.e("facebook_id", facebook_id);
                        f_name = profile.getFirstName();
                        Log.e("f_name", f_name);
                        m_name = profile.getMiddleName();
                        Log.e("m_name", m_name);
                        l_name = profile.getLastName();
                        Log.e("l_name", l_name);
                        full_name = profile.getName();
                        Log.e("full_name", full_name);
                        profile_image = profile.getProfilePictureUri(400, 400).toString();

                    }
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {
            }
        });

    }

    private void RequestData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                JSONObject json = response.getJSONObject();
                Intent i = new Intent(MainActivity.this, SharePage.class);
                i.putExtra("JsonData", json.toString());
                startActivity(i);
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
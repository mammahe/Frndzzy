package com.example.android.i_pac;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SharePage extends AppCompatActivity {
    private static int count = 0;
    private CircleImageView profile_image;
    private TextView name;
    private TextView email;
    private TextView score;
    private CallbackManager callbackManager;
    private AppCompatButton buttonAwesome;
    private AppCompatButton buttonAwesome1;
    private int id;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_page);
        Objects.requireNonNull(getSupportActionBar()).hide();
        FacebookSdk.sdkInitialize(getApplicationContext());

        profile_image = findViewById(R.id.profile_image);
        buttonAwesome = findViewById(R.id.buttonAwesome);
        score = findViewById(R.id.score);
        if (count == 0 || count < 0) {
            score.setText("Score: 0");
        } else {
            score.setText("Score: " + count);
        }
        buttonAwesome1 = findViewById(R.id.buttonAwesome1);
        buttonAwesome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id=1;
                shareData("https://www.hindustantimes.com/india-news/jagan-reddy-eyes-time-tested-path-to-power-in-andhra-pradesh-will-walk-3-000-km-in-6-months/story-41e8MnpE1aYXFdaV6hAQpL.html");

            }
        });
        buttonAwesome1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id=2;
                shareData("https://www.thequint.com/news/politics/how-congress-campaign-won-punjab-election");

            }
        });
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        try {
            JSONObject jsonObj = new JSONObject(getIntent().getStringExtra("JsonData"));
            try {
                String text = "<b>Name :</b> " + jsonObj.getString("name") + "<br><br><b>Email :</b> " + jsonObj.getString("email");
                String url = null;
                try {
                    url = String.valueOf(new URL("https://graph.facebook.com/" + jsonObj.getString("id") + "/picture?type=large"));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                email.setText(jsonObj.getString("email"));
                name.setText(jsonObj.getString("name"));
                Picasso.with(SharePage.this).load(url).into(profile_image);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void shareData(final String s) {

        callbackManager = CallbackManager.Factory.create();
        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                count++;
                score.setText("Score: " + count);
                Toast.makeText(SharePage.this,"Task Succesfully completed",Toast.LENGTH_LONG).show();
                if(id==1){
                    buttonAwesome.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_done_all_white_18dp),null,null,null);
                    id=0;
                }else if(id==2){
                    buttonAwesome1.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_done_all_white_18dp),null,null,null);
                    id=0;
                }else{
                    buttonAwesome1.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                    id=0;
                }
            }

            @Override
            public void onCancel() {
                Log.d(this.getClass().getSimpleName(), "sharing cancelled");
                Toast.makeText(SharePage.this,"You havent shared the post",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(this.getClass().getSimpleName(), "sharing error");
                Toast.makeText(SharePage.this,"You havent shared the post",Toast.LENGTH_LONG).show();

            }
        });

        if (ShareDialog.canShow(ShareLinkContent.class)) {

            ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()

                    .setContentUrl(Uri.parse(s))
                    .build();

            shareDialog.show(shareLinkContent, ShareDialog.Mode.AUTOMATIC);

        }

    }

}

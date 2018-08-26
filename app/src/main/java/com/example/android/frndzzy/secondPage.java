package com.example.android.frndzzy;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;

import java.util.Objects;

public class secondPage extends AppCompatActivity {
    private DBHelper dbHelper;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        dbHelper=new DBHelper(this);
        Intent i = getIntent();
        String name = i.getStringExtra("name");
        getSupportActionBar().setTitle(name);
        int image = i.getIntExtra("image",
                0);
        final int position=i.getIntExtra("position",0);
        ImageView imageView = findViewById(R.id.image);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        int j=dbHelper.getRating(position+1);
       ratingBar.setRating((float)j);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                boolean rowInserted = dbHelper.insertRating((int) rating,position+1);
                if(rowInserted){
                    Log.d("inserted","inserted");
                }

            }
        });
        imageView.setImageResource(image);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

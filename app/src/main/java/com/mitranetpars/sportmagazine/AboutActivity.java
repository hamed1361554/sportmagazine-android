package com.mitranetpars.sportmagazine;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ImageView imageView = (ImageView) findViewById(R.id.aboutus_imageView_logo);
        Picasso.with(getApplicationContext()).load(R.drawable.logo720).into(imageView);
    }
}

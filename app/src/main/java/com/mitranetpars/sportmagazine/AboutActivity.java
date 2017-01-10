package com.mitranetpars.sportmagazine;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.default_color))));
        }

        ImageView imageView = (ImageView) findViewById(R.id.aboutus_imageView_logo);
        Picasso.with(this).load(R.drawable.about_us_700).into(imageView);

        TextView linkTextView = (TextView) findViewById(R.id.http_www_sportmagazine_ir_link);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == android.R.id.home){
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

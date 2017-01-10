package com.mitranetpars.sportmagazine;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class EulaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eula);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.default_color))));
        }
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

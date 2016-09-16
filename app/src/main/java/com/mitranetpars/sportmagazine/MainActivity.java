package com.mitranetpars.sportmagazine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton_onClick(v);
            }
        });

        findViewById(R.id.signinButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signinButton_onCLick(v);
            }
        });
    }

    private void loginButton_onClick(View v) {

    }

    private void signinButton_onCLick(View v) {

    }
}

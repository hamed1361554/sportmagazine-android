package com.mitranetpars.sportmagazine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.mitranetpars.sportmagazine.services.SecurityServicesI;


public class MainActivity extends AppCompatActivity {
    private RadioButton consumerRadioButton;
    private RadioButton producerRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        consumerRadioButton = (RadioButton) findViewById(R.id.consumerRadioButton);
        producerRadioButton = (RadioButton) findViewById(R.id.producerRadioButton);

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

        consumerRadioButton.setChecked(true);
        producerRadioButton.setChecked(false);
    }

    private void loginButton_onClick(View v) {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(loginIntent);
    }

    private void signinButton_onCLick(View v) {
        if (consumerRadioButton.isChecked()) {
            Intent signinIntent = new Intent(MainActivity.this, SigninActivity.class);
            MainActivity.this.startActivity(signinIntent);
        }
    }
}

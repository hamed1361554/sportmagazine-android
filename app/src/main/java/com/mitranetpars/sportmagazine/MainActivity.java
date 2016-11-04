package com.mitranetpars.sportmagazine;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mitranetpars.sportmagazine.common.SecurityEnvironment;
import com.mitranetpars.sportmagazine.common.dto.security.User;

import at.markushi.ui.CircleButton;


public class MainActivity extends AppCompatActivity {
    private RadioButton consumerRadioButton;
    private RadioButton producerRadioButton;

    private Button loginButton;
    private Button signinButton;
    private CircleButton exitButton;
    private CircleButton homeButton;

    private TextView welcomeTextView;

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.consumerRadioButton = (RadioButton) findViewById(R.id.consumerRadioButton);
        this.producerRadioButton = (RadioButton) findViewById(R.id.producerRadioButton);
        this.consumerRadioButton.setChecked(true);
        this.producerRadioButton.setChecked(false);

        this.loginButton = (Button) findViewById(R.id.loginButton);
        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton_onClick(v);
            }
        });

        this.signinButton = (Button) findViewById(R.id.signinButton);
        this.signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signinButton_onCLick(v);
            }
        });

        this.exitButton = (CircleButton) findViewById(R.id.exitbutton);
        this.exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitButton_onClick(v);
            }
        });

        this.homeButton = (CircleButton) findViewById(R.id.homebutton);
        this.homeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                homeButton_onClick(v);
            }
        });

        this.welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (SecurityEnvironment.<SecurityEnvironment>getInstance().getLoginTicket() != null &&
                SecurityEnvironment.<SecurityEnvironment>getInstance().getLoginTicket() != "") {
            this.loginButton.setVisibility(View.INVISIBLE);
            this.signinButton.setVisibility(View.INVISIBLE);
            this.consumerRadioButton.setVisibility(View.INVISIBLE);
            this.producerRadioButton.setVisibility(View.INVISIBLE);

            this.welcomeTextView.setText(getString(R.string.welcometext,
                    SecurityEnvironment.<SecurityEnvironment>getInstance().getUserName()));
            this.welcomeTextView.setVisibility(View.VISIBLE);
            this.exitButton.setVisibility(View.VISIBLE);
            this.homeButton.setVisibility(View.VISIBLE);
        } else {
            this.loginButton.setVisibility(View.VISIBLE);
            this.signinButton.setVisibility(View.VISIBLE);
            this.consumerRadioButton.setVisibility(View.VISIBLE);
            this.producerRadioButton.setVisibility(View.VISIBLE);

            this.welcomeTextView.setText("");
            this.welcomeTextView.setVisibility(View.INVISIBLE);
            this.exitButton.setVisibility(View.INVISIBLE);
            this.homeButton.setVisibility(View.INVISIBLE);
        }
    }

    private void homeButton_onClick(View v) {
        if (SecurityEnvironment.<SecurityEnvironment>getInstance().getLoginTicket() != null &&
                SecurityEnvironment.<SecurityEnvironment>getInstance().getLoginTicket() != "") {
            if (SecurityEnvironment.<SecurityEnvironment>getInstance().getUser().getProductionType() == User.CONSUMER) {
                Intent mainIntent = new Intent(MainActivity.this, ConsumerMainActivity.class);
                MainActivity.this.startActivity(mainIntent);
            } else {
                Intent mainIntent = new Intent(MainActivity.this, ProducerMainActivity.class);
                MainActivity.this.startActivity(mainIntent);
            }
        }
    }

    private void exitButton_onClick(View v) {
        finish();
        System.exit(0);
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

        if (producerRadioButton.isChecked()){
            Intent signinIntent = new Intent(MainActivity.this, ProducerSigninActivity.class);
            MainActivity.this.startActivity(signinIntent);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();

            finish();
            System.exit(0);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.click_back_again_to_eit, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}

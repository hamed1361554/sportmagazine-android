package com.mitranetpars.sportmagazine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mitranetpars.sportmagazine.services.SecurityServicesI;


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
        try
        {
            String ticket = SecurityServicesI.getInstance().login("hamed", "123");
            Toast.makeText(getApplicationContext(), getString(R.string.LoginSuccessful), Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.LoginFailed, ex.getMessage()), Toast.LENGTH_LONG).show();
        }
    }

    private void signinButton_onCLick(View v) {

    }
}

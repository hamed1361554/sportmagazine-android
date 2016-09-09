package com.mitranetpars.sportmagazine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
    }

    public void loginButton_onClick(View v) {
        Toast.makeText(MainActivity.this, "@string/login", Toast.LENGTH_LONG).show();
    }
}

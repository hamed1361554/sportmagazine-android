package com.mitranetpars.sportmagazine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mitranetpars.sportmagazine.common.dto.security.User;
import com.mitranetpars.sportmagazine.services.SecurityServicesI;

public class SigninActivity extends AppCompatActivity {
    private Button signinButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        this.signinButton = (Button) findViewById(R.id.btn_signup);

        this.signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });

        TextView loginLink = (TextView) findViewById(R.id.link_login);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    private void signin() {
        if (!validate()) {
            onSignupFailed();
            return;
        }

        this.signinButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SigninActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.creating_account));
        progressDialog.show();

        EditText userNameEditText = (EditText)findViewById(R.id.input_user_name);
        String userName = userNameEditText.getText().toString();
        EditText fullNameEditText = (EditText)findViewById(R.id.input_full_name);
        String fullName = fullNameEditText.getText().toString();
        EditText addressEditText = (EditText)findViewById(R.id.input_address);
        String address = addressEditText.getText().toString();
        EditText emailEditText = (EditText)findViewById(R.id.input_email);
        String email = emailEditText.getText().toString();
        EditText mobileEditText = (EditText)findViewById(R.id.input_mobile);
        String mobile = mobileEditText.getText().toString();
        EditText passwordEditText = (EditText)findViewById(R.id.input_password);
        String password = passwordEditText.getText().toString();

        try {
            SecurityServicesI.getInstance().create(userName, password, fullName, email, mobile, address);
            Toast.makeText(getApplicationContext(), getString(R.string.CreationSuccessful), Toast.LENGTH_LONG).show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onSignupSuccess or onSignupFailed
                            // depending on success
                            onSignupSuccess();
                            // onSignupFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        }
        catch (Exception ex) {
            Toast.makeText(getApplicationContext(), getString(R.string.CreationFailed, ex.getMessage()), Toast.LENGTH_LONG).show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onSignupSuccess or onSignupFailed
                            // depending on success
                            // onSignupSuccess();
                            onSignupFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        }
    }


    public void onSignupSuccess() {
        this.signinButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        this.signinButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        EditText userNameEditText = (EditText)findViewById(R.id.input_user_name);
        String userName = userNameEditText.getText().toString();
        EditText fullNameEditText = (EditText)findViewById(R.id.input_full_name);
        String fullName = fullNameEditText.getText().toString();
        EditText addressEditText = (EditText)findViewById(R.id.input_address);
        String address = addressEditText.getText().toString();
        EditText emailEditText = (EditText)findViewById(R.id.input_email);
        String email = emailEditText.getText().toString();
        EditText mobileEditText = (EditText)findViewById(R.id.input_mobile);
        String mobile = mobileEditText.getText().toString();
        EditText passwordEditText = (EditText)findViewById(R.id.input_password);
        String password = passwordEditText.getText().toString();
        EditText reEnterPasswordEditText = (EditText)findViewById(R.id.input_reEnterPassword);
        String reEnterPassword = reEnterPasswordEditText.getText().toString();

        if (userName.isEmpty() || userName.length() < 3) {
            userNameEditText.setError(getString(R.string.at_least_3_chars));
            valid = false;
        } else {
            userNameEditText.setError(null);
        }

        if (fullName.isEmpty() || fullName.length() < 3) {
            fullNameEditText.setError(getString(R.string.at_least_3_chars));
            valid = false;
        } else {
            fullNameEditText.setError(null);
        }

        if (address.isEmpty()) {
            addressEditText.setError(getString(R.string.invalid_address));
            valid = false;
        } else {
            addressEditText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError(getString(R.string.invalid_email));
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            mobileEditText.setError(getString(R.string.invalid_mobile));
            valid = false;
        } else {
            mobileEditText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordEditText.setError(getString(R.string.invalid_signin_password));
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            reEnterPasswordEditText.setError(getString(R.string.invalid_signin_reenter_password));
            valid = false;
        } else {
            reEnterPasswordEditText.setError(null);
        }

        return valid;
    }
}

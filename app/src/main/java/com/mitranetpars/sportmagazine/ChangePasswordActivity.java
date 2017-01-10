package com.mitranetpars.sportmagazine;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mitranetpars.sportmagazine.common.dto.security.ChangeUserPasswordData;
import com.mitranetpars.sportmagazine.services.SecurityServicesI;

import org.w3c.dom.Text;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText userNameEditText;
    private Button securityCodeButton;
    private EditText passwordEditText;
    private EditText passwordRetypeEditText;
    private EditText securityCodeEditText;
    private Button changePasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.default_color))));
        }

        this.userNameEditText = (EditText) findViewById(R.id.change_password_username);
        this.securityCodeButton = (Button) findViewById(R.id.change_password_code_button);
        this.passwordEditText = (EditText) findViewById(R.id.change_password_password);
        this.passwordRetypeEditText = (EditText) findViewById(R.id.change_password_retype_password);
        this.securityCodeEditText = (EditText) findViewById(R.id.change_password_security_code);
        this.changePasswordButton = (Button) findViewById(R.id.change_password_button);

        this.securityCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSecurityCode();
            }
        });

        this.changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private boolean validateUserName(){
        boolean valid = true;

        String userName = userNameEditText.getText().toString();

        if (userName.isEmpty() || userName.length() < 3) {
            userNameEditText.setError(getString(R.string.at_least_3_chars));
            valid = false;
        } else {
            userNameEditText.setError(null);
        }

        return valid;
    }

    private boolean validateChangePassword(){
        if (!this.validateUserName()){
            return false;
        }

        boolean valid = true;

        String password = passwordEditText.getText().toString();
        String reEnterPassword = passwordRetypeEditText.getText().toString();
        String securityCode = securityCodeEditText.getText().toString();

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordEditText.setError(getString(R.string.invalid_signin_password));
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            passwordRetypeEditText.setError(getString(R.string.invalid_signin_reenter_password));
            valid = false;
        } else {
            passwordRetypeEditText.setError(null);
        }

        if (securityCode.isEmpty() || securityCode.length() < 4 || securityCode.length() > 10) {
            securityCodeEditText.setError(getString(R.string.invalid_security_code));
            valid = false;
        } else {
            securityCodeEditText.setError(null);
        }

        return valid;
    }

    private void getSecurityCode(){
        if (!this.validateUserName()){
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(ChangePasswordActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.change_password));
        progressDialog.show();

        try {
            this.changePasswordButton.setEnabled(false);
            this.securityCodeButton.setEnabled(false);

            ChangeUserPasswordData data = new ChangeUserPasswordData();
            data.activation_data = "";
            data.current_password = "";
            data.new_password = "";
            data.user_name = userNameEditText.getText().toString();
            SecurityServicesI.getInstance().changePassword(data);

            Toast.makeText(getApplicationContext(), R.string.security_code_sent, Toast.LENGTH_LONG).show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onSignupSuccess or onSignupFailed
                            // depending on success
                            onChangeSuccess();
                            // onSignupFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        } catch (Exception ex){
            Toast.makeText(getApplicationContext(),getString(R.string.failed_due_error, ex.getMessage()), Toast.LENGTH_LONG).show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onSignupSuccess or onSignupFailed
                            // depending on success
                            // onSignupSuccess();
                            onChangeFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        }
    }

    public void onChangeSuccess() {
        this.changePasswordButton.setEnabled(true);
        this.securityCodeButton.setEnabled(true);
        setResult(RESULT_OK, null);
    }

    public void onChangeFailed() {
        this.changePasswordButton.setEnabled(true);
        this.securityCodeButton.setEnabled(true);
    }

    private void changePassword(){
        if (!this.validateChangePassword()){
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(ChangePasswordActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.change_password));
        progressDialog.show();

        try {
            this.changePasswordButton.setEnabled(false);
            this.securityCodeButton.setEnabled(false);

            ChangeUserPasswordData data = new ChangeUserPasswordData();
            data.activation_data = securityCodeEditText.getText().toString();
            data.current_password = "";
            data.new_password = passwordEditText.getText().toString();
            data.user_name = userNameEditText.getText().toString();
            SecurityServicesI.getInstance().changePassword(data);

            Toast.makeText(getApplicationContext(), R.string.password_changed, Toast.LENGTH_LONG).show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onSignupSuccess or onSignupFailed
                            // depending on success
                            onChangeSuccess();
                            progressDialog.dismiss();
                            finish();
                        }
                    }, 3000);
        } catch (Exception ex){
            Toast.makeText(getApplicationContext(),getString(R.string.failed_due_error, ex.getMessage()), Toast.LENGTH_LONG).show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onSignupSuccess or onSignupFailed
                            // depending on success
                            onChangeFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        }
    }
}

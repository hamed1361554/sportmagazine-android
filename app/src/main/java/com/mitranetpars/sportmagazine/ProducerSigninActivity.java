package com.mitranetpars.sportmagazine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mitranetpars.sportmagazine.services.SecurityServicesI;
import com.mitranetpars.sportmagazine.utils.GlobalUtils;
import com.satsuware.usefulviews.LabelledSpinner;

public class ProducerSigninActivity extends AppCompatActivity {
    private Button signinButton;
    private LabelledSpinner producerDivisionSpinner;
    private EditText producerDivisionEditText;
    private EditText userNameEditText;
    private EditText fullNameEditText;
    private EditText nationalCodeEditText;
    private EditText addressEditText;
    private EditText emailEditText;
    private EditText mobileEditText;
    private EditText phoneEditText;
    private EditText passwordEditText;
    private EditText reEnterPasswordEditText;

    private int productionPackageType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producer_signin);

        this.producerDivisionSpinner = (LabelledSpinner) findViewById(R.id.producer_division_spinner);
        this.producerDivisionSpinner.setItemsArray(getResources().getStringArray(R.array.producer_division_items));

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

        this.signinButton = (Button) findViewById(R.id.btn_producer_signup);
        this.signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });

        this.userNameEditText = (EditText)findViewById(R.id.input_producer_user_name);
        this.fullNameEditText = (EditText)findViewById(R.id.input_producer_full_name);
        this.addressEditText = (EditText)findViewById(R.id.input_producer_address);
        this.emailEditText = (EditText)findViewById(R.id.input_producer_email);
        this.nationalCodeEditText = (EditText)findViewById(R.id.input_producer_national_code);
        this.mobileEditText = (EditText)findViewById(R.id.input_producer_mobile);
        this.phoneEditText = (EditText)findViewById(R.id.input_producer_phone_number);
        this.producerDivisionEditText = (EditText)findViewById(R.id.input_producer_division_name);
        this.passwordEditText = (EditText)findViewById(R.id.input_producer_password);
        this.reEnterPasswordEditText = (EditText)findViewById(R.id.input_producer_reEnterPassword);

        Bundle extraBundle = getIntent().getExtras();
        if (extraBundle != null){
            this.productionPackageType = extraBundle.getInt("package_type", 0);
        } else {
            this.productionPackageType = 0;
        }
    }

    private void signin() {
        if (!validate()) {
            onSignupFailed();
            return;
        }

        this.signinButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ProducerSigninActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.creating_account));
        progressDialog.show();

        String userName = userNameEditText.getText().toString();
        String fullName = fullNameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String mobile = mobileEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String nationalCode = nationalCodeEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        int producerDivision = producerDivisionSpinner.getSpinner().getSelectedItemPosition();
        String producerDivisionName = producerDivisionEditText.getText().toString();

        try {
            SecurityServicesI.getInstance().createProducer(userName, password, fullName, email, mobile,
                    address, phone, nationalCode, producerDivision, producerDivisionName,
                    this.productionPackageType);
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
        Toast.makeText(getBaseContext(), R.string.acvtivation_email_sent, Toast.LENGTH_LONG).show();
        this.signinButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), getString(R.string.signup_failed), Toast.LENGTH_LONG).show();
        this.signinButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String userName = userNameEditText.getText().toString();
        String fullName = fullNameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String nationalCode = nationalCodeEditText.getText().toString();
        String mobile = mobileEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        int producerDivision = producerDivisionSpinner.getSpinner().getSelectedItemPosition();
        String producerDivisionName = producerDivisionEditText.getText().toString();
        String password = passwordEditText.getText().toString();
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

        TextView spinnerTextView = (TextView) producerDivisionSpinner.getSpinner().getSelectedView();
        if (producerDivision == -1) {
            spinnerTextView.setError(getString(R.string.invalid_producer_division));
            valid = false;
        } else {
            spinnerTextView.setError(null);
        }

        if (producerDivisionName.isEmpty()) {
            producerDivisionEditText.setError(getString(R.string.invalid_producer_division_name));
            valid = false;
        } else {
            producerDivisionEditText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError(getString(R.string.invalid_email));
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        if (nationalCode.isEmpty() || !GlobalUtils.validateNationalCode(nationalCode)) {
            nationalCodeEditText.setError(getString(R.string.invalid_national_code));
            valid = false;
        } else {
            nationalCodeEditText.setError(null);
        }

        if (mobile.isEmpty() || !(mobile.length()==10 || (mobile.length() == 11 && mobile.startsWith("0")))) {
            mobileEditText.setError(getString(R.string.invalid_mobile));
            valid = false;
        } else {
            mobileEditText.setError(null);
        }

        if (phone.isEmpty() || !(phone.length()==10 || (phone.length() == 11 && phone.startsWith("0")))) {
            phoneEditText.setError(getString(R.string.invalid_phone));
            valid = false;
        } else {
            phoneEditText.setError(null);
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

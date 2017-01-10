package com.mitranetpars.sportmagazine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mitranetpars.sportmagazine.services.SecurityServicesI;
import com.mitranetpars.sportmagazine.utils.ImageUtils;
import com.mvc.imagepicker.ImagePicker;

public class SigninActivity extends AppCompatActivity {
    private Button signinButton;
    private EditText userNameEditText;
    private EditText fullNameEditText;
    private EditText addressEditText;
    private EditText emailEditText;
    private EditText mobileEditText;
    private EditText passwordEditText;
    private EditText reEnterPasswordEditText;

    private ImageView userImageView;
    private Bitmap userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.default_color))));
        }

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

        this.userNameEditText = (EditText)findViewById(R.id.input_user_name);
        this.fullNameEditText = (EditText)findViewById(R.id.input_full_name);
        this.addressEditText = (EditText)findViewById(R.id.input_address);
        this.emailEditText = (EditText)findViewById(R.id.input_email);
        this.mobileEditText = (EditText)findViewById(R.id.input_mobile);
        this.passwordEditText = (EditText)findViewById(R.id.input_password);
        this.reEnterPasswordEditText = (EditText)findViewById(R.id.input_reEnterPassword);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            this.setTextViewDirection();
        }

        // width and height will be at least 200px long (optional).
        ImagePicker.setMinQuality(200, 200);
        this.userImageView = (ImageView) findViewById(R.id.input_user_image);
        findViewById(R.id.acquire_user_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickImage(v);
            }
        });
    }

    public void onPickImage(View view) {
        // Click on image button
        ImagePicker.pickImage(this, getString(R.string.select_your_image));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            Bitmap gotImage = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
            if (gotImage != null) {
                this.userImage = ImageUtils.compressLogo(gotImage);
                //this.userImage = gotImage;
                this.userImageView.setImageBitmap(this.userImage);
            } else {
                this.userImage = null;
                this.userImageView.setImageBitmap(null);
            }
        }
        catch (Exception error) {
            Toast.makeText(getApplicationContext(), getString(R.string.processing_image_error, error.getMessage()), Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setTextViewDirection() {
        this.userNameEditText.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
        this.fullNameEditText.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
        this.addressEditText.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
        this.emailEditText.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
        this.mobileEditText.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
        this.passwordEditText.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
        this.reEnterPasswordEditText.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
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

        String userName = userNameEditText.getText().toString();
        String fullName = fullNameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String mobile = mobileEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        String usrImg = "";
        if (this.userImage != null){
            usrImg = ImageUtils.encodeToBase64(this.userImage);
        }

        try {
            SecurityServicesI.getInstance().create(userName, password, fullName, email, mobile, address, usrImg);
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
        Toast.makeText(getBaseContext(), R.string.signup_failed, Toast.LENGTH_LONG).show();
        this.signinButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String userName = userNameEditText.getText().toString();
        String fullName = fullNameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String mobile = mobileEditText.getText().toString();
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

//        if (address.isEmpty()) {
//            addressEditText.setError(getString(R.string.invalid_address));
//            valid = false;
//        } else {
//            addressEditText.setError(null);
//        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError(getString(R.string.invalid_email));
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        if (mobile.isEmpty() || !(mobile.length()==10 || (mobile.length() == 11 && mobile.startsWith("0")))) {
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

package com.mitranetpars.sportmagazine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mitranetpars.sportmagazine.common.SecurityEnvironment;
import com.mitranetpars.sportmagazine.common.dto.security.User;
import com.mitranetpars.sportmagazine.services.SecurityServicesI;
import com.mitranetpars.sportmagazine.utils.ImageUtils;
import com.mvc.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    //private ImageButton userProfilePhoto;
    private CircleImageView userProfilePhoto;
    private File profilePhotoFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.default_color))));
        }

        User user = SecurityEnvironment.<SecurityEnvironment>getInstance().getUser();

        ((TextView)findViewById(R.id.user_profile_name)).setText(String.format("%s: %s",
                getString(R.string.user_name), user.getUserName()));
        ((TextView)findViewById(R.id.user_profile_email)).setText(String.format("%s: %s",
                getString(R.string.email), user.getEmail()));
        ((TextView)findViewById(R.id.user_profile_fullname)).setText(String.format("%s: %s",
                getString(R.string.full_name), user.getFullName()));
        ((TextView)findViewById(R.id.user_profile_mobile)).setText(String.format("%s: %s",
                getString(R.string.mobile), user.getMobile()));
        ((TextView)findViewById(R.id.user_profile_address)).setText(String.format("%s: %s",
                getString(R.string.address), user.getAddress()));

        TextView nationalCode = (TextView) findViewById(R.id.user_profile_nationalcode);
        TextView phone = (TextView) findViewById(R.id.user_profile_phone);
        if (user.getProductionType() == User.PRODUCER) {
            nationalCode.setVisibility(View.VISIBLE);
            nationalCode.setText(String.format("%s: %s", getString(R.string.national_code), user.getNationalCode()));
            phone.setVisibility(View.VISIBLE);
            phone.setText(String.format("%s: %s", getString(R.string.phone), user.getPhone()));
        } else {
            nationalCode.setVisibility(View.INVISIBLE);
            nationalCode.setText("");
            phone.setVisibility(View.INVISIBLE);
            phone.setText("");
        }

        userProfilePhoto = (CircleImageView) findViewById(R.id.user_profile_photo);
        userProfilePhoto.bringToFront();
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            try {
                profilePhotoFilePath = ImageUtils.decodeAndSaveFromBase64(user.getImage());
                Picasso.with(this).load(profilePhotoFilePath).error(R.drawable.profile128).into(userProfilePhoto);
            } catch (Exception error){
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        userProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickImage(v);
            }
        });
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

    public void onPickImage(View view) {
        // Click on image button
        ImagePicker.pickImage(this, getString(R.string.select_your_image));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            Bitmap gotImage = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
            if (gotImage != null) {
                Bitmap userImage = ImageUtils.compressLogo(gotImage);
                User user =
                        SecurityServicesI.getInstance().updateUser("", "", "", "", ImageUtils.encodeToBase64(userImage));

                try {
                    profilePhotoFilePath = ImageUtils.decodeAndSaveFromBase64(user.getImage());
                    Picasso.with(this).load(profilePhotoFilePath).error(R.drawable.profile128).into(userProfilePhoto);
                } catch (Exception error) {
                    Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (Exception error) {
            Toast.makeText(getApplicationContext(), getString(R.string.processing_image_error, error.getMessage()), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            profilePhotoFilePath.delete();
        } catch (Exception error){
        }

        super.onDestroy();
    }
}

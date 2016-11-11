package com.mitranetpars.sportmagazine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mitranetpars.sportmagazine.common.SecurityEnvironment;
import com.mitranetpars.sportmagazine.common.dto.security.User;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        User user = SecurityEnvironment.<SecurityEnvironment>getInstance().getUser();

        ((TextView)findViewById(R.id.user_profile_name)).setText(user.getUserName());
        ((TextView)findViewById(R.id.user_profile_email)).setText(user.getEmail());
        ((TextView)findViewById(R.id.user_profile_fullname)).setText(user.getFullName());
        ((TextView)findViewById(R.id.user_profile_mobile)).setText(user.getMobile());
        ((TextView)findViewById(R.id.user_profile_address)).setText(user.getAddress());

        TextView nationalCode = (TextView) findViewById(R.id.user_profile_nationalcode);
        TextView phone = (TextView) findViewById(R.id.user_profile_phone);
        if (user.getProductionType() == User.PRODUCER) {
            nationalCode.setVisibility(View.VISIBLE);
            nationalCode.setText(user.getNationalCode());
            phone.setVisibility(View.VISIBLE);
            phone.setText(user.getPhone());
        } else {
            nationalCode.setVisibility(View.INVISIBLE);
            nationalCode.setText("");
            phone.setVisibility(View.INVISIBLE);
            phone.setText("");
        }

        findViewById(R.id.user_profile_photo).bringToFront();
    }
}

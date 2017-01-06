package com.mitranetpars.sportmagazine;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mitranetpars.sportmagazine.common.SecurityEnvironment;
import com.mitranetpars.sportmagazine.common.dto.security.User;
import com.mitranetpars.sportmagazine.widgets.TooltipWindow;
import com.squareup.picasso.Picasso;

import at.markushi.ui.CircleButton;


public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {
    private RadioButton consumerRadioButton;
    private RadioButton producerRadioButton;

    private Button loginButton;
    private Button signinButton;
    private CircleButton logoffButton;
    private CircleButton exitButton;
    private CircleButton homeButton;
    private TextView linkTextView;

    private TextView welcomeTextView;
    private View mContentView;
    private ImageView splash;
    private boolean isSplashShowing;
    private boolean doubleBackToExitPressedOnce = false;

    private TooltipWindow tipWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.isSplashShowing = true;
        this.mContentView = findViewById(R.id.main_activity_relative_layout).getRootView();
        this.splash = (ImageView) findViewById(R.id.splash_image_view);
        Picasso.with(this).load(R.drawable.logo400).into(this.splash);

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
        this.exitButton.setOnLongClickListener(this);

        this.homeButton = (CircleButton) findViewById(R.id.homebutton);
        this.homeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                homeButton_onClick(v);
            }
        });
        this.homeButton.setOnLongClickListener(this);

        this.logoffButton = (CircleButton) findViewById(R.id.logoutbutton);
        this.logoffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoffButton_onClick(v);
            }
        });
        this.logoffButton.setOnLongClickListener(this);

        this.welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);
        this.linkTextView = (TextView) findViewById(R.id.http_www_sportmagazine_ir_link);
        this.linkTextView.setMovementMethod(LinkMovementMethod.getInstance());

        this.splash.bringToFront();
        this.linkTextView.bringToFront();
        this.fadeSplashOut();

        this.tipWindow = new TooltipWindow(MainActivity.this);
    }

    private void fadeSplashOut() {
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        mContentView.setAlpha(0f);
        mContentView.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        int mShortAnimationDuration = 5000;
        mContentView.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        splash.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        splashAnimationEnd();
                    }
                });
    }

    private void splashAnimationEnd(){
        splash.setVisibility(View.GONE);
        linkTextView.setVisibility(View.GONE);
        this.isSplashShowing = false;
        this.setControlsVisibility();
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.setControlsVisibility();
    }

    private void setControlsVisibility() {
        this.welcomeTextView.setText("");
        this.loginButton.setVisibility(View.INVISIBLE);
        this.signinButton.setVisibility(View.INVISIBLE);
        this.consumerRadioButton.setVisibility(View.INVISIBLE);
        this.producerRadioButton.setVisibility(View.INVISIBLE);
        this.welcomeTextView.setVisibility(View.INVISIBLE);
        this.exitButton.setVisibility(View.INVISIBLE);
        this.homeButton.setVisibility(View.INVISIBLE);
        this.logoffButton.setVisibility(View.INVISIBLE);

        if (this.isSplashShowing) {
            return;
        }

        if (SecurityEnvironment.<SecurityEnvironment>getInstance().getLoginTicket() != null &&
                SecurityEnvironment.<SecurityEnvironment>getInstance().getLoginTicket() != "") {
            this.welcomeTextView.setText(getString(R.string.welcometext,
                    SecurityEnvironment.<SecurityEnvironment>getInstance().getUserName()));
            this.welcomeTextView.setVisibility(View.VISIBLE);
            this.exitButton.setVisibility(View.VISIBLE);
            this.homeButton.setVisibility(View.VISIBLE);
            this.logoffButton.setVisibility(View.VISIBLE);
        } else {
            this.loginButton.setVisibility(View.VISIBLE);
            this.signinButton.setVisibility(View.VISIBLE);
            this.consumerRadioButton.setVisibility(View.VISIBLE);
            this.producerRadioButton.setVisibility(View.VISIBLE);
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
            Intent signinIntent = new Intent(MainActivity.this, ProducerPackageSelectionActivity.class);
            MainActivity.this.startActivity(signinIntent);
        }
    }

    private void logoffButton_onClick(View v) {
        SecurityEnvironment.<SecurityEnvironment>getInstance().setLoginTicket("");
        SecurityEnvironment.<SecurityEnvironment>getInstance().setUserName("");
        SecurityEnvironment.<SecurityEnvironment>getInstance().setUser(null);
        this.setControlsVisibility();
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

    @Override
    public boolean onLongClick(View anchor) {
        if (tipWindow.isTooltipShown()) return false;

        CircleButton c = (CircleButton) anchor;
        if(c != null) {
            tipWindow.showToolTip(c);
            return true;
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        if(tipWindow != null && tipWindow.isTooltipShown())
        tipWindow.dismissTooltip();
        super.onDestroy();
    }
}

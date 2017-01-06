package com.mitranetpars.sportmagazine;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

public class ProducerPackageSelectionActivity extends AppCompatActivity {

    private int FREE = 0;
    private int SILVER = 1;
    private int GOLDEN = 2;

    private RadioButton freeRadioButton;
    private RadioButton silverRadioButton;
    private RadioButton goldenRadioButtion;
    private CheckBox agreeContinueCheckBox;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producer_package_selection);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        this.continueButton = (Button) findViewById(R.id.btn_agree_term_of_use_continue);
        this.continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueToRegister(v);
            }
        });
        this.continueButton.setEnabled(false);

        this.freeRadioButton = (RadioButton) findViewById(R.id.freePackageRadioButton);
        this.silverRadioButton = (RadioButton) findViewById(R.id.silverPackageRadioButton);
        this.goldenRadioButtion = (RadioButton) findViewById(R.id.goldenPackageRadioButton);

        this.freeRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (freeRadioButton.isChecked()){
                    silverRadioButton.setChecked(false);
                    goldenRadioButtion.setChecked(false);
                }
            }
        });

        this.silverRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (silverRadioButton.isChecked()) {
                    freeRadioButton.setChecked(false);
                    goldenRadioButtion.setChecked(false);
                }
            }
        });

        this.goldenRadioButtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goldenRadioButtion.isChecked()){
                    freeRadioButton.setChecked(false);
                    silverRadioButton.setChecked(false);
                }
            }
        });

        this.agreeContinueCheckBox = (CheckBox) findViewById(R.id.termof_use_agree_check_box);
        this.agreeContinueCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueButton.setEnabled(agreeContinueCheckBox.isChecked());
            }
        });
    }

    private void continueToRegister(View v){
        if (!this.freeRadioButton.isChecked() &&
                !this.silverRadioButton.isChecked() &&
                !this.goldenRadioButtion.isChecked()){
            Toast.makeText(getApplicationContext(), R.string.production_package_not_selected, Toast.LENGTH_LONG).show();
            return;
        }

        Intent continueIntent = new Intent(this, ProducerSigninActivity.class);
        continueIntent.putExtra("package_type", this.freeRadioButton.isChecked()? FREE :
                                                this.silverRadioButton.isChecked() ? SILVER :
                                                this.goldenRadioButtion.isChecked() ? GOLDEN : FREE);
        this.startActivity(continueIntent);
        finish();
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

package com.example.peterleow.androiduis;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mitranetpars.sportmagazine.R;

//import android.widget.AdapterView;
public class LinearLayout extends Activity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_layout);

        // import android.widget.AutoCompleteTextView;
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView);

        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this,
                R.array.zodiac, android.R.layout.select_dialog_item);

        autoCompleteTextView.setThreshold(1);

        autoCompleteTextView.setAdapter(adapter1);

        // import android.widget.Spinner;
        Spinner spinner = (Spinner) findViewById(R.id.zodiac_spinner);
        // set a listener on spinner
        spinner.setOnItemSelectedListener(this);
        // import android.widget.ArrayAdapter;
        // populate the spinner from data source
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.zodiac, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String selectedItem = parent.getItemAtPosition(position).toString();

        Toast.makeText(parent.getContext(), selectedItem, Toast.LENGTH_SHORT).show();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void onCheckboxClicked(View view) {
        // import android.content.Context;
        Context context = getApplicationContext();
        // import android.widget.Toast;
        int duration = Toast.LENGTH_SHORT;
        // import android.widget.CheckBox;
        CheckBox chkJogging = (CheckBox) findViewById(R.id.chkJogging);
        CheckBox chkSwimming = (CheckBox) findViewById(R.id.chkSwimming);
        CheckBox chkCoding = (CheckBox) findViewById(R.id.chkCoding);
        CheckBox chkWriting = (CheckBox) findViewById(R.id.chkWriting);

        StringBuilder sb = new StringBuilder();
        if (chkJogging.isChecked()) {
            sb.append(", " + chkJogging.getText());
        }
        if (chkSwimming.isChecked()) {
            sb.append(", " + chkSwimming.getText());
        }
        if (chkCoding.isChecked()) {
            sb.append(", " + chkCoding.getText());;
        }
        if (chkWriting.isChecked()) {
            sb.append(", " + chkWriting.getText());
        }
        if (sb.length() > 0) { // No toast if the string is empty
            // Remove the first comma
            String output = sb.deleteCharAt(sb.indexOf(",")).toString();

            // A small pop up box that contains a message for a limited amount of time
            Toast toast = Toast.makeText(context, output, duration);
            toast.show();
        }
    }

    public void onRadioButtonClicked(View view) {
        // import android.widget.RadioButton;
        RadioButton radio = (RadioButton) view;
        boolean checked = radio.isChecked();

        if (checked){
            // import android.content.Context;
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            String output = radio.getText().toString();
            // import android.widget.Toast;
            Toast toast = Toast.makeText(context, output, duration);
            toast.show();
        }
    }

    public void onToggleClicked(View view) {
        // import android.widget.ToggleButton;
        boolean on = ((ToggleButton) view).isChecked();
        //import android.net.wifi.WifiManager;
        WifiManager wifiManager = (WifiManager) this
                .getSystemService(Context.WIFI_SERVICE);

        if (on && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        } else if (!on && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.linear_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

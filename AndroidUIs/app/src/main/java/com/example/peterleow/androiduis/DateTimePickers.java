package com.example.peterleow.androiduis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.peterleow.androiduis.R;

public class DateTimePickers extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time_pickers);
    }

    public void getDate(View view) {
        DatePicker datePicker = (DatePicker) view;
        String setDate = datePicker.getYear() + "/" +
                (datePicker.getMonth() + 1) + "/" +
                datePicker.getDayOfMonth();
        Toast toast = Toast.makeText(this, setDate, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void getTime(View view) {
        TimePicker timePicker = (TimePicker) view;
        String setTime = timePicker.getCurrentHour()  +
                ":" + timePicker.getCurrentMinute();
        Toast toast = Toast.makeText(this, setTime, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.date_time_pickers, menu);
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




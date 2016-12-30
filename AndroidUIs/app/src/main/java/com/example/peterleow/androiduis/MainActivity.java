package com.example.peterleow.androiduis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called when the user touches the button
     */
    public void getRelativeLayoutPage(View view) {
        Intent intent = new Intent(getApplicationContext(), RelativeLayout.class);
        startActivity(intent);
    }

    /**
     * Called when the user touches the button
     */
    public void getTableLayoutPage(View view) {
        Intent intent = new Intent(getApplicationContext(), TableLayout.class);
        startActivity(intent);
    }

    public void getFrameLayoutPage(View view) {
        Intent intent = new Intent(getApplicationContext(), FrameLayout.class);
        startActivity(intent);
    }

    /**
     * Called when the user touches the button
     */
    public void getLinearLayoutPage(View view) {
        Intent intent = new Intent(getApplicationContext(), LinearLayout.class);
        startActivity(intent);
    }

    /**
     * Called when the user touches the button
     */
    public void getListViewPage(View view) {
        Intent intent = new Intent(getApplicationContext(), ListViewActivity.class);
        startActivity(intent);
    }

    public void getGridViewPage(View view) {
        Intent intent = new Intent(getApplicationContext(), GridViewActivity.class);
        startActivity(intent);
    }

    public void getDateTimePickers(View view) {
        Intent intent = new Intent(getApplicationContext(), DateTimePickers.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

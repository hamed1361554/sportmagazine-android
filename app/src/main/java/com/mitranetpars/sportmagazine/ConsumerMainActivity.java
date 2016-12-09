package com.mitranetpars.sportmagazine;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mitranetpars.sportmagazine.widgets.TooltipWindow;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import at.markushi.ui.CircleButton;

public class ConsumerMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnLongClickListener {

    private CircleButton retailButton;

    private SliderLayout sliderShow;
    private ViewPager postsViewPager;

    private TooltipWindow tipWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShoppingCart();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawerView.bringToFront();
                drawerView.requestLayout();
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        this.sliderShow = (SliderLayout) findViewById(R.id.slider);
        HashMap<String,String> urlMaps = new HashMap<String, String>();
        urlMaps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        urlMaps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        urlMaps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        urlMaps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        for (String key: urlMaps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .description(key)
                    .image(urlMaps.get(key));
            sliderShow.addSlider(textSliderView);
        }

        View header = navigationView.getHeaderView(0);
        ImageView logoImageView = (ImageView) header.findViewById(R.id.consumer_nav_header_imageView);
        Picasso.with(this).load(R.drawable.logo240).into(logoImageView);

        this.retailButton = (CircleButton) findViewById(R.id.consumer_retail_purchase_button);
        this.retailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProductsList();
            }
        });
        this.retailButton.setOnLongClickListener(this);

        this.tipWindow = new TooltipWindow(ConsumerMainActivity.this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.consumer_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.consumer_nav_home) {
            // Handle the camera action
        } else if (id == R.id.consumer_nav_products_categories) {
            this.showProductsList();
        } else if (id == R.id.consumer_nav_transactions) {
            this.showInvoicesList();
        } else if (id == R.id.consumer_nav_profile) {
            Intent profileIntent = new Intent(this, ProfileActivity.class);
            this.startActivity(profileIntent);
        }
        else if (id == R.id.consumer_nav_settings) {

        } else if (id == R.id.consumer_nav_about) {
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            this.startActivity(aboutIntent);
        } else if (id == R.id.consumer_nav_eula) {
            Intent eulaIntent = new Intent(this, EulaActivity.class);
            this.startActivity(eulaIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showProductsList() {
        Fragment fragment = new ProductsListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.consumer_main_frame_container, fragment, null);
        transaction.commitAllowingStateLoss();
    }

    private void showShoppingCart() {
        Fragment fragment = new CartListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.consumer_main_frame_container, fragment, null);
        transaction.commitAllowingStateLoss();
    }

    private void showInvoicesList(){
        InvoiceListFragment fragment = new InvoiceListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment.setFragmentManager(fragmentManager);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.consumer_main_frame_container, fragment, null);
        transaction.commitAllowingStateLoss();
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
    public void onDestroy() {
        if(tipWindow != null && tipWindow.isTooltipShown())
            tipWindow.dismissTooltip();
        super.onDestroy();
    }
}

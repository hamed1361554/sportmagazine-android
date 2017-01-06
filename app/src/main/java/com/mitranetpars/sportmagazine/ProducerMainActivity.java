package com.mitranetpars.sportmagazine;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mitranetpars.sportmagazine.adapters.ProductsListAdapter;
import com.mitranetpars.sportmagazine.common.SecurityEnvironment;
import com.mitranetpars.sportmagazine.common.dto.product.Product;
import com.mitranetpars.sportmagazine.common.dto.security.User;
import com.mitranetpars.sportmagazine.services.ProductServicesI;
import com.mitranetpars.sportmagazine.widgets.TooltipWindow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import at.markushi.ui.CircleButton;

public class ProducerMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnLongClickListener {

    private int FREE = 0;
    private int SILVER = 1;
    private int GOLDEN = 2;

    private SliderLayout sliderShow;
    private CircleButton retailPurchase;
    private CircleButton wholesalePurchase;
    private TooltipWindow tipWindow;

    private ListView listview;
    ArrayList<Product> products;
    ProductsListAdapter listAdapter;
    private Date searchStartDate;
    private int currentOffset;
    private int limitSize;
    private int wholesaleType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producer_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        showShoppingCart();
        //    }
        //});
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNextPage();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
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

        //this.sliderShow = (SliderLayout) findViewById(R.id.slider);
        if (this.sliderShow != null) {
            HashMap<String, String> urlMaps = new HashMap<String, String>();
            urlMaps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
            urlMaps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
            urlMaps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
            urlMaps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

            for (String key : urlMaps.keySet()) {
                TextSliderView textSliderView = new TextSliderView(this);
                textSliderView
                        .description(key)
                        .image(urlMaps.get(key));
                sliderShow.addSlider(textSliderView);
            }
        }

        View header = navigationView.getHeaderView(0);
        ImageView logoImageView = (ImageView) header.findViewById(R.id.producer_nav_header_imageView);
        Picasso.with(this).load(R.drawable.logo240).into(logoImageView);

        this.retailPurchase = (CircleButton) findViewById(R.id.producer_retail_purchase_button);
        this.wholesalePurchase = (CircleButton) findViewById(R.id.producer_wholesale_purchase_button);

        this.retailPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProductsList(0);
            }
        });
        this.retailPurchase.setOnLongClickListener(this);
        this.wholesalePurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProductsList(1);
            }
        });
        this.wholesalePurchase.setOnLongClickListener(this);
        this.tipWindow = new TooltipWindow(ProducerMainActivity.this);

        this.retailPurchase.setVisibility(View.INVISIBLE);
        this.wholesalePurchase.setVisibility(View.INVISIBLE);

        this.searchStartDate = Calendar.getInstance().getTime();
        this.currentOffset = 0;
        this.limitSize = 10;
        this.wholesaleType = 1;

        this.listview = (ListView) findViewById(R.id.products_listview);
        this.products = new ArrayList<>();
        this.listAdapter = new ProductsListAdapter(this,
                R.layout.product_list_row,
                this.products);
        this.listAdapter.setFragmentManager(getSupportFragmentManager());
        this.listAdapter.setReplacementID(R.id.producer_main_frame_container);
        this.listview.setAdapter(this.listAdapter);

        Menu nav_Menu = navigationView.getMenu();
        User user = SecurityEnvironment.<SecurityEnvironment>getInstance().getUser();
        if (user.getProductionPackage() == FREE) {
            nav_Menu.findItem(R.id.producer_nav_wholesale_purchase).setVisible(false);
            nav_Menu.findItem(R.id.producer_nav_retail_purchase).setVisible(false);
        }
        if (user.getProductionPackage() == SILVER){
            nav_Menu.findItem(R.id.producer_nav_retail_purchase).setVisible(false);
        }
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
        getMenuInflater().inflate(R.menu.producer_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.producer_action_add_product) {
            Intent productIntent = new Intent(this, ProducerProductActivity.class);
            this.startActivity(productIntent);
            return true;
        }

        if (id == R.id.producer_action_wholesale_retail_products) {
            if (this.wholesaleType == 0) {
                this.wholesaleType = 1;
                this.searchProducts();
                return true;
            }

            this.wholesaleType = 0;
            this.searchProducts();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.producer_nav_home) {

        } else if (id == R.id.producer_nav_retail_purchase) {

        } else if (id == R.id.producer_nav_wholesale_purchase) {

        } else if (id == R.id.producer_nav_profile) {
            Intent profileIntent = new Intent(this, ProfileActivity.class);
            this.startActivity(profileIntent);
        } else if (id == R.id.producer_nav_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            this.startActivity(settingsIntent);
        } else if (id == R.id.producer_nav_about) {
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            this.startActivity(aboutIntent);
        } else if (id == R.id.producer_nav_eula) {
            Intent eulaIntent = new Intent(this, EulaActivity.class);
            this.startActivity(eulaIntent);
        } else if (id == R.id.producer_nav_transactions){
            showInvoicesList();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStop() {
        if (this.sliderShow != null) {
            this.sliderShow.stopAutoCycle();
        }
        super.onStop();
    }

    private void showProductsList(int wholesaleType) {
        ProductsListFragment fragment = new ProductsListFragment();
        fragment.setWholesaleType(wholesaleType);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.producer_main_frame_container, fragment, null);
        transaction.commitAllowingStateLoss();
    }

    private void showShoppingCart() {
        Fragment fragment = new CartListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.producer_main_frame_container, fragment, null);
        transaction.commitAllowingStateLoss();
    }

    private void showInvoicesList() {
        Fragment fragment = new InvoiceListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.producer_main_frame_container, fragment, null);
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

    private void searchProducts(){
        try {
            this.products.clear();
            this.listAdapter.notifyDataSetChanged();
            this.searchStartDate = Calendar.getInstance().getTime();
            this.currentOffset = 0;

            this.loadNextPage();

        } catch (Exception error){
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadNextPage(){
        try {
            ArrayList<Product> list =
                        ProductServicesI.getInstance().search(null,
                                this.searchStartDate, 0, 0, null,
                                "", "", -1, -1, -1,
                                this.wholesaleType, this.currentOffset, this.limitSize);

            if (list == null || list.size() <= 0){
                return;
            }

            this.products.addAll(list);
            this.listAdapter.notifyDataSetChanged();
            this.currentOffset = this.currentOffset + this.limitSize;
        } catch (Exception error){
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
package com.mitranetpars.sportmagazine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mitranetpars.sportmagazine.adapters.ProductsListAdapter;
import com.mitranetpars.sportmagazine.common.SecurityEnvironment;
import com.mitranetpars.sportmagazine.common.dto.product.Product;
import com.mitranetpars.sportmagazine.common.dto.security.User;
import com.mitranetpars.sportmagazine.services.ProductServicesI;
import com.mitranetpars.sportmagazine.services.SecurityServicesI;
import com.mitranetpars.sportmagazine.utils.ImageUtils;
import com.mitranetpars.sportmagazine.utils.SystemUtils;
import com.mitranetpars.sportmagazine.widgets.TooltipWindow;
import com.mvc.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import at.markushi.ui.CircleButton;
import de.hdodenhof.circleimageview.CircleImageView;

public class ConsumerMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnLongClickListener {

    private CircleButton retailButton;
    private CircleButton transactionsButton;

    private SliderLayout sliderShow;
    private TooltipWindow tipWindow;
    private CircleImageView logoImageView;
    private File logoImageFilePath;

    private ListView listview;
    ArrayList<Product> products;
    ProductsListAdapter listAdapter;
    private Date searchStartDate;
    private int currentOffset;
    private int limitSize;
    private int wholesaleType;
    private boolean loadingFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_main);
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

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.default_color))));
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        this.sliderShow = (SliderLayout) findViewById(R.id.slider);
        if (this.sliderShow != null) {
            HashMap<String, String> urlMaps = new HashMap<String, String>();
            urlMaps.put("1",
                    "http://www.sportmagazine.ir/images/slogan/148265487526897.jpg");
            urlMaps.put("2",
                    "http://sportmagazine.ir/images/slogan/148168265248147.jpg");

            for (String key : urlMaps.keySet()) {
                TextSliderView textSliderView = new TextSliderView(this);
                textSliderView
                        .description(getString(R.string.app_name))
                        .image(urlMaps.get(key));
                sliderShow.addSlider(textSliderView);
            }
        }

        User user = SecurityEnvironment.<SecurityEnvironment>getInstance().getUser();

        View header = navigationView.getHeaderView(0);
        logoImageView = (CircleImageView) header.findViewById(R.id.consumer_nav_header_imageView);
        TextView logoTextView = (TextView) header.findViewById(R.id.consumer_nav_header_textView);
        logoTextView.setText(user.getFullName());
        header.setBackgroundColor(Color.parseColor(getString(R.string.default_color)));

        if (user.getImage() == null || user.getImage() == "") {
            Picasso.with(this).load(R.drawable.logo240).into(logoImageView);
        } else {
            //logoImageView.setImageBitmap(ImageUtils.decodeFromBase64(user.getImage()));
            try {
                logoImageFilePath = ImageUtils.decodeAndSaveFromBase64(user.getImage());
                Picasso.with(this).load(logoImageFilePath).error(R.drawable.logo240).into(logoImageView);
            } catch (Exception error) {
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        logoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickImage(v);
            }
        });

        this.retailButton = (CircleButton) findViewById(R.id.consumer_retail_purchase_button);
        this.retailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProductsList();
            }
        });
        this.retailButton.setOnLongClickListener(this);
        this.transactionsButton = (CircleButton) findViewById(R.id.consumer_transactions_list_button);
        this.transactionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInvoicesList();
            }
        });
        this.transactionsButton.setOnLongClickListener(this);

        this.tipWindow = new TooltipWindow(ConsumerMainActivity.this);

        this.retailButton.setVisibility(View.INVISIBLE);
        this.transactionsButton.setVisibility(View.INVISIBLE);

        this.searchStartDate = Calendar.getInstance().getTime();
        this.currentOffset = 0;
        this.limitSize = 10;
        this.wholesaleType = 0;

        this.listview = (ListView) findViewById(R.id.products_listview);
        this.products = new ArrayList<>();
        this.listAdapter = new ProductsListAdapter(this,
                R.layout.product_list_row,
                this.products);
        this.listAdapter.setFragmentManager(getSupportFragmentManager());
        this.listAdapter.setReplacementID(R.id.consumer_main_frame_container);
        this.listview.setAdapter(this.listAdapter);

        this.loadingFlag = false;
        this.listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if(!loadingFlag) {
                        loadingFlag = true;
                        loadNextPage();
                    }
                }
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
                Bitmap userImage = ImageUtils.compressLogo(gotImage);
                User user =
                        SecurityServicesI.getInstance().updateUser("", "", "", "", ImageUtils.encodeToBase64(userImage));

                try {
                    logoImageFilePath = ImageUtils.decodeAndSaveFromBase64(user.getImage());
                    Picasso.with(this).load(logoImageFilePath).error(R.drawable.logo240).into(logoImageView);
                } catch (Exception error) {
                    Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (Exception error) {
            Toast.makeText(getApplicationContext(), getString(R.string.processing_image_error, error.getMessage()), Toast.LENGTH_LONG).show();
        }
    }

    private void exit(){
        SystemUtils.saveProfile(SportMagazineApplication.getContext());
        moveTaskToBack(true);
        Intent intent = new Intent();
        intent.putExtra("forceClose", true);
        setResult(RESULT_OK, intent);
        finish();
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
            this.searchProducts();
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
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            this.startActivity(settingsIntent);
        } else if (id == R.id.consumer_nav_about) {
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            this.startActivity(aboutIntent);
        } else if (id == R.id.consumer_nav_eula) {
            Intent eulaIntent = new Intent(this, EulaActivity.class);
            this.startActivity(eulaIntent);
        } else if (id == R.id.consumer_nav_exit){
            exit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showProductsList() {
        ProductsListFragment fragment = new ProductsListFragment();
        fragment.setWholesaleType(0);
        fragment.setReplacementID(R.id.consumer_main_frame_container);
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
    protected void onStop() {
        if (this.sliderShow != null) {
            this.sliderShow.stopAutoCycle();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if(tipWindow != null && tipWindow.isTooltipShown())
            tipWindow.dismissTooltip();

        try {
            logoImageFilePath.delete();
        } catch (Exception error){
        }

        super.onDestroy();
    }

    private void searchProducts(){
        try {
            this.loadingFlag = false;
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
            this.loadingFlag = false;
        } catch (Exception error){
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

package com.mitranetpars.sportmagazine;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mitranetpars.sportmagazine.common.dto.product.Product;
import com.mitranetpars.sportmagazine.services.ProductServicesI;
import com.mitranetpars.sportmagazine.adapters.ProductsListAdapter;
import com.mitranetpars.sportmagazine.widgets.TooltipWindow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import at.markushi.ui.CircleButton;

/**
 * Created by Hamed on 12/1/2016.
 */

public class ProductsListFragment extends Fragment implements View.OnLongClickListener{
    private ListView listview;
    private EditText searchTextView;
    private CircleButton searchButton;
    private CircleButton advancedSearchButton;
    private CircleButton loadMoreButton;
    private Activity parentActivity;
    private boolean isProductListRetrieved;
    private Date searchStartDate;
    private int currentOffset;
    private int limitSize;
    private int wholesaleType;

    ProgressDialog progressDialog;
    ArrayList<Product> products;
    ProductsListAdapter listAdapter;

    private TooltipWindow tipWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.parentActivity = getActivity();
        this.isProductListRetrieved = false;
        this.searchStartDate = Calendar.getInstance().getTime();
        this.currentOffset = 0;
        this.limitSize = 10;
        this.wholesaleType = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.tipWindow = new TooltipWindow(parentActivity);

        View rootView = inflater
                .inflate(R.layout.product_list_view, container, false);

        this.listview = (ListView) rootView.findViewById(R.id.products_listview);
        this.searchTextView = (EditText) rootView.findViewById(R.id.product_simple_search_text);
        this.searchButton = (CircleButton) rootView.findViewById(R.id.product_search_button);
        this.advancedSearchButton = (CircleButton) rootView.findViewById(R.id.advanced_product_search_button);
        this.loadMoreButton = (CircleButton) rootView.findViewById(R.id.load_products_another_page_button);

        this.loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNextPage(v);
            }
        });
        this.loadMoreButton.setOnLongClickListener(this);
        this.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchProducts(v);
            }
        });
        this.searchButton.setOnLongClickListener(this);

        // listening to single listitem click
        this.listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }
        });

        this.products = new ArrayList<>();
        this.listAdapter = new ProductsListAdapter(this.parentActivity,
                R.layout.product_list_row,
                this.products);
        this.listview.setAdapter(this.listAdapter);

        return rootView;
    }

    private void searchProducts(View v){
        try {
            this.products.clear();
            this.searchStartDate = Calendar.getInstance().getTime();
            this.currentOffset = 0;

            this.loadNextPage(v);

        } catch (Exception error){
            Toast.makeText(this.parentActivity, error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadNextPage(View v){
        try {
            this.products.addAll(ProductServicesI.getInstance().search(null,
                    this.searchStartDate, 0, 0, this.searchTextView.getText().toString().trim(), -1,
                    this.wholesaleType, this.currentOffset, this.limitSize));
            this.listAdapter.notifyDataSetChanged();
            this.currentOffset = this.currentOffset + this.limitSize;
        } catch (Exception error){
            Toast.makeText(this.parentActivity, error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public boolean isProductListRetrieved() {
        return this.isProductListRetrieved;
    }

    public void setProductListRetrieved(boolean flag) {
        this.isProductListRetrieved = flag;
    }

    public void setWholesaleType(int type){
        this.wholesaleType = type;
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

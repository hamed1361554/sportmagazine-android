package com.mitranetpars.sportmagazine;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.mitranetpars.sportmagazine.common.dto.product.Product;
import com.mitranetpars.sportmagazine.common.dto.product.ProductSearchFilter;
import com.mitranetpars.sportmagazine.services.ProductServicesI;
import com.mitranetpars.sportmagazine.adapters.ProductsListAdapter;
import com.mitranetpars.sportmagazine.widgets.TooltipWindow;
import com.satsuware.usefulviews.LabelledSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import at.markushi.ui.CircleButton;

/**
 * Created by Hamed on 12/1/2016.
 */

public class ProductsListFragment extends Fragment implements View.OnLongClickListener {
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
    private int replacementID;

    ProgressDialog progressDialog;
    ArrayList<Product> products;
    ProductsListAdapter listAdapter;

    private TooltipWindow tipWindow;
    private LabelledSpinner categoryLabelledSpinner;
    private ImageView backImageView;

    private boolean isInAdvancedMode;
    private boolean loadingFlag;
    private ProductSearchFilter filter;

    private String searchName;
    private int searchCategory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.parentActivity = getActivity();
        this.isProductListRetrieved = false;
        this.searchStartDate = Calendar.getInstance().getTime();
        this.currentOffset = 0;
        this.limitSize = 10;
        this.wholesaleType = 0;
        this.isInAdvancedMode = false;
        this.filter = new ProductSearchFilter();
        this.searchName = "";
        this.searchCategory = -1;
        this.loadingFlag = false;
    }

    public void setReplacementID(int id){
        this.replacementID = id;
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
                loadNextPage();
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
        this.advancedSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advancedSearchProducts(v);
            }
        });
        this.advancedSearchButton.setOnLongClickListener(this);

        // listening to single listitem click
        this.listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }
        });

        this.backImageView = (ImageView) rootView.findViewById(R.id.products_list_back);

        this.backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.onBackPressed();
            }
        });

        this.products = new ArrayList<>();
        this.listAdapter = new ProductsListAdapter(this.parentActivity,
                R.layout.product_list_row,
                this.products);
        this.listAdapter.setFragmentManager(getFragmentManager());
        this.listAdapter.setReplacementID(this.replacementID);
        this.listview.setAdapter(this.listAdapter);

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

        this.categoryLabelledSpinner = (LabelledSpinner) rootView.findViewById(R.id.product_search_category);
        this.categoryLabelledSpinner.setItemsArray(getResources().getStringArray(R.array.product_category_items));
        this.categoryLabelledSpinner.getSpinner().setSelection(0);
        this.categoryLabelledSpinner.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView,
                                     int position, long id) {
                searchProducts(labelledSpinner);
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }

        });

        return rootView;
    }

    private void searchProducts(View v){
        try {
            this.loadingFlag = false;
            this.isInAdvancedMode = false;
            this.products.clear();
            this.listAdapter.notifyDataSetChanged();
            this.searchStartDate = Calendar.getInstance().getTime();
            this.currentOffset = 0;

            this.searchName = this.searchTextView.getText().toString().trim();
            this.searchCategory = this.categoryLabelledSpinner.getSpinner().getSelectedItemPosition();

            this.loadNextPage();

        } catch (Exception error){
            Toast.makeText(this.parentActivity, error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadNextPage(){
        try {
            ArrayList<Product> list = null;
            if (!this.isInAdvancedMode){
                list =
                    ProductServicesI.getInstance().search(null,
                            this.searchStartDate, 0, 0, this.searchName,
                            "", "", this.searchCategory, -1, -1,
                            this.wholesaleType, this.currentOffset, this.limitSize);
            } else {
                list =
                    ProductServicesI.getInstance().search(null,
                            this.searchStartDate, this.filter.from_price, this.filter.to_price,
                            this.filter.name, this.filter.size, this.filter.brand,
                            this.filter.categories, this.filter.age_categories, this.filter.gender,
                            this.filter.wholesale_type, this.currentOffset, this.limitSize);
            }

            if (list == null || list.size() <= 0){
                return;
            }

            this.products.addAll(list);
            this.listAdapter.notifyDataSetChanged();
            this.currentOffset = this.currentOffset + this.limitSize;
            this.loadingFlag = false;
        } catch (Exception error){
            Toast.makeText(this.parentActivity, error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void advancedSearchProducts(View v){
        this.showAdvancedSearchDialog();
    }

    private void showAdvancedSearchDialog() {
        FragmentManager fm = getFragmentManager();
        ProductAdvancedSearchDialog productAdvancedSearchDialog = new ProductAdvancedSearchDialog();
        productAdvancedSearchDialog.setTargetFragment(this, 1);
        productAdvancedSearchDialog.show(fm, "product_search_filter_fragment");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 1){
            this.filter.name = data.getExtras().getString("name", "");
            this.filter.from_price = data.getExtras().getDouble("from_price", 0);
            this.filter.to_price = data.getExtras().getDouble("to_price", 0);
            this.filter.size = data.getExtras().getString("size", "");
            this.filter.brand = data.getExtras().getString("brand", "");
            this.filter.categories = data.getExtras().getInt("categories", -1);
            this.filter.age_categories = data.getExtras().getInt("age_categories", -1);
            this.filter.gender = data.getExtras().getInt("gender", -1);

            try {
                this.isInAdvancedMode = true;
                this.products.clear();
                this.listAdapter.notifyDataSetChanged();
                this.searchStartDate = Calendar.getInstance().getTime();
                this.currentOffset = 0;

                this.loadNextPage();

            } catch (Exception error){
                Toast.makeText(this.parentActivity, error.getMessage(), Toast.LENGTH_LONG).show();
            }
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

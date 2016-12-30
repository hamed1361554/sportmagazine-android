package com.mitranetpars.sportmagazine;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.mitranetpars.sportmagazine.adapters.ProducerProductsGridViewAdapter;
import com.mitranetpars.sportmagazine.common.dto.product.Product;
import com.mitranetpars.sportmagazine.services.ProductServicesI;
import com.mitranetpars.sportmagazine.widgets.TooltipWindow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import at.markushi.ui.CircleButton;

public class ProducerProductsGridViewActivity extends AppCompatActivity implements View.OnLongClickListener {
    private Date searchStartDate;
    private int currentOffset;
    private int limitSize;
    private ProgressDialog progressDialog;
    private ArrayList<Product> products;
    private ProducerProductsGridViewAdapter gridViewAdapter;
    private CircleButton loadMoreButton;
    private GridView gridView;

    private TooltipWindow tipWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producer_products_grid_view);

        this.tipWindow = new TooltipWindow(this);
        this.gridView = (GridView) findViewById(R.id.producer_products_grid_view);
        this.loadMoreButton = (CircleButton) findViewById(R.id.load_products_another_page_button);

        this.loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNextPage();
            }
        });
        this.loadMoreButton.setOnLongClickListener(this);

        this.searchStartDate = Calendar.getInstance().getTime();
        this.currentOffset = 0;
        this.limitSize = 10;

        this.products = new ArrayList<>();
        this.gridViewAdapter = new ProducerProductsGridViewAdapter(this,
                R.layout.product_grid_view_item,
                this.products);
        this.gridView.setAdapter(this.gridViewAdapter);

        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(getApplicationContext(), "Under Construction", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadNextPage(){
        try {
            ArrayList<Product> list =
                ProductServicesI.getInstance().search(null,
                        this.searchStartDate, 0, 0, "",
                        "", "", -1, -1, -1, -1,
                        this.currentOffset, this.limitSize,
                        true, true);

            if (list == null || list.size() <= 0){
                return;
            }

            this.products.addAll(list);
            this.gridViewAdapter.notifyDataSetChanged();
            this.currentOffset = this.currentOffset + this.limitSize;
        } catch (Exception error){
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
        }
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

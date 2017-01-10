package com.mitranetpars.sportmagazine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.mitranetpars.sportmagazine.adapters.InvoiceItemsListAdapter;
import com.mitranetpars.sportmagazine.common.dto.invoice.InvoiceItem;

import java.util.ArrayList;

/**
 * Created by Hamed on 12/8/2016.
 */

public class InvoiceItemsListFragment extends Fragment {
    private ListView listview;
    private Activity parentActivity;

    ProgressDialog progressDialog;
    ArrayList<InvoiceItem> invoiceItems;
    InvoiceItemsListAdapter listAdapter;
    private ImageView backImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.parentActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater
                .inflate(R.layout.invoice_items_list_view, container, false);
        this.listview = (ListView) rootView.findViewById(R.id.invoice_items_listview);

        this.listAdapter = new InvoiceItemsListAdapter(this.parentActivity,
                R.layout.invoice_items_list_row,
                this.invoiceItems);
        this.listview.setAdapter(this.listAdapter);
        this.listAdapter.notifyDataSetChanged();

        this.backImageView = (ImageView) rootView.findViewById(R.id.invoice_items_back);

        this.backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.onBackPressed();
            }
        });

        return rootView;
    }

    public void setInvoiceItems(ArrayList<InvoiceItem> items){
        this.invoiceItems = items;
    }
}

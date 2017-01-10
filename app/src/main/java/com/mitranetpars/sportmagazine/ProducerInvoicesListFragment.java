package com.mitranetpars.sportmagazine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.mitranetpars.sportmagazine.adapters.ProducerInvoicesListAdapter;
import com.mitranetpars.sportmagazine.common.dto.invoice.ProducerInvoice;
import com.mitranetpars.sportmagazine.common.dto.invoice.ProducerInvoiceSearchFilter;
import com.mitranetpars.sportmagazine.services.InvoiceServicesI;
import com.mitranetpars.sportmagazine.widgets.TooltipWindow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import at.markushi.ui.CircleButton;

/**
 * Created by Hamed on 1/9/2017.
 */

public class ProducerInvoicesListFragment extends Fragment implements View.OnLongClickListener {
    private ListView listview;
    private CircleButton loadMoreButton;
    private Activity parentActivity;
    private Date searchStartDate;
    private int currentOffset;
    private int limitSize;
    private int replacementID;
    private int wholesaleType;

    ProgressDialog progressDialog;
    ArrayList<ProducerInvoice> invoices;
    ProducerInvoicesListAdapter listAdapter;
    private TooltipWindow tipWindow;
    private ProducerInvoiceSearchFilter filter;
    private ImageView backImageView;
    private SimpleDateFormat mFormatter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.parentActivity = getActivity();
        this.searchStartDate = Calendar.getInstance().getTime();
        this.currentOffset = 0;
        this.limitSize = 10;
        this.filter = new ProducerInvoiceSearchFilter();
        this.mFormatter = new SimpleDateFormat("yyyy MMMM dd hh:mm:ss");
    }

    public void setReplacementID(int id){
        this.replacementID = id;
    }

    public void setWholesaleType(int type){
        this.wholesaleType = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.tipWindow = new TooltipWindow(parentActivity);

        View rootView = inflater
                .inflate(R.layout.producer_invoice_list_view, container, false);

        this.listview = (ListView) rootView.findViewById(R.id.producer_invoices_listview);
        this.loadMoreButton = (CircleButton) rootView.findViewById(R.id.load_products_producer_invoices_page_button);

        this.loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNextPage();
            }
        });
        this.loadMoreButton.setOnLongClickListener(this);

        // listening to single listitem click
        this.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }
        });

        this.invoices = new ArrayList<>();
        this.listAdapter = new ProducerInvoicesListAdapter(this.parentActivity,
                R.layout.producer_invoice_list_row,
                this.invoices);
        this.listAdapter.setFragmentManager(getFragmentManager());
        this.listAdapter.setReplacementID(this.replacementID);
        this.listview.setAdapter(this.listAdapter);

        this.backImageView = (ImageView) rootView.findViewById(R.id.producer_invoices_listview_back);

        this.backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.onBackPressed();
            }
        });

        return rootView;
    }

    private void searchProducts(){
        try {
            this.invoices.clear();
            this.listAdapter.notifyDataSetChanged();
            this.searchStartDate = Calendar.getInstance().getTime();
            this.currentOffset = 0;

            this.loadNextPage();

        } catch (Exception error){
            Toast.makeText(this.parentActivity, error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadNextPage(){
        try {
            ArrayList<ProducerInvoice> list =
                    InvoiceServicesI.getInstance().searchProducerInvoices(null,
                            this.searchStartDate, this.wholesaleType,
                            this.currentOffset, this.limitSize);

            if (list == null || list.size() <= 0){
                return;
            }

            this.invoices.addAll(list);
            this.listAdapter.notifyDataSetChanged();
            this.currentOffset = this.currentOffset + this.limitSize;
        } catch (Exception error){
            Toast.makeText(this.parentActivity, error.getMessage(), Toast.LENGTH_LONG).show();
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

package com.mitranetpars.sportmagazine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.mitranetpars.sportmagazine.adapters.InvoiceItemsDetailsEventListener;
import com.mitranetpars.sportmagazine.adapters.InvoiceListAdapter;
import com.mitranetpars.sportmagazine.common.dto.invoice.Invoice;
import com.mitranetpars.sportmagazine.common.dto.invoice.InvoiceItem;
import com.mitranetpars.sportmagazine.services.InvoiceServicesI;
import com.mitranetpars.sportmagazine.utils.DateTimeUtils;
import com.mitranetpars.sportmagazine.widgets.TooltipWindow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;

import at.markushi.ui.CircleButton;

/**
 * Created by Hamed on 12/6/2016.
 */

public class InvoiceListFragment extends Fragment implements InvoiceItemsDetailsEventListener, View.OnLongClickListener{
    private ListView listview;
    private TextView fromDateTextView;
    private TextView toDateTextView;
    private ImageView fromDateImageButton;
    private ImageView toDateImageButton;
    private CircleButton searchButton;
    private CircleButton loadNextPageButton;
    private Activity parentActivity;
    private FragmentManager fragmentManager;
    private ImageView backImageView;

    private Date fromDate;
    private Date toDate;

    ProgressDialog progressDialog;
    ArrayList<Invoice> invoices;
    InvoiceListAdapter listAdapter;

    private int currentOffset;
    private int limitSize;

    private TooltipWindow tipWindow;

    private SlideDateTimeListener fromListener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
            fromDate = date;
            fromDateTextView.setText(DateTimeUtils.formatDateTimeToPersian(date));
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {
            fromDate = null;
            fromDateTextView.setText("");
        }
    };

    private SlideDateTimeListener toListener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
            toDate = date;
            toDateTextView.setText(DateTimeUtils.formatDateTimeToPersian(date));
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {
            toDate = null;
            toDateTextView.setText("");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.parentActivity = getActivity();
        this.fromDate = null;
        this.toDate = null;
        this.toDate = Calendar.getInstance().getTime();
        this.currentOffset = 0;
        this.limitSize = 10;
    }

    @Override
    public void onResume() {
        super.onResume();

        this.loadNextPageButton.setVisibility(View.VISIBLE);
        this.listAdapter.addListener(this);
        this.searchInvoices();
    }

    @Override
    public void onPause() {
        super.onPause();

        this.loadNextPageButton.setVisibility(View.INVISIBLE);
        this.listAdapter.removeListener(this);
    }

    public void setFragmentManager(FragmentManager manager){
        this.fragmentManager = manager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.tipWindow = new TooltipWindow(parentActivity);

        View rootView = inflater
                .inflate(R.layout.invoice_list_view, container, false);

        this.listview = (ListView) rootView.findViewById(R.id.invoices_listview);
        this.fromDateTextView = (TextView) rootView.findViewById(R.id.invoice_list_from_date_text);
        this.fromDateImageButton = (ImageView) rootView.findViewById(R.id.invoice_list_from_date_button);
        this.toDateTextView = (TextView) rootView.findViewById(R.id.invoice_list_to_date_text);
        this.toDateImageButton = (ImageView) rootView.findViewById(R.id.invoice_list_to_date_button);
        this.searchButton = (CircleButton) rootView.findViewById(R.id.invoices_search_button);
        this.loadNextPageButton = (CircleButton) rootView.findViewById(R.id.load_invoices_another_page_button);

        this.fromDateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                new SlideDateTimePicker.Builder(getFragmentManager())
                        .setListener(fromListener)
                        .setInitialDate(new Date())
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
                        //.setIs24HourTime(true)
                        //.setTheme(SlideDateTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();
            }
        });

        this.toDateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                new SlideDateTimePicker.Builder(getFragmentManager())
                        .setListener(toListener)
                        .setInitialDate(new Date())
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
                        //.setIs24HourTime(true)
                        //.setTheme(SlideDateTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();
            }
        });

        this.invoices = new ArrayList<>();
        this.listAdapter = new InvoiceListAdapter(this.parentActivity,
                R.layout.invoice_list_row,
                this.invoices);

        this.listview.setAdapter(this.listAdapter);

        this.toDateTextView.setText(DateTimeUtils.formatDateTimeToPersian(this.toDate));

        this.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInvoices();
            }
        });
        this.searchButton.setOnLongClickListener(this);
        this.loadNextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNextPage();
            }
        });
        this.loadNextPageButton.setOnLongClickListener(this);

        this.backImageView = (ImageView) rootView.findViewById(R.id.invoices_listview_back);

        this.backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.onBackPressed();
            }
        });

        return rootView;
    }

    private void searchInvoices(){
        try {
            this.invoices.clear();
            this.currentOffset = 0;

            this.loadNextPage();

        } catch (Exception error){
            Toast.makeText(this.parentActivity, error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadNextPage(){
        try {
            this.invoices.addAll(InvoiceServicesI.getInstance().search(this.fromDate,
                    this.toDate, this.currentOffset, this.limitSize));
            this.listAdapter.notifyDataSetChanged();
            this.currentOffset = this.currentOffset + this.limitSize;
        } catch (Exception error){
            Toast.makeText(this.parentActivity, error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showInvoiceItemsList(ArrayList<InvoiceItem> items){
        if(this.fragmentManager == null) return;

        InvoiceItemsListFragment fragment = new InvoiceItemsListFragment();
        fragment.setInvoiceItems(items);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.hide(InvoiceListFragment.this);
        transaction.replace(R.id.consumer_main_frame_container, fragment, null);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onItemsDetailsShow(ArrayList<InvoiceItem> items) {
        showInvoiceItemsList(items);
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

package com.mitranetpars.sportmagazine.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mitranetpars.sportmagazine.InvoiceItemsListFragment;
import com.mitranetpars.sportmagazine.InvoiceListFragment;
import com.mitranetpars.sportmagazine.R;
import com.mitranetpars.sportmagazine.common.dto.invoice.Invoice;
import com.mitranetpars.sportmagazine.common.dto.invoice.InvoiceItem;
import com.mitranetpars.sportmagazine.utils.DateTimeUtils;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EventListener;

/**
 * Created by Hamed on 12/6/2016.
 */

public class InvoiceListAdapter extends ArrayAdapter<Invoice> implements EventListener{

    private int layoutResourceId;
    private ArrayList<Invoice> data;
    private LayoutInflater inflater;

    private ArrayList<InvoiceItemsDetailsEventListener> listeners;

    public void addListener(InvoiceItemsDetailsEventListener toAdd) {
        listeners.add(toAdd);
    }

    public void removeListener(InvoiceItemsDetailsEventListener toRemove) {
        listeners.add(toRemove);
    }

    private void showInvoiceItemsDetails(ArrayList<InvoiceItem> items) {
        // Notify everybody that may be interested.
        for (InvoiceItemsDetailsEventListener listener : listeners)
            listener.onItemsDetailsShow(items);
    }

    public InvoiceListAdapter(Context context, int layoutResourceId, ArrayList<Invoice> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.inflater = ((Activity)context).getLayoutInflater();
        this.listeners = new ArrayList<InvoiceItemsDetailsEventListener>();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        InvoiceHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new InvoiceHolder();
            holder.detailImageView = (ImageView) convertView.findViewById(R.id.invoice_list_row_detail);
            holder.detailImageView.setTag(position);
            holder.invoiceCommentTextView = (TextView) convertView.findViewById(R.id.invoice_list_row_comment);
            holder.invoiceDateTextView = (TextView) convertView.findViewById(R.id.invoice_list_date_time);
            holder.invoiceItemQuantityTextView = (TextView) convertView.findViewById(R.id.invoice_list_quantity_tv);
            holder.invoiceTotalPriceTextView = (TextView) convertView.findViewById(R.id.invoice_list_total_price);

            convertView.setTag(holder);
        }
        else {
            holder = (InvoiceHolder)convertView.getTag();
        }

        final Invoice invoice = data.get(position);
        holder.invoiceTotalPriceTextView.setText(String.valueOf(invoice.getTotalPrice()));
        holder.invoiceItemQuantityTextView.setText(String.valueOf(invoice.getInvoiceItems().size()));
        holder.invoiceDateTextView.setText(String.valueOf(DateTimeUtils.formatDateTimeToPersian(invoice.getDate())));
        holder.invoiceCommentTextView.setText(invoice.getComment());

        holder.detailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = Integer.parseInt(v.getTag().toString());
                ArrayList<InvoiceItem> items = data.get(pos).getInvoiceItems();
                showInvoiceItemsDetails(items);
            }
        });

        return convertView;
    }

    private class InvoiceHolder {
        TextView invoiceDateTextView;
        TextView invoiceTotalPriceTextView;
        TextView invoiceCommentTextView;
        TextView invoiceItemQuantityTextView;

        ImageView detailImageView;
    }
}

package com.mitranetpars.sportmagazine.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mitranetpars.sportmagazine.R;
import com.mitranetpars.sportmagazine.SportMagazineApplication;
import com.mitranetpars.sportmagazine.common.dto.invoice.ProducerInvoice;
import com.mitranetpars.sportmagazine.utils.ImageUtils;
import com.mitranetpars.sportmagazine.widgets.ShapedCheckBox;
import com.satsuware.usefulviews.LabelledSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Hamed on 1/9/2017.
 */

public class ProducerInvoicesListAdapter extends ArrayAdapter<ProducerInvoice> {
    private int layoutResourceId;
    private ArrayList<ProducerInvoice> data = null;
    private LayoutInflater inflater = null;
    private FragmentManager fragmentManager;
    private int replacementID;
    private SimpleDateFormat mFormatter;

    public ProducerInvoicesListAdapter(Context context, int layoutResourceId, ArrayList<ProducerInvoice> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.inflater = ((Activity)context).getLayoutInflater();
        this.mFormatter = new SimpleDateFormat("yyyy MMMM dd");
    }

    public void setFragmentManager(FragmentManager fm){
        this.fragmentManager = fm;
    }

    public void setReplacementID(int id){
        this.replacementID = id;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull final ViewGroup parent) {
        ProducerInvoiceHolder holder = null;
        if(convertView == null)
        {
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new ProducerInvoiceHolder();
            holder.holderItemImage = (ImageView)convertView.findViewById(R.id.producer_invoice_list_item_image);
            holder.holderItemName = (TextView)convertView.findViewById(R.id.producer_invoice_list_item_name);
            holder.holderItemPrice = (TextView)convertView.findViewById(R.id.producer_invoice_list_item_price);
            holder.holderItemQuantity = (TextView)convertView.findViewById(R.id.producer_invoice_list_item_quantity_tv);

            holder.sizesLabelledSpinner = (LabelledSpinner)convertView.findViewById(R.id.producer_invoice_list_size_selector);
            holder.brandsLabelledSpinner = (LabelledSpinner)convertView.findViewById(R.id.producer_invoice_list_brand_selector);

            holder.colorsLinearLayout = (LinearLayout)convertView.findViewById(R.id.producer_invoice_list_item_additional_colors);
            holder.holderDateTime = (TextView) convertView.findViewById(R.id.producer_invoice_list_item_date_time);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ProducerInvoiceHolder)convertView.getTag();
        }

        final ProducerInvoice invoice = data.get(position);
        String imageString = invoice.getItemProduct().getImage();
        if (imageString != null && imageString != "") {
            holder.holderItemImage.setImageBitmap(ImageUtils.decodeFromBase64(imageString));
        }
        if (invoice.getItemProduct().getName() != null) {
            holder.holderItemName.setText(invoice.getItemProduct().getName());
        }
        holder.holderItemPrice.setText(String.valueOf(invoice.getItemPrice()));
        holder.holderItemQuantity.setText(String.valueOf(invoice.getItemQuantity()));
        if (invoice.getInvoiceDate() != null) {
            holder.holderDateTime.setText(this.mFormatter.format(invoice.getInvoiceDate()));
        }

        if (invoice.getItemSize() != null) {
            holder.sizesLabelledSpinner.setItemsArray(new String[]{invoice.getItemSize()});
        }
        holder.sizesLabelledSpinner.setEnabled(false);
        if (invoice.getItemBrand() != null) {
            holder.brandsLabelledSpinner.setItemsArray(new String[]{invoice.getItemBrand()});
        }
        holder.brandsLabelledSpinner.setEnabled(false);
        holder.colorsLinearLayout.removeAllViews();

        if (invoice.getItemColor() != null && !invoice.getItemColor().isEmpty()) {
            try {
                String color = invoice.getItemColor();
                int c = Integer.parseInt(color);
                int col = Color.rgb(Color.red(c), Color.green(c), Color.blue(c));

                ShapedCheckBox checkBox = new ShapedCheckBox(SportMagazineApplication.getContext(), null);
                checkBox.setText("CC");
                checkBox.setBackgroundColor(col);
                checkBox.setTextColor(col);
                checkBox.setTag(color);
                checkBox.setChecked(true);
                checkBox.setEnabled(false);
                holder.colorsLinearLayout.addView(checkBox);
            } catch (Exception error) {
            }
        }

        return convertView;
    }

    private class ProducerInvoiceHolder
    {
        ImageView holderItemImage;
        TextView holderItemName;

        TextView holderItemPrice;
        TextView holderItemQuantity;

        LinearLayout colorsLinearLayout;
        LabelledSpinner sizesLabelledSpinner;
        LabelledSpinner brandsLabelledSpinner;

        TextView holderDateTime;
    }
}

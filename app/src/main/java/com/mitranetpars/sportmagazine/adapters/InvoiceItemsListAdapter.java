package com.mitranetpars.sportmagazine.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mitranetpars.sportmagazine.R;
import com.mitranetpars.sportmagazine.common.dto.invoice.InvoiceItem;
import com.mitranetpars.sportmagazine.utils.ImageUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Hamed on 12/8/2016.
 */

public class InvoiceItemsListAdapter extends ArrayAdapter<InvoiceItem> {

    private int layoutResourceId;
    private ArrayList<InvoiceItem> data = null;
    private LayoutInflater inflater = null;

    public InvoiceItemsListAdapter(Context context, int layoutResourceId, ArrayList<InvoiceItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.inflater = ((Activity) context).getLayoutInflater();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        InvoiceItemHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new InvoiceItemHolder();

            holder.invoiceItemProductBrandTextView =
                    (TextView) convertView.findViewById(R.id.invoice_item_row_brand);
            holder.invoiceItemProductColorImageView =
                    (ImageView) convertView.findViewById(R.id.invoice_item_row_color);
            holder.invoiceItemProductCommentTextView =
                    (TextView) convertView.findViewById(R.id.invoice_item_row_comment);
            holder.invoiceItemProductImageVIew =
                    (ImageView) convertView.findViewById(R.id.invoice_item_row_product_image);
            holder.invoiceItemProductNameTextView =
                    (TextView) convertView.findViewById(R.id.invoice_item_row_product_name);
            holder.invoiceItemProductPriceTextView =
                    (TextView) convertView.findViewById(R.id.invoice_item_row_product_price);
            holder.invoiceItemProductQuantityTextView =
                    (TextView) convertView.findViewById(R.id.invoice_item_row_quantity_tv);
            holder.invoiceItemProductSizeTextView =
                    (TextView) convertView.findViewById(R.id.invoice_item_row_size);

            convertView.setTag(holder);
        }
        else {
            holder = (InvoiceItemHolder)convertView.getTag();
        }

        final InvoiceItem invoiceItem = data.get(position);
        holder.invoiceItemProductSizeTextView.setText(invoiceItem.getSize());
        holder.invoiceItemProductQuantityTextView.setText(String.valueOf(invoiceItem.getQuantity()));
        holder.invoiceItemProductPriceTextView.setText(String.valueOf(invoiceItem.getPrice()));
        holder.invoiceItemProductNameTextView.setText(invoiceItem.getProduct().getName());

        String imageString = invoiceItem.getProduct().getImage();
        if (imageString != null && imageString != "") {
            holder.invoiceItemProductImageVIew.setImageBitmap(ImageUtils.decodeFromBase64(imageString));
        }

        holder.invoiceItemProductCommentTextView.setText(invoiceItem.getProduct().getComment());

        String colorString = invoiceItem.getColor();
        if (colorString != null && colorString != "" && !colorString.toLowerCase().contains("none")) {
            int c = Integer.parseInt(invoiceItem.getColor());
            int color = Color.rgb(Color.red(c), Color.green(c), Color.blue(c));

            Resources res = convertView.getResources();
            final Drawable drawable = res.getDrawable(R.drawable.circle_background);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            holder.invoiceItemProductColorImageView.setImageDrawable(drawable);
        }

        holder.invoiceItemProductBrandTextView.setText(invoiceItem.getBrand());

        return convertView;
    }

    private class InvoiceItemHolder {
        TextView invoiceItemProductNameTextView;
        TextView invoiceItemProductPriceTextView;
        TextView invoiceItemProductCommentTextView;
        TextView invoiceItemProductQuantityTextView;
        TextView invoiceItemProductSizeTextView;
        TextView invoiceItemProductBrandTextView;

        ImageView invoiceItemProductImageVIew;
        ImageView invoiceItemProductColorImageView;
    }
}

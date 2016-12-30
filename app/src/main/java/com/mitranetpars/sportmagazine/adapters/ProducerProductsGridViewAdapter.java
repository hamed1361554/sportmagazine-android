package com.mitranetpars.sportmagazine.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mitranetpars.sportmagazine.R;
import com.mitranetpars.sportmagazine.common.dto.product.Product;
import com.mitranetpars.sportmagazine.utils.ImageUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hamed on 12/30/2016.
 */

public class ProducerProductsGridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Product> products;
    private int layoutResourceId;
    private LayoutInflater inflater;

    public ProducerProductsGridViewAdapter(Context c, int l, ArrayList<Product> p) {
        context = c;
        products = p;
        layoutResourceId = l;
        inflater = ((Activity)context).getLayoutInflater();
    }

    public int getCount() {
        return products.size();
    }

    public Object getItem(int position) {
        return products.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView per image item
    public View getView(int position, View convertView, ViewGroup parent) {
        ProductHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(layoutResourceId, parent, false);
            //convertView.setLayoutParams(new GridView.LayoutParams(150, 150));
            convertView.setPadding(10, 10, 10, 10);

            holder = new ProductHolder();
            holder.holderProductImage = (ImageView) convertView.findViewById(R.id.product_grid_view_item_image);
            holder.holderProductImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.holderProductName = (TextView) convertView.findViewById(R.id.product_grid_view_item_name);

            convertView.setTag(holder);
        } else {
            holder = (ProductHolder)convertView.getTag();
        }

        final Product product = products.get(position);
        String imageString = product.getImage();
        if (imageString != null && imageString != "") {
            holder.holderProductImage.setImageBitmap(ImageUtils.decodeFromBase64(imageString));
        } else {
            Picasso.with(context).load(R.drawable.basket64).into(holder.holderProductImage);
        }
        holder.holderProductName.setText(product.getName());

        return convertView;
    }

    private class ProductHolder
    {
        ImageView holderProductImage;
        TextView holderProductName;
    }
}

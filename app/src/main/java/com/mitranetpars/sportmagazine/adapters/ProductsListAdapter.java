package com.mitranetpars.sportmagazine.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mitranetpars.sportmagazine.Cart.CartHelper;
import com.mitranetpars.sportmagazine.R;
import com.mitranetpars.sportmagazine.common.dto.product.Product;
import com.mitranetpars.sportmagazine.utils.ImageUtils;

import java.util.ArrayList;

/**
 * Created by Hamed on 12/2/2016.
 */

public class ProductsListAdapter extends ArrayAdapter<Product> {

    private int layoutResourceId;
    private ArrayList<Product> data = null;
    private LayoutInflater inflater = null;

    public ProductsListAdapter(Context context, int layoutResourceId, ArrayList<Product> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.inflater = ((Activity)context).getLayoutInflater();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ProductHolder holder = null;
        if(convertView == null)
        {
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new ProductHolder();
            holder.holderProductImage = (ImageView)convertView.findViewById(R.id.product_list_item_image);
            holder.holderProductName = (TextView)convertView.findViewById(R.id.product_list_item_name);
            holder.holderProductPrice = (TextView)convertView.findViewById(R.id.product_list_item_price);
            holder.holderProductComment = (TextView)convertView.findViewById(R.id.product_list_item_comment);
            holder.holderProductQuantity = (TextView)convertView.findViewById(R.id.product_list_item_quantity_tv);

            holder.addToCart = (ImageView)convertView.findViewById(R.id.product_list_item_add_to_cart);
            holder.plusQuantity = (ImageView)convertView.findViewById(R.id.product_list_item_cart_plus);
            holder.minusQuantity = (ImageView)convertView.findViewById(R.id.product_list_item_cart_minus);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ProductHolder)convertView.getTag();
        }

        final Product product = data.get(position);
        String imageString = product.getImage();
        if (imageString != null && imageString != "") {
            holder.holderProductImage.setImageBitmap(ImageUtils.decodeFromBase64(imageString));
        }
        holder.holderProductName.setText(product.getName());
        holder.holderProductPrice.setText(String.valueOf(product.getPrice()));
        holder.holderProductComment.setText(product.getComment());
        holder.holderProductQuantity.setText("1");

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView quantityTextView =
                        (TextView)(((View)v.getParent()).findViewById(R.id.product_list_item_quantity_tv));
                CartHelper.getCart().add(product, Integer.parseInt(quantityTextView.getText().toString()));

                quantityTextView.setText(String.valueOf(CartHelper.getCart().getQuantity(product)));
            }
        });

        holder.plusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView quantityTextView =
                        (TextView)(((View)v.getParent()).findViewById(R.id.product_list_item_quantity_tv));

                int newQuantity = Integer.parseInt(quantityTextView.getText().toString()) + 1;
                quantityTextView.setText(String.valueOf(newQuantity));
                CartHelper.getCart().update(product, newQuantity);
            }
        });

        holder.minusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView quantityTextView =
                        (TextView)(((View)v.getParent()).findViewById(R.id.product_list_item_quantity_tv));

                int newQuantity = Integer.parseInt(quantityTextView.getText().toString()) - 1;
                if (newQuantity < 1){
                    return;
                }

                quantityTextView.setText(String.valueOf(newQuantity));
                CartHelper.getCart().update(product, newQuantity);
            }
        });

        return convertView;
    }

    private class ProductHolder
    {
        ImageView holderProductImage;
        TextView holderProductName;
        TextView holderProductPrice;
        TextView holderProductComment;
        TextView holderProductQuantity;

        ImageView addToCart;
        ImageView plusQuantity;
        ImageView minusQuantity;
    }
}

package com.mitranetpars.sportmagazine.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mitranetpars.sportmagazine.SportMagazineApplication;
import com.mitranetpars.sportmagazine.cart.CartHelper;
import com.mitranetpars.sportmagazine.R;
import com.mitranetpars.sportmagazine.common.dto.product.Product;
import com.mitranetpars.sportmagazine.utils.ImageUtils;
import com.mitranetpars.sportmagazine.widgets.ShapedCheckBox;
import com.satsuware.usefulviews.LabelledSpinner;

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
    public View getView(int position, View convertView, @NonNull final ViewGroup parent) {
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

            holder.sizesLabelledSpinner = (LabelledSpinner)convertView.findViewById(R.id.product_list_size_selector);
            holder.brandsLabelledSpinner = (LabelledSpinner)convertView.findViewById(R.id.product_list_brand_selector);

            holder.addToCart = (ImageView)convertView.findViewById(R.id.product_list_item_add_to_cart);
            holder.plusQuantity = (ImageView)convertView.findViewById(R.id.product_list_item_cart_plus);
            holder.minusQuantity = (ImageView)convertView.findViewById(R.id.product_list_item_cart_minus);

            holder.colorsLinearLayout = (LinearLayout)convertView.findViewById(R.id.product_list_item_additional_colors);

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
                View parentView = (View)v.getParent();
                TextView quantityTextView =
                        (TextView)parentView.findViewById(R.id.product_list_item_quantity_tv);

                LinearLayout colors =
                        (LinearLayout) parentView.findViewById(R.id.product_list_item_additional_colors);
                for (int i = 0; i < colors.getChildCount(); i++){
                    ShapedCheckBox cb = (ShapedCheckBox) colors.getChildAt(i);
                    if (cb.isChecked()){
                        product.SelectedColor = i;
                        break;
                    }
                }

                LabelledSpinner sizesSpinner =
                        (LabelledSpinner) parentView.findViewById(R.id.product_list_size_selector);
                product.SelectedSize = sizesSpinner.getSpinner().getSelectedItemPosition();

                LabelledSpinner brandsSpinner =
                        (LabelledSpinner) parentView.findViewById(R.id.product_list_brand_selector);
                product.SelectedBrand = brandsSpinner.getSpinner().getSelectedItemPosition();

                CartHelper.getCart().add(product, Integer.parseInt(quantityTextView.getText().toString()));

                quantityTextView.setText(String.valueOf(CartHelper.getCart().getQuantity(product)));
            }
        });

        holder.plusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parentView = (View)v.getParent();
                TextView quantityTextView =
                        (TextView)parentView.findViewById(R.id.product_list_item_quantity_tv);

                int newQuantity = Integer.parseInt(quantityTextView.getText().toString()) + 1;
                if (newQuantity > 999999){
                    return;
                }
                quantityTextView.setText(String.valueOf(newQuantity));

                parentView = (View)parentView.getParent();
                LinearLayout colors =
                        (LinearLayout) parentView.findViewById(R.id.product_list_item_additional_colors);
                for (int i = 0; i < colors.getChildCount(); i++){
                    ShapedCheckBox cb = (ShapedCheckBox) colors.getChildAt(i);
                    if (cb.isChecked()){
                        product.SelectedColor = i;
                        break;
                    }
                }

                LabelledSpinner sizesSpinner =
                        (LabelledSpinner) parentView.findViewById(R.id.product_list_size_selector);
                product.SelectedSize = sizesSpinner.getSpinner().getSelectedItemPosition();

                LabelledSpinner brandsSpinner =
                        (LabelledSpinner) parentView.findViewById(R.id.product_list_brand_selector);
                product.SelectedBrand = brandsSpinner.getSpinner().getSelectedItemPosition();

                if (CartHelper.getCart().getProducts().contains(product)) {
                    CartHelper.getCart().update(product, newQuantity);
                }else {
                    CartHelper.getCart().add(product, newQuantity);
                }
            }
        });

        holder.minusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parentView = (View)v.getParent();
                TextView quantityTextView =
                        (TextView)parentView.findViewById(R.id.product_list_item_quantity_tv);

                int newQuantity = Integer.parseInt(quantityTextView.getText().toString()) - 1;
                if (newQuantity < 1){
                    return;
                }
                quantityTextView.setText(String.valueOf(newQuantity));

                parentView = (View)parentView.getParent();
                LinearLayout colors =
                        (LinearLayout) parentView.findViewById(R.id.product_list_item_additional_colors);
                for (int i = 0; i < colors.getChildCount(); i++){
                    ShapedCheckBox cb = (ShapedCheckBox) colors.getChildAt(i);
                    if (cb.isChecked()){
                        product.SelectedColor = i;
                        break;
                    }
                }

                LabelledSpinner sizesSpinner =
                        (LabelledSpinner) parentView.findViewById(R.id.product_list_size_selector);
                product.SelectedSize = sizesSpinner.getSpinner().getSelectedItemPosition();

                LabelledSpinner brandsSpinner =
                        (LabelledSpinner) parentView.findViewById(R.id.product_list_brand_selector);
                product.SelectedBrand = brandsSpinner.getSpinner().getSelectedItemPosition();

                if (CartHelper.getCart().getProducts().contains(product)) {
                    CartHelper.getCart().update(product, newQuantity);
                }else {
                    CartHelper.getCart().add(product, newQuantity);
                }
            }
        });

        holder.sizesLabelledSpinner.setItemsArray(product.getSizesArray());
        holder.brandsLabelledSpinner.setItemsArray(product.getBrandsArray());
        holder.colorsLinearLayout.removeAllViews();
        int counter = 0;
        for (String color: product.getColorsArray()){
            int c = Integer.parseInt(color);
            int col = Color.rgb(Color.red(c), Color.green(c), Color.blue(c));

            ShapedCheckBox checkBox = new ShapedCheckBox(SportMagazineApplication.getContext(), null);
            checkBox.setText("CC");
            checkBox.setBackgroundColor(col);
            checkBox.setTextColor(col);
            checkBox.setTag(color);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    colorCheckBoxClicked(v);
                }
            });

            holder.colorsLinearLayout.addView(checkBox);

            if (product.SelectedColor >= 0 && product.SelectedColor == counter){
                checkBox.setChecked(true);
            }

            counter += 1;
        }

        if (product.SelectedBrand >= 0){
            holder.brandsLabelledSpinner.getSpinner().setSelection(product.SelectedBrand);
        }
        if (product.SelectedSize >= 0){
            holder.sizesLabelledSpinner.getSpinner().setSelection(product.SelectedSize);
        }

        return convertView;
    }

    private void colorCheckBoxClicked(View v){
        ShapedCheckBox clicked = (ShapedCheckBox) v;
        if (!clicked.isChecked()) return;

        LinearLayout parent =
                (LinearLayout) v.getParent();

        for (int i = 0; i < parent.getChildCount(); i++){
            ShapedCheckBox cb = (ShapedCheckBox) parent.getChildAt(i);

            if (v.getTag() == cb.getTag()) continue;
            cb.setChecked(false);
        }
    }

    private class ProductHolder
    {
        ImageView holderProductImage;
        TextView holderProductName;
        TextView holderProductPrice;
        TextView holderProductComment;
        TextView holderProductQuantity;

        LabelledSpinner sizesLabelledSpinner;
        LabelledSpinner brandsLabelledSpinner;

        ImageView addToCart;
        ImageView plusQuantity;
        ImageView minusQuantity;

        LinearLayout colorsLinearLayout;
    }
}
